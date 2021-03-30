package xzeroair.trinkets.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.compat.baubles.BaubleHelper;
import xzeroair.trinkets.util.config.trinkets.shared.ConfigAttribs;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;

public class BaubleRaceBase extends TrinketRaceBase implements IBauble {

	public BaubleRaceBase(String name, EntityRace race, TransformationRingConfig config, ConfigAttribs attrib) {
		super(name, race, config, attrib);
		if (ModItems.RaceTrinkets.ITEMS.contains(this)) {
			ModItems.RaceTrinkets.ITEMS.remove(this);
		}
		ModItems.baubles.ITEMS.add(this);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleHelper.getBaubleType(serverConfig.compat.baubles.bauble_type);
	}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			return super.playerCanEquip(itemstack, player);
		}
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			return super.playerCanUnequip(itemstack, player);
		}
		return true;
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			super.playerEquipped(stack, player);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		if (player instanceof EntityPlayer) {
			super.playerUnequipped(stack, player);
		}
	}
}
