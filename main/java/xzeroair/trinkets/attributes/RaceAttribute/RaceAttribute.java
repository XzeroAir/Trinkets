package xzeroair.trinkets.attributes.RaceAttribute;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;

public class RaceAttribute {

	public static final IAttribute ENTITY_RACE = new RangedAttribute(
			(IAttribute) null, Reference.MODID + ".entityRace",
			1.0F, Float.MIN_VALUE, Float.MAX_VALUE
	).setDescription("Entity Race").setShouldWatch(true);

	public static void removeModifier(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null)
			return;
		if (AttributeInstance.getModifier(uuid) != null) {
			AttributeInstance.removeModifier(uuid);
		}
	}

	public static void removeModifier(EntityLivingBase entity, EntityRace race) {
		removeModifier(entity, race.getUUID());
	}

	public static void removeAllModifiersExcluding(EntityLivingBase entity, UUID uuid) {
		final IAttributeInstance AttributeInstance = entity.getAttributeMap().getAttributeInstance(RaceAttribute.ENTITY_RACE);
		if (AttributeInstance == null)
			return;
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
		if (AttributeInstance == null)
			return;
		AttributeInstance.removeAllModifiers();
	}

}
