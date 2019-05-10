package xzeroair.trinkets.attributes.knock;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class KnockResistAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "KNOCK_RESIST";
	protected static final UUID uuid = UUID.fromString("b10cb82e-c272-4b1c-8766-519d142b482a");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		KnockResistAttribute.bonus = MathHelper.clamp(bonus, 0.0D, 1.0D);
	}
	protected static AttributeModifier createModifier() {
		return new AttributeModifier(uuid, id+name, getBonus(), getOperation());
	}
	private static int getOperation() {
		return operation;
	}
	private static void setOperation(int operation) {
		operation = Reference.getOpModifier(operation);
	}
	public static void addModifier(EntityLivingBase entity, double bonus, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		setOperation(operation);
		setBonus(bonus);
		if(AttributeInstance.getModifier(uuid) == null) {
			AttributeInstance.applyModifier(createModifier());
		}
	}
	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
