//
//
import java.io.Serializable;
//
//import javax.swing.ImageIcon;
//
//public class ChatMsg implements Serializable{
//	public final static int MODE_LOGIN = 0x1;
//	public final static int MODE_LOGOUT = 0x2;
//	public final static int MODE_TX_STRING = 0x10;
//	public final static int MODE_TX_FILE = 0x20;
//	public final static int MODE_TX_IMAGE = 0x40;
//	public final static int MODE_TX_POS = 0x100;
//	
//	String userID;
//	int mode;
//	String message;
//	ImageIcon image;/////
//	Object object;
//	long size;
//	
//	public ChatMsg(String userID, int code, String message, Object object, long size) {
//		this.userID = userID;
//		this.mode = code;
//		this.message = message;
//		this.object = object;
//		this.size = size;
//	}
//	
//	public ChatMsg(String userID, int code, String message,  Object object) {
//		this(userID, code, null, object, 0);
//	}
//	public ChatMsg(String userID, int code) {
//		this(userID, code, null, null);
//	}
//	public ChatMsg(String userID, int code, String message) {
//		this(userID, code, message, null);
//	}	
//	public ChatMsg(String userID, int code, Object object) {
//		this(userID, code, null, object, 0);
//	}
//	public ChatMsg(String userID, int code, String filename, long size) {
//		this(userID, code, filename, null, size);
//	}
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
//
//
////import java.io.Serializable;
////
////import javax.swing.ImageIcon;
////
////public class ChatMsg implements Serializable{
////	public final static int MODE_LOGIN =0x1;
////	public final static int MODE_LOGOUT = 0x2;
////	public final static int MODE_TX_STRING = 0x10;
////	public final static int MODE_TX_FILE = 0x20;
////	public final static int MODE_TX_IMAGE = 0x40;
////	
////	String userID;
////	int mode;
////	String message;
////	ImageIcon image;
////	long size;
////	
////	public ChatMsg(String userID, int code, String message, ImageIcon image, long size) {
////		this.userID = userID;
////		this.mode = code;
////		this.message = message;
////		this.image = image;
////		this.size = size;
////		
////	}
////	public ChatMsg(String userID, int code, String message, ImageIcon image) {
////		this(userID, code, message, image, 0);
////	}
////	public ChatMsg(String userID, int code) {
////		this(userID, code,null,null);
////	}
////	public ChatMsg(String userID, int code, String message) {
////		this(userID, code, message, null);
////	}
////	public ChatMsg(String userID, int code, ImageIcon image) {
////		this(userID, code, null, image);
////	}
////	public ChatMsg(String userID, int code, String filename, long size) {
////		this(userID, code, filename, null, size);
////	}
////}
import java.util.Vector;

import javax.swing.ImageIcon;

public class ChatMsg implements Serializable{
	public final static int MODE_LOGIN = 0x1;
	public final static int MODE_LOGOUT = 0x2;
	public final static int MODE_TX_STRING = 0x10;
	public final static int MODE_TX_FILE = 0x20;
	public final static int MODE_TX_IMAGE = 0x40;
	public final static int MODE_TX_POS = 0x100;
	public final static int MODE_ROOM_UPDATE = 0x3;
	public final static int MODE_ROOM_LIST_REQUEST = 0x4;
	public final static int MODE_ROOM_ENTER = 0x5;
	
	public Player player;
	int mode;
	String message;
	ImageIcon image;/////
	Object object;
	long size;
	public ReadyRoom room;
	public Vector <ReadyRoom> serverRooms;
	
	 
	public ChatMsg(Player player, int code, String message, Object object, long size, ReadyRoom room) {
		this.player = player;
		this.mode = code;
		this.message = message;
		this.object = object;
		this.size = size;
		this.room = null;
		
	}
	
	public ChatMsg(Player player, int code, String message,  Object object) {
		this(player, code, null, object, 0, null);
	}
	public ChatMsg(Player player, int code) {
		this(player, code, null, null, 0, null);
	}
	public ChatMsg(Player player, int code, String message) {
//		this(player, code, message, null);
		this.player=player;
	    this.mode = code;
	    this.message = message;
	    this.object = null;
	    this.size =0;
	}
	public ChatMsg(Player player, int code, ReadyRoom room, long roomN) {
//		this(player, code, null, null, 0, room);
		this.player=player;
	    this.mode = code;
	    this.message = null;
	    this.object = null;
	    this.size =0;
	    this.room = room;
	}
	public ChatMsg(Player player, int code, Vector<ReadyRoom> serverRooms, long userN) {
		this.player=player;
	    this.mode = code;
	    this.message = null;
	    this.object = null;
	    this.size =userN;
	    this.serverRooms = serverRooms;
	    System.out.println(serverRooms + "메시지");
	}
	public ChatMsg(Player player, int code, long c) {
		this(player, code, null, null, c, null);
	}
	
	public ChatMsg(Player player, int code, Object object) {
		this(player, code, null, object, 0, null);
	}
	public ChatMsg(Player player, int code, String filename, long size) {
		this(player, code, filename, null, size, null);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

