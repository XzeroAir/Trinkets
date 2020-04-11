package xzeroair.trinkets.items;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.ItemBase;

public class GemGlowing extends ItemBase {

	public GemGlowing(String name) {
		super(name);
		this.setMaxDamage(0);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}