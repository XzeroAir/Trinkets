package xzeroair.trinkets.capabilities.statushandler;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;

public class TrinketStatusEffect {

    private final StatusEffectsEnum effect;
    private int duration;
    private int level;
    private Entity source;

    public TrinketStatusEffect(StatusEffectsEnum effect, int duration, int level, Entity source) {
        this.effect = effect;
        this.duration = duration;
        this.level = level;
        this.source = source;
    }

    //TODO Replace Enum with something else less Hard Coded
    public TrinketStatusEffect(String effect, int duration, int level, Entity source) {
        this.effect = StatusEffectsEnum.getStatusByName(effect);
        this.duration = duration;
        this.level = level;
        this.source = source;
    }

    public int getEffectID() {
        return this.effect.getIndex();
    }

    public String getEffectName() {
        return this.effect.getName();
    }

    public StatusEffectsEnum getEffect() {
        return this.effect;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int dur) {
        this.duration = dur;
    }

    public void addDuration(int dur) {
        this.duration += dur;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int level) {
        this.level += level;
    }

    public Entity getSource() {
        return this.source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }

    public void tickDuration() {
        this.duration--;
    }

}
