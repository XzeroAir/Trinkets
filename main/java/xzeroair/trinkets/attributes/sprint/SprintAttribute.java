package xzeroair.trinkets.attributes.sprint;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class SprintAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "SPRINT_SPEED";
	protected static final UUID uuid = UUID.fromString("d171a59a-63dd-48eb-917a-3e02bdd6d13b");
	protected static final UUID uuidSprintChargeSpeed = UUID.fromString("b9605d5b-76cf-4a23-a7a0-d8540928bfc5");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		SprintAttribute.bonus = MathHelper.clamp(bonus, 0.0D, 1024.0D);
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
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
		setOperation(operation);
		setBonus(bonus);
		if(AttributeInstance.getModifier(uuid) == null) {
			AttributeInstance.applyModifier(createModifier());
		}
	}
	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
