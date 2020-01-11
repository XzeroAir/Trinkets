package xzeroair.trinkets.items.potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import xzeroair.trinkets.items.base.BasePotion;

public class PotionRestorative extends BasePotion {

	//	public static final ResourceLocation ICON = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/potions/effects.png");

	public PotionRestorative(String name) {
		super(name, 16777215);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		super.performEffect(entity, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}
}