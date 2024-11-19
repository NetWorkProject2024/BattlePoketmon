import java.util.Vector;

public class Poketmon {
	private String name;
	private Type type;
	private Skill[] skills = new Skill[4];
	private int attackPower;
	private int defensePower;
	private int maxHp=100;
	private int currentHp=100;
	
	public Poketmon(String name, Type type, int attackPower, int defensePower) {
		this.name = name;
		this.type = type;
		this.attackPower = attackPower;
		this.defensePower = defensePower;
	}
	public static class PoketmonArray {
		public static Vector<Poketmon> poketmons = new Vector<Poketmon>();
		private static String[] names = {"파이리","꼬부기", "이상해씨" };
		private static Type[] type= {new Type(), new Type(), new Type(), new Type()};
		private static int[] typeIdx = {0, 1, 2};
		private static int[] attackPower = {100, 100, 100};
		private static int[] defensePower = {100, 100, 100};
		static {
			for(int i=0; i < names.length; i++) {
				poketmons.add(new Poketmon(names[i], type[typeIdx[i]], attackPower[i], defensePower[i]));
			}
		}
	}
	public String getName() {
		return name;
	}
	public Type getType() {
		return type;
	}
}
