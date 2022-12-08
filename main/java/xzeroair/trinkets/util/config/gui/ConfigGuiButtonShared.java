package xzeroair.trinkets.util.config.gui;

import net.minecraftforge.common.config.Config.Name;
import xzeroair.trinkets.util.Reference;

public class ConfigGuiButtonShared {

	public ConfigGuiButtonShared(String image, int x, int y, int width, int height, int texWidth, int texHeight, int texSizeWidth, int texSizeHeight, int color) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.texSizeWidth = texSizeWidth;
		this.texSizeHeight = texSizeHeight;
		this.color = color;
	}

	@Name("Texture")
	public String image = Reference.RESOURCE_PREFIX + "";
	@Name("Texture X")
	public int x = 0;
	@Name("Texture Y")
	public int y = 0;
	@Name("Button Width")
	public int width = 16;
	@Name("Button Height")
	public int height = 16;
	@Name("Texture Width")
	public int texWidth = 16;
	@Name("Texture Height")
	public int texHeight = 16;
	@Name("Texture Size Width")
	public int texSizeWidth = 16;
	@Name("Texture Size Height")
	public int texSizeHeight = 16;
	@Name("Texture Color")
	public int color = 16777215;
}
