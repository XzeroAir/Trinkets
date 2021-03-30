package xzeroair.trinkets.util;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;

public class TrinketStatusEffect {

	StatusEffectsEnum effect;
	int duration;
	int level;
	EntityLivingBase source;

	public TrinketStatusEffect(StatusEffectsEnum effect, int duration, int level, EntityLivingBase source) {
		this.effect = effect;
		this.duration = duration;
		this.level = level;
		this.source = source;
	}

	//TODO Replace Enum with something else less Hard Coded
	public TrinketStatusEffect(String effect, int duration, int level, EntityLivingBase source) {
		this.effect = StatusEffectsEnum.getStatusByName(effect);
		this.duration = duration;
		this.level = level;
		this.source = source;
	}

	public int getEffectID() {
		return effect.getIndex();
	}

	public String getEffectName() {
		return effect.getName();
	}

	public StatusEffectsEnum getEffect() {
		return effect;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int dur) {
		duration = dur;
	}

	public void addDuration(int dur) {
		duration += dur;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void addLevel(int level) {
		this.level += level;
	}

	public EntityLivingBase getSource() {
		return source;
	}

	public void setSource(EntityLivingBase source) {
		this.source = source;
	}

	public void tickDuration() {
		duration--;
	}

}
