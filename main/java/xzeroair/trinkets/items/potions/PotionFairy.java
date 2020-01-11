package xzeroair.trinkets.items.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
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
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, EffectsFairyRing.getUUID().toString(), 1, 2);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		super.performEffect(entity, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
		if(entity instanceof EntityPlayer) {
			EffectsFairyRing.FairyUnequip(null, (EntityPlayer) entity);
			//			RaceAttribute.removeModifier(entity, Fairy_Ring_Effects.getUUID());
		}
	}

	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}