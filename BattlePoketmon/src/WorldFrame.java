import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WorldFrame {
	private JPanel centerPanel;
	private World worldInfo;
	private JButton readyBtn;
	JFrame worldFrame;
	private Player user;
	private PoketmonFrame poketmonFrame;
	private JLabel userReadyStateLabel = new JLabel();
	
	public WorldFrame(World worldInfo, Player user) {
		this.worldInfo = worldInfo;
		this.user = user;
		poketmonFrame = new PoketmonFrame();
	}
	
	public void create(Player user) {
		worldFrame = new JFrame("BattlePoketmon_WORLD");
		worldFrame.setBounds(200,200,800,600);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		worldFrame.add(entirePanel);
		worldFrame.setVisible(true);
		this.user = user;
//		return worldFrame;		
	}
	
	public JPanel createUserInfoPanel(Player player) {
		JPanel userInfoPanel = new JPanel(new GridLayout(2,1));
		JLabel userName = new JLabel(player.getPlayerName());
		JLabel userReadyState;
	
		if(player.getReady()) {
			userReadyState= new JLabel("준비");
		}
		else {
			userReadyState = new JLabel("준비 X");
		}
		if(player.getId() == this.user.getId()) {
			userReadyStateLabel.setText(userReadyState.getText());
			userInfoPanel.add(userReadyStateLabel);
		}
		else {
			userInfoPanel.add(userName);
			userInfoPanel.add(userReadyState);
		}
		userInfoPanel.add(userName);
		
		return userInfoPanel;
	}
	
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(2,3));
		JLabel xLabel = new JLabel("");
		readyBtn = new JButton("준비");
		readyBtn.setHorizontalAlignment(SwingConstants.CENTER);
		readyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				user.getClient().sendWorldReady(!user.getReady());
			}
		});
		
		abovePanel.add(xLabel);
		abovePanel.add(readyBtn);
		//버튼들
		JButton b_store = new JButton("포켓몬 분양소");
		JButton b_poketmon = new JButton("내 포켓몬");
		
		b_store.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PoketmonStoreFrame pf = new PoketmonStoreFrame(user);
				pf.create();
			}
		});
		b_poketmon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	               poketmonFrame.create_Inventory(user.getPoketmon());
	         }			
		});
		abovePanel.add(b_store);
		abovePanel.add(b_poketmon);
		return abovePanel;
	}
	
	
	
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(0,2));
		
		for (Player player : worldInfo.getUsers()) {
	        centerPanel.add(createUserInfoPanel(player));
	    }
		return centerPanel;
	}
		
	
	public void repaint() {
		System.out.println("현재 인원수 : "+worldInfo.getUsers().size());
		if(user.getReady()) {
			userReadyStateLabel.setText("준비");
		}
		else {
			userReadyStateLabel.setText("준비 X");
		}
		userReadyStateLabel.repaint();
	}
	public void updateUserList() {
	    centerPanel.removeAll(); // 기존 사용자 패널 제거

	    // roomInfo의 모든 유저를 표시
	    
	    for (Player player : worldInfo.getUsers()) {
	        centerPanel.add(createUserInfoPanel(player));
	    }

	    centerPanel.revalidate(); // 패널 레이아웃 갱신
	    centerPanel.repaint();    // 패널 다시 그리기
	    repaint();                // 상단 사용자 수 정보 갱신
	}
	
}
