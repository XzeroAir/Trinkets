package xzeroair.trinkets.items;

import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.ItemBase;

public class IngotGlowing extends ItemBase {

	public IngotGlowing(String name) {
		super(name);
		//		setMaxStackSize(64);
		this.setMaxDamage(0);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
