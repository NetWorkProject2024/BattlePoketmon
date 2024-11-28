import java.io.Serializable;
import java.util.Vector;

public class World implements Serializable{
	public Vector<Player> users = new Vector<Player>();
	private int maxNum;
	private int id = -1;
	private WorldFrame frame;
	private int readyCount = 0;


	public World(int maxNum, Vector<Player> users, int worldId) {
		this.users = users;
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
		user.getClient().sendPlayerReady(false);
		user.setWorld(this);
		System.out.println(user.getWorld());
		this.frame.create();
		
		System.out.println(this.users.size() + "들어왔을 때 월드 인원 수");
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
	

	public WorldFrame getWorldFrame() {
		return this.frame;
	}
	public int getWorldId() {
		return this.id;
	}
	public void increaseReadyCount() {
		this.readyCount++;
	}
	public void decreaseReadyCount() {
		this.readyCount--;
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
