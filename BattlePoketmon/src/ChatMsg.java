
import java.io.Serializable;

import java.util.Vector;

import javax.swing.ImageIcon;

public class ChatMsg implements Serializable{
	public final static int MODE_LOGIN = 0x1;
	public final static int MODE_LOGOUT = 0x2;
	public final static int MODE_TX_STRING = 0x10;
	public final static int MODE_HOME_UPDATE = 0x3;
	public final static int MODE_ROOM_ENTER = 0x5;
	public final static int MODE_ROOM_EXIT = 0x9;
	public final static int MODE_ROOM_CREATE = 0x6;
	public final static int MODE_ROOM_PLAYERREADY = 0x7;
	public final static int MODE_WORlD_ENTER = 0x8;
	public final static int MODE_ATTACK = 0x11;
	public final static int MODE_WORLD_PLAYERREADY = 0x12;
	public final static int MODE_MATCHING = 0x13;
	public final static int MODE_ATTACK_RESULT = 0x14;
	public final static int MODE_BATTLE_END = 0x15;
	public final static int MODE_BATTLE_RESULT = 0x16;
	public final static int MODE_WORLD_END = 0x17;
	public final static int MODE_IMG_REQUEST = 0x18;
	
	
	public Player player;
	public int mode;
	public String message;
	public Object object;
	public Object object2;
	public ImageIcon img;
	public long size;
	public Vector <ReadyRoom> serverRooms;
	public Vector <Player> worldPlayers; //월드 내 플레이어
	
	 
	public ChatMsg(Player player, int code, String message, Object object, long size) {
		this.player = player;
		this.mode = code;
		this.message = message;
		this.object = object;
		this.size = size;
		this.serverRooms = null;		
	}
	
	public ChatMsg(Player player, int code, String message,  Object object) {
		this(player, code, null, object, 0);
	}
	public ChatMsg(Player player, int code) {
		this(player, code, null, null, 0);
	}
	
	public ChatMsg(Player player, int code, String message) {
		this.player=player;
	    this.mode = code;
	    this.message = message;
	    this.object = null;
	    this.size =0;
	}
	public ChatMsg(Player player, int code, ImageIcon img) {
		this.player = player;
		this.mode = code;
		this.img = img;
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
		this.player=player;
	    this.mode = code;
	    this.object = object;
	}
	public ChatMsg(Player player, int code, Object object, Object object2, long size) {
		this.player=player;
	    this.mode = code;
	    this.object = object;
	    this.object2 =object2;
	    this.size = size;
	}
	public ChatMsg(Player player, int code, String filename, long size) {
		this(player, code, filename, null, size);
	}
	
	public ChatMsg(Player player, int code, Object object, long size) {
		this(player, code, null, object, size);
	}
}

