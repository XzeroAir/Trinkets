package xzeroair.trinkets.items.potions;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.base.BasePotion;

public class TransformationPotion extends BasePotion {

	String uuid;

	public TransformationPotion(String name, int color, String uuid, int IconX, int IconY) {
		super(name, 0, color, IconX, IconY);
		this.uuid = uuid;
		this.setIconIndex(IconX, IconY);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, uuid, 1, 0);
	}

	public TransformationPotion(String name, int color, String uuid, int IconX, int IconY, ResourceLocation texturelocation) {
		super(name, 0, color, IconX, IconY);
		this.uuid = uuid;
		ICON = texturelocation;
		this.setIconIndex(IconX, IconY);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, uuid, 1, 0);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	// Triggers when the potion ends, No, Triggers when the Potion isReady
	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
	}

	// works if instant on all Potion Types, No, Only ever Called if the potion is instant
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
		RaceAttribute.removeAllModifiersExcluding(entityLivingBaseIn, UUID.fromString(uuid));
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		RaceAttribute.removeModifier(entity, UUID.fromString(uuid));
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public boolean hasStatusIcon() {
		Minecraft.getMinecraft().renderEngine.bindTexture(ICON);
		return true;
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return super.getCurativeItems();
	}

	public String getRaceUUID() {
		return uuid;
	}
}