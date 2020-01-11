package xzeroair.trinkets.items.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

public class BasePotion extends Potion {

	public BasePotion(String name, int color) {
		super(false, color);
		this.setPotionName("effect." + name);
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		super.performEffect(entity, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}
}