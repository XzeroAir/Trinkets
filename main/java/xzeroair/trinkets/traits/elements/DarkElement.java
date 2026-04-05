package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class DarkElement extends Element {

    public DarkElement() {
        super("Dark");
    }

    @Override
    public Element[] getStrengths() {
        return new Element[]{Elements.LIGHT};
    }

    @Override
    public Element[] getWeaknesses() {
        return new Element[]{Elements.LIGHT};
    }

    @Override
    public int getPrimaryColor() {
        return 3289650;
    }

    @Override
    public int getSecondaryColor() {
        return 1644825;
    }
}
