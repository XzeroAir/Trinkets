package xzeroair.trinkets.capabilities.statushandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.CapabilityBase;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.status.StatusEffectPacket;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class StatusHandler extends CapabilityBase<StatusHandler, EntityLivingBase> {

	Map<String, TrinketStatusEffect> effects = new HashMap<>();

	public StatusHandler(EntityLivingBase entity) {
		super(entity);
	}

	@Override
	public void onUpdate() {
		//		if (object.getName().contains("Airgre")) {
		//			System.out.println(object.getName() + " | " + effects);
		//		}
		if (!effects.isEmpty()) {
			for (final Entry<String, TrinketStatusEffect> entry : effects.entrySet()) {
				if (entry.getValue().getDuration() > 0) {
					entry.getValue().tickDuration();
				} else {
					effects.remove(entry.getKey());
				}
			}
		}
		if (effects.containsKey(StatusEffectsEnum.paralysis.getName())) {
			if (!TrinketHelper.AccessoryCheck(object, ModItems.trinkets.TrinketArcingOrb)) {
				object.motionX = 0;
				if (object.motionY > 0) {
					object.motionY = 0;
				}
				object.motionZ = 0;
				object.onGround = false;
			} else {
				this.remove(StatusEffectsEnum.paralysis.getIndex());
			}
		}
		if (effects.containsKey(StatusEffectsEnum.bleed.getName())) {
			if (!TrinketHelper.AccessoryCheck(object, ModItems.trinkets.TrinketFaelisClaw)) {
				final TrinketStatusEffect bleeding = effects.get(StatusEffectsEnum.bleed.getName());
				final float damage = 0.5F * bleeding.getLevel();
				final int duration = bleeding.getDuration();
				if ((duration % 20) == 0) {
					//					DamageSource bleed = TrinketsDamageSource.bleeding.setDirectSource(bleeding.getSource()).setDamageBypassesArmor();
					object.attackEntityFrom(DamageSource.MAGIC, damage);
					//					entity.attackEntityFrom(new EntityDamageSource("xat.bleed", bleeding.getSource()).setDamageBypassesArmor().setDamageIsAbsolute(), damage);
				}
			} else {
				this.remove(StatusEffectsEnum.bleed.getIndex());
			}
		}
		if (effects.containsKey(StatusEffectsEnum.Invigorated.getName())) {
			final TrinketStatusEffect effect = effects.get(StatusEffectsEnum.Invigorated.getName());
			final UUID uuid = UUID.fromString("02eaa030-91c2-425e-8b3a-9de6aae4df35");
			if (object.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).getModifier(uuid) == null) {
				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ARMOR);
				armor.addModifier(object, 4, 0);
			}
			//			if (object.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid) == null) {
			//				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.MOVEMENT_SPEED);
			//				armor.addModifier(object, 0.25, 1);
			//			}
			if (object.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(uuid) == null) {
				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ATTACK_DAMAGE);
				armor.addModifier(object, 0.5, 1);
			}
			if (effect.getDuration() <= 1) {
				AttributeHelper.removeAttributesByUUID(object, uuid);
			}
		}
	}

	public void apply(int effectIndex, int dur, int level, Entity source) {
		final StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
		final TrinketStatusEffect effect = new TrinketStatusEffect(effectEnum, dur, level, source);
		this.apply(effect);
	}

	public void apply(TrinketStatusEffect effect) {
		if (!(effects.containsKey(effect.getEffectName()))) {
			effects.put(effect.getEffectName(), effect);
			if (!object.getEntityWorld().isRemote) {
				if (object instanceof EntityPlayerMP) {
					NetworkHandler.sendTo(new StatusEffectPacket(effect.getSource(), object, effect), (EntityPlayerMP) object);
				}
			}
		} else {
		}
	}

	public void combine(int effectIndex, int dur, int level) {
		final StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
		if ((effects.containsKey(effectEnum.getName()))) {
			final TrinketStatusEffect e = effects.get(effectEnum.getName());
			e.addDuration(dur);
			e.addLevel(level);
		}
	}

	public void combine(TrinketStatusEffect effect) {
		if ((effects.containsKey(effect.getEffectName()))) {
			final TrinketStatusEffect e = effects.get(effect.getEffectName());
			e.addDuration(effect.getDuration());
			e.addLevel(effect.getLevel());
		}
	}

	public void remove(int effectIndex) {
		final StatusEffectsEnum effect = StatusEffectsEnum.getStatusByIndex(effectIndex);
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

	public void savedNBTData(NBTTagCompound tag) {

	}

	public void loadNBTData(NBTTagCompound nbt) {

	}

}
