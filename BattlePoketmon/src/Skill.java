import java.io.Serializable;
import java.util.Vector;

public class Skill implements Serializable{
   private String name;
   private PType type;
   private int attack;
   
   public Skill(String name, int typeIdx, int attack) {
      this.name = name;
      this.type = PType.TypeArray.types.elementAt(typeIdx);
      this.attack=attack;
   }
   public static class SkillArray{
      public static Vector<Skill> skills = new Vector<Skill>();
      private static String[] names = {"몸통박치기", "물의 파동", "불꽃세례", "잎날가르기", "전광석화", 
    		  "물대포", "거품광선", "아쿠아테일", "파도타기", "튀어오르기", 
    		  "회오리불꽃", "불꽃펀치", "화염방사", "불꽃튀기기", "불꽃엄니", 
    		  "덩굴채찍", "씨폭탄", "솔라빔", "씨기관총", "에너지볼",
    		  "할퀴기", "짓밟기", "누르기", "락클라임", "소닉붐", "메카톤펀치", "기가임팩트", "파괴광선"};
      // 0: 불, 1: 물, 2: 풀, 3: 노말
      private static int[] typeIdxs = {3, 1, 0, 2, 3, 1,1,1,1,1,0,0,0,0,0,2,2,2,2,2,3,3,3,3,3,3,3,3};
      //
      private static int[] attacks = {50, 70, 70, 70, 60,
    		  50,80,60,55,30,
    		  50,70,80,60,60,
    		  50,60,80,60,60,
    		  50,55,55,70,60,70,80,80};
      static {
         for(int i=0; i < names.length; i++) {
            skills.add(new Skill(names[i], typeIdxs[i], attacks[i]));
         }
      }
   }
   public String getName() {
	   return this.name;
   }
   public PType getType() {
      return this.type;
   }
   public int getAttack() {
      return this.attack;
   }
}
