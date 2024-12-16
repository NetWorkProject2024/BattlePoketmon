import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WorldFrame {
	private JPanel centerPanel;
	private World worldInfo;
	private JButton readyBtn;
	private JFrame worldFrame;
	private Player user;
	private PoketmonFrame poketmonFrame;
	private JLabel userReadyStateLabel = new JLabel();
	private JLabel myWinCountLabel = new JLabel();
	private JLabel myLoseCountLabel = new JLabel();
	private JLabel myCoinLabel = new JLabel();
	
	
	public WorldFrame(World worldInfo, Player user) {
		this.worldInfo = worldInfo;
		this.user = user;
		this.myCoinLabel.setText("   내 코인 : "+user.getCoin());
		myCoinLabel.setForeground(Color.WHITE);
		poketmonFrame = new PoketmonFrame();
	}
	
	public void create(Player user) {
		worldFrame = new JFrame("BattlePoketmon_WORLD");
		worldFrame.setBounds(200,200,800,600);
		
		ImageIcon icon = new ImageIcon(getClass().getResource("poketmon/Title.png"));
        Image image = icon.getImage();
        worldFrame.setIconImage(image);
        BackgroundPanel entirePanel = new BackgroundPanel("poketmon/world_Background.png");
        entirePanel.setLayout(new BorderLayout());
        
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		worldFrame.add(entirePanel);
		worldFrame.setVisible(true);
		this.user = user;
		repaint();
		updateUserList();
	}
	
	public JPanel createUserInfoPanel(Player player) {
		JPanel userInfoPanel = new JPanel(new GridLayout(1,0));
		userInfoPanel.setOpaque(false);
		JLabel userName = new JLabel(player.getPlayerName());
		JLabel userReadyState;
		JLabel userWinCountLabel = new JLabel("승 : "+player.getWinCount());
		JLabel userLoseCountLabel = new JLabel("패 : "+player.getLoseCount());
		
		ImageIcon icon = player.getProfile();
        Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel titleImgLabel = new JLabel(new ImageIcon(scaledImage));
        userInfoPanel.add(titleImgLabel);
		
		userInfoPanel.add(userName);
		
		if(player.getReady()) {
			userReadyState= new JLabel("준비");
			userReadyState.setForeground(Color.GREEN);
		}
		else {
			userReadyState = new JLabel("준비 X");
			userReadyState.setForeground(Color.RED);
		}
			
		if(player.getId() == this.user.getId()) {
			userReadyStateLabel.setText(userReadyState.getText());
			userName.setForeground(Color.blue);
			userWinCountLabel.setForeground(Color.blue);
			userLoseCountLabel.setForeground(Color.blue);	
		}
		else {
			userName.setForeground(Color.RED);
			userWinCountLabel.setForeground(Color.RED);
			userLoseCountLabel.setForeground(Color.RED);		
		}
		userInfoPanel.add(userName);
		userInfoPanel.add(userReadyState);	
		userInfoPanel.add(userWinCountLabel);
		userInfoPanel.add(userLoseCountLabel);
		return userInfoPanel;
	}
	
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(0,2));
		abovePanel.setOpaque(false);
		readyBtn = new JButton("준비");
		readyBtn.setHorizontalAlignment(SwingConstants.CENTER);
		readyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				user.getClient().sendWorldReady(!user.getReady());
				}
		});
		abovePanel.add(myCoinLabel);
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
		centerPanel = new JPanel(new GridLayout(0,1));
		centerPanel.setOpaque(false);
		
		for (Player player : worldInfo.getUsers()) {
	        centerPanel.add(createUserInfoPanel(player));
	    }
		return centerPanel;
	}
		
	
	public void repaint() {
		if(user.getReady()) {
			userReadyStateLabel.setText("준비");
		}
		else {
			userReadyStateLabel.setText("준비 X");
		}
		myWinCountLabel.setText("승 : "+user.getWinCount());
		myLoseCountLabel.setText("패 : "+user.getLoseCount());
		myCoinLabel.setText("코인 : "+user.getCoin());
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
	
	public void worldFrameDispose() {
		worldFrame.dispose();
	}
	
}
