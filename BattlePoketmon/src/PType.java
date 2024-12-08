import java.io.Serializable;
import java.util.Vector;

public class PType implements Serializable{
   private String name;
   private int strengthIdx;
   private int weaknessIdx;
   private PType strength;
   private PType weakness;
   
   public PType(String name, int strengthIdx, int weaknessIdx) {
      this.name = name;
      this.strengthIdx=strengthIdx;
      this.weaknessIdx=weaknessIdx;
   }
   public static class TypeArray{
      public static Vector<PType> types = new Vector<PType>();
      private static String[] names = {"불","물", "풀", "노말" };
      static {
         for(int i=0; i < 3; i++) {
            int strengthIdx = (i+2)%3;
            int weaknessIdx =(i+1)%3;
            types.add(new PType(names[i], strengthIdx,weaknessIdx));
         }
         types.add(new PType(names[3], 3,3));
         for(int i=0; i < types.size(); i++) {
            PType currentType = types.elementAt(i);
            currentType.strength=types.elementAt(currentType.strengthIdx);
            currentType.weakness=types.elementAt(currentType.weaknessIdx);
         }
      }
   }
   
   public String getName()
   {
	   return this.name;
   }
   
   public PType getWeakness()
   {
	   return this.weakness;
   }
   public PType getStrength()
   {
	   return this.strength;
   }
}
