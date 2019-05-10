package xzeroair.trinkets.attributes.swim;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class SwimAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "SWIM_SPEED";
	protected static final UUID uuid = UUID.fromString("ef992b8a-e077-4972-8f97-50469b9ce04d");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		SwimAttribute.bonus = MathHelper.clamp(bonus, 0.0D, 1024.0D);
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
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(EntityLivingBase.SWIM_SPEED);
		setOperation(operation);
		setBonus(bonus);
		if(AttributeInstance.getModifier(uuid) == null) {
			AttributeInstance.applyModifier(createModifier());
		}
	}
	public static void removeModifier(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(EntityLivingBase.SWIM_SPEED);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
