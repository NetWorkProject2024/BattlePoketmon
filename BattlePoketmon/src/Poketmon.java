import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Poketmon implements Serializable{
   private String name;
   private PType type;
   private Skill[] skills = new Skill[4];
   private int attackPower;
   private int maxHp=100;
   private int currentHp=100;
   public ImageIcon icon;
   
   public Poketmon(String name, int typeIdx, int attackPower, String icon) {
      this.name = name;
      this.type = PType.TypeArray.types.elementAt(typeIdx);
      this.attackPower = attackPower;
      this.icon = new ImageIcon(icon);
   }
   public Poketmon(Poketmon poketmon) {
	   this.name = poketmon.name;
	   this.type = poketmon.type;
	   this.attackPower=poketmon.attackPower;
	   this.icon = poketmon.icon;
   }
   public static class PoketmonArray{
      public static Vector<Poketmon> poketmons = new Vector<Poketmon>();
      private static String[] names = {"파이리","꼬부기", "이상해씨",
    		  "마릴", "왕눈해", "골덕", "슈륙챙이", "갸라도스", "가이오가", "라프라스", "밀로틱",
    		  "가디", "브케인", "아차모", "나인테일", "날쌩마", "마그마", "초염몽", "엔테이",
    		  "선인왕", "모부기", "베이리프", "무스틈니", "나무킹", "트로피우스", "우츠보트", "메가니움"
    		  
      };
      private static String[] icons = {"src/poketmon/파이리.png","src/poketmon/꼬부기.png", "src/poketmon/이상해씨.png",
    		  "src/poketmon/마릴.png", "src/poketmon/왕눈해.png","src/poketmon/골덕.png", "src/poketmon/슈륙챙이.png","src/poketmon/갸라도스.png", "src/poketmon/가이오가.png", "src/poketmon/라프라스.png", "src/poketmon/밀로틱.png",
    		  "src/poketmon/가디.png", "src/poketmon/브케인.png", "src/poketmon/아차모.png", "src/poketmon/나인테일.png", "src/poketmon/날쌩마.png", "src/poketmon/마그마.png", "src/poketmon/초염몽.png", "src/poketmon/엔테이.png",
    		  "src/poketmon/선인왕.png", "src/poketmon/모부기.png", "src/poketmon/베이리프.png", "src/poketmon/무스틈니.png","src/poketmon/나무킹.png", "src/poketmon/트로피우스.png", "src/poketmon/우츠보트.png", "src/poketmon/메가니움.png"
      };
      // 0 : 불, 1 : 물, 2 : 풀
      private static int[] typeIdx = {0, 1, 2,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2};
      private static int[] attackPower = {100, 100, 100, 120,120,140,140,140,160,160,160,140,120,120,140,140,160,160,160,120,120,140,140,160,160,140,160};
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
   public PType getType() {
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
      boolean[] skillIdxArray = new boolean[Skill.SkillArray.skills.size()];
      for(int i=0; i < skillIdxArray.length; i++) {
    	  skillIdxArray[i]=false;
      }
      while(skillCount < 4) {
         int idx = random.nextInt(Skill.SkillArray.skills.size());
         if(skillIdxArray[idx]) {
        	 continue;
         }
         Skill newSkill = Skill.SkillArray.skills.elementAt(idx);
         if(newSkill.getType()==PType.TypeArray.types.elementAt(3) ||newSkill.getType()==type) {
            this.skills[skillCount]=newSkill;
            skillIdxArray[idx]=true;
            skillCount++;
         }
         
      }
   }
}
