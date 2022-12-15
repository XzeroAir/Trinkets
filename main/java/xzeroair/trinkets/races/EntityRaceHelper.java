package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.init.EntityRaces;

public class EntityRaceHelper {

	public static EntityRace getAttributeRace(EntityLivingBase entity) {
		IAttributeInstance race = entity.getEntityAttribute(RaceAttribute.ENTITY_RACE);
		if (race != null) {
			if (!race.getModifiers().isEmpty()) {
				for (final AttributeModifier modifier : race.getModifiers()) {
					return EntityRace.getByUUID(modifier.getID());
				}
			}
		}
		return null;
	}

	public static EntityRace getTrinketRace(EntityLivingBase entity) {
		final int count = TrinketHelper.countAccessories(
				entity, stack -> stack.getItem() instanceof IRaceProvider
		);
		if ((count > 1) || (count < 1)) {
			return EntityRaces.none;
		} else {
			final ItemStack transformation = TrinketHelper.getAccessory(entity, stack -> stack.getItem() instanceof IRaceProvider);
			return ((IRaceProvider) transformation.getItem()).getRace();
		}
	}
}
