package xzeroair.trinkets.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.config.compat.CompatabilityConfigs;
import xzeroair.trinkets.util.config.gui.TrinketsGui;
import xzeroair.trinkets.util.config.trinkets.DamageShield;
import xzeroair.trinkets.util.config.trinkets.DragonsEye;
import xzeroair.trinkets.util.config.trinkets.DwarfRing;
import xzeroair.trinkets.util.config.trinkets.EnderCrown;
import xzeroair.trinkets.util.config.trinkets.FairyRing;
import xzeroair.trinkets.util.config.trinkets.GlowRing;
import xzeroair.trinkets.util.config.trinkets.GreaterInertia;
import xzeroair.trinkets.util.config.trinkets.InertiaNull;
import xzeroair.trinkets.util.config.trinkets.PoisonStone;
import xzeroair.trinkets.util.config.trinkets.PolarizedStone;
import xzeroair.trinkets.util.config.trinkets.SeaStone;
import xzeroair.trinkets.util.config.trinkets.WeightlessStone;
import xzeroair.trinkets.util.config.trinkets.WitherRing;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("Main.TrinketsConfig.title")
public class TrinketsConfig {

	@Name("Client Settings")
	public static xClient CLIENT = new xClient();
	public static class xClient {

		@Name("Gui Settings")
		public TrinketsGui GUI = new TrinketsGui();

		@Config.Comment("POV Height adjustments when wearing Race Rings. Set to False to Disable. Default True")
		@Name("POV Height Adjustments")
		public boolean cameraHeight = true;

		@Config.Comment("Rendering of Trinkets. Set to False to Disable. Default True")
		@Name("Render Trinkets")
		public boolean rendering = true;

		@Name("Dragon's Eye Settings")
		public Dragon DRAGON_EYE = new Dragon();
		public class Dragon {
			@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79, 20 MIN, 360 MAX")
			@Name("Refresh Rate")
			@RangeInt(min = 20, max = 360)
			public int C00_RR = 79;

			@Config.Comment("Should the Dragon's Eye play a Sound when Ore is Nearby? Set to False to Disable. Default True")
			@Name("Dragon's growl")
			public boolean Dragon_Growl = true;

			@Config.Comment("Should the Dragon's Eye a Sound when Sneaking. Options 'SNEAK', 'STAND', 'BOTH'  Default SNEAK")
			@Name("Dragon's growl Sneak")
			public String Dragon_Growl_Sneak = "SNEAK";

			@Config.Comment("The Volume of the Dragon's growl when detecting nearby Treasure. Default 100, 0 MIN, 300 MAX")
			@Name("Dragon's growl Volume")
			@RangeInt(min = 0, max = 300)
			public int Dragon_Growl_Volume = 100;
		}

		public Fairy FAIRY_RING = new Fairy();
		public class Fairy {
			@Name("Render Trinket on Player")
			public boolean doRender = true;
		}

		public Shield DAMAGE_SHIELD = new Shield();
		public class Shield {
			@Name("Render Trinket on Player")
			public boolean doRender = true;
		}

		public Crown ENDER_CROWN = new Crown();
		public class Crown {
			@Name("Render Trinket on Player")
			public boolean doRender = true;
		}

		public Sea SEA_STONE = new Sea();
		public class Sea {
			@Name("Render Trinket on Player")
			public boolean doRender = true;
		}
	}

	@Name("Server Settings")
	public static xServer SERVER = new xServer();
	public static class xServer {

		@Config.Comment("Should Trinkets and Baubles use it's own Built-in Container")
		@Name("Gui Settings")
		public GuiSettings GUI = new GuiSettings();
		public class GuiSettings {
			@Config.RequiresMcRestart
			@Name("01. Trinkets Gui Enabled")
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
		public Foods Food = new Foods();
		public class Foods {
			@Config.RequiresMcRestart
			@Config.Comment("Should this mod add Magical Foods?. Set to False to Disable. Default True")
			@Name("01. Foods Enabled")
			public boolean foods_enabled = true;

			@Config.Comment("Set to False to Disable. Default True")
			@Name("02. Foods Effects")
			public boolean food_effects = true;

			@Config.Comment("Should the player keep the Food Effects on Death. Set to False to Disable. Default True")
			@Name("03. Keep Effects")
			public boolean keep_effects = true;
		}

		@Name("Potion Settings")
		public Potions Potion = new Potions();
		public class Potions {
			@Config.RequiresMcRestart
			public boolean potions_enabled = true;

			@Config.Comment("If Tough as Nails is Installed, Should the Potions Give water?")
			@Name("01. Potion Give Water")
			public boolean potion_thirst = true;

			@Config.RequiresMcRestart
			@Name("02. Restorative Potion Catalyst Item")
			public String catalyst = "minecraft:nether_star";

