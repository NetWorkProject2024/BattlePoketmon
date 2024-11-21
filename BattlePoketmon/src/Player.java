
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.SwingUtilities;

public class Player implements Serializable{
	private transient Home home = null;
	
	private String serverAddress;
	private int serverPort;
	
	private transient Socket socket;

	private transient ObjectOutputStream out;
	
	private transient Thread receiveThread = null;
	private int userId = 1;
	private String playerName = "";
	private int id = 0;
	private int poketmonIdx = 0;
	private boolean ready = false;
	private ReadyRoom room = null;
	
	public Player(String name, String serverAddress, int serverPort) {
		this.playerName = name;
		this.id = userId++;
		this.serverAddress=serverAddress;
		this.serverPort = serverPort;
		
		
		try {
			connectToServer(this);
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
			send(new ChatMsg(this, ChatMsg.MODE_ROOM_LIST_REQUEST));
			
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
						case ChatMsg.MODE_TX_STRING:
							break;
						case ChatMsg.MODE_ROOM_UPDATE:
//			                home.updateRooms(inMsg.room);			                
//							SwingUtilities.invokeLater(() -> {
//	                            home.updateRoomListPanel();	                          
//	                        });
							System.out.println(inMsg.serverRooms + "<- 뭐가 넘어오니");
							home.updateRoomListPanel(inMsg.serverRooms);
	                        break;
						case ChatMsg.MODE_ROOM_LIST_REQUEST:
//                            home.rooms.add(inMsg.room);
//                            SwingUtilities.invokeLater(() -> {
//	                            home.updateRoomListPanel();
//	                        });                            
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
		send(new ChatMsg(this, ChatMsg.MODE_LOGOUT));
		
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
	        System.err.println("메시지 전송 오류: " + e.getMessage());
	    }
	}
	
	
	private void sendUserID() {
		send(new ChatMsg(this, ChatMsg.MODE_LOGIN));		
	}
	public void sendMessage(String msg) {
		if (msg.isEmpty()) return;
		send(new ChatMsg(this, ChatMsg.MODE_TX_STRING, msg));
    }
	public void sendCreateRoom(ReadyRoom room) {
		if (room == null) {
		    System.err.println("room 객체가 null 상태입니다");
		} else {
			send(new ChatMsg(this, ChatMsg.MODE_ROOM_UPDATE, this.room));
		    System.out.println(room.getRoomName());
		}
	}
	
	public void setPoketmonIdx(int poketmonIdx) {
		this.poketmonIdx = poketmonIdx;
	}
	
	public void getReady() {
		this.ready = true;
	}
	public void setReadyRoom(ReadyRoom myRoom) {
		this.room = myRoom;
	}
	 @Override
	 public String toString() {// 테스트용
	     return "ReadyRoomPlayer [Name=" + getPlayerName() +
	                ", ID=" + getId() +
	                ", Ready=" + ready +
	                ", PoketmonIdx=" + poketmonIdx + "]";
	 }
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPlayerName() {
        return playerName;
    }
	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 54321;
		Player client = new Player("유저", serverAddress, serverPort);

	}
}

