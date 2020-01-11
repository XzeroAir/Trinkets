package xzeroair.trinkets.items.trinkets;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.armor.ArmorAttribute;
import xzeroair.trinkets.attributes.attackdamage.DamageAttribute;
import xzeroair.trinkets.attributes.knock.KnockResistAttribute;
import xzeroair.trinkets.attributes.speed.SpeedAttribute;
import xzeroair.trinkets.attributes.toughness.ToughnessAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketGreaterInertia extends AccessoryBase {

	private final UUID uuid = UUID.fromString("e119ae9a-93b2-4053-ab3c-81108c16ff27");

	public TrinketGreaterInertia(String name) {
		super(name);
	}
	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		if(TrinketsConfig.SERVER.GREATER_INERTIA.damage) {
			DamageAttribute.addModifier(player, TrinketsConfig.SERVER.GREATER_INERTIA.damage_amount, uuid, 2);
		} else {
			DamageAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.GREATER_INERTIA.armor) {
			ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.GREATER_INERTIA.armor_amount, uuid, 2);
		} else {
			ArmorAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.GREATER_INERTIA.toughness) {
			ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.GREATER_INERTIA.toughness_amount, uuid, 2);
		} else {
			ToughnessAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.GREATER_INERTIA.speed) {
			SpeedAttribute.addModifier(player, TrinketsConfig.SERVER.GREATER_INERTIA.speed_amount, uuid, 0);
		} else {
			SpeedAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.GREATER_INERTIA.knockback) {
			KnockResistAttribute.addModifier(player, TrinketsConfig.SERVER.GREATER_INERTIA.knockback_amount, uuid, 2);
		} else {
			KnockResistAttribute.removeModifier(player, uuid);
		}
	}
	@Override
	public void eventLivingJump(ItemStack stack, EntityLivingBase player) {
		if(TrinketsConfig.SERVER.GREATER_INERTIA.jump) {
			final float jumpheight = TrinketsConfig.SERVER.GREATER_INERTIA.jumpheight;
			player.motionY = player.motionY*jumpheight;
		}
	}
	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack, EntityLivingBase player) {
		if(TrinketsConfig.SERVER.GREATER_INERTIA.fall_damage) {
			event.setDamageMultiplier(TrinketsConfig.SERVER.GREATER_INERTIA.falldamage_amount);
		}
	}
	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		DamageAttribute.removeModifier(player, uuid);
		ToughnessAttribute.removeModifier(player, uuid);
		ArmorAttribute.removeModifier(player, uuid);
		SpeedAttribute.removeModifier(player, uuid);
		KnockResistAttribute.removeModifier(player, uuid);
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem()) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketInertiaNull)) {
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
		DamageAttribute.removeModifier(player, uuid);
		ToughnessAttribute.removeModifier(player, uuid);
		ArmorAttribute.removeModifier(player, uuid);
		SpeedAttribute.removeModifier(player, uuid);
		KnockResistAttribute.removeModifier(player, uuid);
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
