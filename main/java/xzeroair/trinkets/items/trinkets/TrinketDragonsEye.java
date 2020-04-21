package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

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
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketDragonsEye extends AccessoryBase {

	public TrinketDragonsEye(String name) {
		super(name);
	}

	private static UUID uuid = UUID.fromString("6a345136-49b7-4b71-88dc-87301e329ac1");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.DRAGON_EYE.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DRAGON_EYE.Attributes, uuid);
		TrinketProperties itemNBT = Capabilities.getTrinketProperties(stack);
		if (itemNBT != null) {
			if (itemNBT.mainAbility() == true) {
				player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 0, false, false));
				if (TrinketsConfig.SERVER.DRAGON_EYE.prevent_blindness) {
					if (player.isPotionActive(MobEffects.BLINDNESS)) {
						player.removePotionEffect(MobEffects.BLINDNESS);
					}
				}
			} else {
				if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
					final PotionEffect potion = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
					if ((potion.getDuration() > 0) && (potion.getDuration() < 220)) {
						player.removePotionEffect(MobEffects.NIGHT_VISION);
					}
				}
			}
		}
		player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 150, 0, false, false));
		if (player.isBurning()) {
			player.extinguish();
		}
		if (TrinketsConfig.SERVER.DRAGON_EYE.compat.tan.immuneToHeat) {
			TANCompat.immuneToHeat(player);
		}
	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		TrinketProperties ICap = Capabilities.getTrinketProperties(stack);
		if ((ICap != null) && !player.world.isRemote) {
			ICap.setTarget(-1);
			NetworkHandler.sendItemDataTo(player, stack, ICap, true, (EntityPlayerMP) player);
		}
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.DRAGON_EYE.Attributes, uuid);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		AttributeHelper.removeAttributes(player, uuid);
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