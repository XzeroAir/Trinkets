package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.mana.EntityManaConfig;
import xzeroair.trinkets.util.config.potions.PotionConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigGlowRing;
import xzeroair.trinkets.util.config.trinkets.ConfigGreaterInertia;
import xzeroair.trinkets.util.config.trinkets.ConfigInertiaNull;
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;
import xzeroair.trinkets.util.config.trinkets.ConfigWeightlessStone;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;

public class ServerConfig {

	private final static String cfgPrefix = Reference.MODID + ".config";
	private final static String serverCfgPrefix = cfgPrefix + ".server";

	@Config.Comment("Settings regarding the Trinkets Container on the server")
	@Config.Name("Trinket Container Settings")
	@LangKey(serverCfgPrefix + ".container.settings")
	public ContainerSettings GUI = new ContainerSettings();

	public class ContainerSettings {
		@Config.RequiresMcRestart
		@Config.Comment("How many slots should the Trinkets container have")
		@Config.Name("01. Container Enabled")
		@LangKey(serverCfgPrefix + ".container.enabled")
		public boolean guiEnabled = true;

		@Config.RequiresMcRestart
		@Config.RangeInt(min = 1, max = 32)
		@Config.Comment("How many slots should the Trinkets container have")
		@Config.Name("02. Container Slots")
		@LangKey(serverCfgPrefix + ".container.slots")
		public int guiSlotsRows = 8;
	}

	@Config.Name("Magical Foods Settings")
	@Config.Comment("")
	@LangKey(serverCfgPrefix + ".food.settings")
	public Foods Food = new Foods();

	public class Foods {
		@Config.RequiresMcRestart
		@Config.Comment("Should this mod add Magical Foods?. Set to False to Disable. Default True")
		@Config.Name("01. Foods Enabled")
		@LangKey(serverCfgPrefix + ".food.registry.enabled")
		public boolean foods_enabled = true;

		@Config.Comment("Set to False to Disable. Default True")
		@Config.Name("02. Foods Effects")
		@LangKey(serverCfgPrefix + ".food.transformation.effect")
		public boolean food_effects = true;

		@Config.Comment("Should the player keep the Food Effects on Death. Set to False to Disable. Default True")
		@Config.Name("03. Keep Effects")
		@LangKey(serverCfgPrefix + ".food.transformation.effect.keep")
		public boolean keep_effects = true;
	}

