package xzeroair.trinkets.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;

public class weightless_stone extends BaubleBase {

	public weightless_stone(String name) {
		super(name);
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if(!player.onGround) {
			player.motionY = 0;
			if((!(player.isSneaking())) && player.isSwingInProgress) {
				player.motionY += 0.1;
			}
			if(player.isSneaking() && player.isSwingInProgress) {
				player.motionY -= 0.1;
			}
		}
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (player.inventory.getCurrentItem().getItem() == ModItems.weightless_stone) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 2, 1));
			}
		}
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}