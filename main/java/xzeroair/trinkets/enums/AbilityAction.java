package xzeroair.trinkets.enums;

public enum AbilityAction {
	BREAKING(0),
	BROKEN(1),
	INTERACT(2),
	HURT(3),
	DAMAGE(4),
	CANCEL(99);

	private static final AbilityAction[] ID = new AbilityAction[values().length];

	private int id;

	private AbilityAction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static AbilityAction Type(int value) {
		if ((value < 0) || (value >= ID.length)) {
			value = 0;
		}
		return ID[value];
	}
}
