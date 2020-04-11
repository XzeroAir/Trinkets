package xzeroair.trinkets.attributes.health;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;

public class HealthAttribute {

	private static final String id = Reference.MODID + ".";
	private static String name = "HEALTH";
	protected static UUID uuid = UUID.fromString("cc79ab38-34fc-43e2-80f8-ed9440e11bdf");
	private static double amount;
	private static int operation;

	private static double getAmount() {
		return amount;
	}

	private static void setAmount(double amount) {
		HealthAttribute.amount = amount;
	}

	protected static AttributeModifier createModifier() {
		return new AttributeModifier(getUUID(), id + name, getAmount(), getOperation());
	}

	private static void setUUID(UUID uuid) {
		HealthAttribute.uuid = uuid;
	}

	private static UUID getUUID() {
		return uuid;
	}

	private static int getOperation() {
		return operation;
	}

	private static void setOperation(int operation) {
		HealthAttribute.operation = operation;
	}

	public static void addModifier(EntityLivingBase entity, double amount, UUID uuid, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
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
				if (Loader.isModLoaded("firstaid")) {
					FirstAidCompat.resetHP(entity);
				}
			}
		}
	}

	public static void removeModifier(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH);
		if (AttributeInstance == null) {
			return;
		}
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}
}
