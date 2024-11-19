import java.util.Vector;

public class ReadyRoom {
	private String roomName = "123";
	private Vector<ReadyRoomPlayer> users=new Vector<ReadyRoomPlayer>();
	private int maxPlayerCount;
	private int currentPlayerCount=0;
	private boolean enable;
	private int id;
	private ReadyRoomFrame frame;
	public ReadyRoom(String roomName, Player user, int maxPlayerCount, int id) {
		this.roomName=roomName;
		ReadyRoomPlayer roomUser = new ReadyRoomPlayer(user);
		this.users.add(roomUser);
		this.maxPlayerCount=maxPlayerCount;
		this.currentPlayerCount=1;
		this.enable=true;
		this.id = id;
		this.frame = new ReadyRoomFrame(this);
		this.frame.create();
	}
	public void enterRoom(Player user) {
		ReadyRoomPlayer roomUser = new ReadyRoomPlayer(user);
		this.users.add(roomUser);
		poketmonPick(roomUser);
		this.currentPlayerCount++;
		if(this.currentPlayerCount >= this.maxPlayerCount) {
			this.enable=false;
		}
		this.frame.repaint();
	}
	public void exitRoom(ReadyRoomPlayer user) {
		this.users.remove(user);
		this.currentPlayerCount--;
		this.enable=true;
	}
	public void gameStart(World world) {
		for(int i=0; i < this.users.size(); i++) {
			world.users.add(new BattlePlayer(this.users.elementAt(i)));
		}
	}

	public void poketmonPick(ReadyRoomPlayer user, int idx) {
		//user.setPoketmonIdx(idx);
	}
	public void poketmonPick(ReadyRoomPlayer user) {
		poketmonPick(user, 0);
	}
	public String getRoomName() {
		return roomName;
	}
	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}
	public int getCurrentPlayerCount() {
		return currentPlayerCount;
	}
	
	public static void main(String[] args) {
		new ReadyRoom("123", new Player(), 4, 0);

	}
}
