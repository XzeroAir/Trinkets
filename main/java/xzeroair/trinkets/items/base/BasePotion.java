package xzeroair.trinkets.items.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;

public class BasePotion extends Potion {

	public BasePotion(String name, int color) {
		super(false, color);
		this.setPotionName("effect." + name);
		this.setRegistryName(new ResourceLocation(Reference.MODID, name));
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
//		return super.isInstant();
		return false;
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
//		super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
		entityLivingBaseIn.heal(5);
		if (TrinketsConfig.SERVER.Potion.potion_thirst) {
			if (entityLivingBaseIn instanceof EntityPlayer) {
				TANCompat.addThirst((EntityPlayer) entityLivingBaseIn, 5, 5);
				TANCompat.clearThirst(entityLivingBaseIn);
			}
		}
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
//		super.performEffect(entity, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
//		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public boolean hasStatusIcon() {
		return false;
	}
}