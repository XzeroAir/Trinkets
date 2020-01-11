package xzeroair.trinkets.items.trinkets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketFairyRing extends AccessoryBase {

	public TrinketFairyRing(String name) {
		super(name);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null) && !cap.getFood().contains("fairy_dew")) {
			EffectsFairyRing.FairyTicks(player);
		}
	}
	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {
		if((TrinketsConfig.SERVER.FAIRY_RING.step_height != false)){
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			if((cap != null) && !cap.getFood().contains("fairy_dew")) {
				EffectsFairyRing.FairyJump(player);
			}
		}
	}
	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null)) {
			EffectsFairyRing.FairyLogIn(player, stack);
		}
	}
	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null) && !cap.getFood().contains("fairy_dew")) {
			EffectsFairyRing.FairyLogout(player, stack);
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem()) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketDwarfRing)) {
			return false;
		}
		return super.playerCanEquip(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
		final boolean client = player.world.isRemote;
		final ISizeCap playerCap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((playerCap != null)) {
			if(player instanceof EntityPlayerMP) {
				if((stack.getItem() instanceof TrinketFairyRing) || (stack.getItem() instanceof TrinketDwarfRing)) {
					NetworkHandler.sendPlayerDataAll(player, playerCap);
				}
			}
			player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
			if((playerCap != null) && !playerCap.getFood().contains("fairy_dew")) {
				EffectsFairyRing.FairyEquip(stack, player);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
		final boolean client = player.world.isRemote;
		final ISizeCap playerCap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((playerCap != null)) {
			if(player instanceof EntityPlayerMP) {
				if((stack.getItem() instanceof TrinketFairyRing) || (stack.getItem() instanceof TrinketDwarfRing)) {
					NetworkHandler.sendPlayerDataAll(player, playerCap);
				}
			}
		}
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		if(!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			if((playerCap != null) && !playerCap.getFood().contains("fairy_dew")) {
				EffectsFairyRing.FairyUnequip(stack, player);
			}
		}
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.FAIRY_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
