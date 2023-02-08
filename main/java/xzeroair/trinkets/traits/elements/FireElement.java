package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class FireElement extends Element {

	public FireElement() {
		super("Fire");
	}

	@Override
	public Element[] getStrengths() {
		return new Element[] {
				Elements.FIRE
		};
	}

	@Override
	public Element[] getWeaknesses() {
		return new Element[] {
				Elements.ICE
		};
	}
}
