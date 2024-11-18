


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
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


public class SharePosServer extends JFrame{
	
	private int port;
	private ServerSocket serverSocket = null;
	
	private Thread acceptThread = null;
	private Vector<ClientHandler> users = new Vector<ClientHandler>();
	
	private JTextArea t_display;
	private JButton b_connect, b_exit, b_disconnect;
	
	public SharePosServer(int port) {
		super("SharePos Server");
		
		buildGUI();
		setSize(400,300);
		setLocation(500,0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		this.port = port;
	}
	
	private void buildGUI() {
		add(createDisplayPanel(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
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
		
		b_connect = new JButton("서버 시작");
		b_connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				acceptThread = new Thread(new Runnable() {
					@Override
					public void run() {
						startServer();						
					}
				});

				acceptThread.start();
				b_disconnect.setEnabled(true);
				b_connect.setEnabled(false);
				b_exit.setEnabled(false);

			}
		});
		
		b_disconnect = new JButton("서버 종료");
		b_disconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				disconnect();
				b_disconnect.setEnabled(false);
				b_connect.setEnabled(true);
				b_exit.setEnabled(true);
			}			
		});
		

		b_exit = new JButton("종료");
		b_exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(serverSocket != null) serverSocket.close();
				}catch(IOException e1) {
					System.err.println("서버 닫기 오류> " + e1.getMessage());					
				}			
				System.exit(-1);
			}			
		});
		
		panel2.add(b_connect);
		panel2.add(b_disconnect);
		panel2.add(b_exit);
		
		b_disconnect.setEnabled(false);
		
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
	

	
	private void printDisplay(String msg) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            t_display.append(msg + "\n");
	            t_display.setCaretPosition(t_display.getDocument().getLength());
	        }
	    });
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
//		private BufferedWriter out;
		private ObjectOutputStream out;
		
		private String uid;
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
//			try {
//				((BufferedWriter)out).write(msg + "\n");
//				out.flush();
//			}catch (IOException e) {
//		        System.err.println("클라이언트 일반 전송 오류> " + e.getMessage());
//		    }
			send(new ChatMsg(uid, ChatMsg.MODE_TX_STRING, msg));
	    }
		
		private void receiveMessages(Socket socket) {
			try {
//				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//				/*BufferedWriter*/ out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

				ChatMsg msg;
				while ((msg = (ChatMsg)in.readObject()) != null){
					if(msg.mode == ChatMsg.MODE_LOGIN) {
						uid = msg.userID;
						
						broadcasting(msg);
						
						for(ClientHandler c: users) {
							String others = c.uid;
							
							if(!uid.equals(others)) {
//								msg.userID = others;		
//								send(msg);
								ChatMsg existingUserMsg = new ChatMsg(c.uid, ChatMsg.MODE_LOGIN);
					            send(existingUserMsg); // 기존 사용자 정보 전송
							}
						}
						printDisplay("새 참가자: " + uid);
						printDisplay("현재 참가자 수: " + users.size());
//						continue;
					}else if (msg.mode == ChatMsg.MODE_LOGOUT) {
						break;
					}else if (msg.mode == ChatMsg.MODE_TX_POS) {
						Point p = (Point) msg.object;
						printDisplay(uid + "(" + p.x + ", " +p.y+")");
						broadcasting(msg);
					}
//					}else if(msg.mode == ChatMsg.MODE_TX_STRING) {
//						String message = uid + ": " + msg.message;
//						printDisplay(message);
//						broadcasting(msg);
//					}else if (msg.mode == ChatMsg.MODE_TX_IMAGE) {
//						printDisplay(uid + ": " + msg.message);
//						broadcasting(msg);
//					}
				}
				users.removeElement(this);
				printDisplay(uid + "퇴장. 현재 참가자 수: " + users.size());				
			}catch(IOException e) {
				users.removeElement(this);
				printDisplay(uid + "연결 끊김. 현재 참가자 수: " + users.size());		
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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

//		private void broadcasting(String msg) {
//			for (ClientHandler user : users) {
//	            user.sendMessage(msg);
//	        }
//		}
		
		private void broadcasting(ChatMsg msg) {
			for (ClientHandler c : users) {
	            c.send(msg);
	        }
		}
		
		@Override
		public void run() {
			receiveMessages(clientSocket);
		}
	}
	
	public static void main(String[] args) {
		int port = 54321;
		SharePosServer s = new SharePosServer(port);

	}

}
