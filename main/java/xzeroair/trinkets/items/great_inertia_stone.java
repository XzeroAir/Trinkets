package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.items.base.BaubleBase;

public class great_inertia_stone extends BaubleBase {

	public great_inertia_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.CHARM;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

		if ((itemstack.getItemDamage()==0)) {
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED,80,3,false,false));
			player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,80,3,false,false));
		}
	}
}
