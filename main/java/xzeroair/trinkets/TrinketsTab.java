package xzeroair.trinkets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;

public class TrinketsTab extends CreativeTabs {

	public TrinketsTab(String label) {
		super("trinketstab");
		//this.setBackgroundImageName("trinketstab.png");
	}
	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.glow_ring);
	}
}