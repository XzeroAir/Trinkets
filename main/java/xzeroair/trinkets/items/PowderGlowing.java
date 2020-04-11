package xzeroair.trinkets.items;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.ItemBase;

public class PowderGlowing extends ItemBase {

	public PowderGlowing(String name) {
		super(name);
		//		this.setMaxStackSize(64);
		this.setMaxDamage(0);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
