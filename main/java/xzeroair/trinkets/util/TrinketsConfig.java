package xzeroair.trinkets.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.config.compat.CompatabilityConfigs;
import xzeroair.trinkets.util.config.gui.TrinketsContainerConfig;
import xzeroair.trinkets.util.config.gui.TrinketsPropertiesConfig;
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
import xzeroair.trinkets.util.helpers.CallHelper;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("xat.config.title")
public class TrinketsConfig {

	private final static String cfgPrefix = Reference.MODID + ".config";

	@Name("Client Settings")
	@LangKey(cfgPrefix + ".client.settings")
	public static xClient CLIENT = new xClient();

	public static class xClient {

		public DebugConfig debug = new DebugConfig();

		public class DebugConfig {

			public boolean debugArmorMaterials = false;

		}

		@Name("Gui Settings")
		@LangKey(cfgPrefix + ".client.gui.settings")
		public TrinketsContainerConfig GUI = new TrinketsContainerConfig();

		@Name("Mana Bar Settings")
		@LangKey(cfgPrefix + ".magic.mana.hud.settings")
		public ManaBar MPBar = new ManaBar();

		public class ManaBar {

			private final String name = "hud";
			private final String PREFIX = Reference.MODID + ".config.magic.mana" + name;

			@Config.Comment("Mana Bar shown")
			@Name("01. Show Mana")
			@LangKey(PREFIX + ".shown")
			public boolean shown = true;

			@Config.Comment("Should the Mana Bar show even when full")
			@Name("02. Show when Full")
			@LangKey(PREFIX + ".shown.always")
			public boolean always_shown = false;

			@Config.Comment("Should the Mana Bar display horizontal or vertical?")
			@Name("03. Mana Bar Horizontal")
			@LangKey(PREFIX + ".horizontal")
			public boolean mana_horizontal = true;

			@Config.Comment("Show next to Mana Bar Text?")
			@Name("04. Hide Text")
			@LangKey(PREFIX + ".text.shown")
			public boolean hide_text = false;

			@Name("05. Location X")
			@LangKey(PREFIX + ".location.x")
			public double translatedX = 0;
			//			public int X = 0;
			@Name("06. Location Y")
			@LangKey(PREFIX + ".location.y")
			public double translatedY = 0;
			//			public int Y = 0;
			//			@Name("07. Translated X")
			//			@LangKey(PREFIX + ".location.x")
			//			public double translatedX = 0;
			//			@Name("08. Translated Y")
			//			@LangKey(PREFIX + ".location.y")
			//			public double translatedY = 0;

			@Name("09. Width")
			public int width = 106;
			@Name("10. Height")
			public int height = 16;

		}

		@Name("Race Properties Gui")
		@LangKey(cfgPrefix + ".race.properties.gui.settings")
		public TrinketsPropertiesConfig raceProperties = new TrinketsPropertiesConfig();

		@Config.Comment("POV Height adjustments when wearing Race Rings. Set to False to Disable. Default True")
		@Name("POV Height Adjustments")
		public boolean cameraHeight = true;

		@Config.Comment("Rendering of Trinkets. Set to False to Disable. Default True")
		@Name("Render Trinkets")
		@LangKey(cfgPrefix + ".client.render.trinkets.all")
		public boolean rendering = true;

		@Config.Comment("When using the Enchanted Race rings, should Trinkets and Baubles Replace the EntityRenderer to solve some of the camera clipping issues?")
		@Name("Replace EntityRenderer")
		@LangKey(cfgPrefix + ".client.entity.renderer.replace")
		public boolean entityRenderer = false;

		@Config.Comment("When Trinket and Baubles Replaces the EntityRenderer is breaks some settings with Optifine, Specifically Fog, use this to turn Fog on or off")
		@Name("EntityRenderer Fog")
		@LangKey(cfgPrefix + ".client.entity.renderer.replace.fog")
		public boolean RendererFog = false;

