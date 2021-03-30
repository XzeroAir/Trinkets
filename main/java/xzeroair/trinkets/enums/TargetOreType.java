package xzeroair.trinkets.enums;

public enum TargetOreType {

	coal(0, "Coal", 4605510),
	iron(1, "Iron", 16764057),
	gold(2, "Gold", 16766720),
	diamond(3, "Diamond", 59135),
	emerald(4, "Emerald", 65357),
	lapis(5, "Lapis Lazuli", 2515356),
	redstone(6, "Redstone", 11546150),
	quarts(7, "Quartz", 15461355),
	chest(8, "Chest", 16766720),
	tin(9, "Tin", 6913940),
	copper(10, "Copper", 9913358),
	aluminum(11, "Aluminum", 8224127),
	silver(12, "Silver", 8096660),
	lead(13, "Lead", 4739441),
	nickel(14, "Nickel", 8223586),
	platinum(15, "Platinum", 2123927),
	iridium(16, "Iridium", 7565953),
	cobalt(17, "Cobalt", 18347),
	ardite(18, "Ardite", 7823905);

	private static final TargetOreType[] ID = new TargetOreType[values().length];

	private int type;
	private String name;
	private int colorValue;
	private final float[] oreColor;

	private TargetOreType(int type, String name, int colorValueIn) {
		this.type = type;
		this.name = name;
		colorValue = colorValueIn;
		final int i = (colorValueIn & 16711680) >> 16;
		final int j = (colorValueIn & 65280) >> 8;
		final int k = (colorValueIn & 255) >> 0;
		oreColor = new float[] { i / 255.0F, j / 255.0F, k / 255.0F };
	}

	public float getOreColor(int index) {
		return oreColor[index];
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return colorValue;
	}

	public static TargetOreType Type(int value) {
		if ((value < 0) || (value >= ID.length)) {
			value = 0;
		}

		return ID[value];
	}

	public static int Color(String name) {
		for (final TargetOreType ore : TargetOreType.values()) {
			if (ore.getName().contentEquals(name)) {
				return ore.getColor();
			}
		}
		return 16777215;
	}

	public static float Color(String name, int index) {
		for (final TargetOreType ore : TargetOreType.values()) {
			if (ore.getName().contentEquals(name)) {
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
