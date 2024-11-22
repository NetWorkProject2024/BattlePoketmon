
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.SwingUtilities;

public class Player implements Serializable{
	private int userId = 0;
	private String playerName = "";
	private int poketmonIdx = 0;
	private boolean ready = false;
	private ReadyRoom room = new ReadyRoom();
	private transient Client client;
	
	public Player(String name, Client client) {
		this.playerName = name;
		this.client = client;
	}
	
	public void setPoketmonIdx(int poketmonIdx) {
		this.poketmonIdx = poketmonIdx;
		
	}
	
	public void getReady() {
		this.ready = true;
	}
	public boolean isReady() {
		return this.ready;
	}
	public void setReadyRoom(ReadyRoom myRoom) {
		this.room = myRoom;
	}
	 @Override
	 public String toString() {// 테스트용
	     return "ReadyRoomPlayer [Name=" + getPlayerName() +
	                ", ID=" + getId() +
	                ", Ready=" + ready +
	                ", PoketmonIdx=" + poketmonIdx + "]";
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
	public ReadyRoom getReadyRoom() {
		return room;
	}
	
}

