//
//
//import java.awt.BorderLayout;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.UnknownHostException;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextField;
//import javax.swing.JTextPane;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.DefaultStyledDocument;
//
//public class WithTalk extends JFrame {
//
//	private JTextField t_input, t_userID, t_hostAddr, t_portNum;
////	private JTextArea t_display;
//	private JTextPane t_display;
//	private DefaultStyledDocument document;
//	private JButton b_connect, b_disconnect, b_send, b_exit, b_select;
//	
//	private String serverAddress;
//	private int serverPort;
//	private String uid;
//	
//	private Socket socket;
////	private Writer out;
////	private Reader in;
//	private ObjectOutputStream out;
//	
//	private Thread receiveThread = null;
//	 
//	public WithTalk(String serverAddress, int port) {
//		super("With Talk");
//		this.serverAddress = serverAddress;
//		this.serverPort = port;
//		
//		buildGUI();
//		setSize(500,300);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		setVisible(true);
//	}
//	
//	private void buildGUI() {
//		add(createDisplayPanel(), BorderLayout.CENTER);
//		
//		JPanel southPanel = new JPanel(new GridLayout(3,0));
//		southPanel.add(createInputPanel());
//		southPanel.add(createInfoPanel());		
//		southPanel.add(createControlPanel());
//		add(southPanel, BorderLayout.SOUTH);
//
//
//	}
//	private JPanel createDisplayPanel() {
//		JPanel panel1 = new JPanel(new BorderLayout());
//		document = new DefaultStyledDocument();
//		t_display = new JTextPane(document);		
//		t_display.setEditable(false);
//		panel1.add(new JScrollPane(t_display), BorderLayout.CENTER);
//		return panel1;
//	}
//	
//	private JPanel createInputPanel() {
//		JPanel panel2 = new JPanel(new BorderLayout());
//		
//		t_input = new JTextField(30);
//		t_input.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sendMessage();
////				receiveMessage();
//			}
//		});
//		
//		b_send = new JButton("보내기");
//		b_send.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sendMessage();
////				receiveMessage();
//			}
//			
//		});
//		b_select = new JButton("선택하기");
//		b_select.addActionListener(new ActionListener() {
//			JFileChooser chooser = new JFileChooser();
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(						
//						"JPG & GIF & PNG Images",
//						"jpg", "gif", "png");
//			
//					chooser.setFileFilter(filter);
//					
//					int ret = chooser.showOpenDialog(WithTalk.this);
//					if(ret != JFileChooser.APPROVE_OPTION) {
//						JOptionPane.showMessageDialog(WithTalk.this, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
//						return;
//				}
//				t_input.setText(chooser.getSelectedFile().getAbsolutePath());
//				sendImage();
//			}			
//			
//		});
//		
//		panel2.add(t_input, BorderLayout.CENTER);
////		panel2.add(b_send, BorderLayout.EAST);
//		JPanel p_button = new JPanel(new GridLayout(1,0));
//		p_button.add(b_select);
//		p_button.add(b_send);
//		panel2.add(p_button,BorderLayout.EAST);
//		t_input.setEnabled(false);
//		b_select.setEnabled(false);
//		b_send.setEnabled(false);
//		
//		
//		return panel2;	
//	}
//	
//	private JPanel createInfoPanel() {
//		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		
//		JLabel id = new JLabel("아이디: ");
//		JLabel serverAddr = new JLabel("서버주소: ");
//		JLabel portNum = new JLabel("포트번호: ");
//		t_userID = new JTextField(7);
//		t_hostAddr = new JTextField(12);
//		t_portNum = new JTextField(5);
//		
//		t_userID.setText("guest" + getLocalAddr().split("\\.")[3]);
//		
//		t_hostAddr.setText(this.serverAddress);  // 서버 주소 설정
//	    t_portNum.setText(String.valueOf(this.serverPort)); // 포트번호 설정
//	    
//	    t_portNum.setHorizontalAlignment(JTextField.CENTER);
//	    
//		infoPanel.add(id);
//		infoPanel.add(t_userID);
//		infoPanel.add(serverAddr);
//		infoPanel.add(t_hostAddr);
//		infoPanel.add(portNum);
//		infoPanel.add(t_portNum);
//		
//		return infoPanel;
//	}
//	
//	private JPanel createControlPanel() {
//		JPanel panel3 = new JPanel(new GridLayout(0,3));
//		
//		b_connect = new JButton("접속하기");
//		b_connect.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				WithTalk.this.serverAddress = t_hostAddr.getText();
//				WithTalk.this.serverPort = Integer.parseInt(t_portNum.getText());
//
//				try {
//				connectToServer();
//				sendUserID();
//				}catch(UnknownHostException e1) {
//					printDisplay("서버 주소와 포트번호를 확인하세요: " + e1.getMessage());
//					return;
//				}
//				catch(IOException e1) {
//					printDisplay("서버와의 연결 오류: " + e1.getMessage());
//					return;
//				}
//				b_connect.setEnabled(false);
//				b_disconnect.setEnabled(true);
//				t_input.setEnabled(true);
//				b_send.setEnabled(true);
//				b_exit.setEnabled(false);
//				b_select.setEnabled(true);
//				
//				t_userID.setEditable(false);
//				t_hostAddr.setEditable(false);
//				t_portNum.setEditable(false);
//			}
//		});
//		
//		b_disconnect = new JButton("접속끊기");
//		b_disconnect.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				disconnect();
//				b_send.setEnabled(false);
//				b_disconnect.setEnabled(false);
//				b_connect.setEnabled(true);
//				b_exit.setEnabled(true);
//				t_input.setEnabled(false);
//			}			
//		});
//		
//		b_exit = new JButton("종료하기");
//		b_exit.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.exit(0);
//			}			
//		});
//		
//		panel3.add(b_connect);
//		panel3.add(b_disconnect);
//		panel3.add(b_exit);
//		
//		b_disconnect.setEnabled(false);
//		
//		return panel3;
//	}
//
//	private String getLocalAddr() {
//		InetAddress local = null;
//		String addr = "";
//		try {
//			local = InetAddress.getLocalHost();
//			addr = local.getHostAddress();
//			System.out.println(addr);
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		return addr;		
//	}
//	
//	private void connectToServer() throws UnknownHostException, IOException {
//			socket = new Socket();
//			SocketAddress sa = new InetSocketAddress(serverAddress, serverPort);
//			socket.connect(sa,3000);
//			
////			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
////			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));			
//			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//			
//			receiveThread = new Thread(new Runnable() {
//				
//				private ObjectInputStream in; 
//				
//				private void receiveMessage() {		
//					try {					
////						String inMsg = ((BufferedReader)in).readLine();
//						ChatMsg inMsg = (ChatMsg)in.readObject();
//						if(inMsg == null) {
//							disconnect();
//							printDisplay("서버 연결 끊김");
//							return;
//						}
////						printDisplay(inMsg);
//						switch(inMsg.mode) {
//						case ChatMsg.MODE_TX_STRING:
//							printDisplay(inMsg.userID + ": " + inMsg.message);
//							break;
//						case ChatMsg.MODE_TX_IMAGE:
//							printDisplay(inMsg.userID + ": " + inMsg.message);
//							printDisplay(inMsg.image);
//							break;
//						}
//						
//					} catch (IOException e) {
//						printDisplay("연결을 종료했습니다.");			
//					}catch (ClassNotFoundException e) {
//						printDisplay("잘못된 객체가 전달되었습니다.");
//					}
//				}
//				
//				@Override
//				public void run() {
//					try {
//						in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//					} catch (IOException e) {
//						printDisplay("입력 스트림이 열리지 않음");
//					}
//					while(receiveThread == Thread.currentThread()) {
//						receiveMessage();
//					}
//				}			
//			});
//			receiveThread.start();
//	}
//	
//	private void disconnect() {
//		send(new ChatMsg(uid, ChatMsg.MODE_LOGOUT));
//		
//		try {
//			receiveThread = null;
//			socket.close();
//		} catch (IOException e) {
//			System.err.println("클라이언트 닫기 오류> " + e.getMessage());
//			System.exit(-1);
//		}
//	}
//	
//	private void printDisplay(String msg) {
////		t_display.append(msg + "\n");
////		t_display.setCaretPosition(t_display.getDocument().getLength());
//		
//		int len = t_display.getDocument().getLength();
//		try {
//			document.insertString(len, msg + "\n", null);
//		}catch(BadLocationException e) {
//			e.printStackTrace();
//		}
//		t_display.setCaretPosition(len);
//	}
//	private void printDisplay(ImageIcon icon) {
//		 t_display.setCaretPosition(t_display.getDocument().getLength());
//		 if(icon.getIconHeight() > 400) {
//			 Image img = icon.getImage();
//			 Image changeImg = img.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
//			 icon = new ImageIcon(changeImg);			 					 
//		 }
//		 t_display.insertIcon(icon);
//		 
//		 printDisplay("");
//		 t_input.setText("");
//	}
//	
//	private void send(ChatMsg msg) {
//		try {
//			out.writeObject(msg);
//			out.flush();
//		} catch(IOException e) {
//			System.err.println("클라이언트 일반 전송 오류> " + e.getMessage());
//		}
//	}
//	
//	private void sendMessage() {
//		String msg = t_input.getText();
//		if (msg.isEmpty()) return;	
//		send(new ChatMsg(uid, ChatMsg.MODE_TX_STRING, msg));  
//        t_input.setText("");
//    }
//	
//	private void sendUserID() {
//		uid = t_userID.getText();
//		send(new ChatMsg(uid, ChatMsg.MODE_LOGIN));	
//	}
//	
//	private void sendImage() {
//		String filename = t_input.getText().strip();
//		if(filename.isEmpty()) return;
//		
//		File file = new File(filename);
//		if(!file.exists()) {
//			printDisplay(">> 파일이 존재하지 않습니다: " + filename);
//			return;
//		}
//		ImageIcon icon = new ImageIcon(filename);
//		send(new ChatMsg(uid, ChatMsg.MODE_TX_IMAGE, file.getName(), icon));
//		
//		t_input.setText("");
//	}
//
//	public static void main(String[] args) {
//		String serverAddress = "localhost";
//		int serverPort = 54321;
//		WithTalk c = new WithTalk(serverAddress, serverPort);
//
//	}
//
//}
