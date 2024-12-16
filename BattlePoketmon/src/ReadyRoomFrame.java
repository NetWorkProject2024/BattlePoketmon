import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
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
			return;
		}
		frame = new JFrame("BattlePoketmon_ReadyRoom");
		ImageIcon icon = new ImageIcon(getClass().getResource("/poketmon/Title.png"));
        Image image = icon.getImage();
        frame.setIconImage(image);
		frame.setBounds(50,50,400,400);
		
		BackgroundPanel entirePanel = new BackgroundPanel("/poketmon/ready_Background.jpg");
        entirePanel.setLayout(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		entirePanel.add(createBelowPanel(),BorderLayout.SOUTH);
		frame.add(entirePanel);
		frame.setVisible(true);
		this.homeFrame=homeFrame;
		this.user = user;
		homeFrame.setVisible(false);
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(1,3));
		abovePanel.setOpaque(false);
		JLabel roomNameSign = new JLabel("방 이름");
		JLabel roomNameLabel = new JLabel(roomInfo.getRoomName());
		userCountLabel = new JLabel(roomInfo.getUsers().size()+"/"+roomInfo.getMaxPlayerCount());
		if(roomInfo.getUsers().size() == roomInfo.getMaxPlayerCount()) {
			userCountLabel.setForeground(Color.GREEN);
		}
		else {
			userCountLabel.setForeground(Color.RED);
		}
		abovePanel.add(roomNameSign);
		abovePanel.add(roomNameLabel);
		abovePanel.add(userCountLabel);
		
		return abovePanel;
	}
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(3,2));
		centerPanel.setOpaque(false);
		return centerPanel;
	}
	public JPanel createBelowPanel() {
		JPanel belowPanel = new JPanel(new GridLayout(1,3));
		belowPanel.setOpaque(false);
		JButton b_backward= new JButton("<-");
		JButton b_select = new JButton("선택");
		JButton b_ready = new JButton("준비");
		b_backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				homeFrame.setVisible(true);
				roomInfo.exitRoom(user);
				frame.dispose();
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
		userPanel.setOpaque(false);
		userPanel.add(createUserInfoPanel(player.getProfile(), player.getPlayerName(),player.getReady(), player.getId()));
		return userPanel;
	}
	public JPanel createUserInfoPanel(ImageIcon profile, String name, boolean readyState, int id) {
		JPanel userInfoPanel = new JPanel(new GridLayout(1,0));
		 userInfoPanel.setOpaque(false);
		
		JPanel imgPanel = new JPanel(new BorderLayout());
		imgPanel.setOpaque(false);
		
		
		ImageIcon icon = profile;
        Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel titleImgLabel = new JLabel(new ImageIcon(scaledImage));
        imgPanel.add(titleImgLabel);
        
		JLabel userName = new JLabel(name);
		userName.setForeground(Color.blue);
		JLabel userPoketmon = new JLabel("");
		JLabel userReadyState;
	
		if(readyState) {
			userReadyState= new JLabel("준비");
			userReadyState.setForeground(Color.GREEN);
		}
		else {
			userReadyState = new JLabel("준비 X");
			userReadyState.setForeground(Color.RED);
		}
		if(id == this.user.getId()) {
			userPoketmonLabel.setText(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()).getName());
			userReadyStateLabel.setText(userReadyState.getText());
			userPoketmonLabel.setForeground(Color.blue);
			
			userInfoPanel.add(imgPanel);
			userInfoPanel.add(userName);
			userInfoPanel.add(userReadyStateLabel);
			userInfoPanel.add(userPoketmonLabel);
		}
		else {
			userName.setForeground(Color.DARK_GRAY);
			userInfoPanel.add(imgPanel);
			userInfoPanel.add(userName);
			userInfoPanel.add(userReadyState);
			userInfoPanel.add(userPoketmon);
		}
		
		
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


	
	public void repaint() {
		userCountLabel.setText(roomInfo.getUsers().size()+"/"+roomInfo.getMaxPlayerCount());
		userPoketmonLabel.setText(Poketmon.PoketmonArray.poketmons.elementAt(user.getPoketmonIdx()).getName());
		if(roomInfo.getUsers().size() == roomInfo.getMaxPlayerCount()) {
			userCountLabel.setForeground(Color.GREEN);
		}
		else {
			userCountLabel.setForeground(Color.RED);
		}
		if(user.getReady()) {
			userReadyStateLabel.setText("준비");
			userReadyStateLabel.setForeground(Color.GREEN);
		}
		else {
			userReadyStateLabel.setText("준비 X");
			userReadyStateLabel.setForeground(Color.RED);
		}
		userCountLabel.repaint();
		userPoketmonLabel.repaint();
		userReadyStateLabel.repaint();
	}
}