	@Config.Name("Potion Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".potion.settings")
	public Potions Potion = new Potions();

	public class Potions {
		private final String potion = serverCfgPrefix + ".potion";

		@Config.RequiresMcRestart
		@Config.Comment("Should T&B Register any potions")
		@Config.Name("00. Potions Enabled")
		@LangKey(potion + ".registry.enabled")
		public boolean potions_enabled = true;

		@Config.Comment("If Tough as Nails is Installed, Should the Potions Give water?")
		@Config.Name("01. Potion Give Water")
		@LangKey(potion + ".toughasnails.water")
		public boolean potion_thirst = true;

		@Config.Name("Normie Potion")
		@Config.Comment("")
		@LangKey(potion + ".human")
		public PotionConfig human = new PotionConfig("minecraft:apple", 3600);
		@Config.Name("Dwarf Potion")
		@Config.Comment("")
		@LangKey(potion + ".dwarf")
		public PotionConfig dwarf = new PotionConfig("minecraft:iron_block", 1200);
		@Config.Name("Elf Potion")
		@Config.Comment("")
		@LangKey(potion + ".elf")
		public PotionConfig elf = new PotionConfig("minecraft:leaves", 1200);
		@Config.Name("Fairy Potion")
		@Config.Comment("")
		@LangKey(potion + ".fairy")
		public PotionConfig fairy = new PotionConfig("minecraft:ghast_tear", 1200);
		@Config.Name("Goblin Potion")
		@Config.Comment("")
		@LangKey(potion + ".goblin")
		public PotionConfig goblin = new PotionConfig("minecraft:leather", 1200);
		@Config.Name("Titan Potion")
		@Config.Comment("")
		@LangKey(potion + ".titan")
		public PotionConfig titan = new PotionConfig("minecraft:golden_apple", 1200);
		@Config.Name("Faelis Potion")
		@Config.Comment("")
		@LangKey(potion + ".faelis")
		public PotionConfig faelis = new PotionConfig("xat:faelis_claw", 1200);
		@Config.Name("Dragon Potion")
		@Config.Comment("")
		@LangKey(potion + ".dragon")
		public PotionConfig dragon = new PotionConfig("minecraft:dragon_breath", 1200);

		@Config.Comment("Should T&B Transformation Potions work only on players")
		@Config.Name("09. Work on players only")
		@LangKey(potion + ".playersonly")
		public boolean players_only = true;
	}

	@Config.Name("Item Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + "." + "items")
	public TrinketItems Items = new TrinketItems();

	public class TrinketItems {
		@Config.Name("Transformation Items")
		@Config.Comment("")
		@LangKey(cfgPrefix + ".items.transformation")
		public RaceRings raceRings = new RaceRings();

		public class RaceRings {
			@Config.Name("Dwarf Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.DwarfRing)
			public TransformationRingConfig DWARF_RING = new TransformationRingConfig();//new DwarfRing();

			@Config.Name("Elf Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.ElfRing)
			public TransformationRingConfig ELF_RING = new TransformationRingConfig();// new DwarfRing(); /// Here

			@Config.Name("Fairy Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.FairyRing)
			public TransformationRingConfig FAIRY_RING = new TransformationRingConfig();// new FairyRing();

			@Config.Name("Goblin Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.GoblinRing)
			public TransformationRingConfig GOBLIN_RING = new TransformationRingConfig();// new DwarfRing(); /// Here

			@Config.Name("Titan Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.TitanRing)
			public TransformationRingConfig TITAN_RING = new TransformationRingConfig();// new TitanRing();

			@Config.Name("Faelis Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.FaelisRing)
			public TransformationRingConfig FAELIS_RING = new TransformationRingConfig();// new FaelisRing();

			@Config.Name("Dragon Ring Settings")
			@Config.Comment("")
			@LangKey(cfgPrefix + "." + ModItems.DragonRing)
			public TransformationRingConfig DRAGON_RING = new TransformationRingConfig();// new FaelisRing();
		}

		@Config.Name("Dragon's Eye Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.DragonsEye)
		public ConfigDragonsEye DRAGON_EYE = new ConfigDragonsEye();

		@Config.Name("Ender Queen's Crown Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.EnderTiara)
		public ConfigEnderCrown ENDER_CROWN = new ConfigEnderCrown();

		@Config.Name("Damage Shield Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.DamageShield)
		public ConfigDamageShield DAMAGE_SHIELD = new ConfigDamageShield();

		@Config.Name("Ring of Enchanted Eyes Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.GlowRing)
		public ConfigGlowRing GLOW_RING = new ConfigGlowRing();

		@Config.Name("Poison Stone Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.Poison)
		public ConfigPoisonStone POISON_STONE = new ConfigPoisonStone();

		@Config.Name("Wither Ring Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.WitherRing)
		public ConfigWitherRing WITHER_RING = new ConfigWitherRing();

		@Config.Name("Polarized Stone Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.Polarized)
		public ConfigPolarizedStone POLARIZED_STONE = new ConfigPolarizedStone();

		@Config.Name("Stone of the Sea Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.Sea)
		public ConfigSeaStone SEA_STONE = new ConfigSeaStone();

		@Config.Name("Stone of Inertia Null Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.InertiaNull)
		public ConfigInertiaNull INERTIA_NULL = new ConfigInertiaNull();

		@Config.Name("Stone of Greater Inertia Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.GreaterInertia)
		public ConfigGreaterInertia GREATER_INERTIA = new ConfigGreaterInertia();

		@Config.Name("Weightless Stone Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.Weightless)
		public ConfigWeightlessStone WEIGHTLESS_STONE = new ConfigWeightlessStone();

		@Config.Name("Arcing Orb Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.ArcingOrb)
		public ConfigArcingOrb ARCING_ORB = new ConfigArcingOrb();

		@Config.Name("Teddy Bear Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.TeddyBear)
		public ConfigTeddyBear TEDDY_BEAR = new ConfigTeddyBear();

		@Config.Name("Faelis Claw Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + ModItems.FaelisClaws)
		public ConfigFaelisClaw FAELIS_CLAW = new ConfigFaelisClaw();

	}

	@Config.Name("Magic Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".magic")
	public EntityManaConfig mana = new EntityManaConfig();

	@Config.Name("Race Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".races")
	public RaceConfigs races = new RaceConfigs();

	public class RaceConfigs {

		@Config.Name("Selection Menu")
		@Config.Comment("")
		public boolean selectionMenu = false;

		@Config.Name("Selection Menu Blacklist")
		@Config.Comment("")
		@Config.RequiresWorldRestart
		public String[] selectionBlacklist = {
				"Dragon"
		};

		@Config.Name("Fairy Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "fairy")
		public FairyConfig fairy = new FairyConfig();

		@Config.Name("Dwarf Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "dwarf")
		public DwarfConfig dwarf = new DwarfConfig();

		@Config.Name("Titan Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "titan")
		public TitanConfig titan = new TitanConfig();

		@Config.Name("Goblin Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "goblin")
		public GoblinConfig goblin = new GoblinConfig();

		@Config.Name("Elf Settings")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "elf")
		public ElfConfig elf = new ElfConfig();

		@Config.Name("Faelis Config")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "faelis")
		public FaelisConfig faelis = new FaelisConfig();

		@Config.Name("Dragon Config")
		@Config.Comment("")
		@LangKey(cfgPrefix + "." + "races." + "dragon")
		public DragonConfig dragon = new DragonConfig();

		//			@Config.Name("Human Settings")
		//			@LangKey(PREFIX + "." + "races." + "human")
		//			public HumanConfig human = new HumanConfig();

	}

	@Config.Name("Misc Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".misc")
	public MiscConfigs misc = new MiscConfigs();

	public class MiscConfigs {
		@Config.RequiresWorldRestart
		@Config.Comment("Does Depth Strider Stack with Swim Speed Attributes?")
		@Config.Name("Depth Strider Stacks")
		@LangKey(cfgPrefix + ".misc.depth")
		public boolean depthStacks = false;

		@Config.RequiresWorldRestart
		@Config.Comment("If enabled, the player will be unable to move when transforming from one race to another")
		@Config.Name("Prevent Movement while transforming")
		@LangKey(cfgPrefix + ".misc.movement")
		public boolean movement = false;

		@Config.Comment("Vanilla MC doesn't handle interaction with increased reach properly, this fixes it")
		@Config.Name("Reach Interaction Fix")
		@LangKey(cfgPrefix + ".misc.interaction.fix")
		public boolean reach = true;

		@Config.Comment("The VIP list may be unaccessible under some circumstance, use this to shorten game launching time.")
		@Config.Name("VIP")
		@LangKey(cfgPrefix + ".misc.vip")
		public boolean retrieveVIP = true;
	}

}
