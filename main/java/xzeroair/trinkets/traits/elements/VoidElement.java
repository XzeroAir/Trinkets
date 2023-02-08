package xzeroair.trinkets.traits.elements;

import xzeroair.trinkets.init.Elements;

public class VoidElement extends Element {

	public VoidElement() {
		super("Void");
	}

	@Override
	public Element[] getStrengths() {
		return new Element[] {
				Elements.FIRE,
				Elements.ICE
		};
	}

}
