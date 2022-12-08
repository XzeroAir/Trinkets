package xzeroair.trinkets.items.base;

import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class ShieldBase extends ItemShield implements IAccessoryInterface, IsModelLoaded {

	String name;

	public ShieldBase(String name) {
		super();
		this.setTranslationKey(name);
		this.setRegistryName(name);

		this.name = name;

		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return "item." + this.getTranslationKey() + ".name";//super.getItemStackDisplayName(stack);
	}

	@Override
	public void registerModels() {

	}

	@Override
	public int getSlot(ItemStack stack) {
		return -1;
	}

	@Override
	public String getItemHandler(ItemStack stack) {
		return "None";
	}

}
