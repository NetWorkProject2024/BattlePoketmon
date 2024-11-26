
import java.io.Serializable;

import java.util.Vector;

import javax.swing.ImageIcon;

public class ChatMsg implements Serializable{
	public final static int MODE_LOGIN = 0x1;
	public final static int MODE_LOGOUT = 0x2;
	public final static int MODE_TX_STRING = 0x10;
	public final static int MODE_TX_POS = 0x100;
	public final static int MODE_ROOM_UPDATE = 0x3;
	public final static int MODE_ROOM_ENTER = 0x5;
	public final static int MODE_ROOM_EXIT = 0x9;
	public final static int MODE_ROOM_CREATE = 0x6;
	public final static int MODE_ROOM_PLAYERREADY = 0x7;
	public final static int MODE_WORlD_ENTER = 0x8;
	
	
	public Player player;
	int mode;
	String message;
	Object object;
	long size;
	public Vector <ReadyRoom> serverRooms;
	
	 
	public ChatMsg(Player player, int code, String message, Object object, long size) {
		this.player = player;
		this.mode = code;
		this.message = message;
		this.object = object;
		this.size = size;
//		this.room = room;
		this.serverRooms = null;
		
	}
	
	public ChatMsg(Player player, int code, String message,  Object object) {
		this(player, code, null, object, 0);
	}
	public ChatMsg(Player player, int code) {
		this(player, code, null, null, 0);
	}
	
	public ChatMsg(Player player, int code, String message) {
//		this(player, code, message, null);
		this.player=player;
	    this.mode = code;
	    this.message = message;
	    this.object = null;
	    this.size =0;
	}
	
	public ChatMsg(Player player, int code, Vector<ReadyRoom> serverRooms) {
		this.player=player;
	    this.mode = code;
	    this.serverRooms = serverRooms;
	}
	
	public ChatMsg(Player player, int code, Vector<ReadyRoom> serverRooms, long size) {
		this.player=player;
	    this.mode = code;
	    this.message = null;
	    this.object = null;
	    this.size =size;
	    this.serverRooms = serverRooms;
	}
	public ChatMsg(Player player, int code, long c) {
		this(player, code, null, null, c);
	}
	
	public ChatMsg(Player player, int code, Object object) {
		this(player, code, null, object, 0);
	}
	public ChatMsg(Player player, int code, String filename, long size) {
		this(player, code, filename, null, size);
	}
	
	public ChatMsg(Player player, int code, Object object, long size) {
		this(player, code, null, object, size);
	}
}

