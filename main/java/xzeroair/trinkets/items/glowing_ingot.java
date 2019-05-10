package xzeroair.trinkets.items;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.ItemBase;

public class glowing_ingot extends ItemBase {

	public glowing_ingot(String name) {
		super(name);
		setMaxStackSize(1);
		setMaxDamage(0);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
