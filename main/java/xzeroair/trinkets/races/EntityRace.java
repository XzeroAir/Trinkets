package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xzeroair.trinkets.Registries;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.dragon.RaceDragon;
import xzeroair.trinkets.races.dragon.RaceDragonDefaultInformation;
import xzeroair.trinkets.races.dwarf.RaceDwarf;
import xzeroair.trinkets.races.dwarf.RaceDwarfDefaultInformation;
import xzeroair.trinkets.races.elf.RaceElf;
import xzeroair.trinkets.races.elf.RaceElfDefaultInformation;
import xzeroair.trinkets.races.faelis.RaceFaelis;
import xzeroair.trinkets.races.faelis.RaceFaelisDefaultInformation;
import xzeroair.trinkets.races.fairy.RaceFairy;
import xzeroair.trinkets.races.fairy.RaceFairyDefaultInformation;
import xzeroair.trinkets.races.goblin.RaceGoblin;
import xzeroair.trinkets.races.goblin.RaceGoblinDefaultInformation;
import xzeroair.trinkets.races.human.RaceHuman;
import xzeroair.trinkets.races.human.RaceHumanDefaultInformation;
import xzeroair.trinkets.races.taurus.RaceTaurus;
import xzeroair.trinkets.races.taurus.RaceTaurusDefaultInformation;
import xzeroair.trinkets.races.titan.RaceTitan;
import xzeroair.trinkets.races.titan.RaceTitanDefaultInformation;
import xzeroair.trinkets.util.ConstantsLang;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityRace extends IForgeRegistryEntry.Impl<EntityRace> implements IDescriptionInterface {

    public static final ForgeRegistry<EntityRace> Registry = Registries.getRaceRegistry();//Trinkets.RaceRegistry;

    /*----------------------------------Constructor----------------------------------------*/

    protected final UUID uuid;
    protected final String name;
    protected int primaryColor = 3289650;
    protected int secondaryColor = 16777215;

    protected int magicAffinityValue = 100;
    protected int raceHeight = 100;
    protected int raceWidth = 100;
    protected boolean canFly = false;

    private EntityRace(String name, String uuid, int color1, int color2, boolean internal) {
        this(name, uuid, color1, color2);
        this.setRegistryName(name);
    }

    public EntityRace(String name, String uuid, int color1, int color2) {
        this.name = name;
        this.uuid = UUID.fromString(uuid);
        this.primaryColor = color1;
        this.secondaryColor = color2;
    }

    public int getID() {
        return Registry.getID(this);
    }

    public static int getIdFromRace(EntityRace race) {
        return race == null ? 0 : Registry.getID(race);
    }

    public static EntityRace getRaceById(int id) {
        return Registry.getValue(id);
    }

    public static EntityRace getByUUID(UUID uuid) {
        for (EntityRace race : Registry.getValuesCollection()) {
            if (race.getUUID().compareTo(uuid) == 0) {
                return race;
            }
        }
        return EntityRaces.none;//.getObjectByUUID(uuid);
    }

    /**
     * Tries to get a race by its name (e.g. human) or a String representation of
     * a numerical ID. If both fail, null is returned.
     */
    @Nullable
    public static EntityRace getByNameOrId(String id) {
        final EntityRace race = Registry.getValue(new ResourceLocation(id.toLowerCase()));

        if (race == null) {
            try {
                return getRaceById(Integer.parseInt(id));
            } catch (final NumberFormatException ignored) {
            }
        }

        return race;
    }

    /*-----------------------------------Code Start-------------------------------------*/

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getRaceHeight() {
        return this.raceHeight;
    }

    public String getTranslationKey() {
        return ConstantsLang.RACE + "." + this.getName().toLowerCase();
    }

    @Override
    public String getDisplayName() {
        return I18n.translateToLocal(this.getTranslationKey().toLowerCase() + ".name").trim();
    }

    @Override
    public void getDescription(List<String> tooltips, int rendMod, int rendID) {
        String translationKey = this.getTranslationKey();
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i <= 10; i++) {
            final String string = helper.getLangTranslation(translationKey + ".tooltip" + i);
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }
    }

    public EntityRace setRaceHeight(int raceHeight) {
        this.raceHeight = raceHeight;
        return this;
    }

    public int getRaceWidth() {
        return this.raceWidth;
    }

    public EntityRace setRaceWidth(int raceWidth) {
        this.raceWidth = raceWidth;
        return this;
    }

    public EntityRace setRaceSize(int sizeV) {
        return this.setRaceHeight(sizeV).setRaceWidth(sizeV);
    }

    public int getMagicAffinity() {
        return this.magicAffinityValue;
    }

    public EntityRace setMagicAffinity(int magicAffinity) {
        this.magicAffinityValue = magicAffinity;
        return this;
    }

    public boolean canFly() {
        return this.canFly;
    }

    public EntityRace setCanFly(boolean canFly) {
        this.canFly = canFly;
        return this;
    }

    public int getPrimaryColor() {
        return this.primaryColor;
    }

    public int getSecondaryColor() {
        return this.secondaryColor;
    }

    public EntityRacePropertiesHandler getRaceHandler(@Nullable EntityLivingBase e, @Nullable EntityProperties properties) {
        switch (this.name) {
            case "Fairy":
                return new RaceFairy(e, properties, new RaceCache(EntityRaces.fairy));
            case "Dwarf":
                return new RaceDwarf(e, properties, new RaceCache(EntityRaces.fairy));
            case "Titan":
                return new RaceTitan(e, properties, new RaceCache(EntityRaces.fairy));
            case "Goblin":
                return new RaceGoblin(e, properties, new RaceCache(EntityRaces.fairy));
            case "Elf":
                return new RaceElf(e, properties, new RaceCache(EntityRaces.fairy));
            case "Faelis":
                return new RaceFaelis(e, properties, new RaceCache(EntityRaces.fairy));
            case "Dragon":
                return new RaceDragon(e, properties, new RaceCache(EntityRaces.fairy));
            case "Human":
                return new RaceHuman(e, properties, new RaceCache(EntityRaces.fairy));
            case "Taurus":
                return new RaceTaurus(e, properties, new RaceCache(EntityRaces.fairy));
            default:
                return new EmptyHandler(e, properties);
        }
    }

    public EntityRacePropertiesHandler getRaceHandler(@Nullable EntityLivingBase e, @Nullable EntityProperties properties, RaceCache raceCache) {
        if (raceCache == null) {
            return this.getRaceHandler(e, properties);
        }
        switch (this.name) {
            case "Fairy":
                return new RaceFairy(e, properties, raceCache);
            case "Dwarf":
                return new RaceDwarf(e, properties, raceCache);
            case "Titan":
                return new RaceTitan(e, properties, raceCache);
            case "Goblin":
                return new RaceGoblin(e, properties, raceCache);
            case "Elf":
                return new RaceElf(e, properties, raceCache);
            case "Faelis":
                return new RaceFaelis(e, properties, raceCache);
            case "Dragon":
                return new RaceDragon(e, properties, raceCache);
            case "Human":
                return new RaceHuman(e, properties, raceCache);
            case "Taurus":
                return new RaceTaurus(e, properties, raceCache);
            default:
                return new EmptyHandler(e, properties);
        }
    }

    public RaceDefaultInformationWrapper getRaceInformation() {
        switch (this.name) {
            case "Fairy":
                return RaceFairyDefaultInformation.INSTANCE;
            case "Dwarf":
                return RaceDwarfDefaultInformation.INSTANCE;
            case "Titan":
                return RaceTitanDefaultInformation.INSTANCE;
            case "Goblin":
                return RaceGoblinDefaultInformation.INSTANCE;
            case "Elf":
                return RaceElfDefaultInformation.INSTANCE;
            case "Faelis":
                return RaceFaelisDefaultInformation.INSTANCE;
            case "Dragon":
                return RaceDragonDefaultInformation.INSTANCE;
            case "Human":
                return RaceHumanDefaultInformation.INSTANCE;
            case "Taurus":
                return RaceTaurusDefaultInformation.INSTANCE;
            default:
                return new RaceDefaultInformationWrapper();
        }
    }

    public boolean isNone() {
        return this.name.compareTo("None") == 0;
    }

    /*------------------------------------------Register Races--------------------------------------------*/

    //@formatter:off
	public static void registerRaces() {
		registerRace((new EntityRace("None"		, "00000000-0000-0000-0000-000000000000", 11107684, 16374701, true)).setRaceSize(100).setMagicAffinity(TrinketsConfig.SERVER.RACES.HUMAN.MAGIC.affinity));
		registerRace((new EntityRace("Human"		, "c82ec7c3-2a9d-4a08-b0dd-7ce086c6771b", 11107684, 16374701, true)).setRaceSize(100).setRaceHeight(TrinketsConfig.SERVER.RACES.HUMAN.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.HUMAN.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.HUMAN.MAGIC.affinity));
		registerRace((new EntityRace("Fairy"		, "e5869fac-0949-41f2-889b-4e6b8ca6d2e7", 12514535, 962222, true)).setRaceSize(25).setRaceHeight(TrinketsConfig.SERVER.RACES.FAIRY.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.FAIRY.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.FAIRY.MAGIC.affinity).setCanFly(true));
		registerRace((new EntityRace("Dwarf"		, "917b555b-944a-4e44-afb6-ca638c6d91e5", 10832170, 7039851, true)).setRaceSize(75).setRaceHeight(TrinketsConfig.SERVER.RACES.DWARF.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.DWARF.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.DWARF.MAGIC.affinity));
		registerRace((new EntityRace("Titan"		, "a3bc433b-7bb7-4bd9-a88c-5fd120d04d59", 10066329, 3223595, true)).setRaceSize(300).setRaceHeight(TrinketsConfig.SERVER.RACES.TITAN.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.TITAN.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.TITAN.MAGIC.affinity));
		registerRace((new EntityRace("Elf"		, "25f92404-35f3-453b-ad48-9b788b2e12fc", 16374701, 11107684, true)).setRaceSize(100).setRaceHeight(TrinketsConfig.SERVER.RACES.ELF.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.ELF.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.ELF.MAGIC.affinity));
		registerRace((new EntityRace("Goblin"		, "d917999a-0399-4c39-bfc5-79784dfff6ed", 6588004, 3096367, true)).setRaceSize(50).setRaceHeight(TrinketsConfig.SERVER.RACES.GOBLIN.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.GOBLIN.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.GOBLIN.MAGIC.affinity));
		registerRace((new EntityRace("Faelis"		, "cdccefa8-6a67-4394-b70d-c737953887a2", 16571252, 4465933, true)).setRaceSize(85).setRaceHeight(TrinketsConfig.SERVER.RACES.FAELIS.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.FAELIS.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.FAELIS.MAGIC.affinity));
		registerRace((new EntityRace("Dragon"		, "3b75821e-6ec6-4dfe-9612-b7a988a7b30b", 3289650, 9509561, true)).setRaceSize(120).setRaceHeight(TrinketsConfig.SERVER.RACES.DRAGON.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.DRAGON.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.DRAGON.MAGIC.affinity).setCanFly(true));
//		registerRace((new EntityRace("Slime"		, "5db9c85c-f830-44c7-b02f-8368ee5eca8a", 0, 0, true)).setRaceSize(100).setMagicAffinity(500));
		registerRace((new EntityRace("Taurus"		, "07f0d6c2-4177-412e-8de5-07c401209e44", 1315860, 4271658, true)).setRaceSize(120).setRaceHeight(TrinketsConfig.SERVER.RACES.TAURUS.SIZE.height).setRaceWidth(TrinketsConfig.SERVER.RACES.TAURUS.SIZE.width).setMagicAffinity(TrinketsConfig.SERVER.RACES.TAURUS.MAGIC.affinity));
//		registerRace((new EntityRace("Orc"		, "591d7d19-dd46-471f-b24f-e9967b1b95ef", 0, 0, true)).setRaceSize(150).setMagicAffinity(25));
//		registerRace((new EntityRace("Succubus"	, "cce3a5ca-134e-40ed-a27d-a89e1f05dc5f", 0, 0, true)).setRaceSize(100).setMagicAffinity(250));
//		registerRace((new EntityRace("Incubus"	, "20a52edc-d7d7-499f-a2e4-9d047df7cfba", 0, 0, true)).setRaceSize(100).setMagicAffinity(250));
//		registerRace((new EntityRace("Nymph"		, "14f31596-09a7-4be0-9592-4cb63d7e74bb", 0, 0, true)).setRaceSize(100).setMagicAffinity(400));
//		registerRace((new EntityRace("Siren"		, "1403f7e8-a427-4326-bcd9-1d7fdc1e22bb", 0, 0, true)).setRaceSize(100).setMagicAffinity(200));
//		registerRace((new EntityRaceMixed("Mixed"	, "10172391-1b14-4a2d-9387-5f819d56426f")).setRaceSize(100).setMagicAffinity(100));
	}
    //@formatter:on

    protected static void registerRace(EntityRace race) {
        Registry.register(race);
    }
}
