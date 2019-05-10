package xzeroair.trinkets.items;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.ItemCap.IItemCap;
import xzeroair.trinkets.capabilities.ItemCap.ItemProvider;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.BaubleBase;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class dragons_eye extends BaubleBase {

	public dragons_eye(String name) {
		super(name);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		final String value = this.enabled ? "Night Vision on":"Night Vision Off";
		final String value2 = this.target < 0 ? "Current Target: None":("Current Target: " + OreTrackingHelper.oreTypesLoaded().get(this.target).toString());
		tooltip.add(value);
		tooltip.add(value2);
	}
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
		if ((itemstack.getItemDamage()==0)) {
			if(itemstack.hasCapability(ItemProvider.itemCapability, null)) {
				final IItemCap itemNBT = itemstack.getCapability(ItemProvider.itemCapability, null);
				if(itemNBT.effect() == true) {
					player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,210,0,false,false));
				}
				if(this.enabled != itemNBT.effect()) {
					this.enabled = itemNBT.effect();
				}
				if(this.target != itemNBT.oreType()) {
					this.target = itemNBT.oreType();
				}
			}
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,150,0,false,false));
		}
	}
	public static void onPlayerLogout(EntityPlayer player) {
		if(TrinketHelper.baubleCheck(player, ModItems.dragons_eye)) {
			if(TrinketHelper.hasCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye))) {
				final IItemCap nbt = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(player, ModItems.dragons_eye));
				if(!player.world.isRemote) {
					nbt.setOreType(-1);
				}
			}
		}
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}