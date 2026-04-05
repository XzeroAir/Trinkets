package xzeroair.trinkets.traits.elements;

public class AirElement extends Element {

    public AirElement() {
        super("Air");
    }

    //	@Override
    //	public Element[] getStrengths() {
    //		return new Element[] {
    //				Elements.POISON
    //		};
    //	}
    //
    //	@Override
    //	public Element[] getWeaknesses() {
    //		return new Element[] {
    //				Elements.EARTH
    //		};
    //	}

    @Override
    public int getPrimaryColor() {
        return 10610356;
    }

    @Override
    public int getSecondaryColor() {
        return 13816530;
    }
}
