import java.io.Serializable;
import java.util.Vector;

public class ReadyRoom implements Serializable{
	private String roomName = "123";
	private Vector<Player> users=new Vector<Player>();
	private int maxPlayerCount;
	private int currentReadyCount=0;
	private boolean enable;
	public int roomId = -1;
	private transient ReadyRoomFrame frame;
	
	public ReadyRoom() {
		
	}
	public ReadyRoom(String roomName, Player user, int maxPlayerCount, int id) {
		this.roomName=roomName;
		this.users.add(user);
		this.maxPlayerCount=maxPlayerCount;
		this.enable=true;
		this.roomId =id;
	}
	
	public void enterRoom(Player user) {
		if (this.frame == null) {
            this.frame = new ReadyRoomFrame(this); // frame 객체가 없으면 새로 생성
        }
		this.frame.create(user.getClient().getHome(), user);
		poketmonPick(user);
		if(this.users.size() >= this.maxPlayerCount) {
			this.enable=false;
		}
		this.frame.repaint();
		this.frame.updateUserList();
	}
	public void exitRoom(Player user) {
		this.enable=true;
		user.getClient().sendExitRoom(user.getReadyRoom());
		
	}
   public void removeUser(Player player) {
       for(int i=0; i < users.size(); i++) {
          if(users.elementAt(i).getId()==player.getId()) {
             users.removeElementAt(i);
          }
       }
   }
	
	public void gameStart(World world) {
		for(int i=0; i < this.users.size(); i++) {
			world.users.add(this.users.elementAt(i));
		}
	}

	public void changeReadyState(Player changedUser, boolean state) {
		for(int i=0; i < this.users.size(); i++) {
			if(this.users.elementAt(i).getId()==changedUser.getId()) {
				this.users.elementAt(i).setReady(state);
			}
			
		}
		this.frame.repaint();
		this.frame.updateUserList();
	}
	public void poketmonPick(Player user, int idx) {
		user.setPoketmonIdx(idx);
	}
	public void poketmonPick(Player user) {
		poketmonPick(user, 0);
	}
	public String getRoomName() {
		return roomName;
	}
	public ReadyRoomFrame getRoomFrame() {
		return frame;
	}
	public int getMaxPlayerCount() {
		return maxPlayerCount;
	}
	public void setMaxPlayerCount(int maxPlayerCount) {
		this.maxPlayerCount=maxPlayerCount;
	}
	public void setRoomName(String name) {
		this.roomName=new String(name);
	}
	public Vector<Player> getUsers() {
	    return users;
	}
	public void clearUser() {
		users.clear();
	}
	public int getCurrentReadyCount() {
		return this.currentReadyCount;
	}
	public void increaseCurrentReadyCount() {
		this.currentReadyCount++;
	}
	public void decreaseCurrentReadyCount() {
		this.currentReadyCount--;
	}
	public void addUser(Player player) {
	    if (!users.contains(player)) {//중복X
	        users.add(player);
	    }
	}
	
}
