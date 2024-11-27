import java.io.Serializable;
import java.util.Vector;

public class Skill implements Serializable{
   private String name;
   private Type type;
   private int attack;
   
   public Skill(String name, int typeIdx, int attack) {
      this.name = name;
      this.type = Type.TypeArray.types.elementAt(typeIdx);
      this.attack=attack;
   }
   public static class SkillArray{
      public static Vector<Skill> skills = new Vector<Skill>();
      private static String[] names = {"몸통박치기", "물의 파동", "불꽃세례", "잎날가르기", "전광석화"};
      // 0: 불, 1: 물, 2: 풀, 3: 노말
      private static int[] typeIdxs = {3, 1, 0, 2, 3};
      private static int[] attacks = {50, 70, 70, 70, 70};
      static {
         for(int i=0; i < names.length; i++) {
            skills.add(new Skill(names[i], typeIdxs[i], attacks[i]));
         }
      }
   }
   public String getName() {
	   return this.name;
   }
   public Type getType() {
      return this.type;
   }
   public int getAttack() {
      return this.attack;
   }
}
