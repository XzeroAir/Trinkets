package xzeroair.trinkets.traits.elements;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.Elements;

public interface IElementProvider {

	default Element getPrimaryElement() {
		return Elements.NEUTRAL;
	}

	default Element getSecondaryElement() {
		return Elements.NEUTRAL;
	}

	default Element[] getSubElements() {
		return Elements.EMPTY;
	}

	default Element getPrimaryElement(ItemStack stack) {
		return this.getPrimaryElement();
	}

	default Element getSecondaryElement(ItemStack stack) {
		return this.getSecondaryElement();
	}

	default Element[] getSubElements(ItemStack stack) {
		return this.getSubElements();
	}

}
