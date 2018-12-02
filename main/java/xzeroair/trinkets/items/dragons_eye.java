package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.items.base.BaubleBase;

public class dragons_eye extends BaubleBase {

	public dragons_eye(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if ((itemstack.getItemDamage()==0)) {
			if(itemstack.hasCapability(ItemProvider.itemCapability, null)) {
				ItemCap itemNBT = itemstack.getCapability(ItemProvider.itemCapability, null);
				if(itemNBT.nightVision() == true) {
					player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,210,0,false,false));
				} else {
					//					player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
				}
			}
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,150,0,false,false));
		}
	}
}