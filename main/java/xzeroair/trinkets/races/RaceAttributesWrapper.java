package xzeroair.trinkets.races;

public class RaceAttributesWrapper {

	protected int size = 100;
	protected int width = 100;
	protected int height = 100;
	protected int color1 = 11107684;
	protected int color2 = 16374701;
	protected int color3 = 16374701;
	protected float opacity = 1F;
	protected float trait_opacity = 1F;
	protected String[] attributes = new String[0];

	public RaceAttributesWrapper() {
	}

	public final int getSize() {
		return size;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeight() {
		return height;
	}

	public final int getPrimaryColor() {
		return color1;
	}

	public final int getSecondaryColor() {
		return color2;
	}

	public final int getOptionalColor() {
		return color3;
	}

	public final float getOpacity() {
		return opacity;
	}

	public final float getTraitOpacity() {
		return trait_opacity;
	}

	public String[] getAttributes() {
		return attributes;
	}
}
