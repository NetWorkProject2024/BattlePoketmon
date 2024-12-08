import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

public class BattleFrame extends JFrame{
	private Player other;
	private Player me;
	private JLabel userName1=null;
	private JProgressBar healthBar1=null;
	private JLabel userName2=null;
	private JProgressBar healthBar2=null;
	private JPanel centerPanel;
	private JButton[] btnVec = new JButton[4];
	
	 private JFrame battleFrame;
	
	public BattleFrame(Player other, Player me) {
		
		this.other = other;
		this.me = me;
		battleFrame = new JFrame("BattlePoketmon_Battle: "+me.getPlayerName());
		battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		battleFrame.setBounds(200,200,400,500);
	}
	
	public JFrame create() {
		BackgroundPanel entirePanel = new BackgroundPanel("src/poketmon/battle_Back.png");
        entirePanel.setLayout(new BorderLayout());
        ImageIcon icon = new ImageIcon("src/poketmon/Title.png");
        Image image = icon.getImage();
        battleFrame.setIconImage(image);
		entirePanel.add(createAbovePanel(),BorderLayout.CENTER);
		entirePanel.add(createSkillPanel(),BorderLayout.SOUTH);
		battleFrame.add(entirePanel);
		battleFrame.setVisible(true);
		
		return battleFrame;		
	}
	public JPanel createUserInfoPanel() {
		JPanel userPanel = new JPanel(new GridLayout(2,4));
		userPanel.setOpaque(false);
		
		JPanel userInfoPanel1 = new JPanel(new GridLayout(0,1));
		JPanel userInfoPanel2 = new JPanel(new GridLayout(0,1));
		userInfoPanel1.setOpaque(false);
		userInfoPanel2.setOpaque(false);
		
		userName1 = new JLabel(me.getPlayerName());
		userName1.setOpaque(false);
		healthBar1 = new JProgressBar(0,100);
		healthBar1.setValue(me.getPoketmon().getCurrentHP());
		healthBar1.setStringPainted(true);
		healthBar1.setForeground(Color.RED);
		JPanel imgPanel1 = new JPanel(new BorderLayout());
		imgPanel1.setOpaque(false);
		ImageIcon iconProfile1 = me.getProfile();
        Image scaledImageP1 = iconProfile1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel titleImgLabel1 = new JLabel(new ImageIcon(scaledImageP1));
        imgPanel1.add(titleImgLabel1);
        JPanel myInfoPanel = new JPanel(new GridLayout(0,3));
        myInfoPanel.setOpaque(false);
        
        myInfoPanel.add(imgPanel1);
        myInfoPanel.add(userName1);

		userName2 = new JLabel(other.getPlayerName());
		userName2.setOpaque(false);
		healthBar2 = new JProgressBar(0,100);
		healthBar2.setValue(other.getPoketmon().getCurrentHP());
		healthBar2.setStringPainted(true);
		healthBar2.setForeground(Color.RED);
		JPanel imgPanel2 = new JPanel(new BorderLayout());
		imgPanel2.setOpaque(false);
		ImageIcon iconProfile2 = other.getProfile();
        Image scaledImageP2 = iconProfile2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel titleImgLabel2 = new JLabel(new ImageIcon(scaledImageP2));
        imgPanel2.add(titleImgLabel2);
        JPanel otherInfoPanel = new JPanel(new FlowLayout());
        otherInfoPanel.setOpaque(false);
        otherInfoPanel.add(imgPanel2);
        otherInfoPanel.add(userName2);
        
		ImageIcon icon1 = me.getPoketmon().icon;//포켓몬 이미지
        Image scaledImage1 = icon1.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        
        JLabel poketmonImgLabel1 = new JLabel(new ImageIcon(scaledImage1));
        
        ImageIcon icon2 = other.getPoketmon().icon;//포켓몬 이미지
        Image scaledImage2 = icon2.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        
        JLabel poketmonImgLabel2 = new JLabel(new ImageIcon(scaledImage2));
        
        JLabel xlabel = new JLabel();
        JLabel xlabel2 = new JLabel();
        JLabel xlabel3 = new JLabel();
        JLabel xlabel4 = new JLabel();
        
        userInfoPanel1.add(xlabel);
        userInfoPanel1.add(xlabel3);
        userInfoPanel1.add(myInfoPanel);
		userInfoPanel1.add(healthBar1);
		
		userInfoPanel2.add(healthBar2);
		userInfoPanel2.add(otherInfoPanel);
		userInfoPanel2.add(xlabel2);
		userInfoPanel2.add(xlabel4);
		
		userPanel.add(userInfoPanel2);
        userPanel.add(poketmonImgLabel2);
       
        
        
        userPanel.add(poketmonImgLabel1);
        userPanel.add(userInfoPanel1);	
		
		return userPanel;
	}
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(1,0));
		abovePanel.setOpaque(false);
		abovePanel.add(createUserInfoPanel());

		return abovePanel;
	}
	public JPanel createSkillPanel() {
		JPanel skillPanel = new JPanel(new GridLayout(0,2));
		for (int i = 0; i< 4;i++) {
			Skill currentSkill = me.getPoketmon().getSkill()[i];
			JButton btn = new JButton(currentSkill.getName());
			btn.setPreferredSize(new java.awt.Dimension(150, 50));
			
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					float attack = (float)(((currentSkill.getAttack()))*me.getPoketmon().getAttackPower()/100.0);
					me.getClient().sendAttack(other, ChatMsg.MODE_ATTACK,currentSkill.getType(), (int)attack);
					
					if(other.getPoketmon().getType().getStrength().getName().equals(currentSkill.getType().getName())) {
						attack -= 20;
					}
					else if(other.getPoketmon().getType().getWeakness().getName().equals(currentSkill.getType().getName())) {
						attack += 20;
					}
					
					int result = other.getPoketmon().getCurrentHP();
					result -= (int)(attack/10);
					other.getPoketmon().setCurrentHP(result);								
					me.setTurn(0);
					other.setTurn(1);
					repaint();
					btnEnabled(false);
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
			btnVec[i]=btn;
		}
    	if(me.getTurn()) {
    		userName1.setForeground(Color.blue);
    		userName2.setForeground(Color.GRAY);
    		btnEnabled(true);
    	}
    	else {
    		userName1.setForeground(Color.GRAY);
    		userName2.setForeground(Color.BLUE);
    		btnEnabled(false);
    	}
		return skillPanel;
	}
	public void repaint() {
		if(me.getTurn()) {
    		userName1.setForeground(Color.blue);
    		userName2.setForeground(Color.GRAY);
    	}
    	else {
    		userName1.setForeground(Color.GRAY);
    		userName2.setForeground(Color.BLUE);
    	}

		healthBar1.setValue(me.getPoketmon().getCurrentHP());
		healthBar2.setValue(other.getPoketmon().getCurrentHP());
		
		userName1.repaint();
		healthBar1.repaint();
		userName2.repaint();
		healthBar2.repaint();
		
	}
	public void btnEnabled(boolean state) {
		for(int i=0; i < 4; i++) {
			btnVec[i].setEnabled(state);
		}
	}
	
	public void battleFrameDispose() {
		battleFrame.dispose();
	}
	
	

}
