package xzeroair.trinkets.enums;

public enum StatusEffectEnum {

    // @formatter:off
    Normal		(		nextID(), "Normal"		, 100	),
    stun		(		nextID(), "Stun"		, 100	),
    paralysis	(		nextID(), "Paralysis"	, 100	),
    bleed		(		nextID(), "Bleed"		, 100	),
    Invigorated	(		nextID(), "Invigorated", 100	),
    charm		(		nextID(), "Charm"		, 100	),
    Rage		(		nextID(), "Rage"		, 100	),
    Berserk		(		nextID(), "Berserk"	, 100	),
    Corrupted	(		nextID(), "Corrupted"	, 100	),
    Infected	(		nextID(), "Infected"	, 100	),
    Inspiration	(		nextID(), "Inspiration", 100	),
    Undead		(		nextID(), "Undead"		, 100	),
    Incorporeal	(		nextID(), "Incorporeal", 100	),
    ManaUp		(		nextID(), "ManaUp"		, 100	)
    // Sun Blessed, Moon Walker, Lucky, Demonification, The Fallen
    //TODO Don't do an Enum, Do something else
    ;
    // @formatter:on
    private static final StatusEffectEnum[] ID = new StatusEffectEnum[values().length];

    private int index;
    private String name;
    private int chance;

    private static int IndexID = 0;

    private StatusEffectEnum(int index, String name, int chance) {
        this.index = index;
        this.name = name;
        this.chance = chance;
    }

    private static int nextID() {
        return IndexID++;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public static StatusEffectEnum Type(int value) {
        if ((value < 0) || (value >= ID.length)) {
            value = 0;
        }

        return ID[value];
    }

    public static StatusEffectEnum getStatusByIndex(int i) {
        for (StatusEffectEnum status : StatusEffectEnum.values()) {
            if (status.getIndex() == i) {
                return status;
            }
        }
        return Normal;
    }

    public static StatusEffectEnum getStatusByName(String string) {
        for (StatusEffectEnum status : StatusEffectEnum.values()) {
            if (status.getName().equalsIgnoreCase(string)) {
                return status;
            }
        }
        return Normal;
    }
}