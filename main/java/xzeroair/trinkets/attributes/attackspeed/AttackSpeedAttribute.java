package xzeroair.trinkets.attributes.attackspeed;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class AttackSpeedAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "ATTACK_SPEED";
	protected static final UUID uuid = UUID.fromString("0d832607-90c5-4dce-ab60-1445b82a36d1");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		AttackSpeedAttribute.bonus = MathHelper.clamp(bonus, 0.0D, 1024.0D);
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
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED);
		setOperation(operation);
		setBonus(bonus);
		if(AttributeInstance.getModifier(uuid) == null) {
			AttributeInstance.applyModifier(createModifier());
		}
	}
	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
