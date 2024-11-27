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
	JFrame worldFrame;
	private Player user;
	private PoketmonFrame poketmonFrame;
	
	JLabel userCountLabel;
	public WorldFrame(World worldInfo, Player user) {
		this.worldInfo = worldInfo;
		this.user = user;
		poketmonFrame = new PoketmonFrame();
	}
	
	public JFrame create() {
		worldFrame = new JFrame("BattlePoketmon_WORLD");
		worldFrame.setBounds(200,200,800,600);
		JPanel entirePanel = new JPanel(new BorderLayout());
		
		entirePanel.add(createAbovePanel(), BorderLayout.NORTH);
		entirePanel.add(createCenterPanel(),BorderLayout.CENTER);
		worldFrame.add(entirePanel);
		worldFrame.setVisible(true);
		return worldFrame;		
	}
	
	public JPanel createAbovePanel() {
		JPanel abovePanel = new JPanel(new GridLayout(2,3));
		JLabel xLabel = new JLabel("");
		timerLabel = new JLabel("TIMER : " + worldInfo.getTimer() + "          ");
		timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		abovePanel.add(xLabel);
		abovePanel.add(timerLabel);
		//버튼들
		JButton b_store = new JButton("포켓몬 분양소");
		JButton b_poketmon = new JButton("내 포켓몬");
		
		b_store.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PoketmonStoreFrame pf = new PoketmonStoreFrame(user);
				pf.create();
			}
		});
		b_poketmon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	               poketmonFrame.create_Inventory(user.getPoketmon());
	         }			
		});
		abovePanel.add(b_store);
		abovePanel.add(b_poketmon);
		return abovePanel;
	}
	
	
	
	public JPanel createCenterPanel() {
		centerPanel = new JPanel(new GridLayout(3,2));
		
		return centerPanel;
	}
		
	
	
}
