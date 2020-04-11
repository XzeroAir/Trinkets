package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketGlowRing extends AccessoryBase {

	public TrinketGlowRing(String name) {
		super(name);
	}

	private static UUID uuid = UUID.fromString("c7100557-afaf-4e69-b538-ef4ed550b470");

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.GLOW_RING.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.GLOW_RING.Attributes, uuid);
		player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 250, 0, false, false));
		if (TrinketsConfig.SERVER.GLOW_RING.prevent_blindness) {
			if (player.isPotionActive(MobEffects.BLINDNESS)) {
				player.removePotionEffect(MobEffects.BLINDNESS);
			}
		}
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
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, uuid);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.GLOW_RING.Attributes, uuid);
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
		return TrinketsConfig.SERVER.GLOW_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}