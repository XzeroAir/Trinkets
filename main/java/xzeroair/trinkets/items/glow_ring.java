package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.items.base.BaubleBase;

public class glow_ring extends BaubleBase {

	public glow_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if ((itemstack.getItemDamage()==0) && ((player.ticksExisted%39)==0)) {
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,240,0,false,false));
		}
	}
}