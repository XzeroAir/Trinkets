package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.compat.artemislib.SizeAttribute;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class dwarf_ring extends BaubleBase {

	public dwarf_ring(String name) {
		super(name);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if((Loader.isModLoaded("artemislib"))) {
			final EntityPlayer p = (EntityPlayer) player;
			if(p.getCapability(SizeCapPro.sizeCapability, null) != null) {
				final ISizeCap nbt = p.getCapability(SizeCapPro.sizeCapability, null);
				if(nbt.getTrans() == true) {
					if(nbt.getSize() == nbt.getTarget()) {
						SizeAttribute.addModifier(player, -0.25, -0.25, 0);
					}
				}
			}
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if((EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.BINDING_CURSE))){
			return true;
		}
		return false;
	}
	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
		if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			cap.setTrans(true);
			cap.setTarget(75);
		}
		//		super.onEquipped(itemstack, player);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		if(player.hasCapability(SizeCapPro.sizeCapability, null)) {
			final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);
			cap.setTrans(false);
			cap.setTarget(100);
		}
		if((Loader.isModLoaded("artemislib"))) {
			SizeAttribute.removeModifier(player);
		}
		//		super.onUnequipped(itemstack, player);
	}
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		if(TrinketHelper.baubleCheck((EntityPlayer)player, ModItems.fairy_ring)) {
			return false;
		}
		return true;
	}
	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
