package xzeroair.trinkets.items.trinkets;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketFairyRing extends AccessoryBase {

	public TrinketFairyRing(String name) {
		super(name);
		TrinketHelper.SizeTrinkets.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.FAIRY_RING.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		EffectsFairyRing.FairyTicks(player);
	}

	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {
		if (TrinketsConfig.SERVER.FAIRY_RING.step_height) {
			EffectsFairyRing.FairyJump(player);
		}
	}

	@Override
	public void eventEntityJoinWorld(ItemStack stack, EntityLivingBase player) {
		EffectsFairyRing.FairyLogIn(player, stack);
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		EffectsFairyRing.FairyLogout(player, stack);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		return super.playerCanEquip(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		TrinketProperties cap = Capabilities.getTrinketProperties(stack);
		if ((cap != null)) {
			if (!(cap.Slot() == -1)) {
				super.playerEquipped(stack, player);
			} else {
				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
				if (!EntityRaceHelper.getRace(player).contentEquals("fairy_dew")) {
					EffectsFairyRing.FairyEquip(stack, player);
				}
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		super.playerUnequipped(stack, player);
		if (!EntityRaceHelper.getRace(player).contentEquals("fairy_dew")) {
			if (!TrinketHelper.AccessoryCheck(player, stack.getItem())) {
				EffectsFairyRing.FairyUnequip(stack, player);
			}
		}
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.FAIRY_RING.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}
