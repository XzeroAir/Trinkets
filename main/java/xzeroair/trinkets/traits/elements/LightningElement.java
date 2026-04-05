package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class LightningElement extends Element {

    public LightningElement() {
        super("Lightning");
    }

    @Override
    public Element[] getStrengths() {
        return new Element[]{Elements.WATER};
    }

    @Override
    public int getPrimaryColor() {
        return 4915275;
    }

    @Override
    public int getSecondaryColor() {
        return 14381019;
    }
}