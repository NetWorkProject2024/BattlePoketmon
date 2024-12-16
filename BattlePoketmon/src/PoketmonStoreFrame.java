import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PoketmonStoreFrame {
   private static final int price=100;
   private Player user;
   private PoketmonFrame poketmonFrame;
   
   public PoketmonStoreFrame(Player user) {
      this.user = user;
      poketmonFrame = new PoketmonFrame();
   }
   
   public void create() {
      JFrame frame = new JFrame("BattlePoketmon_Store");
      ImageIcon icon = new ImageIcon(getClass().getResource("poketmon/Title.png"));
      Image image = icon.getImage();
      frame.setIconImage(image);
      BackgroundPanel p = new BackgroundPanel("poketmon/store_Back.png");
      p.setLayout(new GridLayout(0,1));
      ImageIcon egg = new ImageIcon(getClass().getResource("poketmon/poketmon_egg.png"));//포켓몬 알 이미지
      Image scaledImage = egg.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
      JLabel eggImgLabel = new JLabel(new ImageIcon(scaledImage));
      
      
      JLabel xLabel = new JLabel();
      JLabel xLabel2 = new JLabel();
      JLabel goodsName = new JLabel("포켓몬 알", JLabel.CENTER);
      goodsName.setForeground(Color.WHITE);
      JLabel goodsInfo = new JLabel("포켓몬이 랜덤하게 등장합니다!", JLabel.CENTER);
      JLabel goodsPriceInfo = new JLabel("PRICE : 100 COIN", JLabel.CENTER);
      JButton b_buy = new JButton("분양받기");
      
      b_buy.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int money = user.getCoin();
            if(money >= price) {
               user.addCoin(-price);
               Poketmon newPoketmon = randomPickPoketmon();
               poketmonFrame.create_Store(newPoketmon, user);
               user.getWorld().getWorldFrame().repaint();
            }
         }
      });
      b_buy.setForeground(Color.GREEN);
      frame.setBounds(300, 300, 450, 450);
      p.add(xLabel);
      p.add(xLabel);
      p.add(eggImgLabel);
      p.add(goodsName);
      p.add(goodsInfo);
      p.add(goodsPriceInfo);
      p.add(b_buy);
      frame.add(p);
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
   }
   private Poketmon randomPickPoketmon() {
      Random random = new Random();
      int randomIdx = random.nextInt(Poketmon.PoketmonArray.poketmons.size());
      Poketmon newPoketmon = Poketmon.PoketmonArray.poketmons.elementAt(randomIdx);
      newPoketmon.createSkills();
      return newPoketmon;
   }
   
  
}
