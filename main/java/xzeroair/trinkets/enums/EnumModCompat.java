package xzeroair.trinkets.enums;

public enum EnumModCompat {
    //@formatter:off
    NONE(0),
    SimpleDifficulty(1),
    ToughAsNails(2),
    SURVIVAL(3),
    IceAndFire(4),
    Lycanites(5),
    ElenaiDoge1(6),
    ElenaiDoge2(7),
    FirstAid(8),
    EnhancedVisuals(9),
    BetterDiving(10),
    NEVER(99);
    //@formatter:on
    private static final EnumModCompat[] ID = new EnumModCompat[values().length];

    private int id;

    private EnumModCompat(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EnumModCompat Type(int value) {
        if ((value < 0) || (value >= ID.length)) {
            value = 0;
        }
        return ID[value];
    }
}
