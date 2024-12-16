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
		frame = new JFrame("Select_MyPoketmon");
		ImageIcon icon = new ImageIcon(getClass().getResource("poketmon/Title.png"));
        Image image = icon.getImage();
        frame.setIconImage(image);
		frame.setBounds(100,100,400,300);
		BackgroundPanel panel = new BackgroundPanel("poketmon/selectPoketmon_Background.png");
        panel.setLayout(new BorderLayout());
		
		
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
		panel.setOpaque(false);
		
		for(int i=0; i < 3; i++) {
			
			checkBox[i]= new PocketmonCheckBox(i);
			panel.add(createPocketmonInfoPanel(Poketmon.PoketmonArray.poketmons.elementAt(i), checkBox[i]));
		}
		return panel;
	}
	private JPanel createBelowPanel(PocketmonCheckBox[] checkBox) {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.setOpaque(false);
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
		panel.add(b_save);
		panel.add(b_cancle);
		return panel;
	}
	private JPanel createPocketmonInfoPanel(Poketmon poketmon, PocketmonCheckBox checkBox) {
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setOpaque(false);
		
		JLabel nameLabel = new JLabel(poketmon.getName(), JLabel.CENTER);
		if(poketmon.getName().equals("파이리")) {
			nameLabel.setForeground(Color.RED);
		}else if(poketmon.getName().equals("꼬부기")) {
			nameLabel.setForeground(Color.BLUE);
		}else if(poketmon.getName().equals("이상해씨")) {
			nameLabel.setForeground(Color.GREEN);
		}
		ImageIcon icon = poketmon.icon;//포켓몬 이미지
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_AREA_AVERAGING);
        poketmonImgLabel = new JLabel(new ImageIcon(scaledImage));
        poketmonImgLabel.setHorizontalAlignment(JLabel.CENTER);
        checkBox.checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        checkBox.checkBox.setOpaque(false);

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
