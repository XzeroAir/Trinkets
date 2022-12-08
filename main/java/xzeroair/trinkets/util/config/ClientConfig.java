package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.gui.ClientConfigPropertiesGui;
import xzeroair.trinkets.util.config.gui.ClientConfigTrinketsContainer;

public class ClientConfig {
	private final static String cfgPrefix = Reference.MODID + ".config.client";

	@Config.Name("Debug Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".debug.settings")
	public DebugConfig debug = new DebugConfig();

	public class DebugConfig {
		@Config.Name("Show Armor Materials")
		@Config.Comment("")
		@LangKey(cfgPrefix + ".debug.materials")
		public boolean debugArmorMaterials = false;
		@Config.Name("Show Item Slot IDs")
		@Config.Comment("")
		@LangKey(cfgPrefix + ".debug.slots")
		public boolean showID = false;
		@Config.Name("Show OreDict Entries")
		@Config.Comment("")
		@LangKey(cfgPrefix + ".debug.oredict")
		public boolean showOreDictEntries = false;
	}

	@Config.Name("Trinkets Container Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".gui.settings")
	public ClientConfigTrinketsContainer GUI = new ClientConfigTrinketsContainer();

	@Config.Name("Mana Bar Settings")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".magic.hud.settings")
	public ClientConfigMPBar MPBar = new ClientConfigMPBar();

	public class ClientConfigMPBar {
		private final String PREFIX = cfgPrefix + ".magic.hud";
		@Name("01. Show Mana")
		@Config.Comment("")
		@LangKey(PREFIX + ".shown")
		public boolean shown = true;

		@Name("02. Show when Full")
		@Config.Comment("")
		@LangKey(PREFIX + ".shown.always")
		public boolean always_shown = false;

		@Name("03. Mana Bar Horizontal")
		@Config.Comment("")
		@LangKey(PREFIX + ".horizontal")
		public boolean mana_horizontal = true;

		@Name("04. Hide Text")
		@Config.Comment("")
		@LangKey(PREFIX + ".text.shown")
		public boolean hide_text = false;

		@Name("05. Location X")
		@Config.Comment("")
		@LangKey(PREFIX + ".location.x")
		public double translatedX = 0;

		@Name("06. Location Y")
		@Config.Comment("")
		@LangKey(PREFIX + ".location.y")
		public double translatedY = 0;

		@Name("09. Width")
		@Config.Comment("")
		@LangKey(PREFIX + ".width")
		public int width = 106;

		@Name("10. Height")
		@Config.Comment("")
		@LangKey(PREFIX + ".height")
		public int height = 16;

	}

	@Config.Name("Race Properties Gui")
	@Config.Comment("")
	@LangKey(cfgPrefix + ".race.properties.gui.settings")
	public ClientConfigPropertiesGui raceProperties = new ClientConfigPropertiesGui();

	@Config.Comment("Adjusts players eye height when transformed")
	@Config.Name("POV Adjustments")
	@LangKey(cfgPrefix + ".camera.adjustments")
	public boolean cameraHeight = true;
	@Config.Comment("Main Render Control for all Trinkets")
	@Config.Name("Render Trinkets")
	@LangKey(cfgPrefix + ".render.trinkets.all")
	public boolean rendering = true;

	@Config.Name("Item Settings")
	@Config.Comment("")
	@LangKey("xat.config.items")
	public ClientConfigItems items = new ClientConfigItems();

	public class ClientConfigItems {
		@Config.Name("Dragon's Eye Settings")
		@Config.Comment("")
		@LangKey("xat.config.dragons_eye")
		public ClientConfigDragonsEye DRAGON_EYE = new ClientConfigDragonsEye();

		public class ClientConfigDragonsEye {
			@RangeInt(min = 20, max = 360)
			@Config.Name("Refresh Rate")
			@Config.Comment("Determines of Often the Treasure Finder Pings for Treasure")
			@LangKey(cfgPrefix + ".dragons_eye.orefinder.refresh.rate")
			public int Render_Cooldown = 79;

			@Config.Name("Dragon's Growl")
			@Config.Comment("When The Treasure Finder is Enabled, Should There be a Growl the Treasure Finder Pings Treasure")
			@LangKey(cfgPrefix + ".dragons_eye.growl.enabled")
			public boolean Dragon_Growl = true;

			@Config.Name("Growl Trigger")
			@Config.Comment("Determines When the Dragon's Growl Happens\\nOptions:\\nSNEAK\\nSTAND\\nBOTH")
			@LangKey(cfgPrefix + ".dragons_eye.growl.activation")
			public String Dragon_Growl_Sneak = "SNEAK";

			@Config.Name("Growl Volume")
			@Config.Comment("Dragon's Growl Volume Control")
			@RangeInt(min = 0, max = 300)
			@LangKey(cfgPrefix + ".dragons_eye.growl.volume")
			public int Dragon_Growl_Volume = 100;
		}

		@Config.Name("Ring of the Fairies Settings")
		@Config.Comment("")
		@LangKey("xat.config.fairy_ring")
		public ClientConfigFairyRing FAIRY_RING = new ClientConfigFairyRing();

		public class ClientConfigFairyRing {
			@Config.Name("Render Trinket effects on the player")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".render.trinket")
			public boolean doRender = true;
		}

		@Config.Name("Shield of Honor Settings")
		@Config.Comment("")
		@LangKey("xat.config.damage_shield")
		public ClientConfigDamageShield DAMAGE_SHIELD = new ClientConfigDamageShield();

		public class ClientConfigDamageShield {
			@Config.Name("Render Trinket")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".render.trinket")
			public boolean doRender = true;

			@Config.Name("Block Damage Volume")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".damage_shield.volume")
			@Config.RangeDouble(min = 0)
			public double effectVolume = 0.2D;
		}

		@Config.Name("Ender Queen's Crown Settings")
		@Config.Comment("")
		@LangKey("xat.config.ender_tiara")
		public ClientConfigEnderCrown ENDER_CROWN = new ClientConfigEnderCrown();

		public class ClientConfigEnderCrown {
			@Config.Name("Render Trinket")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".render.trinket")
			public boolean doRender = true;
		}

		@Config.Name("Stone of the Sea Settings")
		@Config.Comment("")
		@LangKey("xat.config.sea_stone")
		public ClientConfigSeaStone SEA_STONE = new ClientConfigSeaStone();

		public class ClientConfigSeaStone {
			@Config.Name("Render Trinket")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".render.trinket")
			public boolean doRender = true;
		}

		@Config.Name("Faelis Claw Settings")
		@Config.Comment("")
		@LangKey("xat.config.faelis_claw")
		public ClientConfigFaelisClaw FAELIS_CLAW = new ClientConfigFaelisClaw();

		public class ClientConfigFaelisClaw {
			@Config.Name("Render Trinket")
			@Config.Comment("")
			@LangKey(cfgPrefix + ".render.trinket")
			public boolean doRender = true;
		}
	}
}
