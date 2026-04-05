package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class Elements {

    public static final Element[] EMPTY;
    public static final Element NEUTRAL;
    public static final Element ICE;
    public static final Element FIRE;
    public static final Element WATER;
    public static final Element EARTH;
    public static final Element AIR;
    public static final Element LIGHTNING;
    public static final Element POISON;
    public static final Element LIGHT;
    public static final Element DARK;
    public static final Element VOID;

    private static Element getRegisteredElement(String name) {
        return getRegisteredElement(Reference.MODID, name);
    }

    private static Element getRegisteredElement(String modid, String name) {
        Element element = Element.Registry.getValue(new ResourceLocation(modid, name));
        if (element == null) {
            throw new IllegalStateException("Invalid Element requested: " + name);
        } else {
            return element;
        }
    }

    static {
        EMPTY = new Element[0];
        NEUTRAL = getRegisteredElement(TrinketsRegistryNames.ModElements.NEUTRAL);
        ICE = getRegisteredElement(TrinketsRegistryNames.ModElements.ICE);
        FIRE = getRegisteredElement(TrinketsRegistryNames.ModElements.FIRE);
        WATER = getRegisteredElement(TrinketsRegistryNames.ModElements.WATER);
        EARTH = getRegisteredElement(TrinketsRegistryNames.ModElements.EARTH);
        AIR = getRegisteredElement(TrinketsRegistryNames.ModElements.AIR);
        LIGHTNING = getRegisteredElement(TrinketsRegistryNames.ModElements.LIGHTNING);
        POISON = getRegisteredElement(TrinketsRegistryNames.ModElements.POISON);
        LIGHT = getRegisteredElement(TrinketsRegistryNames.ModElements.LIGHT);
        DARK = getRegisteredElement(TrinketsRegistryNames.ModElements.DARK);
        VOID = getRegisteredElement(TrinketsRegistryNames.ModElements.VOID);
    }

}
