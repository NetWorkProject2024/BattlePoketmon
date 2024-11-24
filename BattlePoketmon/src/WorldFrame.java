import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WorldFrame {
	private JPanel centerPanel;
	private World worldInfo;
	private JLabel timerLabel;
	JFrame frame;
	
	JLabel userCountLabel;
	public WorldFrame(World worldInfo) {
		this.worldInfo = worldInfo;
	}
	
	public JFrame create() {
		frame = new JFrame("BattlePoketmon_WORLD");
		frame.setBounds(200,200,800,600);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		frame.add(entirePanel);
		frame.setVisible(true);
		return frame;		
	}
	
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(2,3));
		JLabel xLabel = new JLabel("");
		timerLabel = new JLabel("TIMER : " + worldInfo.getTimer() + "          ");
		timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		abovePanel.add(xLabel);
		abovePanel.add(timerLabel);
		//버튼들
		JLabel coinLabel= new JLabel("    <COIN> : ");		
		JButton b_bag = new JButton("가방");
		JButton b_poketmon = new JButton("포켓몬");
		
		b_bag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		b_poketmon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		abovePanel.add(coinLabel);
		abovePanel.add(b_bag);
		abovePanel.add(b_poketmon);
		return abovePanel;
	}
	
	
	
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(3,2));
		
		return centerPanel;
	}
	
//	public static void main(String[] args) {
//		World worldInfo = new World();
//		WorldFrame c = new WorldFrame(worldInfo);
//		c.create();
//	}
//	
	
	
	
	
	
	
	
	
	
	
}
