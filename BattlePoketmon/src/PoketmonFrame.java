import java.awt.BorderLayout;
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

public class PoketmonFrame {
	private JPanel centerPanel;
	private JLabel nameLabel;
	private JLabel typeLabel;
	private JLabel attackLabel;
//	private JLabel defenseLabel;
	private Poketmon poketmon;
	private Player user;
	private JLabel poketmonImgLabel;
	JFrame frame;
	
	public PoketmonFrame() {		
	}
	
	public JFrame create_Store(Poketmon poketmon, Player user) {
		this.poketmon = poketmon;
		frame = new JFrame("Poketmon");
		frame.setBounds(200,200,400,500);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createCenterPanel(),BorderLayout.NORTH);
		entirePanel.add(createInfoPanel(),BorderLayout.CENTER);
		entirePanel.add(createBtnPanel(),BorderLayout.SOUTH);
		frame.add(entirePanel);
		frame.setVisible(true);
		this.user = user;
		return frame;		
	}
	
	public JFrame create_Inventory(Poketmon poketmon) {
		this.poketmon = poketmon;
		frame = new JFrame("Poketmon");
		frame.setBounds(200,200,300,400);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createCenterPanel(),BorderLayout.NORTH);
		entirePanel.add(createInfoPanel(),BorderLayout.CENTER);
		frame.add(entirePanel);
		frame.setVisible(true);
		return frame;		
	}
	
	
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(0,1));
		ImageIcon icon = poketmon.icon;//포켓몬 이미지
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        poketmonImgLabel = new JLabel(new ImageIcon(scaledImage));
        	
		centerPanel.add(poketmonImgLabel);
        
		return centerPanel;
	}
	
	public JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel(new GridLayout(0,2));
		nameLabel = new JLabel("       포켓몬 : " + poketmon.getName());
        typeLabel = new JLabel("       타입 : " + poketmon.getType().getName());
        attackLabel = new JLabel("       공격력 : " + poketmon.getAttackPower());
//        defenseLabel = new JLabel("       방어력 : " + poketmon.getDefensePower());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        typeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        attackLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        defenseLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel xLabel = new JLabel("");
        JLabel xLabel2 = new JLabel("");
        JLabel xLabel3 = new JLabel("");
        JLabel xLabel4 = new JLabel("");
		
		
        infoPanel.add(nameLabel);
        infoPanel.add(xLabel);
        infoPanel.add(typeLabel);

        infoPanel.add(xLabel2);
        infoPanel.add(attackLabel);

        infoPanel.add(xLabel3);
        
        
        for (int i = 0; i< 4;i++) {
			JPanel panel = new JPanel();
			JLabel skillLabel = new JLabel(poketmon.getSkill()[i].getName());
			JLabel skillTypeLabel = new JLabel("타입 : "+poketmon.getSkill()[i].getType().getName());
			JLabel skillAttackLabel = new JLabel("위력 : "+poketmon.getSkill()[i].getAttack());
			panel.add(skillLabel);
			panel.add(skillTypeLabel);
			panel.add(skillAttackLabel);
			
			infoPanel.add(panel);
		}
		return infoPanel;
	}
	
	
	public JPanel createSkillPanel() {
		JPanel skillPanel = new JPanel(new GridLayout(0,2));
		for (int i = 0; i< 4;i++) {
			JPanel panel = new JPanel();
			JLabel skillLabel = new JLabel(poketmon.getSkill()[i].getName());
			JLabel skillTypeLabel = new JLabel("타입 : "+poketmon.getSkill()[i].getType().getName());
			JLabel skillAttackLabel = new JLabel("위력 : "+poketmon.getSkill()[i].getAttack());
			panel.add(skillLabel);
			panel.add(skillTypeLabel);
			panel.add(skillAttackLabel);
			
			skillPanel.add(panel);
		}
		
		return skillPanel;
	}
	
	public JPanel createBtnPanel() {
		JPanel btnPanel = new JPanel(new GridLayout(0,2));
		//버튼들
		JButton b_change = new JButton("교환");
		JButton b_keep = new JButton("취소");
		
		b_change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				user.setPoketmon(poketmon);
				frame.dispose();
				
			}
		});
		b_keep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnPanel.add(b_change);
		btnPanel.add(b_keep);
		
		return btnPanel;
		
	}
	
	
}
