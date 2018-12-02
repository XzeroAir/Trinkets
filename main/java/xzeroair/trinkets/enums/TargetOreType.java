package xzeroair.trinkets.enums;

public enum TargetOreType {

	coal(0, "Coal", 4605510),
	iron(1, "Iron", 16764057),
	gold(2, "Gold", 16766720),
	diamond(3, "Diamond", 59135),
	emerald(4, "Emerald", 65357),
	lapis(5, "Lapis", 2515356),
	redstone(6, "Redstone", 11546150),
	quarts(7, "Quartz", 15461355),
	chest(8, "Chest", 16766720),
	tin(8, "Tin", 6913940),
	copper(8, "Copper", 9913358),
	aluminum(8, "Aluminum", 8224127),
	silver(8, "Silver", 8096660),
	lead(8, "Lead", 4739441),
	nickel(8, "Nickel", 8223586),
	platinum(8, "Platinum", 2123927),
	iridium(8, "Iridium", 7565953),
	cobalt(8, "Cobalt", 18347),
	ardite(8, "Ardite", 7823905);

	private static final TargetOreType[] ID = new TargetOreType[values().length];

	private int type;
	private String name;
	private final float[] oreColor;

	private TargetOreType(int type, String name, int colorValueIn) {
		this.type = type;
		this.name = name;
		int i = (colorValueIn & 16711680) >> 16;
		int j = (colorValueIn & 65280) >> 8;
		int k = (colorValueIn & 255) >> 0;
		this.oreColor = new float[] {(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
	}

	public float getOreColor(int index) {
		return this.oreColor[index];
	}

	public int getType() {
		return this.type;
	}
	public String getName() {
		return this.name;
	}

	public static TargetOreType Type(int value)
	{
		if ((value < 0) || (value >= ID.length))
		{
			value = 0;
		}

		return ID[value];
	}

	public static float Color(String name, int index) {
		for(TargetOreType ore : TargetOreType.values()) {
			if(ore.getName() == name) {
				return ore.getOreColor(index);
			}
		}
		return 1F;
	}

	public int id() {
		switch (this) {
		case coal:
			return 16;
		case iron:
			return 15;
		case gold:
			return 14;
		case diamond:
			return 56;
		case emerald:
			return 129;
		case lapis:
			return 21;
		case redstone:
			return 73;
		case quarts:
			return 153;
		case chest:
			return 54;
		default:
			return 92;
		}
	}
}
