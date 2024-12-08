
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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client{
	private transient Home home = null;
	
	private String serverAddress;
	private int serverPort;
	
	private transient Socket socket;

	private transient ObjectOutputStream out;
	private BattleFrame battleFrame=null;
	private transient Thread receiveThread = null;
	private int userId = 0;
	private Player player;
	private ReadyRoom room = null;
	private World world = null;
	private String name;
	
	
	public Client(String serverAddress, int serverPort) {
//		this.id = userId++;
		this.serverAddress=serverAddress;
		this.serverPort = serverPort;
		
		this.player = new Player(name, this);
		
		try {
			connectToServer(this.player);
//			sendUserID();
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
							break;
						case ChatMsg.MODE_TX_STRING:
							break;
						case ChatMsg.MODE_HOME_UPDATE:
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
								
								if(player.getId() == inMsg.player.getId()) {
									player.setReadyRoom(((ReadyRoom)inMsg.object));
									player.getReadyRoom().enterRoom(player);
								}
								else {
									player.getReadyRoom().getUsers().clear();
									for(int i=0; i < ((ReadyRoom)inMsg.object).getUsers().size(); i++) {
										player.getReadyRoom().addUser(((ReadyRoom)inMsg.object).getUsers().elementAt(i));
									}
									
									player.getReadyRoom().getRoomFrame().repaint();
									player.getReadyRoom().getRoomFrame().updateUserList();
								}
							
							break;
						case ChatMsg.MODE_ROOM_EXIT:	
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
							((World)inMsg.object).enterWorld(player);
							System.out.println("생성한 월드의 인원수: " + player.getWorld().users);
							break;
							
						case ChatMsg.MODE_WORLD_PLAYERREADY:
							boolean worldReadyState = false;
							if(inMsg.size == (long)0) {
								worldReadyState = false;
							}
							else if(inMsg.size == (long)1){
								worldReadyState = true;
							}
							System.out.println("서버에서 준비 상태 받는 중 >> player : "+inMsg.player+" , ready 상태 : "+worldReadyState + "ㅇㅇ" + ((World)inMsg.object).users);
							if(player.getId()==inMsg.player.getId()) {
								player.setReady(worldReadyState);
							}
							player.getWorld().changeReadyState(inMsg.player, worldReadyState);
							
							System.out.println(player.getReadyRoom().getUsers() + "레디 상태 받았을 때 인원수");
							break;
							
						case ChatMsg.MODE_MATCHING:
							if(player.getId()==inMsg.player.getId()) {
								
								player.setOtherPlayer((Player)inMsg.object);
								player.getOtherPlayer().setPoketmon((Poketmon)inMsg.object2);
								System.out.println("포켓몬 소유 확인 : " + player.getPoketmon()+", "+ player.getOtherPlayer().getPoketmon());
								player.setTurn(inMsg.size);
								player.getOtherPlayer().setTurn(1-(inMsg.size));
								battleFrame = new BattleFrame(player.getOtherPlayer(), player);
								battleFrame.create();
							}
							break;
						case ChatMsg.MODE_ATTACK:
							System.out.println("공격받음" + inMsg.player.getId());
							int result = 0;
							System.out.println(result + "결과값");
							System.out.println((int)inMsg.size);
//							System.out.println(player.getPoketmon().getDefensePower());
							//sendResult(player, ChatMsg.MODE_ATTACK_RESULT, result);
							if(inMsg.player.getId()==player.getId()) {
								
								result = player.getPoketmon().getCurrentHP();
								result -= (int)(inMsg.size/10);
								
//								if(player.getPoketmon().getType().getStrength().getName().equals(((Type)inMsg.object).getName())) {
//									result -= 2;
//								}
//								else if(player.getPoketmon().getType().getWeakness().getName().equals(((Type)inMsg.object).getName())) {
//									result += 2;
//								}
								//물 -> 불-강하게
								//물-> 풀 - 약하게
								//풀 -> 물 - 강하게
								//풀 -> 불 - 약하게
								//불 -> 풀 - 강하게
								//불 -> 물-약하게
								
								System.out.println("나 " + player.getId());
								player.getPoketmon().setCurrentHP(result);
								player.setTurn(1);
								player.getOtherPlayer().setTurn(0);

								if(result<=0) {
									sendBattleResult(player, ChatMsg.MODE_BATTLE_END, player.getOtherPlayer());//진 사람이 보낸다
									System.out.println(player.getId() + "가 졌다" + player.getOtherPlayer().getId() + "와의 배틀에서");
								}
							}
							
							battleFrame.repaint();
							battleFrame.btnEnabled(true);
							
							
							break;
							
						case ChatMsg.MODE_BATTLE_END:
							Player winner = (Player)(inMsg.object);			
							showMessage(
					            "배틀 종료",
					            "배틀 결과\n승자: " + winner.getPlayerName()  + "\n패자: " +inMsg.player.getPlayerName()					            
					        );
							if (battleFrame != null) {
					            battleFrame.battleFrameDispose(); // 배틀 종료 시 배틀 프레임 닫기
					            battleFrame = null;
					        }
							player.getPoketmon().setCurrentHP(100);
							player.getOtherPlayer().getPoketmon().setCurrentHP(100);
							System.out.println("포켓몬 체력 초기화 >> 내 포켓몬 : "+player.getPoketmon().getCurrentHP()+" 상대방 포켓몬 : "+player.getOtherPlayer().getPoketmon().getCurrentHP());
							
							if(player.getId() == winner.getId()) {
								player.increaseWinCount();
							}
							else {
								player.increaseLoseCount();
							}
							sendWorldReady(false);
							
							break;
						case ChatMsg.MODE_BATTLE_RESULT:
							boolean isWin = false;
							if(inMsg.size == (long)0) {
								isWin = false;
								if(inMsg.player.getId()==player.getId()) {
									player.addCoin(100);
								}
							}
							else if(inMsg.size == (long)1){
								isWin = true;
								if(inMsg.player.getId()==player.getId()) {
									player.addCoin(200);
								}
							}
							player.getWorld().changeWinLoseCount(inMsg.player, isWin);

							break;
						case ChatMsg.MODE_WORLD_END:
							System.out.println("월드 엔딩");
							player.getWorld().getWorldFrame().worldFrameDispose();
							
							//
							if(battleFrame!=null) {
								battleFrame.battleFrameDispose();
							}
							
							ResultFrame resultFrame = new ResultFrame(player.getWorld().users);
							resultFrame.create();
							//엔딩 프레임 얻기
							
							//정보 초기화
							player.setCoin(100);
							player.setLoseCount(0);
							player.setWinCount(0);
							sendPlayerReady(false);
							break;
						}
						
						
					}catch (IOException e) {
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
						System.out.println("클라이언트의 객체 인풋 스트림 생성 실패");
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
	public void sendUserProfile() {
		send(new ChatMsg(player, ChatMsg.MODE_IMG_REQUEST, player.getProfile()));
	}
	
	public void sendUserID() {
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
	
	public void sendWorldReady(boolean state) {
		int size = 0;
		if(state) {
			
			size = 1;
		}
		else {
			size = 0;
		}
		System.out.println("월드 입장 중 서버에게 준비 상태 공지>> player : " +this.player +" , ready 상태 : "+state +" , world : " +this.player.getWorld() + " 내 포켓몬: " + this.player.getPoketmon());
		send(new ChatMsg(this.player, ChatMsg.MODE_WORLD_PLAYERREADY, this.player.getPoketmon(),size));
	}
	public void sendAttack(Player other, int mode, Object object, int attack) {
		send(new ChatMsg(other, mode, object, attack));
	}
	public JFrame getHome() {
		return home;
	}
	public void sendBattleResult(Player loser, int mode, Player winner) {//배틀 승부 결과 보내기
		send(new ChatMsg(loser, mode, winner));
	}
	
	
	private void showMessage(String title, String message) {
	    JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	    
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	
	public static void main(String[] args) {
//		String serverAddress = "172.29.81.194";
//				172.29.81.194
		String serverAddress="localhost";
		int serverPort = 54321;
		
		Client client = new Client(serverAddress, serverPort);

	}
}

