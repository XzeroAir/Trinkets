package xzeroair.trinkets.items;

import baubles.api.BaubleType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
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
		EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			ICap cap = p.getCapability(CapPro.sizeCapability, null);
			cap.setTrans(true);
			cap.setTarget(75);
		}
		super.onEquipped(itemstack, player);
	}
	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
		EntityPlayer p = (EntityPlayer) player;
		if(p.hasCapability(CapPro.sizeCapability, null)) {
			ICap cap = p.getCapability(CapPro.sizeCapability, null);
			cap.setTrans(false);
			cap.setTarget(100);
		}
		super.onUnequipped(itemstack, player);
	}
	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		if(itemstack.getItemDamage()==0) {
			if(TrinketHelper.baubleCheck((EntityPlayer)player, ModItems.small_ring)) {
				return false;
			}
		}
		return true;
	}
}
