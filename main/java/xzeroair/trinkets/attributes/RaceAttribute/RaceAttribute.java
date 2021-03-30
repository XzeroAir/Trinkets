package xzeroair.trinkets.attributes.RaceAttribute;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;

public class RaceAttribute {

	public static final IAttribute ENTITY_RACE = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityRace",
			1.0F, Float.MIN_VALUE, Float.MAX_VALUE
	).setDescription("Entity Race").setShouldWatch(true);

	private static final String id = Reference.MODID + ".";
	private static String race = "";
	private static String name = "race";
	protected static UUID uuid = UUID.fromString("f7fa2c13-6794-49df-b7bc-9451154dc3ec");
	private static double amount;
	private static int operation;

	private static double getAmount() {
		return amount;
	}

	private static void setAmount(double amount) {
		RaceAttribute.amount = amount;
	}

	protected static AttributeModifier createModifier() {
		return new AttributeModifier(getUUID(), id + name + race, getAmount(), getOperation());
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
		RaceAttribute.operation = operation;
	}

	private static void setRace(String race) {
		RaceAttribute.race = race;
	}

	public static void addModifier(EntityLivingBase entity, String name, double amount, UUID uuid, int operation) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null) {
			return;
		}
		if ((AttributeInstance.getModifier(uuid) != null)) {
			if ((AttributeInstance.getModifier(uuid).getAmount() != amount) || (getOperation() != operation)) {
				removeModifier(entity, uuid);
			}
		}
		if (amount != 0) {
			setRace(name);
			setOperation(operation);
			setAmount(amount);
			setUUID(uuid);
			if (AttributeInstance.getModifier(uuid) == null) {
				AttributeInstance.applyModifier(createModifier());
			}
		}
	}

	public static void addModifier(EntityLivingBase entity, double amount, EntityRace race, int operation) {
		if ((race == null) || race.equals(EntityRaces.human)) {
			return;
		}
		addModifier(entity, race.getName(), amount, race.getUUID(), operation);
	}

	public static void removeModifier(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null) {
			return;
		}
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}

	public static void removeModifier(EntityLivingBase entity, EntityRace race) {
		removeModifier(entity, race.getUUID());
	}

	public static void removeAllModifiersExcluding(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null) {
			return;
		}
		if (!AttributeInstance.getModifiers().isEmpty()) {
			for (AttributeModifier modifier : AttributeInstance.getModifiers()) {
				if (modifier.getID().compareTo(uuid) != 0) {
					AttributeInstance.removeModifier(modifier);
				}
			}
		}
	}

	public static void removeAllModifiersExcluding(EntityLivingBase entity, EntityRace race) {
		removeAllModifiersExcluding(entity, race.getUUID());
	}

	public static void removeAllModifiers(EntityLivingBase entity) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null) {
			return;
		}
		AttributeInstance.removeAllModifiers();
	}

}
