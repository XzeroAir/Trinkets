package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.BaubleBase;

public class inertia_stone extends BaubleBase {

	public inertia_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.CHARM;
	}
}