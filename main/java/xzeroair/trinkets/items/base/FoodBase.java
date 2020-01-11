package xzeroair.trinkets.items.base;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class FoodBase extends ItemFood implements IsModelLoaded, IDescriptionInterface {

	int cooldown = 0;
	boolean canEat = true;

	public FoodBase(String name, int heal, float saturation) {
		super(heal, saturation, false);
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
		ModItems.foods.ITEMS.add(this);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(this.cooldown > 0) {
			this.canEat = false;
			this.cooldown--;
		} else {
			if(this.canEat == false) {
				this.canEat = true;
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public int getCooldown() {
		return this.cooldown;
	}
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public boolean getEdible() {
		return this.canEat;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return false;
	}
}