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

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;

public class MovementHandler extends EventBaseHandler {

    @SubscribeEvent
    public void onCollideWithBlock(@Nonnull PlayerSPPushOutOfBlocksEvent event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, prop -> {
            if (!prop.isNormalSize()) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public void livingJump(@Nonnull LivingJumpEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        final IAttributeInstance jump = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
        if ((jump != null) && !jump.getModifiers().isEmpty()) {
            if (jump.getBaseValue() != 1.0D) {
                jump.setBaseValue(1.0D);
            }
            double jumpHeight = jump.getAttributeValue();

            if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
                jumpHeight -= entity.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1;
            }
            double jumpVelocity = 0.42D * jumpHeight;
            entity.motionY += (jumpVelocity - 0.42D);

            double horizontalScale = 1.0D + (jumpHeight - 1.0D) * 0.1D;
            horizontalScale = MathHelper.clamp(horizontalScale, 0.9D, 1.1D);

            if (entity.isSprinting()) {
                float yaw = entity.rotationYaw * 0.017453292F;
                double boost = 0.2D * horizontalScale;
                entity.motionX -= MathHelper.sin(yaw) * boost;
                entity.motionZ += MathHelper.cos(yaw) * boost;
            }
        }

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
                    Trinkets.LOGGER.error("Trinkets had an Error with Ability:{}", key);
                    e.printStackTrace();
                }
            }
        });
    }

    @SubscribeEvent
    public void livingFall(@Nonnull LivingFallEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        final float distanceFallen = event.getDistance();
        final float fallDamageMultiplier = event.getDamageMultiplier();

        // ATTRIBUTE
        final IAttributeInstance jump = entity.getAttributeMap().getAttributeInstance(JumpAttribute.Jump);
        if ((jump != null) && !jump.getModifiers().isEmpty()) {
            double jumpHeight = jump.getAttributeValue();
            PotionEffect potioneffect = entity.getActivePotionEffect(MobEffects.JUMP_BOOST);
            float jumpboost = potioneffect == null ? 0.0F : (float) (potioneffect.getAmplifier() + 1);
            jumpHeight -= jumpboost;
            jumpHeight = StringUtils.getAccurateDouble(jumpHeight);

            if (jumpHeight > 0.0D) {
                float newDistance = (float) (distanceFallen - jumpHeight);
                if (newDistance < 0.0F) {
                    newDistance = 0.0F;
                }
                event.setDistance(newDistance);
                entity.fallDistance = Math.max(newDistance, 0.0F);
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
                    Trinkets.LOGGER.error("Trinkets had an Error with Ability:{}", key);
                    e.printStackTrace();
                }
            }
            if (fallDistance != distanceFallen) {
                event.setDistance(MathHelper.clamp(fallDistance, 0, fallDistance));
            }
            if (damageMultiplier != fallDamageMultiplier) {
                event.setDamageMultiplier(MathHelper.clamp(damageMultiplier, 0, damageMultiplier));
            }

            prop.getRaceHandler().fall(event);

            return event.isCanceled() || (event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || bool;
        });
        if ((event.getDistance() <= 0) || (event.getDamageMultiplier() <= 0) || cancel) {
            this.cancelEvent(event);
        }
    }
}
