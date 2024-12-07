import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResultFrame {
   
   
   private Vector<Player> users = new Vector<Player>();
   
   public ResultFrame(Vector<Player> users) {
      for(int i=0; i < users.size(); i++) {
         this.users.add(users.elementAt(i));
      }
   }
   public void create() {
      JFrame frame = new JFrame();
      JPanel panel = new JPanel(new GridLayout(0,1));
      frame.setBounds(200,200,600,400);
      users.sort(new WinCountComparator());
      for(int i=0; i < users.size(); i++) {
         panel.add(creatUuserInfoPanel(users.elementAt(i), i+1));
      }
      frame.add(panel);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   }
   public JPanel creatUuserInfoPanel(Player user, int order) {
      JPanel panel = new JPanel(new GridLayout(1,0));
      JLabel orderLabel = new JLabel("등수 : "+order);
      
      JPanel imgPanel = new JPanel(new BorderLayout());
      ImageIcon icon = user.getProfile();
      Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
      JLabel titleImgLabel = new JLabel(new ImageIcon(scaledImage));
      imgPanel.add(titleImgLabel);
      
      
      JLabel nameLabel = new JLabel(user.getPlayerName());
      JLabel winCountLabel = new JLabel("승 : "+user.getWinCount());
      JLabel loseCountLabel = new JLabel("패 : "+user.getLoseCount());
      
      orderLabel.setForeground(Color.GREEN);
      winCountLabel.setForeground(Color.RED);
      loseCountLabel.setForeground(Color.BLUE);
      panel.add(orderLabel);
      panel.add(imgPanel);
      panel.add(nameLabel);
      panel.add(winCountLabel);
      panel.add(loseCountLabel);
      return panel;
   }
   class WinCountComparator implements Comparator<Player>{
      @Override
      public int compare(Player user1, Player user2) {
         return user2.getWinCount()-user1.getWinCount();
      }
   }


}
