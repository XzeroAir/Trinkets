package xzeroair.trinkets.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
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
import xzeroair.trinkets.util.config.trinkets.TitanRing;
import xzeroair.trinkets.util.config.trinkets.WeightlessStone;
import xzeroair.trinkets.util.config.trinkets.WitherRing;

@Config(name = Reference.configPath, modid = Reference.MODID)
@Config.LangKey("xat.config.title")
public class TrinketsConfig {

	private final static String PREFIX = Reference.MODID + ".config";

	@Name("Client Settings")
	@LangKey(PREFIX + ".client.settings")
	public static xClient CLIENT = new xClient();

	public static class xClient {

		@Name("Gui Settings")
		@LangKey(PREFIX + ".client.gui.settings")
		public TrinketsGui GUI = new TrinketsGui();

		@Config.Comment("POV Height adjustments when wearing Race Rings. Set to False to Disable. Default True")
		@Name("POV Height Adjustments")
		public boolean cameraHeight = true;

		@Config.Comment("Rendering of Trinkets. Set to False to Disable. Default True")
		@Name("Render Trinkets")
		@LangKey(PREFIX + ".client.render.trinkets.all")
		public boolean rendering = true;
		
		@Config.Comment("When using the Enchanted Race rings, should Trinkets and Baubles Replace the EntityRenderer to solve some of the camera clipping issues?")
		@Name("Replace EntityRenderer")
		@LangKey(PREFIX + ".client.entity.renderer.replace")
		public boolean entityRenderer = true;
		
		@Config.Comment("When Trinket and Baubles Replaces the EntityRenderer is breaks some settings with Optifine, Specifically Fog, use this to turn Fog on or off")
		@Name("EntityRenderer Fog")
		@LangKey(PREFIX + ".client.entity.renderer.replace.fog")
		public boolean RendererFog = true;

		@Name("Dragon's Eye Settings")
		@LangKey(PREFIX + ".dragons_eye")
		public Dragon DRAGON_EYE = new Dragon();

		public class Dragon {
			@Config.Comment("How Often the Dragon's Eye effect triggers in Ticks. try to avoid lower values. Default 79, 20 MIN, 360 MAX")
			@Name("Refresh Rate")
			@RangeInt(min = 20, max = 360)
			@LangKey(PREFIX + ".dragons_eye.client.orefinder.refresh.rate")
			public int C00_RR = 79;

			@Config.Comment("Should the Dragon's Eye play a Sound when Ore is Nearby? Set to False to Disable. Default True")
			@Name("Dragon's growl")
			@LangKey(PREFIX + ".dragons_eye.client.growl.enabled")
			public boolean Dragon_Growl = true;

			@Config.Comment("Should the Dragon's Eye a Sound when Sneaking. Options 'SNEAK', 'STAND', 'BOTH'  Default SNEAK")
			@Name("Dragon's growl Sneak")
			@LangKey(PREFIX + ".dragons_eye.client.growl.activation")
			public String Dragon_Growl_Sneak = "SNEAK";

			@Config.Comment("The Volume of the Dragon's growl when detecting nearby Treasure. Default 100, 0 MIN, 300 MAX")
			@Name("Dragon's growl Volume")
			@RangeInt(min = 0, max = 300)
			@LangKey(PREFIX + ".dragons_eye.client.growl.volume")
			public int Dragon_Growl_Volume = 100;
		}

		@Name("Fairy Ring Settings")
		@LangKey(PREFIX + ".fairy_ring")
		public Fairy FAIRY_RING = new Fairy();

		public class Fairy {
			@Name("Render Trinket on Player")
			@LangKey(PREFIX + ".client.render.trinket")
			public boolean doRender = true;
		}

		@Name("Shield of Honor Settings")
		@LangKey(PREFIX + ".damage_shield")
		public Shield DAMAGE_SHIELD = new Shield();

		public class Shield {
			@Name("Render Trinket on Player")
			@LangKey(PREFIX + ".client.render.trinket")
			public boolean doRender = true;
		}

		@Name("Ender Queen's Crown Settings")
		@LangKey(PREFIX + ".ender_tiara")
		public Crown ENDER_CROWN = new Crown();

