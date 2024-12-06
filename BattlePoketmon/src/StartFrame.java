import java.awt.BorderLayout;
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
	
	public StartFrame(Player player) {
		startFrame = new JFrame();
		startFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		startFrame.setBounds(200,200,400,500);
		this.player = player;
	}
	
	public JFrame create() {		
		startFrame.add(createTitleImgPanel(),BorderLayout.CENTER);
		startFrame.add(createUserInfoPanel(),BorderLayout.SOUTH);
		startFrame.setVisible(true);		
		return startFrame;		
	}
	
	public JPanel createTitleImgPanel() {
		JPanel imgPanel = new JPanel(new BorderLayout());
		ImageIcon icon = new ImageIcon("src/poketmon/Start.png");
        Image scaledImage = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        JLabel titleImgLabel = new JLabel(new ImageIcon(scaledImage));
        imgPanel.add(titleImgLabel);
        return imgPanel;
	}
	
	public JPanel createUserInfoPanel() {		
		JPanel userInfoPanel = new JPanel(new FlowLayout());
		JLabel nameLabel = new JLabel("이름: ");
		
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
				
					ImageIcon profile = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
					player.setProfile(profile);
			}		
		});
		
        JTextField nameField = new JTextField(25);
        nameField.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		player.setPlayerName(nameField.getText());
        		player.getClient().sendUserID();
        		player.getClient().sendUserProfile();
        		startFrame.dispose();
			}
        });
        nameLabel.setPreferredSize(new java.awt.Dimension(50, 80));
        userInfoPanel.add(nameLabel);        
        userInfoPanel.add(nameField);
        userInfoPanel.add(b_select);
		return userInfoPanel;
	}
	public void startFrameDispose() {
		System.out.println("StartFrame dispose");
		startFrame.dispose();
	}
	

	

}
