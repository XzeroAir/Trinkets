package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class WaterElement extends Element {

    public WaterElement() {
        super("Water");
    }

    @Override
    public Element[] getStrengths() {
        return new Element[]{Elements.FIRE};
    }

    @Override
    public Element[] getWeaknesses() {
        return new Element[]{Elements.LIGHTNING};
    }

    @Override
    public int getPrimaryColor() {
        return 100;
    }

    @Override
    public int getSecondaryColor() {
        return 20680;
    }

}
