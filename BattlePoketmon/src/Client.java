
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Client{
	private transient Home home = null;
	
	private String serverAddress;
	private int serverPort;
	
	private transient Socket socket;

	private transient ObjectOutputStream out;
	
	private transient Thread receiveThread = null;
	private int userId = 0;
	private Player player;
	private ReadyRoom room = null;
	private World world = null;
	
	public Client(String name, String serverAddress, int serverPort) {
//		this.id = userId++;
		this.serverAddress=serverAddress;
		this.serverPort = serverPort;
		this.player = new Player(name, this);
		
		try {
			connectToServer(this.player);
			sendUserID();
		}catch(IOException e) {
			
		}
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
	
	private void connectToServer(Player player) throws UnknownHostException, IOException {
			socket = new Socket();
			SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
			socket.connect(sa,3000);		
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//			send(new ChatMsg(player, ChatMsg.MODE_ROOM_LIST_REQUEST));
			
			receiveThread = new Thread(new Runnable() {
				
				private ObjectInputStream in; 
				
				private void receiveMessage() {		
					try {					
						ChatMsg inMsg = (ChatMsg)in.readObject();
						if(inMsg == null) {
							disconnect();
							return;
						}
						switch(inMsg.mode) {
						case ChatMsg.MODE_LOGIN:
							home = new Home(player, inMsg.serverRooms);
							player.setId(inMsg.size);
							System.out.println("id바뀜??" +player.getId());
						case ChatMsg.MODE_TX_STRING:
							break;
						case ChatMsg.MODE_ROOM_UPDATE:
//			                home.updateRooms(inMsg.room);			                
//							SwingUtilities.invokeLater(() -> {
//	                            home.updateRoomListPanel();	                          
//	                        });
							System.out.println(inMsg.serverRooms + "<- 뭐가 넘어오니");
							for(int i=0; i < inMsg.serverRooms.size(); i++) {
								System.out.println("Update 될 때의 각 방의 현재 인원: "+inMsg.serverRooms.elementAt(i).getUsers());
							}							
							home.updateRoomListPanel(inMsg.serverRooms);
							home.repaint();
	                        break;						
						case ChatMsg.MODE_ROOM_ENTER:
							//System.out.println("방에 누가 들어옴 클라이언트의 roomId : "+player.getReadyRoom().roomId+", 서버가 보낸 roomId : "+inMsg.room.roomId);
						
//							if(player.getReadyRoom().roomId == inMsg.room.roomId) {
								player.setReadyRoom(((ReadyRoom)inMsg.object));
								System.out.println("내 room 세팅" + player.getReadyRoom().roomId + "메시지 내 룸: " + ((ReadyRoom)inMsg.object).roomId);
								System.out.println("내 room 세팅" + player.getReadyRoom().getUsers() + "메시지 내 룸: " + ((ReadyRoom)inMsg.object).getUsers());
								System.out.println("방의 유저 수 : "+player.getReadyRoom().getUsers() + "내 방 유저들");
								player.getReadyRoom().enterRoom(player);
//							}
//							home.repaint();
							
							break;
						case ChatMsg.MODE_ROOM_EXIT:	
//								player.getReadyRoom().exitRoom(inMsg.player);
							for(int i=0; i < player.getReadyRoom().getUsers().size(); i++) {
								if(player.getReadyRoom().getUsers().elementAt(i).getId() == inMsg.player.getId()) {
									player.getReadyRoom().getUsers().remove(player.getReadyRoom().getUsers().elementAt(i));
								}
							}
							System.out.println("방의 유저 수 : "+((ReadyRoom)inMsg.object).getUsers() + "남아있는 방 유저들");
							player.getReadyRoom().getRoomFrame().updateUserList();
							break;
						case ChatMsg.MODE_ROOM_CREATE:
							System.out.println("방 서버가 생성");
							home.joinReadyRoom(((ReadyRoom)inMsg.object));
							home.repaint();
							break;
						case ChatMsg.MODE_ROOM_PLAYERREADY:
							boolean state;
							if(inMsg.size == (long)0) {
								state = false;
							}
							else {
								state = true;
							}
							System.out.println("서버에서 준비 상태 받는 중 >> player : "+inMsg.player+" , ready 상태 : "+state);
							if(player.getId()==inMsg.player.getId()) {
								player.setReady(state);
							}
							player.getReadyRoom().changeReadyState(inMsg.player, state);
							
							System.out.println(player.getReadyRoom().getCurrentReadyCount() + "레디 상태 받았을 때 변화");
							break;							
						case ChatMsg.MODE_WORlD_ENTER:
							System.out.println("월드_서버가 생성");
							world = ((World)inMsg.object);
							world.enterWorld(player);
							break;
						}
						
						
					} catch (IOException e) {
						System.err.println("서버 연결 끊김: " + e.getMessage());
						System.exit(-1);
					}catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void run() {
					try {
						System.out.println(socket);
						in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					} catch (IOException e) {

					}
					while(receiveThread == Thread.currentThread()) {
						receiveMessage();
					}
				}			
			});
			receiveThread.start();
	}
	
	private void disconnect() {
		send(new ChatMsg(this.player, ChatMsg.MODE_LOGOUT));
		
		try {
			receiveThread = null;
			socket.close();
		} catch (IOException e) {
			System.err.println("클라이언트 닫기 오류> " + e.getMessage());
			System.exit(-1);
		}
	}
	
	private void send(ChatMsg msg) {
		if (socket == null || socket.isClosed() || !socket.isConnected()) {
	        System.err.println("소켓 연결 상태를 확인하세요.");
	        return;
	    }
	    try {
	        out.writeObject(msg);
	        out.flush();
	    } catch (IOException e) {
	        System.err.println("메시지 전송 오류: " + e.getMessage() + e.toString());
	    }
	}
	
	
	private void sendUserID() {
		send(new ChatMsg(this.player, ChatMsg.MODE_LOGIN));
	}
	public void sendMessage(String msg) {
		if (msg.isEmpty()) return;
		send(new ChatMsg(this.player, ChatMsg.MODE_TX_STRING, msg));
    }
	public void sendCreateRoom(String name, long size) {
		send(new ChatMsg(this.player, ChatMsg.MODE_ROOM_CREATE, name, size));
	}
	public void sendEnterRoom(ReadyRoom room) {
		if (room == null) {
		    System.err.println("room 객체가 null 상태입니다");
		} else {
			System.out.println(room.getUsers() + "룸에 있는 사람들");
			send(new ChatMsg(this.player, ChatMsg.MODE_ROOM_ENTER, room, 0));
		    System.out.println(room.getRoomName());
		}
	}
	public void sendExitRoom(ReadyRoom room) {
		if (room == null) {
		    System.err.println("room 객체가 null 상태입니다");
		} else {
			send(new ChatMsg(this.player, ChatMsg.MODE_ROOM_EXIT, room, 0));			
		}
	}
	
	public void sendPlayerReady(boolean state) {
		int size = 0;
		if(state) {
			
			size = 1;
		}
		else {
			size = 0;
		}
		System.out.println("서버에게 준비 상태 알리는 중 >> player : " +this.player +" , ready 상태 : "+state +" , size : " +size);
		send(new ChatMsg(this.player, ChatMsg.MODE_ROOM_PLAYERREADY, size));
	}
	
	public JFrame getHome() {
		return home;
	}
	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 54321;
		Client client = new Client("player", serverAddress, serverPort);

	}
}

