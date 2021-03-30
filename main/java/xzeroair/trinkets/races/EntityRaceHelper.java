package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.Item;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.TrinketRaceBase;

public class EntityRaceHelper {

	public static EntityRace getAttributeRace(EntityLivingBase entity) {
		AbstractAttributeMap attributes = entity.getAttributeMap();
		if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
			final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
			if (!race.getModifiers().isEmpty()) {
				for (AttributeModifier modifier : race.getModifiers()) {
					return EntityRace.getByUUID(modifier.getID());//RaceEnum.getRaceByUUID(modifier.getID());
				}
			}
		}
		return null;
	}

	public static EntityRace getTrinketRace(EntityLivingBase entity) {
		//TODO Fix this so Addons can work
		int i = 0;
		if (TrinketHelper.AccessoryCheck(entity, ModItems.RaceTrinkets.ITEMS)) {
			TrinketRaceBase t = null;
			for (Item trinket : ModItems.RaceTrinkets.ITEMS) {
				if (TrinketHelper.AccessoryCheck(entity, trinket)) {
					i++;
					if (trinket instanceof TrinketRaceBase) {
						t = (TrinketRaceBase) trinket;
					}
				}
			}
			if (t != null) {
				if ((i > 1)) {
					return EntityRaces.none;
				} else {
					return t.getRacialTraits();
				}
			}
		}
		return null;
	}
}
