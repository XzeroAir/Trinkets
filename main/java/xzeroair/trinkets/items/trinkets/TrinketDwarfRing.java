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
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketDwarfRing extends AccessoryBase {

	public TrinketDwarfRing(String name) {
		super(name);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null) && !cap.getFood().contentEquals("dwarf_stout")) {
			EffectsDwarfRing.DwarfTicks(player);
		}
	}

	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {

	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null)) {
			EffectsDwarfRing.DwarfLogIn(player, stack);
		}
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
		if((cap != null) && !cap.getFood().contentEquals("dwarf_stout")) {
			EffectsDwarfRing.DwarfLogout(player);
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketFairyRing) || TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		}
		return super.playerCanEquip(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
		final boolean client = player.world.isRemote;
		final ISizeCap playerCap = player.getCapability(SizeCapPro.sizeCapability, null);
		if(playerCap != null) {
			if(player instanceof EntityPlayerMP) {
				if((stack.getItem() instanceof TrinketFairyRing) || (stack.getItem() instanceof TrinketDwarfRing)) {
					NetworkHandler.sendPlayerDataAll(player, playerCap);
				}
			}
			player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
			if((playerCap != null) && !playerCap.getFood().contentEquals("dwarf_stout")) {
				EffectsDwarfRing.DwarfEquip(stack, player);
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
			if((playerCap != null) && !playerCap.getFood().contentEquals("dwarf_stout")) {
				EffectsDwarfRing.DwarfUnequip(stack, player);
			}
		}
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.DWARF_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