		@Name("Items")
		public TrinketItems items = new TrinketItems();

		public class TrinketItems {
			@Name("Dragon's Eye Settings")
			@LangKey(cfgPrefix + ".dragons_eye")
			public Dragon DRAGON_EYE = new Dragon();

			public class Dragon {
				@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79, 20 MIN, 360 MAX")
				@Name("Refresh Rate")
				@RangeInt(min = 20, max = 360)
				@LangKey(cfgPrefix + ".dragons_eye.client.orefinder.refresh.rate")
				public int C00_RR = 79;

				@Config.Comment("Should the Dragon's Eye play a Sound when Ore is Nearby? Set to False to Disable. Default True")
				@Name("Dragon's growl")
				@LangKey(cfgPrefix + ".dragons_eye.client.growl.enabled")
				public boolean Dragon_Growl = true;

				@Config.Comment("Should the Dragon's Eye a Sound when Sneaking. Options 'SNEAK', 'STAND', 'BOTH'  Default SNEAK")
				@Name("Dragon's growl Sneak")
				@LangKey(cfgPrefix + ".dragons_eye.client.growl.activation")
				public String Dragon_Growl_Sneak = "SNEAK";

				@Config.Comment("The Volume of the Dragon's growl when detecting nearby Treasure. Default 100, 0 MIN, 300 MAX")
				@Name("Dragon's growl Volume")
				@RangeInt(min = 0, max = 300)
				@LangKey(cfgPrefix + ".dragons_eye.client.growl.volume")
				public int Dragon_Growl_Volume = 100;
			}

			@Name("Fairy Ring Settings")
			@LangKey(cfgPrefix + ".fairy_ring")
			public Fairy FAIRY_RING = new Fairy();

			public class Fairy {
				@Name("Render Trinket on Player")
				@LangKey(cfgPrefix + ".client.render.trinket")
				public boolean doRender = true;
			}

			@Name("Shield of Honor Settings")
			@LangKey(cfgPrefix + ".damage_shield")
			public Shield DAMAGE_SHIELD = new Shield();

			public class Shield {
				@Name("Render Trinket on Player")
				@LangKey(cfgPrefix + ".client.render.trinket")
				public boolean doRender = true;

				public double effectVolume = 0.2D;
			}

			@Name("Ender Queen's Crown Settings")
			@LangKey(cfgPrefix + ".ender_tiara")
			public Crown ENDER_CROWN = new Crown();

			public class Crown {
				@Name("Render Trinket on Player")
				@LangKey(cfgPrefix + ".client.render.trinket")
				public boolean doRender = true;
			}

			@Name("Stone of the Sea Settings")
			@LangKey(cfgPrefix + ".sea_stone")
			public Sea SEA_STONE = new Sea();

			public class Sea {
				@Name("Render Trinket on Player")
				@LangKey(cfgPrefix + ".client.render.trinket")
				public boolean doRender = true;
			}

			@Name("Faelis Claw Settings")
			@LangKey(cfgPrefix + ".faelis_claw")
			public Claw FAELIS_CLAW = new Claw();

			public class Claw {
				@Name("Render Trinket on Player")
				@LangKey(cfgPrefix + ".client.render.trinket")
				public boolean doRender = true;
			}
		}
	}

	@Name("Server Settings")
	@LangKey(cfgPrefix + ".server.settings")
	public static xServer SERVER = new xServer();

	public static class xServer {

		@Config.Comment("Should Trinkets and Baubles use it's own Built-in Container")
		@Name("Gui Settings")
		@LangKey(cfgPrefix + ".server.gui.settings")
		public GuiSettings GUI = new GuiSettings();

		public class GuiSettings {
			@Config.RequiresMcRestart
			@Name("01. Trinkets Gui Enabled")
			@LangKey(cfgPrefix + ".server.gui.settings.enabled")
			public boolean guiEnabled = true;

