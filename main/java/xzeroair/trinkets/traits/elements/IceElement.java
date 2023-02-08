package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class IceElement extends Element {

	public IceElement() {
		super("Ice");
	}

	@Override
	public Element[] getStrengths() {
		return new Element[] {
				Elements.ICE
		};
	}

	@Override
	public Element[] getWeaknesses() {
		return new Element[] {
				Elements.FIRE
		};
	}
}
