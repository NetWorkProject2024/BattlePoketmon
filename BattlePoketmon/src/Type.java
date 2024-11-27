import java.io.Serializable;
import java.util.Vector;

public class Type implements Serializable{
   private String name;
   private int strengthIdx;
   private int weaknessIdx;
   private Type strength;
   private Type weakness;
   
   public Type(String name, int strengthIdx, int weaknessIdx) {
      this.name = name;
      this.strengthIdx=strengthIdx;
      this.weaknessIdx=weaknessIdx;
   }
   public static class TypeArray{
      public static Vector<Type> types = new Vector<Type>();
      private static String[] names = {"불","물", "풀", "노말" };
      static {
         for(int i=0; i < 3; i++) {
            int strengthIdx = (i+2)%3;
            int weaknessIdx =(i+1)%3;
            types.add(new Type(names[i], strengthIdx,weaknessIdx));
         }
         types.add(new Type(names[3], 3,3));
         for(int i=0; i < types.size(); i++) {
            Type currentType = types.elementAt(i);
            currentType.strength=types.elementAt(currentType.strengthIdx);
            currentType.weakness=types.elementAt(currentType.weaknessIdx);
         }
      }
   }
   
   public String getName()
   {
	   return this.name;
   }
}
