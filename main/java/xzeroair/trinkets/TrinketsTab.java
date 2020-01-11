package xzeroair.trinkets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;

public class TrinketsTab extends CreativeTabs {

	/**
	 * @param label
	 */
	public TrinketsTab(String label) {
		super("trinketstab");
	}
	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModItems.baubles.BaubleGlowRing);
	}
}