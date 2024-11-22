import java.util.Vector;

public class World {
	private String roomName = "123";
	public Vector<Player> users = new Vector<Player>();
	private int maxNum;
	private int coinCount = 10;
	private int id = -1;
	private int Timer = -1;







	public String getRoomName() {
		return roomName;
	}


	public int getTimer() {
		return this.Timer;
	}

}
