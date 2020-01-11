package xzeroair.trinkets.items.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.items.effects.EffectsDwarfRing;
import xzeroair.trinkets.util.Reference;

public class PotionDwarf extends BasePotion {

	public static final ResourceLocation ICON = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/potions/effects.png");

	public PotionDwarf(String name) {
		super(name, 10832170);
		this.setIconIndex(1, 0);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, EffectsDwarfRing.getUUID().toString(), 2, 2);
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		super.performEffect(entity, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
		if(entity instanceof EntityPlayer) {
			EffectsDwarfRing.DwarfUnequip(null, (EntityPlayer) entity);
			//			RaceAttribute.removeModifier(entity, Dwarf_Ring_Effects.getUUID());
		}
	}

	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}
}