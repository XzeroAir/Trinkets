package xzeroair.trinkets.enums;

public enum CustomTeddyBearTypes {

    //@formatter:off
    NORMAL(0, ""),
    REMBO(1, "rembos"),
    SCARY(2, "scary"),
    SHIVAXI(3, "shivaxi"),
    BEE(4, "bee"),
    PANDA(5, "panda"),
    ARTSY(6, "artsy"),
    TWILIGHT(7, "twilight"),
    RYU(8, "ryu"),
    KEN(9, "ken"),
    NYAN(10, "nyan"),
    NYAN_OLD(11, "nyan_old"),
    BOOM(12, "boom"),
    RIXXI(13, "rixxi"),
    SNOWIE(14, "snowie");
    //@formatter:on

    private final int id;
    private final String name;

    CustomTeddyBearTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static int getMaxLength() {
        return values().length;
    }

    public static CustomTeddyBearTypes getType(int value) {
        if (value < 0 || value >= values().length) {
            return NORMAL;
        }
        return values()[value];
    }

}
