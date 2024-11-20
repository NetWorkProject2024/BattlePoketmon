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
	JFrame frame;
	JLabel userCountLabel;
	public ReadyRoomFrame(ReadyRoom roomInfo) {
		this.roomInfo=roomInfo;
	}


	public JFrame create() {
		frame = new JFrame("대기방");
		frame.setBounds(50,50,200,200);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		entirePanel.add(createBelowPanel(),BorderLayout.SOUTH);
		frame.add(entirePanel);
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
		
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(1,3));
		JLabel roomNameSign = new JLabel("방 이름");
		JLabel roomNameLabel = new JLabel(roomInfo.getRoomName());
		userCountLabel = new JLabel(roomInfo.getCurrentPlayerCount()+"/"+roomInfo.getMaxPlayerCount());
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
				
			}
		});
		b_select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				new SelectStartingPoketmonFrame(new ReadyRoomPlayer(new Player("userName"), roomInfo)).create();
			}
		});
		b_ready.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
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
		//userPanel.add(createUserInfoPanel(player.getName(),player.getReady()));
		return userPanel;
	}
	public JPanel createUserInfoPanel(String name, boolean readyState) {
		JPanel userInfoPanel = new JPanel(new GridLayout(2,1));
		JLabel userName = new JLabel(name);
		JLabel userReadyState;
		if(readyState) {
			userReadyState= new JLabel("준비");
		}
		else {
			userReadyState = new JLabel("준비 X");
		}
		 
		userInfoPanel.add(userName);
		userInfoPanel.add(userReadyState);
		
		return userInfoPanel;
	}
	public void addUser(Player player) {
		centerPanel.add(createUserPanel(player));
		
	}
	public void repaint() {
		userCountLabel.repaint();
	}
}
