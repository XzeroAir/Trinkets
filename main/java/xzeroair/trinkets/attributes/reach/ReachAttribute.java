package xzeroair.trinkets.attributes.reach;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.util.Reference;

public class ReachAttribute {
	private static final String id = Reference.MODID + ".";
	private static String name = "REACH_DISTANCE";
	protected static UUID uuid = UUID.fromString("e7d29ce9-ff64-49d8-8241-d6a76a658de3");
	private static double amount;
	private static int operation;

	private static double getAmount() {
		return amount;
	}

	private static void setAmount(double amount) {
		ReachAttribute.amount = amount;
	}

	protected static AttributeModifier createModifier() {
		return new AttributeModifier(getUUID(), id + name, getAmount(), getOperation());
	}

	private static void setUUID(UUID uuid) {
		ReachAttribute.uuid = uuid;
	}

	private static UUID getUUID() {
		return uuid;
	}

	private static int getOperation() {
		return operation;
	}

	private static void setOperation(int operation) {
		ReachAttribute.operation = operation;
	}

	public static void addModifier(EntityPlayer entity, double amount, UUID uuid, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
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

	public static void removeModifier(EntityPlayer entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
		if (AttributeInstance == null) {
			return;
		}
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
