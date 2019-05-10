package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.BaubleBase;

public class poison_stone extends BaubleBase {

	public poison_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BODY;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(player.isPotionActive(MobEffects.POISON)) {
			player.removeActivePotionEffect(MobEffects.POISON);
		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}