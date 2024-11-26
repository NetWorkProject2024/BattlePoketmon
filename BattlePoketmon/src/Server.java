

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Server extends JFrame{
	
	private int port;
	private ServerSocket serverSocket = null;
	
	private Thread acceptThread = null;
	private Vector<ClientHandler> users = new Vector<ClientHandler>();
	
	private JTextArea t_display;
	private JButton b_exit;
	
	private static Vector<ReadyRoom> rooms = new Vector<ReadyRoom>();
	private static Vector<World> worlds = new Vector<World>();
	private static int roomIdCounter = 0;
	private static int userIdCounter = 0;
	private static int worldIdCounter = 0;
	
	
	public Server(int port) {
		super("BattlePoketmon_Server");
		
		buildGUI();
		setSize(400,300);
		setLocation(100,0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		this.port = port;
		
		acceptThread = new Thread(new Runnable() {
			@Override
			public void run() {
				startServer();						
			}
		});

		acceptThread.start();
		
	}
	private JPanel createDisplayPanel() {
		t_display = new JTextArea();
		t_display.setEditable(false);
		JPanel panel1 = new JPanel(new BorderLayout());
		JScrollPane s = new JScrollPane(t_display);
		panel1.add(s);
		return panel1;		
	}
	
	private JPanel createControlPanel() {
		JPanel panel2 = new JPanel(new GridLayout(1,0));
		b_exit = new JButton("종료");
		b_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					disconnect();
					if(serverSocket != null) serverSocket.close();
				}catch(IOException e1) {
					System.err.println("서버 닫기 오류> " + e1.getMessage());					
				}			
				System.exit(-1);
			}			
		});
		
		panel2.add(b_exit);		
		return panel2;
	}
	
	private void disconnect() {
		try {
			acceptThread = null;
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("서버 소켓 닫기 오류> " + e.getMessage());
			System.exit(-1);
		}
	}
	
	private void startServer() {
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			printDisplay("서버가 시작되었습니다: " + getLocalAddr());
			System.out.println("서버가 시작되었습니다.");

			while (acceptThread == Thread.currentThread()) {
				clientSocket = serverSocket.accept();
				String cAddr = clientSocket.getInetAddress().getHostAddress();
				t_display.append("클라이언트가 연결되었습니다: " + cAddr + "\n");
				System.out.println("클라이언트가 연결되었습니다.\n");
				
				ClientHandler cHandler = new ClientHandler(clientSocket);
				users.add(cHandler);
				cHandler.start();
			}
			
		} catch (SocketException e) {
			printDisplay("서버 소켓 종료");
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(clientSocket != null) clientSocket.close();
				if(serverSocket != null) serverSocket.close();				
			}catch(IOException e) {
				System.err.println("서버 닫기 오류> " + e.getMessage());
				System.exit(-1);
			}
		}
	}
	
	private synchronized int generateRoomId() {
	    return ++roomIdCounter;
	}
	private synchronized int generateWorldId() {
	    return ++worldIdCounter;
	}
	
	private synchronized int generateUserId() {
	    return ++userIdCounter;
	}

	
	public void printDisplay(String msg) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            t_display.append(msg + "\n");
	            t_display.setCaretPosition(t_display.getDocument().getLength());
	        }
	    });
	}
	

	
	private void buildGUI() {
		add(createDisplayPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
	}
	
	
	private String getLocalAddr() {
		InetAddress local = null;
		String addr = "";
		try {
			local = InetAddress.getLocalHost();
			addr = local.getHostAddress();
			System.out.println(addr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return addr;		
	}
	
		
	private class ClientHandler extends Thread{
		private Socket clientSocket;
		private ObjectOutputStream out;
		
		private String uid;
		private Player client;
		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}
		
		private void send(ChatMsg msg) {
			try {
				out.writeObject(msg);
				out.flush();
			}catch(IOException e) {
				System.err.println("클라이언트 일반 전송 오류> " + e.getMessage());
			}			
		}
		private void sendMessage(String msg) {
			send(new ChatMsg(client, ChatMsg.MODE_TX_STRING, msg));
	    }
		
		private void receiveMessages(Socket socket) {
			try {
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				
				ChatMsg msg;
				while ((msg = (ChatMsg)in.readObject()) != null){
					if(msg.mode == ChatMsg.MODE_LOGIN) {
						client=msg.player;
						client.setId(generateUserId());
						uid = client.getPlayerName();
						printDisplay("새 참가자: " + uid + client.getId());
						printDisplay("현재 참가자 수: " + users.size());
						
//						sendRoomList(client);
						sendLogin(client);
						
						continue;
					}else if (msg.mode == ChatMsg.MODE_LOGOUT) {
						break;
					}else if(msg.mode == ChatMsg.MODE_TX_STRING) {
						String message = uid + ": " + msg.message;
						printDisplay(message);
						broadcasting(msg);
						
					}else if (msg.mode == ChatMsg.MODE_ROOM_UPDATE) {
						System.out.println(((ReadyRoom)msg.object) + "-업데이트");						
						msg.serverRooms = new Vector<>(rooms); // rooms 값을 복사하여 설정
						System.out.println(msg.serverRooms + "-방송전");
						broadcasting(msg);
					}else if (msg.mode == ChatMsg.MODE_ROOM_ENTER) {
						System.out.println("방에 "+msg.player+"가 들어왔습니다");
						System.out.println(((ReadyRoom)msg.object));
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).roomId==((ReadyRoom)msg.object).roomId) {
								rooms.elementAt(i).addUser(msg.player);
								((ReadyRoom)msg.object).roomId=rooms.elementAt(i).roomId;
								roomCopy(rooms.elementAt(i),((ReadyRoom)msg.object));
								System.out.println(rooms.elementAt(i).getUsers() + "check!!");
							}
						}
						
//						roomUpdate(msg.room);
						
						broadcastingInSameRoom((ReadyRoom)msg.object,msg);
						send(msg);
						client.setReadyRoom(((ReadyRoom)msg.object));
						System.out.println("copy후의 >>"+((ReadyRoom)msg.object).getUsers()+"방의 현재 인원수");
						
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						for(int i=0; i <rooms.size(); i++) {
							System.out.println("readyRooms 의 유저 >> "+readyRooms.elementAt(i).getUsers() + "check!!");
						}
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_ROOM_UPDATE, readyRooms, 0));
					}else if(msg.mode == ChatMsg.MODE_ROOM_EXIT) {
						System.out.println(msg.player+"가 " + ((ReadyRoom)msg.object) + "방을 나갔습니다");
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).roomId==((ReadyRoom)msg.object).roomId) {
								rooms.elementAt(i).removeUser(msg.player);
								((ReadyRoom)msg.object).roomId=rooms.elementAt(i).roomId;
								
								roomCopy(rooms.elementAt(i), ((ReadyRoom)msg.object));
								System.out.println(rooms.elementAt(i).getUsers() + "check!!");								
															
							}
						}						
						broadcastingInSameRoom((ReadyRoom)msg.object,msg);
						System.out.println("copy후의 >>"+((ReadyRoom)msg.object).getUsers()+"방의 현재 인원수");
						
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).getUsers().size()==0) {
								rooms.removeElementAt(i);
							}
						}						
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						for(int i=0; i <rooms.size(); i++) {
							System.out.println("readyRooms 의 유저 >> "+readyRooms.elementAt(i).getUsers() + "check!!");
						}
						
						
						
						
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_ROOM_UPDATE, readyRooms, 0));
						msg.player.getReadyRoom().decreaseCurrentReadyCount();
						
						
					}
					else if(msg.mode == ChatMsg.MODE_ROOM_CREATE) {
						ReadyRoom newRoom = new ReadyRoom(msg.message, msg.player, (int)msg.size, generateRoomId());
						rooms.add(newRoom);
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						send(new ChatMsg(msg.player, ChatMsg.MODE_ROOM_CREATE, newRoom, newRoom.roomId));
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_ROOM_UPDATE, readyRooms,0));
						
					}
					else if(msg.mode == ChatMsg.MODE_ROOM_PLAYERREADY) {
						if(msg.size==(long)0) {
							msg.player.setReady(false);
						}
						else {
							msg.player.setReady(true);
							
						}
						ChatMsg newMsg = new ChatMsg(msg.player, msg.mode, msg.player.getReadyRoom(), msg.size);
						System.out.println("클라이언트에게 준비 상태 받는 중 >> player : "+newMsg.player+", size : "+newMsg.size);
						broadcastingInSameRoom((ReadyRoom)newMsg.object,  newMsg);
						
						
						enterWorld(msg.player);						
					}
					
				}
				users.removeElement(this);
				printDisplay(uid + "퇴장. 현재 참가자 수: " + users.size());				
			}catch(IOException e) {
				users.removeElement(this);
				printDisplay(uid + "연결 끊김. 현재 참가자 수: " + users.size());		
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}finally{
				try {
	                socket.close();
	            } catch (IOException e) {
	                System.err.println("서버 닫기 오류> " + e.getMessage());
	                System.exit(-1);
	            }
			}
		}

		private void roomCopy(ReadyRoom origin, ReadyRoom changed) {
			changed.roomId=origin.roomId;
			changed.clearUser();
			for(int i=0; i < origin.getUsers().size(); i++) {
				changed.addUser(origin.getUsers().elementAt(i));
			}
			changed.setRoomName(origin.getRoomName());
			changed.setMaxPlayerCount(origin.getMaxPlayerCount());
			
		}
		private void roomCopy(Vector<ReadyRoom> origin, Vector<ReadyRoom> changed) {
			changed.clear();
			for(int i=0; i < origin.size(); i++) {
				ReadyRoom tmp = new ReadyRoom();
				roomCopy(origin.elementAt(i), tmp);
				changed.add(tmp);
			}
			
		}
		private void enterWorld(Player user) {
			for(int i=0; i <rooms.size(); i++) {
				if(rooms.elementAt(i).roomId == user.getReadyRoom().roomId) {
					if(user.getReady()) {
						rooms.elementAt(i).increaseCurrentReadyCount();
					}
					else if(!user.getReady()){
						rooms.elementAt(i).decreaseCurrentReadyCount();
					}
					System.out.println(rooms.elementAt(i).getCurrentReadyCount() + "현재 레디 인원 수");
					if(rooms.elementAt(i).getCurrentReadyCount() == rooms.elementAt(i).getMaxPlayerCount()) {
						World newWorld = new World(rooms.elementAt(i).getMaxPlayerCount(), rooms.elementAt(i).getUsers(), generateWorldId());
						worlds.add(newWorld);
						System.out.println(worlds +"\n 월드 벡터" + newWorld.users);
						
						broadcastingInSameRoom(rooms.elementAt(i), new ChatMsg(user, ChatMsg.MODE_WORlD_ENTER, newWorld));
					}
				}
			}
		}
		
		private void roomUpdate(ReadyRoom room) {
			for(int i=0; i <rooms.size(); i++) {
				if(rooms.elementAt(i).roomId==room.roomId) {
					rooms.remove(rooms.elementAt(i));
					rooms.add(i, room);
				}
			}
		}
		
		private void sendLogin(Player user) {
			send(new ChatMsg(user, ChatMsg.MODE_LOGIN, rooms, userIdCounter));
		}

		
		private void broadcasting(ChatMsg msg) {

            System.out.println(msg.serverRooms +"방송중");
			for (ClientHandler c : users) {
	            c.send(msg);
	        }
		}
		private void broadcastingInSameRoom(ReadyRoom room, ChatMsg msg) {
			for (ClientHandler c : users) {
				if(c.client.getReadyRoom().roomId == room.roomId) {
					c.send(msg);
					System.out.println("sameRoom broadcasting");
				}	            
	        }
		}
		
		
		
		@Override
		public void run() {
			receiveMessages(clientSocket);
		}
	}
    
	public static void main(String[] args) {
		int port = 54321;
		Server s = new Server(port);
	}
}
