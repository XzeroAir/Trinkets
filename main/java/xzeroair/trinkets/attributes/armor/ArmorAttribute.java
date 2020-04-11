package xzeroair.trinkets.attributes.armor;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import xzeroair.trinkets.util.Reference;

public class ArmorAttribute {

	private static final String id = Reference.MODID + ".";
	private static String name = "ARMOR";
	protected static UUID uuid = UUID.fromString("87f7864f-fbbf-4a64-bce8-a261d90c4ed6");
	private static double amount;
	private static int operation;

	private static double getAmount() {
		return amount;
	}

	private static void setAmount(double amount) {
		ArmorAttribute.amount = amount;
	}

	protected static AttributeModifier createModifier() {
		return new AttributeModifier(getUUID(), id + name, getAmount(), getOperation());
	}

	private static void setUUID(UUID uuid) {
		ArmorAttribute.uuid = uuid;
	}

	private static UUID getUUID() {
		return uuid;
	}

	private static int getOperation() {
		return operation;
	}

	private static void setOperation(int operation) {
		ArmorAttribute.operation = operation;
	}

	public static void addModifier(EntityLivingBase entity, double amount, UUID uuid, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR);
		if (AttributeInstance == null) {
			return;
		}
		if ((AttributeInstance.getModifier(uuid) != null)) {
			if ((AttributeInstance.getModifier(uuid).getAmount() != amount) || (getOperation() != operation)) {
				removeModifier(entity, uuid);
			}
		}
		if (amount != 0) {
			setOperation(operation);
			setAmount(amount);
			setUUID(uuid);
			if (AttributeInstance.getModifier(uuid) == null) {
				AttributeInstance.applyModifier(createModifier());
			}
		}
	}

	public static void removeModifier(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR);
		if (AttributeInstance == null) {
			return;
		}
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}