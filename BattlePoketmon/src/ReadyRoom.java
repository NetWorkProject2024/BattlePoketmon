import java.util.Vector;

public class ReadyRoom {
	private String roomName = "123";
	private Vector<ReadyRoomPlayer> users=new Vector<ReadyRoomPlayer>();
	private int maxPlayerCount;
	private int currentPlayerCount=0;
	private boolean enable;
	private int id;
	
	public ReadyRoom(String roomName, ReadyRoomPlayer user, int maxPlayerCount, int id) {
		this.roomName=roomName;
		this.users.add(user);
		this.maxPlayerCount=maxPlayerCount;
		this.currentPlayerCount=1;
		this.enable=true;
		this.id = id;
	}
	public void enterRoom(ReadyRoomPlayer user) {
		this.users.add(user);
		this.currentPlayerCount++;
		if(this.currentPlayerCount >= this.maxPlayerCount) {
			this.enable=false;
		}
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
}
