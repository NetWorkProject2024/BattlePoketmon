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

	public Vector<ReadyRoom> rooms = new Vector<ReadyRoom>();
	public static int roomCount = 0;//방 만들때++
	private JPanel roomListPanel;
	private JScrollPane scrollPane;
	private JButton b_createRoom;
	private Player player;
	private static Server s;
	
	public Home() {
		super("Battle Poketmon");
//		player = new Player("나");//홈 들어오면 '나'생성
//		
//		// 미리 방 생성해두기(스크롤 확인용)
//        createReadyRoom(player, "방 1", 2);
//        createReadyRoom(player, "방 2", 4);createReadyRoom(player, "방 6", 2);
//        createReadyRoom(player, "방 3", 4);createReadyRoom(player, "방 7", 2);
//        createReadyRoom(player, "방 4", 4);createReadyRoom(player, "방 8", 2);
//        createReadyRoom(player, "방 5", 4);
        
		buildHomeGUI();
		setSize(400,300);
		setLocation(500,0);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void buildHomeGUI() {
		setLayout(new BorderLayout());
		add(topPanel(), BorderLayout.NORTH);
//		add(roomListPanel(), BorderLayout.CENTER);
		
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
	
	private JScrollPane roomListPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 0, 10));

	    for (ReadyRoom room : rooms) {
	    	JLabel roomNameLabel = new JLabel(String.format("%s", room.getRoomName()));
            JLabel playerCountLabel = new JLabel(String.format("(%d / %d)", room.getCurrentPlayerCount(), room.getMaxPlayerCount()));

	        JButton roomButton = new JButton();
	        roomButton.setLayout(new BorderLayout());
	        roomButton.add(roomNameLabel, BorderLayout.CENTER);
	        roomButton.add(playerCountLabel, BorderLayout.EAST);

	        Dimension buttonSize = new Dimension(180, 50);
	        roomButton.setPreferredSize(buttonSize);
	        roomButton.setMinimumSize(buttonSize);
	        roomButton.setMaximumSize(buttonSize);
	        
	        // 방 버튼 클릭 -> 입장
	        roomButton.addActionListener(e -> joinReadyRoom(room, player));
	        	        
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	        buttonPanel.add(roomButton);	        
	        panel.add(buttonPanel);
	    }
	    JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    return scrollPane;
	}

	//방 만들기
	private void openCreateRoomDialog() {
	    JFrame createRoomFrame = new JFrame("방 만들기");
	    createRoomFrame.setSize(400, 200);
	    createRoomFrame.setLocationRelativeTo(this); // 메인 창 중앙에 표시
	    createRoomFrame.setLayout(new GridLayout(4, 2, 10, 10)); // 4행 2열로 변경

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
	            joinReadyRoom(createReadyRoom(player, roomName, maxPlayers[0]), player);	 
	            createRoomFrame.dispose();
	            updateRoomListPanel();
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

	private void updateRoomListPanel() {
        roomListPanel.removeAll();
        scrollPane = roomListPanel();
        roomListPanel.add(scrollPane, BorderLayout.CENTER);
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }
	
	
	public ReadyRoom createReadyRoom(Player user, String roomName, int maxPlayers) {
		ReadyRoom readyRoom = new ReadyRoom(roomName, user, maxPlayers, roomCount++);
		rooms.add(readyRoom);
		s.printDisplay(user.getPlayerName() + "->" + readyRoom.getRoomName() + " 방 생성");
		
		
		
		return readyRoom;
	}
	
	public void joinReadyRoom(ReadyRoom room, Player user) {
		s.printDisplay(room.getRoomName() + " 대기방으로 ㄱㄱ");
		room.enterRoom(user);
	}
	
}
