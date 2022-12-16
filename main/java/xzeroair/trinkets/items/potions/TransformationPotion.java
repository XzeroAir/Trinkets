<<<<<<< Updated upstream
package xzeroair.trinkets.items.potions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;

public class TransformationPotion extends BasePotion {

	// I assume before the packet syncs to the client, the client removes the attribute or something, in the empty race handler

	protected String uuid;

	public TransformationPotion(String name, int color, int duration, String UUID, boolean isBadEffect) {
		this(Reference.MODID, name, color, duration, UUID, isBadEffect);
	}

	public TransformationPotion(String modid, String name, int color, int duration, String UUID, boolean isBadEffect) {
		super(modid, name, color, duration, isBadEffect);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, UUID, 1, 0);
		uuid = UUID;
	}

	public TransformationPotion(String modid, String name, int color, int duration, String UUID, boolean isBadEffect, int IconX, int IconY, ResourceLocation texture) {
		super(modid, name, color, duration, isBadEffect, IconX, IconY, texture);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, UUID, 1, 0);
		uuid = UUID;
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		List<EntityRace> negate = new ArrayList<>();
		for (EntityRace race : EntityRace.Registry) {
			if (race.getUUID().compareTo(UUID.fromString(this.getRaceUUID())) != 0) {
				negate.add(race);
			}
		}
		List<Potion> removal = new ArrayList<>();
		for (Entry<Potion, PotionEffect> effect : entity.getActivePotionMap().entrySet()) {
			if (effect.getKey() instanceof TransformationPotion) {
				TransformationPotion pot = (TransformationPotion) effect.getKey();
				if (!pot.getRaceUUID().contentEquals(this.getRaceUUID())) {
					removal.add(pot);
				}
			}
		}
		for (Potion pot : removal) {
			entity.removePotionEffect(pot);
		}
		RaceAttribute.removeAllModifiersExcluding(entity, UUID.fromString(this.getRaceUUID()));
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;//super.isReady(duration, amplifier);
	}

	@Override
	public boolean isInstant() {
		return false;//super.isInstant();
	}

	@Override
	public boolean isBadEffect() {
		return super.isBadEffect();
	}

	@Override
	public boolean isBeneficial() {
		return super.isBeneficial();
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		List<ItemStack> cures = new ArrayList<>();
		return cures;
	}

	public String getRaceUUID() {
		return uuid;
	}
=======
package xzeroair.trinkets.items.potions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.items.base.BasePotion;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;

public class TransformationPotion extends BasePotion {

	// I assume before the packet syncs to the client, the client removes the attribute or something, in the empty race handler

	protected String uuid;

	public TransformationPotion(String name, int color, int duration, String UUID, boolean isBadEffect) {
		this(Reference.MODID, name, color, duration, UUID, isBadEffect);
	}

	public TransformationPotion(String modid, String name, int color, int duration, String UUID, boolean isBadEffect) {
		super(modid, name, color, duration, isBadEffect);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, UUID, 1, 0);
		uuid = UUID;
	}

	public TransformationPotion(String modid, String name, int color, int duration, String UUID, boolean isBadEffect, int IconX, int IconY, ResourceLocation texture) {
		super(modid, name, color, duration, isBadEffect, IconX, IconY, texture);
		this.registerPotionAttributeModifier(RaceAttribute.ENTITY_RACE, UUID, 1, 0);
		uuid = UUID;
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		List<EntityRace> negate = new ArrayList<>();
		for (EntityRace race : EntityRace.Registry) {
			if (race.getUUID().compareTo(UUID.fromString(this.getRaceUUID())) != 0) {
				negate.add(race);
			}
		}
		List<Potion> removal = new ArrayList<>();
		for (Entry<Potion, PotionEffect> effect : entity.getActivePotionMap().entrySet()) {
			if (effect.getKey() instanceof TransformationPotion) {
				TransformationPotion pot = (TransformationPotion) effect.getKey();
				if (!pot.getRaceUUID().contentEquals(this.getRaceUUID())) {
					removal.add(pot);
				}
			}
		}
		for (Potion pot : removal) {
			entity.removePotionEffect(pot);
		}
		RaceAttribute.removeAllModifiersExcluding(entity, UUID.fromString(this.getRaceUUID()));
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entity, attributeMapIn, amplifier);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;//super.isReady(duration, amplifier);
	}

	@Override
	public boolean isInstant() {
		return false;//super.isInstant();
	}

	@Override
	public boolean isBadEffect() {
		return super.isBadEffect();
	}

	@Override
	public boolean isBeneficial() {
		return super.isBeneficial();
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		List<ItemStack> cures = new ArrayList<>();
		return cures;
	}

	public String getRaceUUID() {
		return uuid;
	}
>>>>>>> Stashed changes
}