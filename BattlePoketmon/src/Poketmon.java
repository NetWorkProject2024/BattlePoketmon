import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Poketmon implements Serializable{
   private String name;
   private Type type;
   private Skill[] skills = new Skill[4];
   private int attackPower;
//   private int defensePower;
   private int maxHp=100;
   private int currentHp=100;
   public ImageIcon icon;
   
   public Poketmon(String name, int typeIdx, int attackPower, String icon) {
      this.name = name;
      this.type = Type.TypeArray.types.elementAt(typeIdx);
      this.attackPower = attackPower;
//      this.defensePower = defensePower;
      this.icon = new ImageIcon(icon);
   }
   public Poketmon(Poketmon poketmon) {
	   this.name = poketmon.name;
	   this.type = poketmon.type;
	   this.attackPower=poketmon.attackPower;
//	   this.defensePower=poketmon.defensePower;
	   this.icon = poketmon.icon;
   }
   public static class PoketmonArray{
      public static Vector<Poketmon> poketmons = new Vector<Poketmon>();
      private static String[] names = {"파이리","꼬부기", "이상해씨" };
      private static String[] icons = {"src/poketmon/fff.png","src/poketmon/kko.png", "src/poketmon/lee.png" };
      private static int[] typeIdx = {0, 1, 2};
      private static int[] attackPower = {100, 100, 100};
//      private static int[] defensePower = {100, 100, 100};
      static {
         for(int i=0; i < names.length; i++) {
//            poketmons.add(new Poketmon(names[i], typeIdx[i], attackPower[i], defensePower[i], icons[i]));
        	 poketmons.add(new Poketmon(names[i], typeIdx[i], attackPower[i], icons[i]));
         }
      }
   }
   public void setName(String name) {
	   this.name=name;
   }
   public String getName() {
      return name;
   }
   public Type getType() {
      return type;
   }
   
   public int getAttackPower() {
      return attackPower;
   }
//   public int getDefensePower() {
//	      return defensePower;
//   }
   public int getCurrentHP() {
	   return this.currentHp;
   }
   public void setCurrentHP(int HP) {
	   this.currentHp=HP;
   }
   public Skill[] getSkill() {
	   return skills;
   }
 
   public void createSkills() {
      Random random = new Random();
      int skillCount = 0;
      while(skillCount < 4) {
         int idx = random.nextInt(Skill.SkillArray.skills.size());
         Skill newSkill = Skill.SkillArray.skills.elementAt(idx);
         if(newSkill.getType()==Type.TypeArray.types.elementAt(3) ||newSkill.getType()==type) {
            this.skills[skillCount]=newSkill;
            skillCount++;
         }
         
      }
   }
}
