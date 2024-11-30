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
	private JLabel userName1=null;
	private JProgressBar healthBar1=null;
	private JLabel userName2=null;
	private JProgressBar healthBar2=null;
	private JPanel centerPanel;
//	private World worldInfo;
	
	
	public BattleFrame(Player other, Player me) {
		this.other = other;
		this.me = me;
	}
	
	public JFrame create() {
		JFrame battleFrame = new JFrame(me.getPlayerName()+me.getId());
		battleFrame.setBounds(200,200,300,600);
		JPanel entirePanel = new JPanel(new GridLayout(2,0));
		
		entirePanel.add(createAbovePanel());
		entirePanel.add(createSkillPanel());
		battleFrame.add(entirePanel);
		battleFrame.setVisible(true);
		return battleFrame;		
	}
	public JPanel createUserInfoPanel() {
		JPanel userPanel = new JPanel(new GridLayout(2,4));
		
		JPanel userInfoPanel1 = new JPanel(new GridLayout(0,1));
		JPanel userInfoPanel2 = new JPanel(new GridLayout(0,1));
		
		userName1 = new JLabel(me.getPlayerName() + me.getId());
		healthBar1 = new JProgressBar(0,100);
		healthBar1.setValue(me.getPoketmon().getCurrentHP());
		healthBar1.setStringPainted(true);
		healthBar1.setForeground(Color.RED);

		userName2 = new JLabel(other.getPlayerName() + other.getId());
		healthBar2 = new JProgressBar(0,100);
		healthBar2.setValue(other.getPoketmon().getCurrentHP());
		healthBar2.setStringPainted(true);
		healthBar2.setForeground(Color.RED);
		
		ImageIcon icon1 = me.getPoketmon().icon;//포켓몬 이미지
        Image scaledImage1 = icon1.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        
        JLabel poketmonImgLabel1 = new JLabel(new ImageIcon(scaledImage1));
        
        ImageIcon icon2 = other.getPoketmon().icon;//포켓몬 이미지
        Image scaledImage2 = icon2.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        
        JLabel poketmonImgLabel2 = new JLabel(new ImageIcon(scaledImage2));
        
        
        userInfoPanel1.add(userName1);
		userInfoPanel1.add(healthBar1);
		
        
		userInfoPanel2.add(healthBar2);
		userInfoPanel2.add(userName2);
        userPanel.add(poketmonImgLabel2);
        userPanel.add(userInfoPanel2);
        userPanel.add(userInfoPanel1);	
        userPanel.add(poketmonImgLabel1);
    	if(me.getTurn()) {
    		userName1.setForeground(Color.GREEN);
    		userName2.setForeground(Color.GRAY);
    	}
    	else {
    		userName1.setForeground(Color.GRAY);
    		userName2.setForeground(Color.GREEN);
    	}
        
		
		return userPanel;
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(1,0));
		abovePanel.add(createUserInfoPanel());
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
					float attack = (float)(((currentSkill.getAttack()))*me.getPoketmon().getAttackPower()/100.0);
					me.getClient().sendAttack(other, ChatMsg.MODE_ATTACK, (int)attack);
					System.out.println("attack 값: " + (int)attack);
					System.out.println("공격버튼 누른 사람: " + me.getId());
					
					int result = other.getPoketmon().getCurrentHP();
					result -= (int)(attack/10);
					System.out.println("상대 " + other.getId());
					other.getPoketmon().setCurrentHP(result);								
					me.setTurn(0);
					other.setTurn(1);
					repaint();
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
	public void repaint() {
		//System.out.println(user.getTurn() +"ID: "+ user.getId());
		if(me.getTurn()) {
    		userName1.setForeground(Color.GREEN);
    		userName2.setForeground(Color.GRAY);
    	}
    	else {
    		userName1.setForeground(Color.GRAY);
    		userName2.setForeground(Color.GREEN);
    	}

		healthBar1.setValue(me.getPoketmon().getCurrentHP());
		healthBar2.setValue(other.getPoketmon().getCurrentHP());
		
		userName1.repaint();
		healthBar1.repaint();
		userName2.repaint();
		healthBar2.repaint();
		
	}
	
	
	

}
