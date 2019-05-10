package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.attributes.sprint.SprintAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.handlers.ItemEffectHandler;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class Greater_inertia_stone extends BaubleBase {

	public Greater_inertia_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.CHARM;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

	}

	public static void playerTick(EntityPlayer player) {
		if(TrinketHelper.baubleCheck(player, ModItems.greater_inertia_stone)) {
			if(player.isSprinting() && !player.isInWater()) {
				ItemEffectHandler.count();
				final int time = ItemEffectHandler.getCount();
				final float speedVar = (float) time/100;
				if(speedVar == 4) {
					SprintAttribute.addModifier(player, 0.1D, 2);
				}
				if(player.world.isRemote && player.onGround) {
					player.motionX *= MathHelper.clamp(1.2+(speedVar/10), 0, 1.6);
					player.motionZ *= MathHelper.clamp(1.2+(speedVar/10), 0, 1.6);
				}
			} else {
				SprintAttribute.removeModifier(player);
				if(ItemEffectHandler.getCount() > 0) {
					ItemEffectHandler.resetCount();
				}
			}
		}
	}
	public static void playerJump(EntityPlayer player) {
		if(TrinketHelper.baubleCheck(player, ModItems.greater_inertia_stone)) {
			player.motionY = player.motionY*2f;
		}
	}
	public static void playerLogout(EntityLivingBase player) {
		SprintAttribute.removeModifier(player);
	}
	@Override
	public void onUnequipped(ItemStack par1ItemStack, EntityLivingBase player) {
		SprintAttribute.removeModifier(player);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
