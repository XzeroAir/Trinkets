package xzeroair.trinkets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
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
		if (Loader.isModLoaded("baubles")) {
			return new ItemStack(ModItems.baubles.BaubleGlowRing);
		} else {
			return new ItemStack(ModItems.trinkets.TrinketGlowRing);
		}
	}
}