		public class Crown {
			@Name("Render Trinket on Player")
			@LangKey(PREFIX + ".client.render.trinket")
			public boolean doRender = true;
		}

		@Name("Stone of the Sea Settings")
		@LangKey(PREFIX + ".sea_stone")
		public Sea SEA_STONE = new Sea();

		public class Sea {
			@Name("Render Trinket on Player")
			@LangKey(PREFIX + ".client.render.trinket")
			public boolean doRender = true;
		}
	}

	@Name("Server Settings")
	@LangKey(PREFIX + ".server.settings")
	public static xServer SERVER = new xServer();

	public static class xServer {

		@Config.Comment("Should Trinkets and Baubles use it's own Built-in Container")
		@Name("Gui Settings")
		@LangKey(PREFIX + ".server.gui.settings")
		public GuiSettings GUI = new GuiSettings();

		public class GuiSettings {
			@Config.RequiresMcRestart
			@Name("01. Trinkets Gui Enabled")
			@LangKey(PREFIX + ".server.gui.settings.enabled")
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
		@LangKey(PREFIX + ".server.food.settings")
		public Foods Food = new Foods();

		public class Foods {
			@Config.RequiresMcRestart
			@Config.Comment("Should this mod add Magical Foods?. Set to False to Disable. Default True")
			@Name("01. Foods Enabled")
			@LangKey(PREFIX + ".food.registry.enabled")
			public boolean foods_enabled = true;

			@Config.Comment("Set to False to Disable. Default True")
			@Name("02. Foods Effects")
			@LangKey(PREFIX + ".food.transformation.effect")
			public boolean food_effects = true;

			@Config.Comment("Should the player keep the Food Effects on Death. Set to False to Disable. Default True")
			@Name("03. Keep Effects")
			@LangKey(PREFIX + ".food.transformation.effect.keep")
			public boolean keep_effects = true;
		}

		@Name("Potion Settings")
		@LangKey(PREFIX + ".server.potion.settings")
		public Potions Potion = new Potions();

		public class Potions {
			@Config.RequiresMcRestart
			@Name("00. Potions Enabled")
			@LangKey(PREFIX + ".potion.registry.enabled")
			public boolean potions_enabled = true;

			@Config.Comment("If Tough as Nails is Installed, Should the Potions Give water?")
			@Name("01. Potion Give Water")
			@LangKey(PREFIX + ".potion.toughasnails.water")
			public boolean potion_thirst = true;

			@Config.RequiresMcRestart
			@Name("02. Restorative Potion Catalyst Item")
			@LangKey(PREFIX + ".potion.catalyst.restorative")
			public String catalyst = "minecraft:nether_star";

			@Config.RequiresMcRestart
			@Name("03. Fairy Potion Catalyst Item")
			@LangKey(PREFIX + ".potion.catalyst.fairy")
			public String fairy_catalyst = "minecraft:ghast_tear";

			@Config.RequiresMcRestart
			@Name("04. Fairy Potion Effect Duration")
			@LangKey(PREFIX + ".potion.fairy.duration")
			public int FairyDuration = 1200;

			@Config.RequiresMcRestart
			@Name("05. Dwarf Potion Catalyst Item")
			@LangKey(PREFIX + ".potion.catalyst.dwarf")
			public String dwarf_catalyst = "minecraft:iron_block";

			@Config.RequiresMcRestart
			@Name("06. Dwarf Potion Effect Duration")
			@LangKey(PREFIX + ".potion.dwarf.duration")
			public int DwarfDuration = 1200;

			@Config.RequiresMcRestart
			@Name("07. Titan Potion Catalyst Item")
			@LangKey(PREFIX + ".potion.catalyst.titan")
			public String titan_catalyst = "minecraft:bone_block";

			@Config.RequiresMcRestart
			@Name("08. Titan Potion Effect Duration")
			@LangKey(PREFIX + ".potion.titan.duration")
			public int TitanDuration = 1200;
		}

		@Name("Fairy Ring Settings")
		@LangKey(PREFIX + ".fairy_ring")
		public FairyRing FAIRY_RING = new FairyRing();

