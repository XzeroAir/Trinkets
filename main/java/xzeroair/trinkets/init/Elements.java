package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;

public class Elements {

	public static final Element[] EMPTY;
	public static final Element NEUTRAL;
	public static final Element ICE;
	public static final Element FIRE;
	public static final Element WATER;
	public static final Element LIGHTNING;
	//	public static final Element EARTH;
	//	public static final Element AIR;
	//	public static final Element LIGHTNING;
	//	public static final Element POSION;
	//	public static final Element LIGHT;
	//	public static final Element DARK;
	//	public static final Element VOID;

	private static Element getRegisteredElement(String name) {
		return getRegisteredElement(Reference.MODID, name);
	}

	private static Element getRegisteredElement(String modid, String name) {
		Element element = Element.Registry.getValue(new ResourceLocation(modid, name));
		if (element == null) {
			throw new IllegalStateException("Invalid Race requested: " + name);
		} else {
			return element;
		}
	}

	static {
		EMPTY = new Element[0];
		NEUTRAL = getRegisteredElement("Neutral");
		ICE = getRegisteredElement("Ice");
		FIRE = getRegisteredElement("Fire");
		WATER = getRegisteredElement("Water");
		LIGHTNING = getRegisteredElement("Lightning");
		//		EARTH = getRegisteredElement("Earth");
		//		AIR = getRegisteredElement("Air");
		//		POSION = getRegisteredElement("Poison");
		//		LIGHT = getRegisteredElement("Light");
		//		DARK = getRegisteredElement("Dark");
		//		VOID = getRegisteredElement("Void");
	}

	//	public static final List<Element> ELEMENTS = new ArrayList<>();
	//
	//	public static final Element Test = new Element("test");
	//
	//	protected static void registerElements() {
	//		registerElement(Test);
	//	}
	//
	//	protected static void registerElement(Element element) {
	//		ELEMENTS.add(element);
	//	}
	//
	//	public static void init() {
	//		registerElements();
	//	}

}
