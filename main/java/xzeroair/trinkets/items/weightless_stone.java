package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class weightless_stone extends BaubleBase {

	public weightless_stone(String name) {
		super(name);
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if ((itemstack.getItemDamage()==0)) {
			if(!player.isSneaking()) {
				player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION,40,0,true,true));
			}
			else {
				player.removeActivePotionEffect(MobEffects.LEVITATION);
			}
		}
	}
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.inventory.getCurrentItem().getItem() == ModItems.weightless_stone) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 2, 1));
			}
		}
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(entity instanceof EntityLiving) {
			EntityLivingBase living = (EntityLivingBase) entity;
			living.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 120, 1, true, true));
		}
		return true;
	}
}