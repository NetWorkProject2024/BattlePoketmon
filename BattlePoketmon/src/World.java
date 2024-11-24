import java.util.Vector;

public class World {
	public Vector<Player> users = new Vector<Player>();
	private int maxNum;
	private int coinCount = 10;
	private int id = -1;
	private int Timer = -1;


	public World(int maxNum, Vector<Player> users, int worldId) {
		this.users = users;
	}






	public int getTimer() {
		return this.Timer;
	}

}
