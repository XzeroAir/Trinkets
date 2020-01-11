package xzeroair.trinkets.items.trinkets;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.armor.ArmorAttribute;
import xzeroair.trinkets.attributes.knock.KnockResistAttribute;
import xzeroair.trinkets.attributes.toughness.ToughnessAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketInertiaNull extends AccessoryBase {

	public TrinketInertiaNull(String name) {
		super(name);
	}

	private final UUID uuid = UUID.fromString("8192af5d-98de-4c1e-a125-e99864b99634");

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		if(TrinketsConfig.SERVER.INERTIA_NULL.knockback) {
			KnockResistAttribute.addModifier(player, TrinketsConfig.SERVER.INERTIA_NULL.knockback_amount, uuid, 2);
		} else {
			KnockResistAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.INERTIA_NULL.armor) {
			ArmorAttribute.addModifier(player, TrinketsConfig.SERVER.INERTIA_NULL.armor_amount, uuid, 2);
		} else {
			ArmorAttribute.removeModifier(player, uuid);
		}
		if(TrinketsConfig.SERVER.INERTIA_NULL.toughness) {
			ToughnessAttribute.addModifier(player, TrinketsConfig.SERVER.INERTIA_NULL.toughness_amount, uuid, 2);
		} else {
			ToughnessAttribute.removeModifier(player, uuid);
		}
	}
	@Override
	public void eventPlayerLogout(ItemStack stack, EntityLivingBase player) {
		KnockResistAttribute.removeModifier(player, uuid);
		ToughnessAttribute.removeModifier(player, uuid);
		ArmorAttribute.removeModifier(player, uuid);
	}
	@Override
	public void eventLivingFall(LivingFallEvent event, ItemStack stack,  EntityLivingBase player) {
		if(TrinketsConfig.SERVER.INERTIA_NULL.fall_damage) {
			event.setDamageMultiplier(TrinketsConfig.SERVER.INERTIA_NULL.falldamage_amount);
		}
	}
	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if(TrinketHelper.AccessoryCheck(player, stack.getItem()) || TrinketHelper.AccessoryCheck(player, ModItems.trinkets.TrinketGreaterInertia)) {
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
		KnockResistAttribute.removeModifier(player, uuid);
		ToughnessAttribute.removeModifier(player, uuid);
		ArmorAttribute.removeModifier(player, uuid);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return TrinketsConfig.SERVER.INERTIA_NULL.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}
}