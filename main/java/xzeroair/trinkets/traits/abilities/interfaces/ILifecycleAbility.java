package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.EntityLivingBase;

public interface ILifecycleAbility extends IAbilityInterface {

    default void onJoinWorld(EntityLivingBase entity) {
    }

    default void onLogin(EntityLivingBase entity) {
    }

    default void onLogout(EntityLivingBase entity) {
    }

    default void onChangedDimension(EntityLivingBase entity, int fromDimension, int toDimension) {
    }
}
