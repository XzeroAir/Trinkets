package xzeroair.trinkets.enums;

public enum EnumModelRenderLocation {
    //@formatter:off
    HEAD(0),
    BODY(1),
    LEFT_ARM(2),
    RIGHT_ARM(3),
    LEFT_LEG(4),
    RIGHT_LEG(5);
    //@formatter:on
    private static final EnumModelRenderLocation[] ID = new EnumModelRenderLocation[values().length];

    private int id;

    private EnumModelRenderLocation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EnumModelRenderLocation Type(int value) {
        if ((value < 0) || (value >= ID.length)) {
            value = 0;
        }
        return ID[value];
    }
}
