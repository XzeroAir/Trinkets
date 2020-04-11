package xzeroair.trinkets.items.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.items.effects.EffectsTitanRing;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.firstaid.FirstAidCompat;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class PotionRestorative extends BasePotion {

	// public static final ResourceLocation ICON = new
	// ResourceLocation(Reference.RESOURCE_PREFIX + "textures/potions/effects.png");

	public PotionRestorative(String name) {
		super(name, 16777215);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
//		return super.isReady(duration, amplifier);
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
//		return super.isInstant();
		return true;
	}

	// works if instant on all Potion Types
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
//		super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
		EffectsFairyRing.FairyUnequip(null, entityLivingBaseIn);
		EffectsDwarfRing.DwarfUnequip(null, entityLivingBaseIn);
		EffectsTitanRing.TitanUnequip(null, entityLivingBaseIn);
		AttributeHelper.removeRaceAttribute(entityLivingBaseIn);
//		AbstractAttributeMap attributes = entityLivingBaseIn.getAttributeMap();
//		if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
//			final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
//			if (!race.getModifiers().isEmpty()) {
//				race.removeAllModifiers();
//			}
//		}
		entityLivingBaseIn.clearActivePotions();
		if (TrinketsConfig.SERVER.Potion.potion_thirst) {
			if (entityLivingBaseIn instanceof EntityPlayer) {
				TANCompat.addThirst((EntityPlayer) entityLivingBaseIn, 20, 20);
				TANCompat.clearThirst(entityLivingBaseIn);
				TANCompat.ClearTempurature((EntityPlayer) entityLivingBaseIn);
			}
		}
		entityLivingBaseIn.heal(20);
		if (Loader.isModLoaded("firstaid")) {
			FirstAidCompat.resetHP(entityLivingBaseIn);
		}
	}

	// if IsReady is True and not instant, Doesn't effect splash potions?
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
//		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
//		AbstractAttributeMap attributes = attributeMapIn;
//		if (attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE) != null) {
//			final IAttributeInstance race = attributes.getAttributeInstance(RaceAttribute.ENTITY_RACE);
//			if (!race.getModifiers().isEmpty()) {
//				race.removeAllModifiers();
//			}
//		}
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}
}