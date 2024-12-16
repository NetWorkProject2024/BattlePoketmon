import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Home extends JFrame{

	public static int roomCount = 0;//방 만들때++
	private JPanel roomListPanel;
	private JScrollPane scrollPane;
	private JButton b_createRoom;
	private Player player;
	public static Vector<RoomBtn> roomBtns = new Vector<RoomBtn>();
	
	public Home(Player player, Vector<ReadyRoom> serverRooms) {
		super("Battle Poketmon");
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/poketmon/Title.png"));
        Image image = icon.getImage();
        this.setIconImage(image);
        this.player = player;
		buildHomeGUI(serverRooms);
		setSize(600,450);
		setLocation(500,0);
		setVisible(true);
	}
	
	private void buildHomeGUI(Vector<ReadyRoom> serverRooms) {
		 BackgroundPanel mainPanel = new BackgroundPanel("/poketmon/home_background.png");
	        mainPanel.setLayout(new BorderLayout());
	        mainPanel.add(topPanel(), BorderLayout.NORTH);
	        mainPanel.add(createRoomListPanel(serverRooms), BorderLayout.CENTER);
	        add(mainPanel);
	}
	private JPanel topPanel() {
		JPanel topP = new JPanel(new BorderLayout());	
		topP.setOpaque(false); // 배경 투명 처리
		JLabel title = new JLabel("BattlePoketmon_" + player.getPlayerName(), JLabel.CENTER);
		title.setForeground(Color.WHITE);
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
	    roomListPanel.setOpaque(false);
	    
//	    // 방 버튼 추가
	    for (ReadyRoom room : serverRooms) {
	        roomListPanel.add(createRoomBtn(room));
	    }
	    updateRoomListPanel(serverRooms);
	    JScrollPane scrollPane = new JScrollPane(roomListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
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
    }
	
	private JButton createRoomBtn(ReadyRoom room) {
		RoomBtn newBtn = new RoomBtn(room);
        addBtn(newBtn);
		return newBtn.roomBtn;
	}
	
	//방 만들기
	private void openCreateRoomDialog() {
	    JFrame createRoomFrame = new JFrame("방 만들기");
	    ImageIcon icon = new ImageIcon(getClass().getResource("/poketmon/Title.png"));
        Image image = icon.getImage();
        createRoomFrame.setIconImage(image);
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
	            createReadyRoom(player, roomName, maxPlayers[0]);
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

	public void createReadyRoom(Player user, String roomName, int maxPlayers) {
		user.getClient().sendCreateRoom(roomName, (long)maxPlayers);
		return;
	}

	
	public void joinReadyRoom(ReadyRoom room) {
		player.setReadyRoom(room);
		if(room.getUsers().contains(player)) {
			return;
		}
		player.getClient().sendEnterRoom(room);
	}
	public void addBtn(RoomBtn newBtn) {
		if(!roomBtns.contains(newBtn)) roomBtns.add(newBtn);
	}
	public void repaint() {
		for(int i=0; i < roomBtns.size(); i++) {
			roomBtns.elementAt(i).setPlayerCount();
			roomBtns.elementAt(i).repaint();
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
			playerCountLabel = new JLabel(String.format("(%d / %d)", this.room.getUsers().size(), this.room.getMaxPlayerCount()), JLabel.RIGHT);
			roomBtn = new JButton();
	        roomBtn.setLayout(new BorderLayout());
	        roomBtn.add(roomNameLabel, BorderLayout.WEST);
	        roomBtn.add(playerCountLabel, BorderLayout.EAST);
	        
	        roomBtn.setPreferredSize(new Dimension(180, 50));
	        
	        // 방 버튼 클릭 -> 입장
	        if(this.room.getUsers().size()>= this.room.getMaxPlayerCount()) {
        		roomBtn.setEnabled(false);
        	}
	        roomBtn.addActionListener(e -> 
	        {
	        	if(this.room.getUsers().size()< this.room.getMaxPlayerCount()) {
		        	joinReadyRoom(this.room);
		     
	        	}
	        }
	        );
		}
		void setPlayerCount() {
			playerCountLabel.setText(String.format("(%d / %d)", this.room.getUsers().size(), this.room.getMaxPlayerCount()));
		}
		void repaint() {
			roomNameLabel.repaint();
			playerCountLabel.repaint();
			roomBtn.repaint();
		}
	}
}
