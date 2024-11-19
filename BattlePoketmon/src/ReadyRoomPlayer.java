
public class ReadyRoomPlayer extends Player{
	
	private int poketmonIdx = 0;
	private boolean ready = false;
	private ReadyRoom room = null;
	
	public ReadyRoomPlayer(Player user, ReadyRoom room) {
		 super(user.getPlayerName()); //이름 유지
	        this.room = room;
	        this.setId(user.getId());//ID 유지		
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
	 
	// 테스트용
	 public static void main(String[] args) {
	        Player usera = new Player("A"); // 방 만드는 사람
	        Player userb = new Player("B"); // 방 만드는 사람
	        Player userc = new Player("C"); // 방 만드는 사람
	        ReadyRoom room = new ReadyRoom("roomName", usera, 6, usera.getId()); // 가상의 ReadyRoom 객체
	        ReadyRoomPlayer player1 = new ReadyRoomPlayer(userb, room);
	        ReadyRoomPlayer player2 = new ReadyRoomPlayer(userc, room);
	        ReadyRoomPlayer player3 = new ReadyRoomPlayer(usera, room);
	        ReadyRoomPlayer player4 = new ReadyRoomPlayer(usera, room);
	        
	        player1.setPoketmonIdx(1);
	        player1.getReady();

	        System.out.println(player3);
	        System.out.println(player1);
	        System.out.println(player2);
	        System.out.println(player4);  
	 }
}
