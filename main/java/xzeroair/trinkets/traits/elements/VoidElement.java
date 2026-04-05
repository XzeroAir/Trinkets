package xzeroair.trinkets.traits.elements;

public class VoidElement extends Element {

    public VoidElement() {
        super("Void");
    }

    //	@Override
    //	public Element[] getStrengths() {
    //		return new Element[] {
    //				Elements.AIR,
    //				Elements.DARK,
    //				Elements.EARTH,
    //				Elements.FIRE,
    //				Elements.ICE,
    //				Elements.LIGHT,
    //				Elements.LIGHTNING,
    //				Elements.POISON,
    //				Elements.WATER,
    //		};
    //	}

    @Override
    public int getPrimaryColor() {
        return 3289650;
    }

    @Override
    public int getSecondaryColor() {
        return 9509561;
    }

}
