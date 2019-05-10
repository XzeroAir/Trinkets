package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.base.BaubleBase;

public class wither_ring extends BaubleBase {

	public wither_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(player.isPotionActive(MobEffects.WITHER)) {
			player.removeActivePotionEffect(MobEffects.WITHER);
		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}