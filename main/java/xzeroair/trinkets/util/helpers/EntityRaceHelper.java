package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.effects.EffectsTitanRing;
import xzeroair.trinkets.items.foods.Dwarf_Stout;
import xzeroair.trinkets.items.foods.Fairy_Food;
import xzeroair.trinkets.items.foods.Titan_Spirit;

public class EntityRaceHelper {

	public static String getRace(EntityLivingBase entity) {
		AbstractAttributeMap attributes = entity.getAttributeMap();
		if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
			final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
			final AttributeModifier fairyFood = race.getModifier(Fairy_Food.getUUID());
			final AttributeModifier dwarfFood = race.getModifier(Dwarf_Stout.getUUID());
			final AttributeModifier titanFood = race.getModifier(Titan_Spirit.getUUID());
			final AttributeModifier fairyPotion = race.getModifier(EffectsFairyRing.getUUID());
			final AttributeModifier dwarfPotion = race.getModifier(EffectsDwarfRing.getUUID());
			final AttributeModifier titanPotion = race.getModifier(EffectsTitanRing.getUUID());
			if (fairyPotion != null) {
				return "fairy_dew";
			} else if (dwarfPotion != null) {
				return "dwarf_stout";
			} else if (titanPotion != null) {
				return "titan_spirit";
			} else {
				if (fairyFood != null) {
					return "fairy_dew";
				} else if (dwarfFood != null) {
					return "dwarf_stout";
				} else if (titanFood != null) {
					return "titan_spirit";
				} else {
					return "none";
				}
			}
		}
		return "none";
	}
}
