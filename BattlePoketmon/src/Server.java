

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Server extends JFrame{
	
	private int port;
	private String host;
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
	
	
	public Server(String host, int port) {
		super("BattlePoketmon_Server");
		ImageIcon icon = new ImageIcon("src/poketmon/Title.png");
        Image image = icon.getImage();
        this.setIconImage(image);
		buildGUI();
		setSize(400,300);
		setLocation(100,0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		this.host = host;
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
			serverSocket = new ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"));
//			serverSocket = new ServerSocket(port);
			printDisplay("서버가 시작되었습니다: " + getLocalAddr());

			while (acceptThread == Thread.currentThread()) {
				clientSocket = serverSocket.accept();
				String cAddr = clientSocket.getInetAddress().getHostAddress();
				t_display.append("클라이언트가 연결되었습니다: " + cAddr + "\n");
				
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
					if(msg.mode == ChatMsg.MODE_IMG_REQUEST) {
						for(int i = 0; i<users.size();i++) {
							if(users.elementAt(i).client != null && msg.player.getId()==users.elementAt(i).client.getId()) {
								users.elementAt(i).client.setProfile(msg.img);
							}
						}
						
					}else if(msg.mode == ChatMsg.MODE_LOGIN) {
						client=msg.player;
						client.setId(generateUserId());
						uid = client.getPlayerName();
						printDisplay("새 참가자: " + uid);
						printDisplay("현재 참가자 수: " + users.size());
						sendLogin(client);						
					}else if (msg.mode == ChatMsg.MODE_LOGOUT) {
						break;
					}else if(msg.mode == ChatMsg.MODE_TX_STRING) {
						String message = uid + ": " + msg.message;
						printDisplay(message);
						broadcasting(msg);
						
					}else if (msg.mode == ChatMsg.MODE_HOME_UPDATE) {
						msg.serverRooms = new Vector<>(rooms); // rooms 값을 복사하여 설정
						broadcasting(msg);
					}else if (msg.mode == ChatMsg.MODE_ROOM_ENTER) {
						printDisplay(((ReadyRoom)msg.object).getRoomName()+"방에 "+msg.player.getPlayerName()+"가 들어왔습니다");
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).roomId==((ReadyRoom)msg.object).roomId) {
								rooms.elementAt(i).addUser(msg.player);
								((ReadyRoom)msg.object).roomId=rooms.elementAt(i).roomId;
								roomCopy(rooms.elementAt(i),((ReadyRoom)msg.object));
							}
						}
						broadcastingInSameRoom((ReadyRoom)msg.object,msg);
						send(msg);
						client.setReadyRoom(((ReadyRoom)msg.object));
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_HOME_UPDATE, readyRooms, 0));
					}
					
					
					
					else if(msg.mode == ChatMsg.MODE_ROOM_EXIT) {
						printDisplay(msg.player+"가 " + ((ReadyRoom)msg.object) + "방을 나갔습니다");
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).roomId==((ReadyRoom)msg.object).roomId) {
								rooms.elementAt(i).removeUser(msg.player);
								((ReadyRoom)msg.object).roomId=rooms.elementAt(i).roomId;
								
								roomCopy(rooms.elementAt(i), ((ReadyRoom)msg.object));
															
							}
						}						
						broadcastingInSameRoom((ReadyRoom)msg.object,msg);
						for(int i=0; i <rooms.size(); i++) {
							if(rooms.elementAt(i).getUsers().size()==0) {
								rooms.removeElementAt(i);
							}
						}						
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_HOME_UPDATE, readyRooms, 0));
						msg.player.getReadyRoom().decreaseCurrentReadyCount();
					}
					else if(msg.mode == ChatMsg.MODE_ROOM_CREATE) {
						
						ReadyRoom newRoom = new ReadyRoom(msg.message, msg.player, (int)msg.size, generateRoomId());
						rooms.add(newRoom);
						Vector<ReadyRoom> readyRooms = new Vector<ReadyRoom>();
						roomCopy(rooms, readyRooms);
						printDisplay(msg.player.getPlayerName() + "가 " + newRoom.getRoomName() + "방을 생성했습니다.");
						send(new ChatMsg(msg.player, ChatMsg.MODE_ROOM_CREATE, newRoom, newRoom.roomId));
						broadcasting(new ChatMsg(msg.player, ChatMsg.MODE_HOME_UPDATE, readyRooms,0));
					}
					
					else if(msg.mode == ChatMsg.MODE_ROOM_PLAYERREADY) {
						if(msg.size==(long)0) {
							msg.player.setReady(false);
						}
						else {
							msg.player.setReady(true);
						}
						ChatMsg newMsg = new ChatMsg(msg.player, msg.mode, msg.player.getReadyRoom(), msg.size);
						broadcastingInSameRoom((ReadyRoom)newMsg.object,  newMsg);
						
						
						ReadyRoom currentRoom = roomReadyCount((ReadyRoom)newMsg.object, msg.player.getReady());//월드 진입
						if(currentRoom.getCurrentReadyCount()==currentRoom.getMaxPlayerCount()) {
							World newWorld = new World(currentRoom.getMaxPlayerCount(), currentRoom.getUsers(), generateWorldId());
							worlds.add(newWorld);
							setWorldInSameRoom(newWorld);
							broadcastingInSameRoom((ReadyRoom)newMsg.object, new ChatMsg(msg.player, ChatMsg.MODE_WORlD_ENTER, newWorld));
							printDisplay(currentRoom.getRoomName() + "방에서 게임을 시작했습니다.");
						}
					}
					
					
					else if(msg.mode == ChatMsg.MODE_WORLD_PLAYERREADY) {
						World currentWorld=null;
						for(int i=0; i < worlds.size(); i++) {
							if(worlds.elementAt(i).getWorldId()==msg.player.getWorld().getWorldId()) {
								currentWorld = worlds.elementAt(i);
							}
						}
						if(msg.size==(long)0) {
							msg.player.setReady(false);
						}
						else if(msg.size==(long)1){
							msg.player.setReady(true);
							for(int i=0; i < users.size(); i++) {
								if(msg.player.getId()==users.elementAt(i).client.getId()) {
									users.elementAt(i).client.setPoketmon((Poketmon)msg.object);
								}
							}
						}
						ChatMsg newMsg = new ChatMsg(msg.player, msg.mode, currentWorld, msg.size);
						broadcastingInSameWorld((World)newMsg.object, newMsg);
						
						int readyCount = 0;
						for(int i=0; i < currentWorld.getUsers().size(); i++) {
							for(int j=0; j < users.size(); j++) {
								if(currentWorld.getUsers().elementAt(i).getId() == users.elementAt(j).client.getId()) {
									if(currentWorld.getUsers().elementAt(i).getReady()) {
										readyCount++;
									}
									currentWorld.getUsers().elementAt(i).setPoketmon(users.elementAt(j).client.getPoketmon());
									users.elementAt(j).client.setPoketmon(currentWorld.getUsers().elementAt(i).getPoketmon());
								}
							}
						}
						currentWorld.setReadyCount(readyCount);
						if(currentWorld.getReadyCount() == currentWorld.getMaxNum()) {
							matching(currentWorld);
						}
					}
					else if(msg.mode == ChatMsg.MODE_ATTACK) {
						Player other=null;
						for(int i=0; i < users.size(); i++) {
							if(users.elementAt(i).client.getId()==msg.player.getId()) {
								int result=(int)msg.size;
								if(users.elementAt(i).client.getPoketmon().getType().getStrength().getName().equals(((PType)msg.object).getName())) {
									result -= 20;
								}
								else if(users.elementAt(i).client.getPoketmon().getType().getWeakness().getName().equals(((PType)msg.object).getName())) {
									result += 20;
								}
								users.elementAt(i).send(new ChatMsg(msg.player, msg.mode, msg.object, result));//맞은 사람이 떄린 사람에게 보낸다.
								other = users.elementAt(i).client.getOtherPlayer();
							}
						}
						
					}
					else if(msg.mode == ChatMsg.MODE_BATTLE_END) {//배틀 끝났을 시
						Player other = (Player)(msg.object);
						printDisplay(other.getPlayerName() + "와의 배틀에서"+msg.player.getPlayerName() + "가 졌다. ");
						for(int i=0; i < users.size(); i++) {
							if(users.elementAt(i).client.getId()==msg.player.getId()) {
								printDisplay("진 사람: " + users.elementAt(i).client.getPlayerName());
								users.elementAt(i).client.increaseLoseCount();		
								users.elementAt(i).send(msg);
								broadcastingInSameWorld(users.elementAt(i).client.getWorld(), new ChatMsg(users.elementAt(i).client, ChatMsg.MODE_BATTLE_RESULT, (long)0));
							}
							if(users.elementAt(i).client.getId()==other.getId()) {//이긴 사람
								printDisplay("이긴 사람: " + users.elementAt(i).client.getPlayerName());
								users.elementAt(i).client.increaseWinCount();//이긴 사람 승수 올리기	
								users.elementAt(i).send(msg);
								broadcastingInSameWorld(users.elementAt(i).client.getWorld(), new ChatMsg(users.elementAt(i).client, ChatMsg.MODE_BATTLE_RESULT, (long)1));//프레임에서 배틀 결과 업데이트
								if(users.elementAt(i).client.getWinCount()==3) {
									broadcastingInSameWorld(users.elementAt(i).client.getWorld(), new ChatMsg(users.elementAt(i).client, ChatMsg.MODE_WORLD_END, users.elementAt(i).client.getWorld()));//월드 END									
									printDisplay(users.elementAt(i).client.getReadyRoom().getRoomName()+"방 게임 종료");
								}
							}
						}
						
					}
					else if(msg.mode == ChatMsg.MODE_WORLD_END) {
						for(int i=0; i < users.size(); i++) {
							if(msg.player.getId()==users.elementAt(i).client.getId()) {
								users.elementAt(i).client.setCoin(100);
								users.elementAt(i).client.setWinCount(0);
								users.elementAt(i).client.setLoseCount(0);
							}
						}
						
					}
					
				}
				
				users.removeElement(this);//퇴장시 처리
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

		
		
		private ReadyRoom roomReadyCount(ReadyRoom room, boolean state) {
			ReadyRoom currentRoom =null;
			for(int i=0; i < rooms.size(); i++) {
				if(rooms.elementAt(i).roomId==room.roomId) {
					currentRoom = rooms.elementAt(i);
				}
			}
			if(currentRoom == null) {
				return null;
			}
			if(state) {
				currentRoom.increaseCurrentReadyCount();
			}
			else {
				currentRoom.decreaseCurrentReadyCount();
			}
			return currentRoom;
			
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

			for (ClientHandler c : users) {
	            c.send(msg);
	        }
		}
		
		private void broadcastingInSameRoom(ReadyRoom room, ChatMsg msg) {
			for (ClientHandler c : users) {
				if(c.client.getReadyRoom().roomId == room.roomId) {
					c.send(msg);
				}	            
	        }
		}
		
		private void broadcastingInSameWorld(World world, ChatMsg msg) {
		    for (ClientHandler c : users) {
		        World playerWorld = c.client.getWorld(); // 클라이언트의 월드 가져오기
		        if (playerWorld != null && playerWorld.getWorldId() == world.getWorldId()) {
		            c.send(msg);
		        } else {
		        	printDisplay("Skipping client: World is null or doesn't match");
		        }
		    }
		}

		
		private void setWorldInSameRoom(World world) {
			for(int i=0; i < world.getUsers().size(); i++) {
				for(int j=0; j < users.size(); j++) {
					if(world.getUsers().elementAt(i).getId() == users.elementAt(j).client.getId()) {
						users.elementAt(j).client.setWorld(world);
					}
				}
			}
		}
		// 배틀 - 유저 매칭				
		private void matching(World world) {
			Collections.shuffle(world.users);
			for (int i = 0; i< world.users.size();i+=2) {
				ChatMsg msg = new ChatMsg(world.users.elementAt(i), ChatMsg.MODE_MATCHING, world.users.elementAt(i+1), world.users.elementAt(i+1).getPoketmon(), 1);
				broadcastingInSameWorld(world, msg);
				world.users.elementAt(i).setOtherPlayer(world.users.elementAt(i+1));
				msg = new ChatMsg(world.users.elementAt(i+1), ChatMsg.MODE_MATCHING, world.users.elementAt(i), world.users.elementAt(i).getPoketmon(), 0);
				broadcastingInSameWorld(world, msg);
				world.users.elementAt(i+1).setOtherPlayer(world.users.elementAt(i));
				printDisplay(world.users.elementAt(i+1).getPlayerName()+"와 " + world.users.elementAt(i+1).getOtherPlayer().getPlayerName()+"가 배틀합니다.");
			}
			for(int i=0; i < users.size(); i++) {
				for(int j=0; j < world.users.size(); j++) {
					if(world.users.elementAt(j).getId()==users.elementAt(i).client.getId()) {
						users.elementAt(i).client.setOtherPlayer(world.users.elementAt(j).getOtherPlayer());
					}
				}
			}
//			if(world == null){
//				printDisplay("Matching failed");//확인용
//			}		
		}

		
		
		@Override
		public void run() {
			
			receiveMessages(clientSocket);
		}
	}
	
	
	
	
	
	
    
	public static void main(String[] args) {
		String filePath = "C:\\address.txt";
		
		String host=null;
		int port = 54321;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			host = reader.readLine();
			port = Integer.parseInt(reader.readLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		Server s = new Server(host, port);
		
	}
}
