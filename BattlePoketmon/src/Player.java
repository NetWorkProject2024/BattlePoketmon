////
////public class Player {
////	private static int userId = 1;
////	private String playerName = "";
////	private int id = 0;
////	
////	public Player(String name) {
////		this.playerName = name;
////		this.id = userId++;
////	}
////	
////	public int getId() {
////		return id;
////	}
////	
////	public void setId(int id) {
////		this.id = id;
////	}
////	
////	public String getPlayerName() {
////        return playerName;
////    }
////	
////	
////	 @Override
////	    public String toString() {
////	        return "BattlePlayer [Name=" + getPlayerName() + ", ID=" + getId() + "]";
////	    }
////
////	
////}
//
//
//
//public class Player {
//	private static int userId = 1;
//	private String playerName = "";
//	private int id = 0;
//	private int poketmonIdx = 0;
//	private boolean ready = false;
//	private ReadyRoom room = null;
//	
//	public Player(String name) {
//		this.playerName = name;
//		this.id = userId++;
//	}
//	
//	public Player(String name, ReadyRoom room) {
//		 this.playerName = name;
//		this.id = userId++;
//	     this.room = room;		
//	}
//
//	public void setPoketmonIdx(int poketmonIdx) {
//		this.poketmonIdx = poketmonIdx;
//	}
//	
//	public void getReady() {
//		this.ready = true;
//	}
//	
//	 @Override
//	    public String toString() {// 테스트용
//	        return "ReadyRoomPlayer [Name=" + getPlayerName() +
//	                ", ID=" + getId() +
//	                ", Ready=" + ready +
//	                ", PoketmonIdx=" + poketmonIdx + "]";
//	    }
//	
//	
//	public int getId() {
//		return id;
//	}
//	
//	public void setId(int id) {
//		this.id = id;
//	}
//	
//	public String getPlayerName() {
//        return playerName;
//    }
//	
//
//
//	
//}


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class Player implements Serializable{
	private transient Home home = null;
	
	private String serverAddress;
	private int serverPort;
	
	private transient Socket socket;

	private transient ObjectOutputStream out;
	
	private transient Thread receiveThread = null;
	private static int userId = 1;
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
		
		home = new Home();
		try {
			connectToServer();
			sendUserID();
		}catch(IOException e) {
			
		}
	}
	public Player(String name, ReadyRoom room, String serverAddress, int serverPort) {
		this.playerName = name;
		this.id = userId++;
	    this.room = room;
	    this.serverAddress=serverAddress;
		this.serverPort = serverPort;
		
		home = new Home();
		try {
			connectToServer();
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
	
	private void connectToServer() throws UnknownHostException, IOException {
			socket = new Socket();
			SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
			socket.connect(sa,3000);		
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
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
						case ChatMsg.MODE_TX_STRING:
							break;
						case ChatMsg.MODE_TX_IMAGE:

							break;
						}
						
					} catch (IOException e) {
						System.err.println("서버 연결 실패: " + e.getMessage());
					}catch (ClassNotFoundException e) {

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
//		try {
//			out.writeObject(msg);
//			out.flush();
//		} catch(IOException e) {
//			System.err.println("클라이언트 일반 전송 오류> " + e.getMessage());
//		}
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
	
	
	public void setPoketmonIdx(int poketmonIdx) {
		this.poketmonIdx = poketmonIdx;
	}
	
	public void getReady() {
		this.ready = true;
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

