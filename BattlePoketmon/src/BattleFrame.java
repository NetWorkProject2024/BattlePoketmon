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
import javax.swing.JProgressBar;

public class BattleFrame {
	private Player other;
	private Player me;
	
	private JPanel centerPanel;
//	private World worldInfo;
	
	
	public BattleFrame(Player other, Player me) {
		this.other = other;
		this.me = me;
	}
	
	public JFrame create() {
		JFrame battleFrame = new JFrame("BattlePoketmon_WORLD");
		battleFrame.setBounds(200,200,300,600);
		JPanel entirePanel = new JPanel(new GridLayout(2,0));
		
		entirePanel.add(createAbovePanel());
		entirePanel.add(createSkillPanel());
		battleFrame.add(entirePanel);
		battleFrame.setVisible(true);
		return battleFrame;		
	}
	public JPanel createUserInfoPanel(Player user, boolean isOther) {
		JPanel userPanel = new JPanel(new GridLayout(1,2));
		
		JPanel userInfoPanel = new JPanel(new GridLayout(0,1));
		
		JLabel userName = new JLabel(user.getPlayerName());
		JProgressBar healthBar = new JProgressBar(0,100);
		healthBar.setValue(user.getPoketmon().getCurrentHP());
		healthBar.setStringPainted(true);
		healthBar.setForeground(Color.RED);

		ImageIcon icon = user.getPoketmon().icon;//포켓몬 이미지
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        
        JLabel poketmonImgLabel = new JLabel(new ImageIcon(scaledImage));
        
        
    	if(isOther) {
			userInfoPanel.add(userName);
			userInfoPanel.add(healthBar);
			userPanel.add(userInfoPanel);	
	        userPanel.add(poketmonImgLabel);
		}
		else {
			userInfoPanel.add(healthBar);
			userInfoPanel.add(userName);
	        userPanel.add(poketmonImgLabel);
	        userPanel.add(userInfoPanel);	
		}
    	

        
		
		return userPanel;
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(2,0));
		abovePanel.add(createUserInfoPanel(other, true));
		abovePanel.add(createUserInfoPanel(me, false));
		return abovePanel;
	}
	public JPanel createSkillPanel() {
		JPanel skillPanel = new JPanel(new GridLayout(0,2));
		for (int i = 0; i< 4;i++) {
			Skill currentSkill = me.getPoketmon().getSkill()[i];
			JButton btn = new JButton(currentSkill.getName());
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					me.getClient().sendAttack(other, ChatMsg.MODE_ATTACK, currentSkill.getAttack());
				}
			});
			switch(me.getPoketmon().getSkill()[i].getType().getName()) {
			case "불":
				btn.setForeground(Color.RED);break;
			case "물":
				btn.setForeground(Color.BLUE);break;
			case "풀":
				btn.setForeground(Color.GREEN);break;
			case "노말":
				btn.setForeground(Color.GRAY);break;				
			}
			skillPanel.add(btn);
		}
		
		return skillPanel;
	}
	
	
	

}
