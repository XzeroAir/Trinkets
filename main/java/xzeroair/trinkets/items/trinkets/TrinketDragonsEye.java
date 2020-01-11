package xzeroair.trinkets.items.trinkets;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketDragonsEye extends AccessoryBase {

	public TrinketDragonsEye(String name) {
		super(name);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
		if(iCap != null) {
			final String Type = iCap.oreTarget() < 0?"None":TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.oreTarget()];
			final String getName = Type.contains(";")?OreTrackingHelper.translateOreName(Type):Type.replace("ore", "");
			final String value = iCap.ability() ? "Night Vision on":"Night Vision Off";
			final String value2 = iCap.oreTarget() < 0 ? "Current Target: None":("Current Target: " + getName);
			tooltip.add(value);
			tooltip.add(value2);
		}
	}
	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		final IAccessoryInterface itemNBT = stack.getCapability(TrinketProvider.itemCapability, null);
		if(itemNBT != null) {
			if(itemNBT.ability() == true) {
				player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION,210,0,false,false));
			} else {
				player.removePotionEffect(MobEffects.NIGHT_VISION);
			}
		}
		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,150,0,false,false));
		if(player.isBurning()) {
			player.extinguish();
		}
		if(TrinketsConfig.SERVER.DRAGON_EYE.prevent_blindness) {
			if(player.isPotionActive(MobEffects.BLINDNESS)) {
				player.removePotionEffect(MobEffects.BLINDNESS);
			}
		}
		if(TrinketsConfig.SERVER.DRAGON_EYE.immuneToHeat) {
			TANCompat.immuneToHeat(player);
		}
	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		final IAccessoryInterface ICap = stack.getCapability(TrinketProvider.itemCapability, null);
		if((ICap != null) && !player.world.isRemote) {
			ICap.setOreTarget(-1);
			NetworkHandler.sendItemDataTo(player, stack, ICap, true, (EntityPlayerMP) player);
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketEnderTiara) || TrinketHelper.AccessoryCheck(player, stack.getItem())) {
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
		return TrinketsConfig.SERVER.DRAGON_EYE.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}