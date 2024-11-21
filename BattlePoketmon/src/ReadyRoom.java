import java.io.Serializable;
import java.util.Vector;

public class ReadyRoom implements Serializable{
	private String roomName = "123";
	private Vector<Player> users=new Vector<Player>();
	private int maxPlayerCount;
	private int currentPlayerCount=0;
	private boolean enable;
	public int roomId = 0;
	private transient ReadyRoomFrame frame;
	
	public ReadyRoom(String roomName, Player user, int maxPlayerCount, int id) {
		this.roomName=roomName;
		this.users.add(user);
		this.maxPlayerCount=maxPlayerCount;
		this.currentPlayerCount=0;
		this.enable=true;
		this.roomId =id;
		this.frame = new ReadyRoomFrame(this);
	}
	
	public void enterRoom(Player user) {
		if (this.frame == null) {
            this.frame = new ReadyRoomFrame(this); // frame 객체가 없으면 새로 생성
        }
		this.frame.create();
		this.users.add(user);
		poketmonPick(user);
		this.currentPlayerCount++;
		if(this.currentPlayerCount >= this.maxPlayerCount) {
			this.enable=false;
		}
		this.frame.addUser(user);
//		this.frame.createUserInfoPanel(user.getPlayerName(), false); // 플레이어 목록 업데이트
		this.frame.repaint();
		this.frame.updateUserList();
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
	
	public Vector<Player> getUsers() {
	    return users;
	}
	public void addUser(Player player) {
	    if (!users.contains(player)) {//중복X
	        users.add(player);
	        currentPlayerCount++;
	    }
	}
	
	public int getCurrentPlayerCount() {
		return currentPlayerCount;
	}

}
