
import java.io.Serializable;

import javax.swing.ImageIcon;

public class Player implements Serializable{
   private int userId = 0;
   private String playerName = "";
   private int poketmonIdx = 0;
   private Poketmon poketmon = null;
   private boolean ready = false;
   private int coin=100;
   private ReadyRoom room = new ReadyRoom();
   private World world;
   private Player other;
   private transient Client client;
   private boolean turn;
   private int winCount = 0;
   private int loseCount = 0;
   private ImageIcon profile;
   
   public Player(String name, Client client) {
      this.playerName = name;
      this.client = client;
      
      this.profile = new ImageIcon(getClass().getResource("/poketmon/default.png"));
      StartFrame startFrame = new StartFrame(this);
      startFrame.create();
   }
   
   public void setPoketmonIdx(int poketmonIdx) {
      this.poketmonIdx = poketmonIdx;
   }
   public int getPoketmonIdx() {
      return this.poketmonIdx;
   }
   public boolean getReady() {
      return ready;
   }
   public void setReady(boolean ready) {
      this.ready = ready;
   }
   public void setReadyRoom(ReadyRoom myRoom) {
      this.room = myRoom;
   }
   public void setWorld(World myWorld) {
	   this.world = myWorld;
   }

   
   public int getId() {
      return userId;
   }
   public Client getClient() {
      return client;
   }
   public void setId(long size) {
      this.userId = (int) size;
   }
   
   public String getPlayerName() {
        return this.playerName;
    }
   public void setPlayerName(String newName) {
       this.playerName = newName;
   }
   public ReadyRoom getReadyRoom() {
      return room;
   }
   public void addCoin(int value) {
      this.coin += value;
   }
   public int getCoin() {
      return this.coin;
   }
   public void setCoin(int coin) {
	   this.coin = coin;
   }
   public Poketmon getPoketmon() {
	   return this.poketmon;
   }
   
   public World getWorld() {
	   return this.world;
   }

   public Player getOtherPlayer() {
	   return this.other;
   }
   public void setOtherPlayer(Player other) {
	   this.other = other;
   }
   public void setPoketmon(Poketmon poketmon) {
	   this.poketmon=poketmon;
   }
   public void setProfile(ImageIcon profile) {
	   this.profile = profile;
   }
   public ImageIcon getProfile() {
	   return this.profile;
   }
   public void setTurn(long size) {
	   if(size == (long)0) {
		   this.turn = false;
	   }
	   else {
		   this.turn = true;
	   }
   }
   public boolean getTurn() {
	   return this.turn;
   }
   public void setWinCount(int winCount) {
	   this.winCount = winCount;
   }
   public int getWinCount() {
	   return this.winCount;
   }
   public void setLoseCount(int loseCount) {
	   this.loseCount=loseCount;
   }
   public int getLoseCount() {
	   return this.loseCount;
   }
   public void increaseWinCount() {
	   this.winCount++;
   }
   public void increaseLoseCount() {
	   this.loseCount++;
   }
}

