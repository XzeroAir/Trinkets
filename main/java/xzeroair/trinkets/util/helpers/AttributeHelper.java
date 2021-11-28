package xzeroair.trinkets.util.helpers;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;

public class AttributeHelper {

	public static void removeAttributes(EntityLivingBase entity, UUID uuid) {
		final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
		for (final IAttributeInstance attribute : attributes) {
			if (!attribute.getAttribute().equals(RaceAttribute.ENTITY_RACE)) {
				if (attribute.getModifier(uuid) != null) {
					attribute.removeModifier(uuid);
				}
			}
		}
	}

	public static void removeAttributesByUUID(EntityLivingBase entity, UUID uuid) {
		final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
		for (final IAttributeInstance attribute : attributes) {
			if (attribute.getModifier(uuid) != null) {
				attribute.removeModifier(uuid);
			}
		}
	}

	//	public static void test(EntityLivingBase entity, String string) {
	//		final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
	//		IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstanceByName(string);
	//		if (attribute != null) {
	//
	//		}
	//	}
}