			@Config.RequiresMcRestart
			@Name("03. Fairy Potion Catalyst Item")
			public String fairy_catalyst = "minecraft:ghast_tear";

			@Config.RequiresMcRestart
			@Name("04. Fairy Potion Effect Duration")
			public int FairyDuration = 1200;

			@Config.RequiresMcRestart
			@Name("05. Dwarf Potion Catalyst Item")
			public String dwarf_catalyst = "minecraft:iron_block";

			@Config.RequiresMcRestart
			@Name("06. Dwarf Potion Effect Duration")
			public int DwarfDuration = 1200;
		}

		@Name("Fairy Ring Settings")
		public FairyRing FAIRY_RING = new FairyRing();

		@Name("Dwarf Ring Settings")
		public DwarfRing DWARF_RING = new DwarfRing();

		@Name("Dragon's Eye Settings")
		public DragonsEye DRAGON_EYE = new DragonsEye();

		@Name("Ender Queen's Crown Settings")
		public EnderCrown ENDER_CROWN = new EnderCrown();

		@Name("Stone of the Sea Settings")
		public SeaStone SEA_STONE = new SeaStone();

		@Name("Ring of Enchanted Eyes Settings")
		public GlowRing GLOW_RING = new GlowRing();

		@Name("Poison Stone Settings")
		public PoisonStone POISON_STONE = new PoisonStone();

		@Name("Wither Ring Settings")
		public WitherRing WITHER_RING = new WitherRing();

		@Name("Damage Shield Settings")
		public DamageShield DAMAGE_SHIELD = new DamageShield();

		@Name("Polarized Stone Settings")
		public PolarizedStone POLARIZED_STONE = new PolarizedStone();

		@Name("Stone of Greater Inertia Settings")
		public GreaterInertia GREATER_INERTIA = new GreaterInertia();

		@Name("Stone of Inertia Null Settings")
		public InertiaNull INERTIA_NULL = new InertiaNull();

		@Name("Weightless Stone Settings")
		public WeightlessStone WEIGHTLESS_STONE = new WeightlessStone();

	}

	@Name("Compatability Settings")
	public static CompatabilityConfigs compat = new CompatabilityConfigs();

	public static void setBlocklist(String[] string, boolean whitelist) {
		if ((string != null)) {
			if (SERVER.DRAGON_EYE.BLOCKS.Blocks != string.clone()) {
				SERVER.DRAGON_EYE.BLOCKS.Blocks = string;
			}
		}
	}

	public static String[] getBlockListArray(boolean whitelist) {
		return SERVER.DRAGON_EYE.BLOCKS.Blocks;
	}

	public static void saveBlockList() {
		final Configuration cfg = Trinkets.config;
		final List<String> fillList = new ArrayList<>();
		//		try {
		//			fillList.add("minecraft:coal_ore");
		//			fillList.add("minecraft:iron_ore");
		//			fillList.add("minecraft:gold_ore");
		//			fillList.add("minecraft:lapis_ore");
		//			fillList.add("minecraft:redstone_ore");
		//			fillList.add("minecraft:diamond_ore");
		//			fillList.add("minecraft:emerald_ore");
		//			fillList.add("minecraft:quartz_ore");
		//			fillList.add("minecraft:chest");
		//
		//			for(final Object block : OreDictionary.getOreNames()){
		//				if(block.toString().contains("ore")) {
		//					for(final ItemStack blocks:OreDictionary.getOres(block.toString())) {//;
		//						final ResourceLocation blockRL = blocks.getItem().getRegistryName();
		//						final String blockModID = blockRL.getNamespace();
		//						final String blockName = blockRL.toString();
		//						if(!(blockModID.contains("minecraft") || blockModID.contains("armor") ||
		//								blockModID.contains("levelup2"))) {
		//							final String name =
		//									blocks.getItem().getRegistryName().toString();//block.toString();
		//							if(!blocks.getHasSubtypes()) {
		//								if(true) {;
		//								fillList.add(name);
		//								}
		//							} else {
		//								final int Meta = blocks.getMetadata();//block.toString();
		//								if(true && (Meta < 32767)) {;
		//								fillList.add(name + "[" + Meta + "]");
		//								}
		//							}
		//						}
		//					}
		//				}
		//			}
		//			if((!fillList.isEmpty())) {
		//				final String[] s = new String[fillList.size()];
		//				for(int i = 0;i<fillList.size();i++) {
		//					s[i] = fillList.get(i);
		//				}
		//				SERVER.DRAGON_EYE.BLOCKS.Blocks = s;
		//				Save();
		//			}
		//		} catch (final Exception e1) {
		//			Trinkets.log.error("Xat had a problem loading it's configuration");
		//		} finally {
		//		}
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