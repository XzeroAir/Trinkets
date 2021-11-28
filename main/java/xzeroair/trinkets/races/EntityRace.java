package xzeroair.trinkets.races;

import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.races.dragon.RaceDragon;
import xzeroair.trinkets.races.dragon.RaceDragonAttributes;
import xzeroair.trinkets.races.dwarf.RaceDwarf;
import xzeroair.trinkets.races.dwarf.RaceDwarfAttributes;
import xzeroair.trinkets.races.elf.RaceElf;
import xzeroair.trinkets.races.elf.RaceElfAttributes;
import xzeroair.trinkets.races.faelis.RaceFaelis;
import xzeroair.trinkets.races.faelis.RaceFaelisAttributes;
import xzeroair.trinkets.races.fairy.RaceFairy;
import xzeroair.trinkets.races.fairy.RaceFairyAttributes;
import xzeroair.trinkets.races.goblin.RaceGoblin;
import xzeroair.trinkets.races.goblin.RaceGoblinAttributes;
import xzeroair.trinkets.races.human.RaceHuman;
import xzeroair.trinkets.races.titan.RaceTitan;
import xzeroair.trinkets.races.titan.RaceTitanAttributes;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.registry.TrinketRegistry;

public class EntityRace {

	public static final TrinketRegistry<ResourceLocation, EntityRace> Registry = Trinkets.RaceRegistry;
	private static int IndexID = 0;

	/*----------------------------------Constructor----------------------------------------*/

	private final UUID uuid;
	private final String name;
	private int primaryColor = 0;
	private int secondaryColor = 16777215;

	protected int magicAffinityValue = 100;
	protected int raceSize = 100;
	//	protected RaceAttributesWrapper attributes;

	public EntityRace(String name, String uuid, int color1, int color2) {
		this.name = name;
		this.uuid = UUID.fromString(uuid);
		//		attributes= new RaceAttributesWrapper();
		primaryColor = color1;
		secondaryColor = color2;
	}

	private static int nextID() {
		return IndexID++;
	}

	public static int getIdFromRace(EntityRace race) {
		return race == null ? 0 : Registry.getIDForObject(race);
	}

	public static ResourceLocation getRegistryNameFromRace(EntityRace race) {
		return race == null ? new ResourceLocation(Reference.MODID, "human") : Registry.getNameForObject(race);
	}

	public static EntityRace getRaceById(int id) {
		return Registry.getObjectById(id);
	}

	public static EntityRace getByUUID(UUID uuid) {
		return Registry.getObjectByUUID(uuid);
	}

	/**
	 * Tries to get an race by it's name (e.g. human) or a String representation of
	 * a numerical ID. If both fail, null is returned.
	 */
	@Nullable
	public static EntityRace getByNameOrId(String id) {
		final EntityRace race = Registry.getObject(new ResourceLocation(id.toLowerCase()));

		if (race == null) {
			try {
				return getRaceById(Integer.parseInt(id));
			} catch (final NumberFormatException var3) {
			}
		}

		return race;
	}

	/*-----------------------------------Code Start-------------------------------------*/

	public String getName() {
		return name;
	}

	public UUID getUUID() {
		return uuid;
	}

	public int getRaceSize() {
		return raceSize;
	}

	public EntityRace setRaceSize(int sizeV) {
		raceSize = sizeV;
		return this;
	}

	public int getMagicAffinity() {
		return magicAffinityValue;
	}

	public EntityRace setMagicAffinity(int magicAffinity) {
		magicAffinityValue = magicAffinity;
		return this;
	}

	public int getPrimaryColor() {
		return primaryColor;
	}

	public int getSecondaryColor() {
		return secondaryColor;
	}

	public EntityRacePropertiesHandler getRaceHandler(EntityLivingBase e) {
		switch (name) {
		case "Fairy":
			return new RaceFairy(e, Capabilities.getEntityRace(e));
		case "Dwarf":
			return new RaceDwarf(e, Capabilities.getEntityRace(e));
		case "Titan":
			return new RaceTitan(e, Capabilities.getEntityRace(e));
		case "Goblin":
			return new RaceGoblin(e, Capabilities.getEntityRace(e));
		case "Elf":
			return new RaceElf(e, Capabilities.getEntityRace(e));
		case "Faelis":
			return new RaceFaelis(e, Capabilities.getEntityRace(e));
		case "Dragon":
			return new RaceDragon(e, Capabilities.getEntityRace(e));
		case "Human":
			return new RaceHuman(e, Capabilities.getEntityRace(e));
		default:
			return new RaceHuman(e, Capabilities.getEntityRace(e));
		}
	}

	public RaceAttributesWrapper getRaceAttributes() {
		switch (name) {
		case "Fairy":
			return new RaceFairyAttributes();
		case "Dwarf":
			return new RaceDwarfAttributes();
		case "Titan":
			return new RaceTitanAttributes();
		case "Goblin":
			return new RaceGoblinAttributes();
		case "Elf":
			return new RaceElfAttributes();
		case "Faelis":
			return new RaceFaelisAttributes();
		case "Dragon":
			return new RaceDragonAttributes();
		case "Human":
			return new RaceAttributesWrapper();
		default:
			return new RaceAttributesWrapper();
		}
	}

