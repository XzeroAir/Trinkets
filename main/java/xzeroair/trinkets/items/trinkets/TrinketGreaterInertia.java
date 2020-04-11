package xzeroair.trinkets.items.trinkets;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class TrinketGreaterInertia extends AccessoryBase {

	private final UUID uuid = UUID.fromString("e119ae9a-93b2-4053-ab3c-81108c16ff27");

	public TrinketGreaterInertia(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		TranslationHelper.addOtherTooltips(stack, worldIn, TrinketsConfig.SERVER.GREATER_INERTIA.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.GREATER_INERTIA.Attributes, this.uuid);
	}

	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {
		if (TrinketsConfig.SERVER.GREATER_INERTIA.jump) {
			final float jumpheight = TrinketsConfig.SERVER.GREATER_INERTIA.jumpheight;
			player.motionY = player.motionY * jumpheight;
		}
	}

	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase player) {
		if (TrinketsConfig.SERVER.GREATER_INERTIA.fall_damage) {
			event.setDamageMultiplier(TrinketsConfig.SERVER.GREATER_INERTIA.falldamage_amount);
		}
	}

	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		AttributeHelper.removeAttributes(player, this.uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, stack.getItem()) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketInertiaNull)) {
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
				AttributeHelper.handleAttributes(player, TrinketsConfig.SERVER.GREATER_INERTIA.Attributes, this.uuid);
			}
		}
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		AttributeHelper.removeAttributes(player, this.uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.GREATER_INERTIA.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}

}
