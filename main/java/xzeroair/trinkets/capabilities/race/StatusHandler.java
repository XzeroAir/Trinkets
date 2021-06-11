package xzeroair.trinkets.capabilities.race;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketStatusEffect;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class StatusHandler {

	EntityLivingBase entity;
	EntityProperties properties;
	Map<String, TrinketStatusEffect> effects = new HashMap<>();
	//	List<TrinketStatusEffect> effects = new ArrayList<TrinketStatusEffect>();

	public StatusHandler(EntityLivingBase entity, EntityProperties properties) {
		this.entity = entity;
		this.properties = properties;
	}

	public void onUpdate() {
		if (!effects.isEmpty()) {
			for (Entry<String, TrinketStatusEffect> entry : effects.entrySet()) {
				if (entry.getValue().getDuration() > 0) {
					entry.getValue().tickDuration();
				} else {
					effects.remove(entry.getKey());
				}
			}
		}
		if (effects.containsKey(StatusEffectsEnum.paralysis.getName())) {
			if (!TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketArcingOrb)) {
				entity.motionX = 0;
				if (entity.motionY > 0) {
					entity.motionY = 0;
				}
				entity.motionZ = 0;
				entity.onGround = false;
			} else {
				this.remove(StatusEffectsEnum.paralysis.getIndex());
			}
		}
		if (effects.containsKey(StatusEffectsEnum.bleed.getName())) {
			if (!(entity instanceof EntityPlayer) && !TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketFaelisClaw)) {
				TrinketStatusEffect bleeding = effects.get(StatusEffectsEnum.bleed.getName());
				float damage = 0.5F * bleeding.getLevel();
				if ((bleeding.getDuration() % 20) == 0) {
					entity.attackEntityFrom(DamageSource.MAGIC, damage);
				}
			} else {
				this.remove(StatusEffectsEnum.bleed.getIndex());
			}
		}
		if (effects.containsKey(StatusEffectsEnum.Invigorated.getName())) {
			TrinketStatusEffect effect = effects.get(StatusEffectsEnum.Invigorated.getName());
			UUID uuid = UUID.fromString("02eaa030-91c2-425e-8b3a-9de6aae4df35");
			if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).getModifier(uuid) == null) {
				UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ARMOR);
				armor.addModifier(entity, 4, 0);
			}
			if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid) == null) {
				UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.MOVEMENT_SPEED);
				armor.addModifier(entity, 0.25, 1);
			}
			if (entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(uuid) == null) {
				UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ATTACK_DAMAGE);
				armor.addModifier(entity, 0.5, 1);
			}
			if (effect.getDuration() <= 1) {
				AttributeHelper.removeAttributesByUUID(entity, uuid);
			}
			// +10 Armor, Op 0
			// +25% movement speed
			// +50% attack dmg
			// 20 seconds Duration
		}
	}

	public void apply(int effectIndex, int dur, int level, EntityLivingBase source) {
		StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
		TrinketStatusEffect effect = new TrinketStatusEffect(effectEnum, dur, level, source);
		this.apply(effect);
	}

	public void apply(TrinketStatusEffect effect) {
		if (!(effects.containsKey(effect.getEffectName()))) {
			effects.put(effect.getEffectName(), effect);
		}
	}

	public void combine(int effectIndex, int dur, int level) {
		StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
		if ((effects.containsKey(effectEnum.getName()))) {
			TrinketStatusEffect e = effects.get(effectEnum.getName());
			e.addDuration(dur);
			e.addLevel(level);
		}
	}

	public void combine(TrinketStatusEffect effect) {
		if ((effects.containsKey(effect.getEffectName()))) {
			TrinketStatusEffect e = effects.get(effect.getEffectName());
			e.addDuration(effect.getDuration());
			e.addLevel(effect.getLevel());
		}
	}

	public void remove(int effectIndex) {
		StatusEffectsEnum effect = StatusEffectsEnum.getStatusByIndex(effectIndex);
		if (effects.containsKey(effect.getName())) {
			effects.remove(effect.getName());
		}
	}

	public void remove(StatusEffectsEnum effect) {
		if (effects.containsKey(effect.getName())) {
			effects.remove(effect.getName());
		}
	}

	public void removeAll() {
		if (!effects.isEmpty()) {
			effects.clear();
		}
	}

	public Map<String, TrinketStatusEffect> getActiveEffects() {
		return effects;
	}
	//	public class Duration {
	//		int duration = 0;
	//
	//		public Duration(int dur) {
	//			duration = dur;
	//		}
	//
	//		public void onUpdate() {
	//			if (duration > 0) {
	//				duration--;
	//			}
	//		}
	//
	//		public int getDuration() {
	//			return duration;
	//		}
	//	}

}
