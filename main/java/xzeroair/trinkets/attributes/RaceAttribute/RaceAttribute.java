package xzeroair.trinkets.attributes.RaceAttribute;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class RaceAttribute {

	public static final IAttribute ENTITY_RACE = new RangedAttribute((IAttribute) null, Reference.MODID + ".entityRace",
			1.0F, Float.MIN_VALUE, Float.MAX_VALUE).setDescription("Entity Race").setShouldWatch(true);

	private static final String id = Reference.MODID + ".";
	private static String name = "Race";
	protected static UUID uuid = UUID.fromString("f7fa2c13-6794-49df-b7bc-9451154dc3ec");
	private static double bonus;
	private static int operation;
	private static double getBonus() {
		return bonus;
	}
	private static void setBonus(double bonus) {
		RaceAttribute.bonus = MathHelper.clamp(bonus, -1024.0D, 1024.0D);
	}
	protected static AttributeModifier createModifier() {
		return new AttributeModifier(getUUID(), id+name, getBonus(), getOperation());
	}
	private static void setUUID(UUID uuid) {
		RaceAttribute.uuid = uuid;
	}
	private static UUID getUUID() {
		return uuid;
	}
	private static int getOperation() {
		return operation;
	}
	private static void setOperation(int operation) {
		operation = Reference.getOpModifier(operation);
	}
	public static void addModifier(EntityLivingBase entity, double bonus, UUID uuid, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if((AttributeInstance.getModifier(uuid) != null)) {
			if(AttributeInstance.getModifier(uuid).getAmount() != bonus) {
				removeModifier(entity, uuid);
			}
		}
		if(bonus != 0) {
			setOperation(operation);
			setBonus(bonus);
			setUUID(uuid);
			if(AttributeInstance.getModifier(uuid) == null) {
				AttributeInstance.applyModifier(createModifier());
			}
		}
	}
	public static void removeModifier(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if(AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}

}
