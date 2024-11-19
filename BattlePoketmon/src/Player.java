
public class Player {
	private static int userId = 1;
	private String playerName = "";
	private int id = 0;
	
	public Player(String name) {
		this.playerName = name;
		this.id = userId++;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPlayerName() {
        return playerName;
    }
	
	
	 @Override
	    public String toString() {
	        return "BattlePlayer [Name=" + getPlayerName() + ", ID=" + getId() + "]";
	    }

	
}