	/*------------------------------------------Register Races--------------------------------------------*/

//@formatter:off
	public static void registerRaces() {
		registerRace(nextID(), (new EntityRace("None"		, "00000000-0000-0000-0000-000000000000", 11107684, 16374701)).setRaceSize(100).setMagicAffinity(100));
		registerRace(nextID(), (new EntityRace("Human"		, "c82ec7c3-2a9d-4a08-b0dd-7ce086c6771b", 11107684, 16374701)).setRaceSize(100).setMagicAffinity(100));
		registerRace(nextID(), (new EntityRace("Fairy"		, "e5869fac-0949-41f2-889b-4e6b8ca6d2e7", 12514535, 962222)).setRaceSize(25).setMagicAffinity(500));
		registerRace(nextID(), (new EntityRace("Dwarf"		, "917b555b-944a-4e44-afb6-ca638c6d91e5", 10832170, 7039851)).setRaceSize(75).setMagicAffinity(100));
		registerRace(nextID(), (new EntityRace("Titan"		, "a3bc433b-7bb7-4bd9-a88c-5fd120d04d59", 10066329, 3223595)).setRaceSize(300).setMagicAffinity(50));
		registerRace(nextID(), (new EntityRace("Elf"		, "25f92404-35f3-453b-ad48-9b788b2e12fc", 40960, 962222)).setRaceSize(100).setMagicAffinity(200));
		registerRace(nextID(), (new EntityRace("Goblin"		, "d917999a-0399-4c39-bfc5-79784dfff6ed", 6588004, 3096367)).setRaceSize(50).setMagicAffinity(75));
		registerRace(nextID(), (new EntityRace("Faelis"		, "cdccefa8-6a67-4394-b70d-c737953887a2", 16571252, 4465933)).setRaceSize(85).setMagicAffinity(125));
//		registerRace(nextID(), (new EntityRace("Slime"		, "5db9c85c-f830-44c7-b02f-8368ee5eca8a", 0, 0)).setRaceSize(100).setMagicAffinity(500));
		registerRace(nextID(), (new EntityRace("Dragon"		, "3b75821e-6ec6-4dfe-9612-b7a988a7b30b", 0, 0)).setRaceSize(120).setMagicAffinity(400));

//		registerRace(nextID(), (new EntityRace("Orc"		, "591d7d19-dd46-471f-b24f-e9967b1b95ef", 0, 0)).setRaceSize(150).setMagicAffinity(25));
//		registerRace(nextID(), (new EntityRace("Succubus"	, "cce3a5ca-134e-40ed-a27d-a89e1f05dc5f", 0, 0)).setRaceSize(100).setMagicAffinity(250));
//		registerRace(nextID(), (new EntityRace("Incubus"	, "20a52edc-d7d7-499f-a2e4-9d047df7cfba", 0, 0)).setRaceSize(100).setMagicAffinity(250));
//		registerRace(nextID(), (new EntityRace("Nymph"		, "14f31596-09a7-4be0-9592-4cb63d7e74bb", 0, 0)).setRaceSize(100).setMagicAffinity(400));
//		registerRace(nextID(), (new EntityRace("Siren"		, "1403f7e8-a427-4326-bcd9-1d7fdc1e22bb", 0, 0)).setRaceSize(100).setMagicAffinity(200));
//		registerRace(nextID(), (new EntityRaceMixed("Mixed"	, "10172391-1b14-4a2d-9387-5f819d56426f")).setRaceSize(100).setMagicAffinity(100));

		// TODO Move this event somewhere where it actually works
//		RegisterRaceEvent registryEvent = new RegisterRaceEvent(Registry);
//		MinecraftForge.EVENT_BUS.post(registryEvent);
//		for(Entry<ResourceLocation, EntityRace> entry : registryEvent.getEntries().entrySet()) {
//			Trinkets.log.info("Registering " + entry.getKey());
//			registerRace(nextID(), entry.getKey(), entry.getValue());
//		}
	}
//@formatter:on

	private static void registerRace(int id, EntityRace race) {
		registerRace(id, new ResourceLocation(Reference.MODID, race.getName().toLowerCase()), race);
	}

	private static void registerRace(int id, ResourceLocation textualID, EntityRace race) {
		Registry.register(id, textualID, race.getUUID(), race);
		//		if (!((race.getUUID().compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0) || (race.getUUID().compareTo(UUID.fromString("10172391-1b14-4a2d-9387-5f819d56426f")) == 0))) {
		//			if (Loader.isModLoaded("baubles")) {
		//				ModItems.baubles.ITEMS.add(new BaubleRaceBase(race.getName().toLowerCase() + "_ring", race));
		//			} else {
		//				ModItems.RaceTrinkets.ITEMS.add(new TrinketRaceBase(race.getName().toLowerCase() + "_ring", race));
		//			}
		//		}
	}
}
