package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class ClientConfigPropertiesGui {

	private final String name = "race.properties.gui";
	private final String PREFIX = Reference.MODID + ".config.client." + name;

	@Config.Comment("Race Properties Gui Button Enabled?")
	@Name("01. Race Properties Button")
	@LangKey(PREFIX + ".shown")
	public boolean shown = true;

	@Config.Comment("Gui Button Configs")
	@Name("Gui Button")
	public TrinketButton button = new TrinketButton();

	public class TrinketButton {
		@Config.Comment("The Button ID for the Trinkets API. Default: 67")
		@Name("Button ID")
		public int ID = 69;

		@Config.Comment("Trinkets API Button X Potion. Default 2")
		@Name("Button Location X")
		public int X = 28; // 0.01

		@Config.Comment("Trinkets API Button Y Potion. Default 37")
		@Name("Button Location Y")
		public int Y = 66; // 0.39

		@Config.Comment("The Clickable Height of the Button. Default: 11")
		@Name("Button Height")
		public int bHeight = 10;

		@Config.Comment("The Clickable Width of the Button. Default: 4")
		@Name("Button Width")
		public int bWidth = 10;

		private String image = Reference.RESOURCE_PREFIX + "textures/gui/mana_button.png";
		private int x = 0;
		private int y = 0;
		private int width = 10;
		private int height = 10;
		private int texWidth = 32;
		private int texHeight = 32;
		private int texSizeWidth = 32;
		private int texSizeHeight = 64;
		private int color = 16777215;
		public ConfigGuiButtonShared texture = new ConfigGuiButtonShared(image, x, y, width, height, texWidth, texHeight, texSizeWidth, texSizeHeight, color);
	}
}
