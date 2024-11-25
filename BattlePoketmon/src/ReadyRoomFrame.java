import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ReadyRoomFrame{
	private JPanel centerPanel;
	private ReadyRoom roomInfo;
	private JFrame frame;
	private JLabel userCountLabel;
	private JLabel userPoketmonLabel = new JLabel();
	private JLabel userReadyStateLabel = new JLabel();
	private JFrame homeFrame;
	private Player user;
	private ReadyRoomFrame readyRoomFrame=this;
	public ReadyRoomFrame(ReadyRoom roomInfo) {
		this.roomInfo=roomInfo;
	}


	public void create(JFrame homeFrame, Player user) {
		if(frame != null) {
			repaint();
			System.out.println("frame != null 확인!!");
//			frame.dispose();
			return;
		}
		frame = new JFrame("대기방");
		frame.setBounds(50,50,200,200);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		entirePanel.add(createBelowPanel(),BorderLayout.SOUTH);
		frame.add(entirePanel);
		frame.setVisible(true);
		this.homeFrame=homeFrame;
		this.user = user;
		homeFrame.setVisible(false);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//return frame;
		
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(1,3));
		JLabel roomNameSign = new JLabel("방 이름");
		JLabel roomNameLabel = new JLabel(roomInfo.getRoomName());
		userCountLabel = new JLabel(roomInfo.getUsers().size()+"/"+roomInfo.getMaxPlayerCount());
		abovePanel.add(roomNameSign);
		abovePanel.add(roomNameLabel);
		abovePanel.add(userCountLabel);
		
		return abovePanel;
	}
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(3,2));
		
		return centerPanel;
	}
	public JPanel createBelowPanel() {
		JPanel belowPanel = new JPanel(new GridLayout(1,3));
		JButton b_backward= new JButton("<-");
		JButton b_select = new JButton("선택");
		JButton b_ready = new JButton("준비");
		b_backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				homeFrame.setVisible(true);
			}
		});
		b_select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectStartingPoketmonFrame selectFrame = new SelectStartingPoketmonFrame(user, readyRoomFrame);
				selectFrame.create();
			}
		});
		b_ready.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//user.setReady(!user.getReady());
				//user.getClient().sendPlayerReady(user.getReady());
				user.getClient().sendPlayerReady(!user.getReady());
			}
		});
		belowPanel.add(b_backward);
		belowPanel.add(b_select);
		belowPanel.add(b_ready);
		return belowPanel;
	}
	public JPanel createUserPanel(Player player) {
		JPanel userPanel = new JPanel(new BorderLayout());
		//ImageIcon img =player.getImage();
		//userPanel.add(img);
		userPanel.add(createUserInfoPanel(player.getPlayerName() + player.getId(),player.getReady(), player.getId()));
		return userPanel;
	}
	public JPanel createUserInfoPanel(String name, boolean readyState, int id) {
		JPanel userInfoPanel = new JPanel(new GridLayout(2,1));
		JLabel userName = new JLabel(name);
		JLabel userPoketmon = new JLabel("");
		JLabel userReadyState;
	
		if(readyState) {
			userReadyState= new JLabel("준비");
		}
		else {
			userReadyState = new JLabel("준비 X");
		}
		if(id == this.user.getId()) {
			userPoketmonLabel.setText(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()).getName());
			userReadyStateLabel.setText(userReadyState.getText());
			userInfoPanel.add(userPoketmonLabel);
			userInfoPanel.add(userReadyStateLabel);
		}
		else {
			userInfoPanel.add(userName);
			userInfoPanel.add(userPoketmon);
			userInfoPanel.add(userReadyState);
		}
		userInfoPanel.add(userName);
		
		return userInfoPanel;
	}
	
	public void updateUserList() {
	    centerPanel.removeAll(); // 기존 사용자 패널 제거

	    // roomInfo의 모든 유저를 표시
	    
	    for (Player player : roomInfo.getUsers()) {
	        centerPanel.add(createUserPanel(player));
	    }

	    centerPanel.revalidate(); // 패널 레이아웃 갱신
	    centerPanel.repaint();    // 패널 다시 그리기
	    repaint();                // 상단 사용자 수 정보 갱신
	}

	
	public void addUser(Player player) {
	    roomInfo.addUser(player);  // ReadyRoom 클래스에 유저 추가
	    updateUserList();          // 사용자 목록 갱신
	}
	
	public void repaint() {
		System.out.println("현재 인원수 : "+roomInfo.getUsers().size());
		userCountLabel.setText(roomInfo.getUsers().size()+"/"+roomInfo.getMaxPlayerCount());
		userPoketmonLabel.setText(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()).getName());
		if(user.getReady()) {
			userReadyStateLabel.setText("준비");
		}
		else {
			userReadyStateLabel.setText("준비 X");
		}
		userCountLabel.repaint();
		userPoketmonLabel.repaint();
		userReadyStateLabel.repaint();
	}
}
