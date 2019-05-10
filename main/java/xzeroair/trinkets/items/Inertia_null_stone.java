package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.attributes.knock.KnockResistAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class Inertia_null_stone extends BaubleBase {

	public Inertia_null_stone(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.CHARM;
	}

	public static void playerTick(EntityPlayer player) {
		if(TrinketHelper.baubleCheck(player, ModItems.inertia_null_stone)) {
			KnockResistAttribute.addModifier(player, 1.0D, 0);
		}
	}
	public static void playerLogout(EntityLivingBase player) {
		KnockResistAttribute.removeModifier(player);
	}
	@Override
	public void onUnequipped(ItemStack par1ItemStack, EntityLivingBase player) {
		KnockResistAttribute.removeModifier(player);
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}