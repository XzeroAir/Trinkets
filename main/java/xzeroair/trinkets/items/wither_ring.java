package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class wither_ring extends BaubleBase {

	public wither_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public boolean hasEffect(ItemStack par1ItemStack) {
		return false;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if ((itemstack.getItemDamage()==0)) {
			if(player.isPotionActive(MobEffects.WITHER)) {
				player.removeActivePotionEffect(MobEffects.WITHER);
			}
		}
	}
}