import java.io.Serializable;
import java.util.Vector;

public class ReadyRoom implements Serializable{
	private String roomName = "123";
	private Vector<Player> users=new Vector<Player>();
	private int maxPlayerCount;
	private int currentPlayerCount=0;
	private boolean enable;
	private int id;
	private transient ReadyRoomFrame frame;
	public ReadyRoom(String roomName, Player user, int maxPlayerCount, int id) {
		this.roomName=roomName;
		this.users.add(user);
		this.maxPlayerCount=maxPlayerCount;
		this.currentPlayerCount=1;
		this.enable=true;
		this.id = id;
		this.frame = new ReadyRoomFrame(this);

	}
	public void enterRoom(Player user) {
		this.frame.create();
		this.users.add(user);
		poketmonPick(user);
		this.currentPlayerCount++;
		if(this.currentPlayerCount >= this.maxPlayerCount) {
			this.enable=false;
		}
		this.frame.repaint();
	}
	public void exitRoom(Player user) {
		this.users.remove(user);
		this.currentPlayerCount--;
		this.enable=true;
	}
	public void gameStart(World world) {
		for(int i=0; i < this.users.size(); i++) {
			world.users.add(this.users.elementAt(i));
		}
	}

	public void poketmonPick(Player user, int idx) {
		//user.setPoketmonIdx(idx);
	}
	public void poketmonPick(Player user) {
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

}
