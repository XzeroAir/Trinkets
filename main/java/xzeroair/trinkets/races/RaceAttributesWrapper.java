package xzeroair.trinkets.races;

import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class RaceAttributesWrapper extends AttributeConfigWrapper {

	protected int size = 100;
	protected int width = 100;
	protected int height = 100;
	protected int color1 = 11107684;
	protected int color2 = 16374701;
	protected int color3 = 16374701;
	protected float opacity = 1F;
	protected float trait_opacity = 1F;
	protected IAttributeConfigHelper config;

	public RaceAttributesWrapper() {

	}

	public RaceAttributesWrapper(IAttributeConfigHelper config) {
		super(config);
	}

	public RaceAttributesWrapper(int size, int width, int height, int primaryColor, int secondaryColor, int optionalColor, float opacity, float traitOpacity, IAttributeConfigHelper config) {
		super(config);
		this.size = size;
		this.width = width;
		this.height = height;
		color1 = primaryColor;
		color2 = secondaryColor;
		color3 = optionalColor;
		this.opacity = opacity;
		trait_opacity = traitOpacity;
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
}