		@Name("Dwarf Ring Settings")
		@LangKey(PREFIX + ".dwarf_ring")
		public DwarfRing DWARF_RING = new DwarfRing();

		@Name("Titan Ring Settings")
		@LangKey(PREFIX + ".titan_ring")
		public TitanRing TITAN_RING = new TitanRing();

		@Name("Dragon's Eye Settings")
		@LangKey(PREFIX + ".dragons_eye")
		public DragonsEye DRAGON_EYE = new DragonsEye();

		@Name("Ender Queen's Crown Settings")
		@LangKey(PREFIX + ".ender_tiara")
		public EnderCrown ENDER_CROWN = new EnderCrown();

		@Name("Damage Shield Settings")
		@LangKey(PREFIX + ".damage_shield")
		public DamageShield DAMAGE_SHIELD = new DamageShield();

		@Name("Ring of Enchanted Eyes Settings")
		@LangKey(PREFIX + ".glow_ring")
		public GlowRing GLOW_RING = new GlowRing();

		@Name("Poison Stone Settings")
		@LangKey(PREFIX + ".poison_stone")
		public PoisonStone POISON_STONE = new PoisonStone();

		@Name("Wither Ring Settings")
		@LangKey(PREFIX + ".wither_ring")
		public WitherRing WITHER_RING = new WitherRing();

		@Name("Polarized Stone Settings")
		@LangKey(PREFIX + ".polarized_stone")
		public PolarizedStone POLARIZED_STONE = new PolarizedStone();

		@Name("Stone of the Sea Settings")
		@LangKey(PREFIX + ".sea_stone")
		public SeaStone SEA_STONE = new SeaStone();

		@Name("Stone of Inertia Null Settings")
		@LangKey(PREFIX + ".inertia_null_stone")
		public InertiaNull INERTIA_NULL = new InertiaNull();

		@Name("Stone of Greater Inertia Settings")
		@LangKey(PREFIX + ".greater_inertia_stone")
		public GreaterInertia GREATER_INERTIA = new GreaterInertia();

		@Name("Weightless Stone Settings")
		@LangKey(PREFIX + ".weightless_stone")
		public WeightlessStone WEIGHTLESS_STONE = new WeightlessStone();

	}

	@Name("Compatability Settings")
	@LangKey(PREFIX + ".compatability")
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
		// try {
		// fillList.add("minecraft:coal_ore");
		// fillList.add("minecraft:iron_ore");
		// fillList.add("minecraft:gold_ore");
		// fillList.add("minecraft:lapis_ore");
		// fillList.add("minecraft:redstone_ore");
		// fillList.add("minecraft:diamond_ore");
		// fillList.add("minecraft:emerald_ore");
		// fillList.add("minecraft:quartz_ore");
		// fillList.add("minecraft:chest");
		//
		// for(final Object block : OreDictionary.getOreNames()){
		// if(block.toString().contains("ore")) {
		// for(final ItemStack blocks:OreDictionary.getOres(block.toString())) {//;
		// final ResourceLocation blockRL = blocks.getItem().getRegistryName();
		// final String blockModID = blockRL.getNamespace();
		// final String blockName = blockRL.toString();
		// if(!(blockModID.contains("minecraft") || blockModID.contains("armor") ||
		// blockModID.contains("levelup2"))) {
		// final String name =
		// blocks.getItem().getRegistryName().toString();//block.toString();
		// if(!blocks.getHasSubtypes()) {
		// if(true) {;
		// fillList.add(name);
		// }
		// } else {
		// final int Meta = blocks.getMetadata();//block.toString();
		// if(true && (Meta < 32767)) {;
		// fillList.add(name + "[" + Meta + "]");
		// }
		// }
		// }
		// }
		// }
		// }
		// if((!fillList.isEmpty())) {
		// final String[] s = new String[fillList.size()];
		// for(int i = 0;i<fillList.size();i++) {
		// s[i] = fillList.get(i);
		// }
		// SERVER.DRAGON_EYE.BLOCKS.Blocks = s;
		// Save();
		// }
		// } catch (final Exception e1) {
		// Trinkets.log.error("Xat had a problem loading it's configuration");
		// } finally {
		// }
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