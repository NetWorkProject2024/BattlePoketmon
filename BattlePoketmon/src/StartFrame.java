import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StartFrame extends JFrame{
		
	 private JFrame startFrame;
	 private Player player;
	
	 private ImageIcon profile = new ImageIcon("src/poketmon/default.png");//기본 프로필 이미지
     private Image scaledImage = profile.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
     private JLabel profileImgLabel = new JLabel(new ImageIcon(scaledImage));
     
	public StartFrame(Player player) {
		startFrame = new JFrame("BattlePoketmon");
		startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		startFrame.setBounds(200,200,550,500);

        ImageIcon icon = new ImageIcon("src/poketmon/Title.png");
        Image image = icon.getImage();
        startFrame.setIconImage(image);
		this.player = player;
	}
	
	public JFrame create() {		
		// 배경 패널 사용
        BackgroundPanel mainPanel = new BackgroundPanel("src/poketmon/Start.png");
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(createTitleImgPanel(),BorderLayout.CENTER);
        mainPanel.add(createUserInfoPanel(),BorderLayout.SOUTH);
        startFrame.add(mainPanel);
		startFrame.setVisible(true);		
		return startFrame;		
	}
	
	public JPanel createTitleImgPanel() {
		JPanel imgPanel = new JPanel(new BorderLayout());
		imgPanel.setOpaque(false);
        return imgPanel;
	}
	
	public JPanel createUserInfoPanel() {		
		JPanel userInfoPanel = new JPanel(new FlowLayout());
		userInfoPanel.setOpaque(false);
		JLabel nameLabel = new JLabel("이름: ");
		nameLabel.setForeground(Color.WHITE);
		 // 프로필 이미지 크기 조정
	    profileImgLabel.setPreferredSize(new java.awt.Dimension(100, 100)); // JLabel 크기 설정

		JButton b_select = new JButton("프로필 설정");
		b_select.addActionListener(new ActionListener() {
			JFileChooser chooser = new JFileChooser();
			@Override
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						
						"JPG & GIF & PNG Images",
						"jpg", "gif", "png");
			
					chooser.setFileFilter(filter);
					
					int ret = chooser.showOpenDialog(startFrame);
					if(ret != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(startFrame, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
						return;
					}
				
					profile = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
					scaledImage = profile.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
					profileImgLabel.setIcon(new ImageIcon(scaledImage));//프로필 사진 변경
					player.setProfile(profile);
					
			}		
		});
		
        JTextField nameField = new JTextField(10);
        nameField.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		player.setPlayerName(nameField.getText());
        		player.getClient().sendUserID();
        		player.getClient().sendUserProfile();
        		startFrame.dispose();
			}
        });
        nameLabel.setHorizontalAlignment(JLabel.CENTER); // 수평 가운데 정렬

        userInfoPanel.add(profileImgLabel);
        userInfoPanel.add(b_select);
        userInfoPanel.add(nameLabel);        
        userInfoPanel.add(nameField);
		return userInfoPanel;
	}
	public void startFrameDispose() {
		startFrame.dispose();
	}
	

	

}
