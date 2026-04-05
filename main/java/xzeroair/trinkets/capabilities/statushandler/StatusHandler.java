package xzeroair.trinkets.capabilities.statushandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.CapabilityEntityBase;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.status.StatusEffectPacket;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StatusHandler extends CapabilityEntityBase<StatusHandler, EntityLivingBase> {

    Map<String, TrinketStatusEffect> effects = new HashMap<>();

    public StatusHandler(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public void onUpdate() {
        if (!this.effects.isEmpty()) {
            for (final Entry<String, TrinketStatusEffect> entry : this.effects.entrySet()) {
                if (entry.getValue().getDuration() > 0) {
                    entry.getValue().tickDuration();
                } else {
                    this.effects.remove(entry.getKey());
                }
            }
        }
        if (this.effects.containsKey(StatusEffectsEnum.paralysis.getName())) {
            if (!TrinketHelper.AccessoryCheck(this.getEntity(), ModItems.trinkets.TrinketArcingOrb)) {
                this.getEntity().motionX = 0;
                if (this.getEntity().motionY > 0) {
                    this.getEntity().motionY = 0;
                }
                this.getEntity().motionZ = 0;
                this.getEntity().onGround = false;
            } else {
                this.remove(StatusEffectsEnum.paralysis.getIndex());
            }
        }
        if (this.effects.containsKey(StatusEffectsEnum.bleed.getName())) {
            if (!TrinketHelper.AccessoryCheck(this.getEntity(), ModItems.trinkets.TrinketFaelisClaw)) {
                final TrinketStatusEffect bleeding = this.effects.get(StatusEffectsEnum.bleed.getName());
                final float damage = 0.5F * bleeding.getLevel();
                final int duration = bleeding.getDuration();
                if ((duration % 20) == 0) {
                    //					DamageSource bleed = TrinketsDamageSource.bleeding.setDirectSource(bleeding.getSource()).setDamageBypassesArmor();
                    this.getEntity().attackEntityFrom(DamageSource.MAGIC, damage);
                    //					entity.attackEntityFrom(new EntityDamageSource("xat.bleed", bleeding.getSource()).setDamageBypassesArmor().setDamageIsAbsolute(), damage);
                }
            } else {
                this.remove(StatusEffectsEnum.bleed.getIndex());
            }
        }
//        if (this.effects.containsKey(StatusEffectsEnum.Invigorated.getName())) {
        //			final TrinketStatusEffect effect = effects.get(StatusEffectsEnum.Invigorated.getName());
        //			final UUID uuid = UUID.fromString("02eaa030-91c2-425e-8b3a-9de6aae4df35");
        //			if (this.getEntity().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).getModifier(uuid) == null) {
        //				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ARMOR);
        //				armor.addModifier(this.getEntity(), 4, 0);
        //			}
        //			//			if (this.getEntity().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid) == null) {
        //			//				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.MOVEMENT_SPEED);
        //			//				armor.addModifier(this.getEntity(), 0.25, 1);
        //			//			}
        //			if (this.getEntity().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(uuid) == null) {
        //				final UpdatingAttribute armor = new UpdatingAttribute(uuid, SharedMonsterAttributes.ATTACK_DAMAGE);
        //				armor.addModifier(this.getEntity(), 0.5, 1);
        //			}
        //			if (effect.getDuration() <= 1) {
        //				AttributeHelper.removeAttributesByUUID(this.getEntity(), uuid);
        //			}
//        }
    }

    public void apply(int effectIndex, int dur, int level, Entity source) {
        final StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
        final TrinketStatusEffect effect = new TrinketStatusEffect(effectEnum, dur, level, source);
        this.apply(effect);
    }

    public void apply(TrinketStatusEffect effect) {
        if (!(this.effects.containsKey(effect.getEffectName()))) {
            this.effects.put(effect.getEffectName(), effect);
            if (!this.getEntity().getEntityWorld().isRemote) {
                if (this.getEntity() instanceof EntityPlayerMP) {
                    NetworkHandler.sendTo(new StatusEffectPacket(effect.getSource(), this.getEntity(), effect), (EntityPlayerMP) this.getEntity());
                }
            }
        }
    }

    public void combine(int effectIndex, int dur, int level) {
        final StatusEffectsEnum effectEnum = StatusEffectsEnum.getStatusByIndex(effectIndex);
        if ((this.effects.containsKey(effectEnum.getName()))) {
            final TrinketStatusEffect e = this.effects.get(effectEnum.getName());
            e.addDuration(dur);
            e.addLevel(level);
        }
    }

    public void combine(TrinketStatusEffect effect) {
        if ((this.effects.containsKey(effect.getEffectName()))) {
            final TrinketStatusEffect e = this.effects.get(effect.getEffectName());
            e.addDuration(effect.getDuration());
            e.addLevel(effect.getLevel());
        }
    }

    public void remove(int effectIndex) {
        final StatusEffectsEnum effect = StatusEffectsEnum.getStatusByIndex(effectIndex);
        this.effects.remove(effect.getName());
    }

    public void remove(StatusEffectsEnum effect) {
        this.effects.remove(effect.getName());
    }

    public void removeAll() {
        if (!this.effects.isEmpty()) {
            this.effects.clear();
        }
    }

    public Map<String, TrinketStatusEffect> getActiveEffects() {
        return this.effects;
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
