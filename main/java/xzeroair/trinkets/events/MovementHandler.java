package xzeroair.trinkets.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.StringUtils;

import java.util.Map;
import java.util.Map.Entry;

public class MovementHandler extends EventBaseHandler {

    @SubscribeEvent
    public void onCollideWithBlock(PlayerSPPushOutOfBlocksEvent event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, prop -> {
            if (!prop.isNormalSize()) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public void livingJump(LivingJumpEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        // TODO Add Config to disable attribute
        final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
        if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
            double motionBase = attribute.getBaseValue();
            if (motionBase != 1.0D) {
                attribute.setBaseValue(1.0D);
            }
            double motion = attribute.getAttributeValue();
            if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
                motion -= entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1;
            }
            double motionXZ = Math.max(0.0D, ((0.42D * (motion - 1D)) / 0.42D));
            motion = (motion / 10.0D);
            entity.motionY += motion;
            if (entity.isSprinting()) {
                entity.motionX *= (motionXZ);
                entity.motionZ *= (motionXZ);
            }
        }
        //				entity.motionY = 0.368129F;
        Capabilities.getEntityProperties(entity, prop -> {
            if (TrinketsConfig.getClientStore().BLOCK_MOVEMENT) {
                if (prop.getRaceHandler().isTransforming()) {
                    entity.motionY = 0;
                    return;
                }
            }
            prop.getRaceHandler().jump();
            Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
            for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
                String key = entry.getKey();
                AbilityHolder value = entry.getValue();
                try {
                    IAbilityInterface ability = value.getAbility();
                    if ((ability instanceof IJumpAbility)) {
                        ((IJumpAbility) ability).jump(entity);
                    }
                } catch (Exception e) {
                    Trinkets.log.error("Trinkets had an Error with Ability:" + key);
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void livingFall(LivingFallEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote) {
            return;
        }
        final float baseDistance = event.getDistance();
        final float baseMultiplier = event.getDamageMultiplier();

        // ATTRIBUTE
        final IAttributeInstance attribute = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
        if ((attribute != null) && !attribute.getModifiers().isEmpty()) {
            double value = attribute.getAttributeValue();
            PotionEffect potioneffect = entity.getActivePotionEffect(MobEffects.JUMP_BOOST);
            float jumpboost = potioneffect == null ? 0.0F : (float) (potioneffect.getAmplifier() + 1);
            value -= jumpboost;
            value = StringUtils.getAccurateDouble(value);
            if (value > 0.0F) {
                event.setDistance((float) (baseDistance - value));
            }
        }
        // END ATTRIBUTE

        boolean cancel = Capabilities.getEntityProperties(entity, false, (prop, bool) -> {
            float fallDistance = event.getDistance();
            float damageMultiplier = event.getDamageMultiplier();

            Map<String, AbilityHolder> abilities = prop.getAbilityHandler().getActiveAbilities();
            for (Entry<String, AbilityHolder> entry : abilities.entrySet()) {
                String key = entry.getKey();
                AbilityHolder value = entry.getValue();
                try {
                    IAbilityInterface ability = value.getAbility();
                    if ((ability instanceof IJumpAbility)) {
                        final IJumpAbility fall = (IJumpAbility) ability;
                        final float abilityDistance = fall.fallDistance(entity, fallDistance);
                        if (fallDistance != abilityDistance) {
                            fallDistance = abilityDistance;
                        }
                        final float abilityModifier = fall.fallDamageMultiplier(entity, damageMultiplier);
                        if (damageMultiplier != abilityModifier) {
                            damageMultiplier = abilityModifier;
                        }
                        final boolean abilityCancel = fall.fall(entity, fallDistance, damageMultiplier, bool);
                        if (bool != abilityCancel) {
                            bool = abilityCancel;
                        }
                    }
                } catch (Exception e) {
                    Trinkets.log.error("Trinkets had an Error with Ability:" + key);
                    e.printStackTrace();
                }
            }
            if (fallDistance != baseDistance) {
                event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
            }
            if (damageMultiplier != baseMultiplier) {
                event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
            }

            prop.getRaceHandler().fall(event);

            return event.isCanceled() || (event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || bool;
        });
        if (cancel) {
            this.cancelEvent(event);
        }
    }
}