			@Config.RequiresMcRestart
			@Config.RangeInt(min = 1, max = 4)
			@Name("02. Gui Slots Rows")
			public int guiSlotsRows = 1;

			@Config.RequiresMcRestart
			@Config.RangeInt(min = 0, max = 8)
			@Name("03. Gui Slots Row Length")
			public int guiSlotsRowLength = 8;
		}

		@Name("Magical Foods Settings")
		@LangKey(cfgPrefix + ".server.food.settings")
		public Foods Food = new Foods();

		public class Foods {
			@Config.RequiresMcRestart
			@Config.Comment("Should this mod add Magical Foods?. Set to False to Disable. Default True")
			@Name("01. Foods Enabled")
			@LangKey(cfgPrefix + ".food.registry.enabled")
			public boolean foods_enabled = true;

			@Config.Comment("Set to False to Disable. Default True")
			@Name("02. Foods Effects")
			@LangKey(cfgPrefix + ".food.transformation.effect")
			public boolean food_effects = true;

			@Config.Comment("Should the player keep the Food Effects on Death. Set to False to Disable. Default True")
			@Name("03. Keep Effects")
			@LangKey(cfgPrefix + ".food.transformation.effect.keep")
			public boolean keep_effects = true;
		}

		@Name("Potion Settings")
		@LangKey(cfgPrefix + ".server.potion.settings")
		public Potions Potion = new Potions();

		public class Potions {
			private final String potion = cfgPrefix + ".potion";
			@Config.RequiresMcRestart
			@Name("00. Potions Enabled")
			@LangKey(potion + ".registry.enabled")
			public boolean potions_enabled = true;

			@Config.Comment("If Tough as Nails is Installed, Should the Potions Give water?")
			@Name("01. Potion Give Water")
			@LangKey(potion + ".toughasnails.water")
			public boolean potion_thirst = true;

			@Config.RequiresMcRestart
			@Name("02. Restorative Potion Catalyst Item")
			@LangKey(potion + ".restore.catalyst")
			public String restoreCatalyst = "minecraft:nether_star";

			@Name("Normie Potion")
			@LangKey(potion + ".human")
			public PotionConfig human = new PotionConfig("minecraft:apple", 3600);
			@Name("Dwarf Potion")
			@LangKey(potion + ".dwarf")
			public PotionConfig dwarf = new PotionConfig("minecraft:iron_block", 1200);
			@Name("Elf Potion")
			@LangKey(potion + ".elf")
			public PotionConfig elf = new PotionConfig("minecraft:leaves", 1200);
			@Name("Fairy Potion")
			@LangKey(potion + ".fairy")
			public PotionConfig fairy = new PotionConfig("minecraft:ghast_tear", 1200);
			@Name("Goblin Potion")
			@LangKey(potion + ".goblin")
			public PotionConfig goblin = new PotionConfig("minecraft:leather", 1200);
			@Name("Titan Potion")
			@LangKey(potion + ".titan")
			public PotionConfig titan = new PotionConfig("minecraft:golden_apple", 1200);
			@Name("Faelis Potion")
			@LangKey(potion + ".faelis")
			public PotionConfig faelis = new PotionConfig("xat:faelis_claw", 1200);
			@Name("Dragon Potion")
			@LangKey(potion + ".dragon")
			public PotionConfig dragon = new PotionConfig("minecraft:dragon_breath", 1200);

			@Config.RequiresMcRestart
			@Name("09. Work on players only")
			@LangKey(potion + ".playersonly")
			public boolean players_only = true;
		}

		@Name("Item Settings")
		@LangKey(cfgPrefix + "." + "items")
		public TrinketItems Items = new TrinketItems();

		public class TrinketItems {

			@Name("Transformation Items")
			@LangKey(cfgPrefix + "." + "items.transformation")
			public RaceRings raceRings = new RaceRings();

