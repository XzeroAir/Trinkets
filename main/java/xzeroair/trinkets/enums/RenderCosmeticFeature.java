package xzeroair.trinkets.enums;

public enum RenderCosmeticFeature {

    //@formatter:off
    HUMAN(0),
    FAIRY(1),
    DWARF(2),
    ELF(3),
    GOBLIN(4),
    FAELIS(5),
    TITAN(6),
    DRAGON(7),
    TAURUS(8),
    TAURUS_BELL(9),
    TAURUS_F(10),
    TAURUS_F_BELL(11),
    TAURIAN_BELL(12),
    SUCCUBUS(13),
    GENERIC_HORNS(14),
    GENERIC_HORNS_INVERTED(15),
    DRAGON_HORNS(16),
    FAELIS_TAIL(17);

    //@formatter:on

    private final int id;

    RenderCosmeticFeature(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static int getMaxLength() {
        return values().length;
    }

    public static RenderCosmeticFeature cosmetic(int value) {
        if ((value < 0) || (value >= values().length)) {
            value = 0;
        }
        return values()[value];
    }

}
