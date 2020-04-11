package xzeroair.trinkets.util.helpers;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.attributes.armor.ArmorAttribute;
import xzeroair.trinkets.attributes.attackdamage.DamageAttribute;
import xzeroair.trinkets.attributes.attackspeed.AttackSpeedAttribute;
import xzeroair.trinkets.attributes.health.HealthAttribute;
import xzeroair.trinkets.attributes.knock.KnockResistAttribute;
import xzeroair.trinkets.attributes.luck.LuckAttribute;
import xzeroair.trinkets.attributes.reach.ReachAttribute;
import xzeroair.trinkets.attributes.speed.SpeedAttribute;
import xzeroair.trinkets.attributes.swim.SwimAttribute;
import xzeroair.trinkets.attributes.toughness.ToughnessAttribute;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class AttributeHelper {

	// private static UUID uuid =
	// UUID.fromString("d222c4fa-0e05-4b90-98c0-1f574d9d2558");
	//
	// TranslationHelper.addTooltips(this,
	// TrinketsConfig.SERVER.TITAN_RING.Attributes, tooltip);
	//
	// AttributeHelper.handleAttributes(player,
	// TrinketsConfig.SERVER.TITAN_RING.Attributes, uuid);
	//
	// final Collection<IAttributeInstance> attributes =
	// player.getAttributeMap().getAllAttributes();
	// for(final IAttributeInstance attribute : attributes) {
	// for(final AttributeModifier modifier : attribute.getModifiers()) {
	// if(modifier.getID().toString().contentEquals(uuid.toString())) {
	// attribute.removeModifier(modifier);
	// }
	// }
	// }

	public static void removeRaceAttribute(EntityLivingBase player) {
		AbstractAttributeMap attributes = player.getAttributeMap();
		if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
			final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
			if (!race.getModifiers().isEmpty()) {
				race.removeAllModifiers();
			}
		}
	}

	public static void removeAttributes(EntityLivingBase player, UUID uuid) {
		final Collection<IAttributeInstance> attributes = player.getAttributeMap().getAllAttributes();
		for (final IAttributeInstance attribute : attributes) {
			for (final AttributeModifier modifier : attribute.getModifiers()) {
				if (modifier.getID().toString().contentEquals(uuid.toString())) {
					attribute.removeModifier(modifier);
				}
			}
		}
	}

	public static void handleAttributes(EntityLivingBase player, IAttributeConfigHelper AttributeConfig, UUID uuid) {
		if (AttributeConfig.ArmorAttributeEnabled()) {
			final double amount = AttributeConfig.ArmorAttributeAmount();
			final int op = AttributeConfig.ArmorAttributeOperation();
			ArmorAttribute.addModifier(player, amount, uuid, op);
		} else {
			ArmorAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.DamageAttributeEnabled()) {
			final double amount = AttributeConfig.DamageAttributeAmount();
			final int op = AttributeConfig.DamageAttributeOperation();
			DamageAttribute.addModifier(player, amount, uuid, op);
		} else {
			DamageAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.AttackSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.AttackSpeedAttributeAmount();
			final int op = AttributeConfig.AttackSpeedAttributeOperation();
			AttackSpeedAttribute.addModifier(player, amount, uuid, op);
		} else {
			AttackSpeedAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.HealthAttributeEnabled()) {
			final double amount = AttributeConfig.HealthAttributeAmount();
			final int op = AttributeConfig.HealthAttributeOperation();
			HealthAttribute.addModifier(player, amount, uuid, op);
		} else {
			HealthAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.KnockbackAttributeEnabled()) {
			final double amount = AttributeConfig.KnockbackAttributeAmount();
			final int op = AttributeConfig.KnockbackAttributeOperation();
			KnockResistAttribute.addModifier(player, amount, uuid, op);
		} else {
			KnockResistAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.MovementSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.MovementSpeedAttributeAmount();
			final int op = AttributeConfig.MovementSpeedAttributeOperation();
			SpeedAttribute.addModifier(player, amount, uuid, op);
		} else {
			SpeedAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.ArmorToughnessAttributeEnabled()) {
			final double amount = AttributeConfig.ArmorToughnessAttributeAmount();
			final int op = AttributeConfig.ArmorToughnessAttributeOperation();
			ToughnessAttribute.addModifier(player, amount, uuid, op);
		} else {
			ToughnessAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.SwimSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.SwimSpeedAttributeAmount();
			final int op = AttributeConfig.SwimSpeedAttributeOperation();
			SwimAttribute.addModifier(player, amount, uuid, op);
		} else {
			SwimAttribute.removeModifier(player, uuid);
		}
		if (AttributeConfig.LuckAttributeEnabled()) {
			final double amount = AttributeConfig.LuckAttributeAmount();
			final int op = AttributeConfig.LuckAttributeOperation();
			LuckAttribute.addModifier(player, amount, uuid, op);
		} else {
			LuckAttribute.removeModifier(player, uuid);
		}
		if (player instanceof EntityPlayer) {
			if (AttributeConfig.ReachAttributeEnabled()) {
				final double amount = AttributeConfig.ReachAttributeAmount();
				final int op = AttributeConfig.ReachAttributeOperation();
				ReachAttribute.addModifier((EntityPlayer) player, amount, uuid, op);
			} else {
				ReachAttribute.removeModifier((EntityPlayer) player, uuid);
			}
		}
	}

}
