import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Home extends JFrame{

//	public Vector<ReadyRoom> rooms = new Vector<>();
	public static int roomCount = 0;//방 만들때++
	private JPanel roomListPanel;
	private JScrollPane scrollPane;
	private JButton b_createRoom;
	private Player player;
	public static Vector<RoomBtn> roomBtns = new Vector<RoomBtn>();
	
	public Home(Player player, Vector<ReadyRoom> serverRooms) {
		super("Battle Poketmon");

        this.player = player;
		buildHomeGUI(serverRooms);
		setSize(400,300);
		setLocation(500,0);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void buildHomeGUI(Vector<ReadyRoom> serverRooms) {
		setLayout(new BorderLayout());
		add(topPanel(), BorderLayout.NORTH);
		add(createRoomListPanel(serverRooms), BorderLayout.CENTER);
		
	}
	private JPanel topPanel() {
		JPanel topP = new JPanel(new BorderLayout());		
		JLabel title = new JLabel("BattlePoketmon", JLabel.CENTER);
		b_createRoom = new JButton("방 만들기");
		b_createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateRoomDialog();
            }
        });
		topP.add(title, BorderLayout.CENTER);
		topP.add(b_createRoom, BorderLayout.EAST);
		return topP;
	}
	
	
	private JScrollPane createRoomListPanel(Vector<ReadyRoom> serverRooms) {
	    roomListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	    
//	    // 방 버튼 추가
	    for (ReadyRoom room : serverRooms) {
	        roomListPanel.add(createRoomBtn(room));
	    }
	    updateRoomListPanel(serverRooms);
	    JScrollPane scrollPane = new JScrollPane(roomListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    roomListPanel.setPreferredSize(calculatePreferredSize(serverRooms)); 
	    
	    return scrollPane;
	}
	
	//패널 크기 계산
	private Dimension calculatePreferredSize(Vector <ReadyRoom> rooms) {
	    int rows = (rooms.size() + 1) / 2;
	    int panelHeight = rows * 60;
	    return new Dimension(400, panelHeight);
	}
	
   public void updateRoomListPanel(Vector <ReadyRoom> rooms) {
	   roomListPanel.removeAll();
	    for (ReadyRoom room : rooms) {
	        roomListPanel.add(createRoomBtn(room));
	    }
	    roomListPanel.setPreferredSize(calculatePreferredSize(rooms));
	    roomListPanel.revalidate();
	    roomListPanel.repaint();
	   System.out.println("리스트업데이트");
    }
	
	private JButton createRoomBtn(ReadyRoom room) {
//		JLabel roomNameLabel = new JLabel(room.getRoomName(), JLabel.LEFT);
//        JLabel playerCountLabel = new JLabel(String.format("(%d / %d)", room.getCurrentPlayerCount(), room.getMaxPlayerCount()), JLabel.RIGHT);

//        JButton roomBtn = new JButton();
//        roomBtn.setLayout(new BorderLayout());
//        roomBtn.add(roomNameLabel, BorderLayout.WEST);
//        roomBtn.add(playerCountLabel, BorderLayout.EAST);

//        roomBtn.setPreferredSize(new Dimension(180, 50));
        
        // 방 버튼 클릭 -> 입장
//        roomBtn.addActionListener(e -> joinReadyRoom(room));
		RoomBtn newBtn = new RoomBtn(room);
        addBtn(newBtn);
		return newBtn.roomBtn;
	}
	
	//방 만들기
	private void openCreateRoomDialog() {
	    JFrame createRoomFrame = new JFrame("방 만들기");
	    createRoomFrame.setSize(400, 200);
	    createRoomFrame.setLocationRelativeTo(this);
	    createRoomFrame.setLayout(new GridLayout(4, 2, 10, 10));

	    JLabel labelRoomName = new JLabel("방 이름:", 10);
	    JTextField fieldRoomName = new JTextField();

	    JLabel labelMaxPlayers = new JLabel("최대 인원수:", 10);

	    JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    JRadioButton radio2Players = new JRadioButton("2명", true); // 기본값: 2명
	    JRadioButton radio4Players = new JRadioButton("4명");
	    JRadioButton radio6Players = new JRadioButton("6명");

	    ButtonGroup group = new ButtonGroup();
	    group.add(radio2Players);
	    group.add(radio4Players);
	    group.add(radio6Players);
	    radioPanel.add(radio2Players);
	    radioPanel.add(radio4Players);
	    radioPanel.add(radio6Players);
	    
	    int[] maxPlayers = {2};
	    radio2Players.addActionListener(e -> maxPlayers[0] = 2);
	    radio4Players.addActionListener(e -> maxPlayers[0] = 4);
	    radio6Players.addActionListener(e -> maxPlayers[0] = 6);

	    JButton buttonCreate = new JButton("확인");
	    buttonCreate.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String roomName = fieldRoomName.getText().trim();
	            // 입력 유효성 검사
	            if (roomName.isEmpty()) {
	                JOptionPane.showMessageDialog(createRoomFrame, "방 이름을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            // 방 생성 및 대기방으로 이동
	            ReadyRoom newRoom = createReadyRoom(player, roomName, maxPlayers[0], 0);
	            joinReadyRoom(newRoom);
	            createRoomFrame.dispose();
	        }
	    });

	    JButton buttonCancel = new JButton("취소");
	    buttonCancel.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            createRoomFrame.dispose();
	        }
	    });

	    createRoomFrame.add(labelRoomName);
	    createRoomFrame.add(fieldRoomName);
	    createRoomFrame.add(labelMaxPlayers);
	    createRoomFrame.add(radioPanel);
	    createRoomFrame.add(buttonCreate);
	    createRoomFrame.add(buttonCancel);
	    createRoomFrame.setVisible(true);
	}

	public ReadyRoom createReadyRoom(Player user, String roomName, int maxPlayers, int id) {
		ReadyRoom readyRoom = new ReadyRoom(roomName, user, maxPlayers, id);
		player.sendMessage("방을 만들었습니다.");
		player.setReadyRoom(readyRoom);
		player.sendCreateRoom(readyRoom);	
		return readyRoom;
	}

	
	public void joinReadyRoom(ReadyRoom room) {
		player.sendMessage(player.getId() + "가 "+room.roomId + "대기방 ㄱㄱ");
		player.setReadyRoom(room);
		room.enterRoom(player);
		player.sendEnterRoom(room);
		System.out.println(room.roomId  + "<- roomId");
	}
	public void addBtn(RoomBtn newBtn) {
		if(!roomBtns.contains(newBtn)) roomBtns.add(newBtn);
	}
	public void repaint() {
		for(int i=0; i < roomBtns.size(); i++) {
			roomBtns.elementAt(i).setPlayerCount();
			roomBtns.elementAt(i).repaint();
			System.out.println("버튼을 다시 그리는 중입니다.");
		}
	}
	class RoomBtn {
		JLabel roomNameLabel;
        JLabel playerCountLabel;
        JButton roomBtn;
        ReadyRoom room;
		RoomBtn(ReadyRoom room){
			this.room = room;
			roomNameLabel = new JLabel(room.getRoomName(), JLabel.LEFT);
			playerCountLabel = new JLabel(String.format("(%d / %d)", this.room.getCurrentPlayerCount(), this.room.getMaxPlayerCount()), JLabel.RIGHT);
			roomBtn = new JButton();
	        roomBtn.setLayout(new BorderLayout());
	        roomBtn.add(roomNameLabel, BorderLayout.WEST);
	        roomBtn.add(playerCountLabel, BorderLayout.EAST);
	        
	        roomBtn.setPreferredSize(new Dimension(180, 50));
	        
	        // 방 버튼 클릭 -> 입장
	        roomBtn.addActionListener(e -> joinReadyRoom(room));
		}
		void setPlayerCount() {
			System.out.println("setPlayerCount에서의 방의 현재 인원수 : "+this.room.getCurrentPlayerCount());
			playerCountLabel.setText(String.format("(%d / %d)", this.room.getCurrentPlayerCount(), this.room.getMaxPlayerCount()));
		}
		void repaint() {
			roomNameLabel.repaint();
			playerCountLabel.repaint();
			roomBtn.repaint();
		}
	}
}
