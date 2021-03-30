package xzeroair.trinkets.traits.elements;

import java.util.UUID;

public enum ElementalEnum {

	// @formatter:off
	neutral		(		nextID(), "Human"		, "00000000-0000-0000-0000-000000000000", 1F	, 100	),
	earth		(		nextID(), "Fairy"		, "e5869fac-0949-41f2-889b-4e6b8ca6d2e7", 3F	, 25	),
	fire		(		nextID(), "Dwarf"		, "917b555b-944a-4e44-afb6-ca638c6d91e5", 0.7F	, 75	),
	water		(		nextID(), "Titan"		, "a3bc433b-7bb7-4bd9-a88c-5fd120d04d59", 0.3F	, 300	),
	air			(		nextID(), "Elf"			, "25f92404-35f3-453b-ad48-9b788b2e12fc", 2F	, 100	),
	lava		(		nextID(), "Succubus"	, "cce3a5ca-134e-40ed-a27d-a89e1f05dc5f", 3F	, 100	),
	ice			(		nextID(), "Dragon"		, "3b75821e-6ec6-4dfe-9612-b7a988a7b30b", 4F	, 100	),
	lightning	(		nextID(), "Siren"		, "1403f7e8-a427-4326-bcd9-1d7fdc1e22bb", 3F	, 100	),
	nature		(		nextID(), "Nymph"		, "14f31596-09a7-4be0-9592-4cb63d7e74bb", 2F	, 100	),
	light		(		nextID(), "Orc"			, "591d7d19-dd46-471f-b24f-e9967b1b95ef", 0.5F	, 150	),
	dark		(		nextID(), "Goblin"		, "d917999a-0399-4c39-bfc5-79784dfff6ed", 0.75F	, 50	),
	mixed		(		nextID(), "Mixed"		, "10172391-1b14-4a2d-9387-5f819d56426f", 1F	, 100	);
	// @formatter:on
	private static final ElementalEnum[] ID = new ElementalEnum[values().length];

	private int index;
	private String name;
	private String uuid;
	private float affinity;
	private int size;

	private static int IndexID = 0;

	private ElementalEnum(int index, String name, String uuid, float affinity, int size) {
		this.index = index;
		this.name = name;
		this.uuid = uuid;
		this.size = size;
		this.affinity = affinity;
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

	public UUID getUUID() {
		return UUID.fromString(uuid);
	}

	public float getAffinity() {
		return affinity;
	}

	public int getSize() {
		return size;
	}

	public static ElementalEnum Type(int value) {
		if ((value < 0) || (value >= ID.length)) {
			value = 0;
		}

		return ID[value];
	}

	public static ElementalEnum getelementByIndex(int i) {
		for (ElementalEnum element : ElementalEnum.values()) {
			if (element.getIndex() == i) {
				return element;
			}
		}
		return neutral;
	}

	public static ElementalEnum getelementByName(String string) {
		for (ElementalEnum element : ElementalEnum.values()) {
			if (element.getName().equalsIgnoreCase(string)) {
				return element;
			}
		}
		return neutral;
	}

	public static ElementalEnum getelementByUUID(UUID uuid) {
		for (ElementalEnum element : ElementalEnum.values()) {
			if (element.getUUID().compareTo(uuid) == 0) {
				return element;
			}
		}
		return neutral;
	}

	public static UUID getelementIDByName(String string) {
		for (ElementalEnum element : ElementalEnum.values()) {
			if (element.getName().equalsIgnoreCase(string)) {
				return element.getUUID();
			}
		}
		return neutral.getUUID();
	}

	public static String getelementNameByUUID(UUID uuid) {
		for (ElementalEnum element : ElementalEnum.values()) {
			if (element.getUUID().compareTo(uuid) == 0) {
				return element.getName();
			}
		}
		return neutral.getName();
	}
}
