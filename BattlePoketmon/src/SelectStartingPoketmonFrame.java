import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectStartingPoketmonFrame {
	private Player player;
	private JFrame frame;
	private ReadyRoomFrame readyRoomFrame;
	
	private JLabel poketmonImgLabel;
	public SelectStartingPoketmonFrame(Player player, ReadyRoomFrame readyRoomFrame) {
		this.player = player;
		this.readyRoomFrame = readyRoomFrame;
	}
	public JFrame create() {
		frame = new JFrame("스타팅 포켓몬 고르기");
		frame.setBounds(200,200,600,600);
		JPanel panel = new JPanel(new BorderLayout());
		PocketmonCheckBox[] checkBox = new PocketmonCheckBox[3];
		ButtonGroup group = new ButtonGroup();
		
		panel.add(createCenterPanel(checkBox), BorderLayout.CENTER);
		panel.add(createBelowPanel(checkBox), BorderLayout.SOUTH);
		group.add(checkBox[0].checkBox);
		group.add(checkBox[1].checkBox);
		group.add(checkBox[2].checkBox);
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}
	private JPanel createCenterPanel(PocketmonCheckBox[] checkBox) {
		JPanel panel = new JPanel(new GridLayout(1,3));
		
		for(int i=0; i < 3; i++) {
			
			checkBox[i]= new PocketmonCheckBox(i);
			panel.add(createPocketmonInfoPanel(Poketmon.PoketmonArray.poketmons.elementAt(i), checkBox[i]));
		}
		return panel;
	}
	private JPanel createBelowPanel(PocketmonCheckBox[] checkBox) {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		JButton b_save = new JButton("확인");
		JButton b_cancle = new JButton("취소");
		
		b_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i < 3; i++) {
					if(checkBox[i].checkBox.isSelected()) {
						player.setPoketmonIdx(checkBox[i].id);
						readyRoomFrame.repaint();
						frame.dispose();
					}
				}
			}
		});
		
		b_cancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		b_save.setForeground(Color.GREEN);
		b_cancle.setForeground(Color.RED);
		panel.add(b_save);
		panel.add(b_cancle);
		return panel;
	}
	private JPanel createPocketmonInfoPanel(Poketmon poketmon, PocketmonCheckBox checkBox) {
		JPanel panel = new JPanel(new GridLayout(3,1));
		//ImageIcon img = poketmon.getImage();
		JLabel nameLabel = new JLabel(poketmon.getName());
		
		ImageIcon icon = poketmon.icon;//포켓몬 이미지
        Image scaledImage = icon.getImage().getScaledInstance(3000, 3000, Image.SCALE_SMOOTH);
        poketmonImgLabel = new JLabel(new ImageIcon(scaledImage));
        
        panel.add(poketmonImgLabel);
		
		panel.add(nameLabel);
		panel.add(checkBox.checkBox);
		
		return panel;
	}
	class PocketmonCheckBox{
		private int id;
		private JCheckBox checkBox;
		PocketmonCheckBox(int id){
			checkBox=new JCheckBox();
			this.id = id;
		}
	}
}
