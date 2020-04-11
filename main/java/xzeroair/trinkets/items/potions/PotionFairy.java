package xzeroair.trinkets.items.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.effects.EffectsFairyRing;
import xzeroair.trinkets.util.Reference;

public class PotionFairy extends BasePotion {

	public static final ResourceLocation ICON = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/potions/effects.png");

	public PotionFairy(String name) {
		super(name, 12514535);
		this.setIconIndex(0, 0);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, EffectsFairyRing.getUUID().toString(), 1, 0);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

	}

	// works if instant on all Potion Types
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {

	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
		EffectsFairyRing.FairyUnequip(null, entity);
	}

	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}