			public class RaceRings {
				@Name("Dwarf Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.DwarfRing)
				public TransformationRingConfig DWARF_RING = new TransformationRingConfig();//new DwarfRing();

				@Name("Elf Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.ElfRing)
				public TransformationRingConfig ELF_RING = new TransformationRingConfig();// new DwarfRing(); /// Here

				@Name("Fairy Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.FairyRing)
				public TransformationRingConfig FAIRY_RING = new TransformationRingConfig();// new FairyRing();

				@Name("Goblin Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.GoblinRing)
				public TransformationRingConfig GOBLIN_RING = new TransformationRingConfig();// new DwarfRing(); /// Here

				@Name("Titan Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.TitanRing)
				public TransformationRingConfig TITAN_RING = new TransformationRingConfig();// new TitanRing();

				@Name("Faelis Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.FaelisRing)
				public TransformationRingConfig FAELIS_RING = new TransformationRingConfig();// new FaelisRing();

				@Name("Dragon Ring Settings")
				@LangKey(cfgPrefix + "." + ModItems.DragonRing)
				public TransformationRingConfig DRAGON_RING = new TransformationRingConfig();// new FaelisRing();
			}

			@Name("Dragon's Eye Settings")
			@LangKey(cfgPrefix + "." + ModItems.DragonsEye)
			public ConfigDragonsEye DRAGON_EYE = new ConfigDragonsEye();

			@Name("Ender Queen's Crown Settings")
			@LangKey(cfgPrefix + "." + ModItems.EnderTiara)
			public ConfigEnderCrown ENDER_CROWN = new ConfigEnderCrown();

			@Name("Damage Shield Settings")
			@LangKey(cfgPrefix + "." + ModItems.DamageShield)
			public ConfigDamageShield DAMAGE_SHIELD = new ConfigDamageShield();

			@Name("Ring of Enchanted Eyes Settings")
			@LangKey(cfgPrefix + "." + ModItems.GlowRing)
			public ConfigGlowRing GLOW_RING = new ConfigGlowRing();

			@Name("Poison Stone Settings")
			@LangKey(cfgPrefix + "." + ModItems.Poison)
			public ConfigPoisonStone POISON_STONE = new ConfigPoisonStone();

			@Name("Wither Ring Settings")
			@LangKey(cfgPrefix + "." + ModItems.WitherRing)
			public ConfigWitherRing WITHER_RING = new ConfigWitherRing();

			@Name("Polarized Stone Settings")
			@LangKey(cfgPrefix + "." + ModItems.Polarized)
			public ConfigPolarizedStone POLARIZED_STONE = new ConfigPolarizedStone();

			@Name("Stone of the Sea Settings")
			@LangKey(cfgPrefix + "." + ModItems.Sea)
			public ConfigSeaStone SEA_STONE = new ConfigSeaStone();

			@Name("Stone of Inertia Null Settings")
			@LangKey(cfgPrefix + "." + ModItems.InertiaNull)
			public ConfigInertiaNull INERTIA_NULL = new ConfigInertiaNull();

			@Name("Stone of Greater Inertia Settings")
			@LangKey(cfgPrefix + "." + ModItems.GreaterInertia)
			public ConfigGreaterInertia GREATER_INERTIA = new ConfigGreaterInertia();

			@Name("Weightless Stone Settings")
			@LangKey(cfgPrefix + "." + ModItems.Weightless)
			public ConfigWeightlessStone WEIGHTLESS_STONE = new ConfigWeightlessStone();

			@Name("Arcing Orb Settings")
			@LangKey(cfgPrefix + "." + ModItems.ArcingOrb)
			public ConfigArcingOrb ARCING_ORB = new ConfigArcingOrb();

			@Name("Teddy Bear Settings")
			@LangKey(cfgPrefix + "." + ModItems.TeddyBear)
			public ConfigTeddyBear TEDDY_BEAR = new ConfigTeddyBear();

			@Name("Faelis Claw Settings")
			@LangKey(cfgPrefix + "." + ModItems.FaelisClaws)
			public ConfigFaelisClaw FAELIS_CLAW = new ConfigFaelisClaw();

		}

		@Name("Magic Settings")
		@LangKey(cfgPrefix + "." + "magic")
		public EntityManaConfig mana = new EntityManaConfig();

		@Name("Race Settings")
		@LangKey(cfgPrefix + ".races")
		public RaceConfigs races = new RaceConfigs();

		public class RaceConfigs {

			@Name("Fairy Settings")
			@LangKey(cfgPrefix + "." + "races." + "fairy")
			public FairyConfig fairy = new FairyConfig();

			@Name("Dwarf Settings")
			@LangKey(cfgPrefix + "." + "races." + "dwarf")
			public DwarfConfig dwarf = new DwarfConfig();

			@Name("Titan Settings")
			@LangKey(cfgPrefix + "." + "races." + "titan")
			public TitanConfig titan = new TitanConfig();

			@Name("Goblin Settings")
			@LangKey(cfgPrefix + "." + "races." + "goblin")
			public GoblinConfig goblin = new GoblinConfig();

			@Name("Elf Settings")
			@LangKey(cfgPrefix + "." + "races." + "elf")
			public ElfConfig elf = new ElfConfig();

			@Name("Faelis Config")
			@LangKey(cfgPrefix + "." + "races." + "faelis")
			public FaelisConfig faelis = new FaelisConfig();

			@Name("Dragon Config")
			@LangKey(cfgPrefix + "." + "races." + "dragon")
			public DragonConfig dragon = new DragonConfig();

			//			@Name("Human Settings")
			//			@LangKey(PREFIX + "." + "races." + "human")
			//			public HumanConfig human = new HumanConfig();

		}

		@Name("Misc Settings")
		@LangKey(cfgPrefix + ".misc")
		public MiscConfigs misc = new MiscConfigs();

		public class MiscConfigs {
			@Config.RequiresWorldRestart
			@Config.Comment("Does Depth Strider Stack with Swim Speed Attributes?")
			@Name("Depth Strider Stacks")
			@LangKey(cfgPrefix + "." + "misc." + "depth")
			public boolean depthStacks = false;

			@Config.RequiresWorldRestart
			@Config.Comment("If enabled, the player will be unable to move when transforming from one race to another")
			@Name("Prevent Movement while transforming")
			@LangKey(cfgPrefix + "." + "misc." + "movement")
			public boolean movement = false;
		}
	}

	@Name("Compatability Settings")
	@LangKey(cfgPrefix + ".compatability")
	public static CompatabilityConfigs compat = new CompatabilityConfigs();

	public static Map<String, String> writeConfigMap() {
		final Map<String, String> configMap = new HashMap<>();
		configMap.put("oreEnabled", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder);
		configMap.put("oreEnabled.closest", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest);
		configMap.put("oreEnabled.blocks", CallHelper.combineStringArray(TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks));
		configMap.put("oreEnabled.hd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD);
		configMap.put("oreEnabled.vd", "" + TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD);
		configMap.put("dragonFlight", "" + TrinketsConfig.SERVER.races.dragon.creative_flight);
		configMap.put("dragonFlightCost", "" + TrinketsConfig.SERVER.races.dragon.flight_cost);
		configMap.put("dragonFlightSpeed", "" + TrinketsConfig.SERVER.races.dragon.flight_speed);
		configMap.put("dragonBreathCost", "" + TrinketsConfig.SERVER.races.dragon.breath_cost);
		configMap.put("dragonBreathDamage", "" + TrinketsConfig.SERVER.races.dragon.breath_damage);
		configMap.put("fairyFlight", "" + TrinketsConfig.SERVER.races.fairy.creative_flight);
		configMap.put("fairyFlightSpeed", "" + TrinketsConfig.SERVER.races.fairy.flight_speed);
		configMap.put("fairyFlightCost", "" + TrinketsConfig.SERVER.races.fairy.flight_cost);
		configMap.put("TrinketContainer", "" + TrinketsConfig.SERVER.GUI.guiEnabled);
		configMap.put("compatTAN", "" + TrinketsConfig.compat.toughasnails);
		configMap.put("compatSD", "" + TrinketsConfig.compat.simpledifficulty);
		configMap.put("compatArtemisLib", "" + TrinketsConfig.compat.artemislib);
		configMap.put("compatElenaiDodge1", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatElenaiDodge2", "" + TrinketsConfig.compat.elenaiDodge);
		configMap.put("compatEnhancedVisuals", "" + TrinketsConfig.compat.enhancedvisuals);
		configMap.put("compatLycanites", "" + TrinketsConfig.compat.lycanites);
		configMap.put("compatDefiledLands", "" + TrinketsConfig.compat.defiledlands);
		configMap.put("miscDepthStacking", "" + TrinketsConfig.SERVER.misc.depthStacks);
		configMap.put("miscBlockMovement", "" + TrinketsConfig.SERVER.misc.movement);
		return configMap;
	}

	public static void readConfigMap(Map<String, String> configMap) {
		if ((configMap != null) && !configMap.isEmpty()) {
			Trinkets.log.info("Found Server Config");
			for (final Entry<String, String> config : configMap.entrySet()) {
				try {
					if (config.getKey().contentEquals("oreEnabled")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.closest")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.closest = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.blocks")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks = CallHelper.deconstructStringArray(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.hd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C001_HD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("oreEnabled.vd")) {
						TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.DR.C00_VD = Integer.parseInt(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlight")) {
						TrinketsConfig.SERVER.races.dragon.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightCost")) {
						TrinketsConfig.SERVER.races.dragon.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonFlightSpeed")) {
						TrinketsConfig.SERVER.races.dragon.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathCost")) {
						TrinketsConfig.SERVER.races.dragon.breath_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("dragonBreathDamage")) {
						TrinketsConfig.SERVER.races.dragon.breath_damage = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlight")) {
						TrinketsConfig.SERVER.races.fairy.creative_flight = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightSpeed")) {
						TrinketsConfig.SERVER.races.fairy.flight_speed = Double.parseDouble(config.getValue());
					}
					if (config.getKey().contentEquals("fairyFlightCost")) {
						TrinketsConfig.SERVER.races.fairy.flight_cost = Float.parseFloat(config.getValue());
					}
					if (config.getKey().contentEquals("TrinketContainer")) {
						TrinketsConfig.SERVER.GUI.guiEnabled = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatTAN")) {
						TrinketsConfig.compat.toughasnails = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatSD")) {
						TrinketsConfig.compat.simpledifficulty = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatArtemisLib")) {
						TrinketsConfig.compat.artemislib = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge1")) {
						TrinketsConfig.compat.elenaiDodge = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatElenaiDodge2")) {
						TrinketsConfig.SERVER.GUI.guiEnabled = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatEnhancedVisuals")) {
						TrinketsConfig.compat.enhancedvisuals = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatLycanites")) {
						TrinketsConfig.compat.lycanites = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("compatDefiledLands")) {
						TrinketsConfig.compat.defiledlands = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscDepthStacking")) {
						TrinketsConfig.SERVER.misc.depthStacks = Boolean.parseBoolean(config.getValue());
					}
					if (config.getKey().contentEquals("miscBlockMovement")) {
						TrinketsConfig.SERVER.misc.movement = Boolean.parseBoolean(config.getValue());
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Save() {
		ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
	}

	public static void readConfig() {
		final Configuration cfg = Trinkets.config;
		try {
			cfg.load();

		} catch (final Exception e1) {
			Trinkets.log.error("Xat had a problem loading it's configuration");
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}

}