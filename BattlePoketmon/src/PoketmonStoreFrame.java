import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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
      JFrame frame = new JFrame("포켓몬 분양소");
      JPanel p = new JPanel(new GridLayout(4,0));
      JLabel goodsName = new JLabel("포켓몬 알");
      JLabel goodsInfo = new JLabel("포켓몬이 랜덤하게 등장합니다!");
      JLabel goodsPriceInfo = new JLabel("100Coin");
      JButton b_buy = new JButton("분양받기");
      
      b_buy.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int money = user.getCoin();
//            if(money >= price) {
               user.addCoin(-price);
               Poketmon newPoketmon = randomPickPoketmon();
               poketmonFrame.create(newPoketmon);
//            }
         }
      });
      frame.setBounds(300, 300, 150, 150);
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
