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
		System.out.println(user.getPoketmon() + "유저 포켓몬 소지 유무");
		if (this.frame == null) {
            this.frame = new WorldFrame(this, user); // frame 객체가 없으면 새로 생성            
        }
//		user.getClient().sendPlayerReady(false);
		user.setWorld(this);
		user.getClient().sendWorldReady(false);
		System.out.println(user.getWorld());
		this.frame.create(user);
		System.out.println(user.getWorld() + "내 월드" + user.getWorld().id);
		
		System.out.println(this.users.size() + "들어왔을 때 월드 인원 수");
		System.out.println(user.getWorld().users + "월드 내 유저들 확인");

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
//	public void increaseReadyCount() {
//		this.readyCount++;
//	}
//	public void decreaseReadyCount() {
//		this.readyCount--;
//	}
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
