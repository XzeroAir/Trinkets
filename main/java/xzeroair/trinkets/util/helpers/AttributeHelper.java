package xzeroair.trinkets.util.helpers;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.attributes.GenericAttribute;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

public class AttributeHelper {

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
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.ARMOR);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.ARMOR);
		}
		if (AttributeConfig.DamageAttributeEnabled()) {
			final double amount = AttributeConfig.DamageAttributeAmount();
			final int op = AttributeConfig.DamageAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.ATTACK_DAMAGE);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.ATTACK_DAMAGE);
		}
		if (AttributeConfig.AttackSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.AttackSpeedAttributeAmount();
			final int op = AttributeConfig.AttackSpeedAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.ATTACK_SPEED);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.ATTACK_SPEED);
		}
		if (AttributeConfig.HealthAttributeEnabled()) {
			final double amount = AttributeConfig.HealthAttributeAmount();
			final int op = AttributeConfig.HealthAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.MAX_HEALTH);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.MAX_HEALTH);
		}
		if (AttributeConfig.KnockbackAttributeEnabled()) {
			final double amount = AttributeConfig.KnockbackAttributeAmount();
			final int op = AttributeConfig.KnockbackAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		}
		if (AttributeConfig.MovementSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.MovementSpeedAttributeAmount();
			final int op = AttributeConfig.MovementSpeedAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.MOVEMENT_SPEED);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.MOVEMENT_SPEED);
		}
		if (AttributeConfig.ArmorToughnessAttributeEnabled()) {
			final double amount = AttributeConfig.ArmorToughnessAttributeAmount();
			final int op = AttributeConfig.ArmorToughnessAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.ARMOR_TOUGHNESS);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.ARMOR_TOUGHNESS);
		}
		if (AttributeConfig.SwimSpeedAttributeEnabled()) {
			final double amount = AttributeConfig.SwimSpeedAttributeAmount();
			final int op = AttributeConfig.SwimSpeedAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, EntityLivingBase.SWIM_SPEED);
		} else {
			GenericAttribute.removeModifier(player, uuid, EntityLivingBase.SWIM_SPEED);
		}
		if (AttributeConfig.LuckAttributeEnabled()) {
			final double amount = AttributeConfig.LuckAttributeAmount();
			final int op = AttributeConfig.LuckAttributeOperation();
			GenericAttribute.addModifier(player, amount, uuid, op, SharedMonsterAttributes.LUCK);
		} else {
			GenericAttribute.removeModifier(player, uuid, SharedMonsterAttributes.LUCK);
		}
		if (player instanceof EntityPlayer) {
			if (AttributeConfig.ReachAttributeEnabled()) {
				final double amount = AttributeConfig.ReachAttributeAmount();
				final int op = AttributeConfig.ReachAttributeOperation();
				GenericAttribute.addModifier(player, amount, uuid, op, EntityPlayer.REACH_DISTANCE);
			} else {
				GenericAttribute.removeModifier(player, uuid, EntityPlayer.REACH_DISTANCE);
			}
		}
	}

}
