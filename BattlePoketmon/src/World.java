import java.io.Serializable;
import java.util.Vector;

public class World implements Serializable{
	public Vector<Player> users = new Vector<Player>();
	private int maxNum;
	private int id = -1;
	private int Timer = -1;
	private WorldFrame frame;


	public World(int maxNum, Vector<Player> users, int worldId) {
		this.users = users;
		this.maxNum = maxNum;
		this.id = worldId;
		this.Timer = 60;
	}


	public void enterWorld(Player user) {
		Poketmon newPoketmon = new Poketmon(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()));
		newPoketmon.createSkills();
		user.setPoketmon(newPoketmon);
		System.out.println(user.getPoketmon() + "유저 포켓몬 소지 유무");
		if (this.frame == null) {
            this.frame = new WorldFrame(this, user); // frame 객체가 없으면 새로 생성
        }
		
		
		
		this.frame.create();
		
//		this.frame.repaint();
	}

	public WorldFrame getWorldFrame() {
		return this.frame;
	}


	public int getTimer() {
		return this.Timer;
	}

}
