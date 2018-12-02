package xzeroair.trinkets.enums;

public enum EnumTrinketType {

	amulet,
	upperRing,
	lowerRing,
	rings,
	belt,
	head,
	body,
	charm;

	public int slot() {
		switch (this) {
		case amulet:
			return 0;
		case upperRing:
			return 1;
		case lowerRing:
			return 2;
		case belt:
			return 3;
		case head:
			return 4;
		case body:
			return 5;
		case charm:
			return 6;
		case rings:
			return 7;
		default:
			return 0;
		}
	}

}
