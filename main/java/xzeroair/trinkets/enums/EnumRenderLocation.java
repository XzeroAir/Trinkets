package xzeroair.trinkets.enums;

public enum EnumRenderLocation {
    //@formatter:off
    ALWAYS(0),
    ITEM(1),
    GUI(2),
    RACE(3),
    ITEM_BEFORE(4),
    ITEM_ADVANCED(5),
    ITEM_AFTER(6),
    GUI_BEFORE(7),
    GUI_AFTER(8),
    ITEM_CTRL(9),
    ITEM_SHIFT(10),
    ITEM_ALT(11),
    RACE_BEFORE(12),
    RACE_AFTER(13),
    NEVER(99);
    //@formatter:on
    private static final EnumRenderLocation[] ID = new EnumRenderLocation[values().length];

    private int id;

    private EnumRenderLocation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static EnumRenderLocation Type(int value) {
        if ((value < 0) || (value >= ID.length)) {
            value = 0;
        }
        return ID[value];
    }
}
