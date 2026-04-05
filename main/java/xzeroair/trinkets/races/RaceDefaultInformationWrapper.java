package xzeroair.trinkets.races;

public class RaceDefaultInformationWrapper {

    protected final int size;
    protected final int width;
    protected final int height;
    protected final int color1;
    protected final int color2;
    protected final int color3;
    protected final float opacity;
    protected final float trait_opacity;
    protected final String[] attributes;

    public RaceDefaultInformationWrapper(int width, int height, int color1, int color2, int color3) {
        this.size = height;
        this.width = width;
        this.height = height;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.opacity = 1F;
        this.trait_opacity = 1F;
        this.attributes = new String[0];
    }

    public RaceDefaultInformationWrapper() {
        this.size = 100;
        this.width = 100;
        this.height = 100;
        this.color1 = 11107684;
        this.color2 = 16374701;
        this.color3 = 16374701;
        this.opacity = 1F;
        this.trait_opacity = 1F;
        this.attributes = new String[0];
    }

    public int getSize() {
        return this.size;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getPrimaryColor() {
        return this.color1;
    }

    public int getSecondaryColor() {
        return this.color2;
    }

    public int getOptionalColor() {
        return this.color3;
    }

    public int getPrimaryTraitMaxVariants() {
        return 0;
    }

    public int getSecondaryTraitMaxVariants() {
        return 0;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public float getTraitOpacity() {
        return this.trait_opacity;
    }

    public String[] getAttributes() {
        return this.attributes;
    }
}
