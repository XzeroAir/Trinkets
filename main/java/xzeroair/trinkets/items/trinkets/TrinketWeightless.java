package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketWeightless extends AccessoryBase {

	public TrinketWeightless(String name) {
		super(name);
	}
	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
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
			if (player.inventory.getCurrentItem().getItem() == this) {
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 2, 1));
			}
		}
	}
	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack,  EntityLivingBase player) {
		event.setDamageMultiplier(0.1F);
	}
	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}
	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		super.playerEquipped(stack, player);
	}
	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.WEIGHTLESS_STONE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}