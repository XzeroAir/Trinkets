package xzeroair.trinkets.entity.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.entity.EntitySummonWrapper;

public interface EntityWrapperBehaviorTemplate {

    ResourceLocation getId();

    void apply(EntitySummonWrapper wrapper);

    default boolean isSummonerValid(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return true;
    }

    default boolean canPerchOnSummoner(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return false;
    }

    default ShoulderDismountMode getShoulderDismountMode(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return ShoulderDismountMode.SNEAK;
    }

    default void killedEntity(EntitySummonWrapper wrapper, EntityLivingBase target) {
    }

    default void tick(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
    }

    default void pathFailed(EntitySummonWrapper wrapper, EntityLivingBase target) {
    }

    default void ownerUnavailable(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
    }

    default void ownerChangingDimension(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        this.ownerUnavailable(wrapper, summoner);
    }

    default void died(EntitySummonWrapper wrapper) {
    }

    default boolean suppressMovement(EntitySummonWrapper wrapper) {
        return false;
    }

    default boolean processInteract(EntitySummonWrapper wrapper, EntityPlayer player, EnumHand hand) {
        return false;
    }

    default float getFollowTeleportDistance(EntitySummonWrapper wrapper, EntityLivingBase summoner) {
        return -1.0F;
    }
}
