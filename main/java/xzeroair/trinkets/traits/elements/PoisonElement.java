package xzeroair.trinkets.traits.elements;

public class PoisonElement extends Element {

    public PoisonElement() {
        super("Poison");
    }

    //	@Override
    //	public Element[] getStrengths() {
    //		return new Element[] {
    //				Elements.WATER,
    //				Elements.AIR,
    //		};
    //	}
    //
    //	@Override
    //	public Element[] getWeaknesses() {
    //		return new Element[] {
    //				Elements.FIRE
    //		};
    //	}

    @Override
    public int getPrimaryColor() {
        return 3307570;
    }

    @Override
    public int getSecondaryColor() {
        return 38912;
    }

}
