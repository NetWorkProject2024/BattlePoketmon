import java.awt.BorderLayout;
import java.awt.GridLayout;
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
	public SelectStartingPoketmonFrame(Player player) {
		this.player = player;
	}
	public JFrame create() {
		frame = new JFrame("스타팅 포켓몬 고르기");
		frame.setBounds(50,50,200,200);
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
		panel.add(b_save);
		panel.add(b_cancle);
		return panel;
	}
	private JPanel createPocketmonInfoPanel(Poketmon poketmon, PocketmonCheckBox checkBox) {
		JPanel panel = new JPanel(new GridLayout(3,1));
		//ImageIcon img = poketmon.getImage();
		JLabel nameLabel = new JLabel(poketmon.getName());
		
		
		
		//panel.add(img);
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
