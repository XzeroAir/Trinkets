package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.SlidingOption;
import xzeroair.trinkets.util.Reference;

public class TrinketsGui {

	@Name("Slot Settings")
	public SlotSettings SLOT = new SlotSettings();
	public class SlotSettings {

		@Name("01. Gui Show Slot ID")
		public boolean showID = false;

		//
		//		@Name("04. Gui Slots Rotation")
		//		public int roration = 0;

	}

	@Name("01. Gui Texture")
	public String GuiTex = Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png";

	@Config.Comment("Texture Size in Pixels. 2, 4, 8, 16, 32, 64, etc. Default: 256")
	@Name("02. Gui Size")
	public int guiTexSize = 256;

	@Config.Comment("Trinkets API Gui X Potion. Default 0")
	@Name("03. Gui Location X")
	public int X = -14;

	@Config.Comment("Trinkets API Gui Y Potion. Default 0")
	@Name("04. Gui Location Y")
	public int Y = 7;

	@Config.Comment("Show Active Potion Effects while in the Trinkets Gui. Default: True")
	@Name("05. Potion Icons")
	public boolean PotionIcons = true;

	@Name("06. Gui Z Level")
	@RangeInt(min = 0, max = 1)
	@SlidingOption
	public int Z = 1;

	@Config.Comment("Hex Color Followed by Alpha float value. Default: #FFFFFF:1")
	@Name("07. Texture Color Overlay")
	public String GuiColor = "#FFFFFF:1";

	@Config.Comment("Gui Button Configs")
	@Name("Gui Button")
	public TrinketButton button = new TrinketButton();
	public class TrinketButton {

		@Config.Comment("The Button ID for the Trinkets API. Default: 67")
		@Name("01. Button ID")
		public int ID = 67;

		@Config.Comment("Trinkets API Button X Potion. Default 2")
		@Name("02. Button Location X")
		public int X = -24;

		@Config.Comment("Trinkets API Button Y Potion. Default 37")
		@Name("03. Button Location Y")
		public int Y = -24;

		@Config.Comment("Button X Offset. Default: -39")
		@Name("04. X Offset")
		public int Xoffset = 0;

		@Config.Comment("Button Y Offset. Default: 0")
		@Name("05. Y Offset")
		public int Yoffset = 0;

		@Config.Comment("The Clickable Height of the Button. Default: 11")
		@Name("06. Button Height")
		public int Height = 16;

		@Config.Comment("The Clickable Width of the Button. Default: 4")
		@Name("07. Button Width")
		public int Width = 16;

		@Name("Texture")
		public ButtonTexture Texture = new ButtonTexture();
		public class ButtonTexture {

			@Name("01. Open Button")
			public String openButtonTex = Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png";

			@Config.Comment("Visible Texture Size in Pixels. 2, 4, 8, 16, 32, 64, etc. Default: 16")
			@Name("02. Texture Size")
			public int openButtonTexSize = 16;

			@Config.Comment("Texture Offset Y")
			@Name("03. Open Texture Y")
			public int openButtonTexY = 0;

			@Config.Comment("Texture Offset X")
			@Name("04. Open Texture X")
			public int openButtonTexX = 224;

			@Config.Comment("To get the Scale, divide 1 by the Source Texture size. (ex. 1/256 = 0.00390625) Default: 0.00390625F")
			@Name("05. Texture Scale")
			public float openButtonScale = 0.00390625F;

			@Config.Comment("Hex Color Followed by Alpha float value. Default: #FFFFFF:1")
			@Name("06. Texture Color Overlay")
			public String openButtonColor = "#FFFFFF:1";

			@Name("07. Close Button")
			public String closeButtonTex = Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png";

			@Config.Comment("Texture Size in Pixels. 2, 4, 8, 16, 32, 64, etc. Default: 16")
			@Name("08. Texture Size")
			public int closeButtonTexSize = 16;

			@Config.Comment("Texture Offset Y")
			@Name("09. Close Texture Y")
			public int closeButtonTexY = 0;

			@Config.Comment("Texture Offset X")
			@Name("10. Close Texture X")
			public int closeButtonTexX = 208;

			@Config.Comment("TexSize + TexOffset * TexScale. Default: 0.00390625F")
			@Name("11. Texture Scale")
			public float closeButtonScale = 0.00390625F;

			@Config.Comment("Hex Color Followed by Alpha float value. Default: #FFFFFF:1")
			@Name("12. Texture Color Overlay")
			public String closeButtonColor = "#FFFFFF:1";

		}
	}

}
