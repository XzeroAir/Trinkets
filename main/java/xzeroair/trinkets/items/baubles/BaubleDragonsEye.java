package xzeroair.trinkets.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.util.compat.baubles.BaublesHelper;

public class BaubleDragonsEye extends TrinketDragonsEye implements IBauble {

	public BaubleDragonsEye(String name) {
		super(name);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaublesHelper.getBaubleType(serverConfig.compat.baubles.bauble_type);
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return super.canEquipAccessory(stack, player);
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return super.canUnequipAccessory(stack, player);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		super.onAccessoryEquipped(stack, player);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		super.onAccessoryUnequipped(stack, player);
	}
}