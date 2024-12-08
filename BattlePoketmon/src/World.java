import java.io.Serializable;
import java.util.Vector;

public class World implements Serializable{
	public Vector<Player> users = new Vector<Player>();
	private int maxNum;
	private int id = -1;
	private transient WorldFrame frame;
	private int readyCount = 0;

	public World() {
		
	}
	public World(int maxNum, Vector<Player> users, int worldId) {
		
		this.users = new Vector<Player>();
		for(int i=0; i < users.size(); i++) {
			this.users.add(users.elementAt(i));
		}
		
		this.maxNum = maxNum;
		this.id = worldId;
	}


	public void enterWorld(Player user) {
		Poketmon newPoketmon = new Poketmon(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()));
		newPoketmon.createSkills();
		user.setPoketmon(newPoketmon);
		if (this.frame == null) {
            this.frame = new WorldFrame(this, user); // frame 객체가 없으면 새로 생성            
        }
		user.setWorld(this);
		user.getClient().sendWorldReady(false);
		this.frame.create(user);
		this.frame.repaint();
		this.frame.updateUserList();
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
	public void changeWinLoseCount(Player changedUser, boolean state) {
		for(int i=0; i < this.users.size(); i++) {
			if(this.users.elementAt(i).getId()==changedUser.getId()) {
				if(state) {
					this.users.elementAt(i).increaseWinCount();
				}
				else {
					this.users.elementAt(i).increaseLoseCount();
				}
			}
			
		}
		this.frame.repaint();
		this.frame.updateUserList();
	}

	public WorldFrame getWorldFrame() {
		return this.frame;
	}
	public int getWorldId() {
		return this.id;
	}
	public void setReadyCount(int count) {
		this.readyCount=count;
	}
	public int getReadyCount() {
		return this.readyCount;
	}
	public int getMaxNum() {
		return this.maxNum;
	}
	public Vector<Player> getUsers() {
	    return users;
	}
}
