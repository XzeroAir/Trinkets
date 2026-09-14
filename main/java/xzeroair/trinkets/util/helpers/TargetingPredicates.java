package xzeroair.trinkets.util.helpers;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public final class TargetingPredicates {

    private static final Predicate<Entity> TARGETABLE_ENTITY = TargetingPredicates::isTargetableEntity;
    private static final Predicate<Entity> COMBAT_ENTITY = Predicates.and(entity -> !(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb), EntitySelectors.NOT_SPECTATING, entity -> entity.canBeCollidedWith());
    private static final Predicate<Entity> TARGETABLE = Predicates.and(TARGETABLE_ENTITY, COMBAT_ENTITY);
    private static final Predicate<Entity> SUPPORT_TARGET = Predicates.and(TARGETABLE_ENTITY, EntitySelectors.NOT_SPECTATING, entity -> (entity instanceof EntityLivingBase) || (entity instanceof EntityItem));

    private TargetingPredicates() {
    }

    public static boolean isTargetable(@Nullable Entity entity) {
        return TARGETABLE.apply(entity);
    }

    public static Predicate<Entity> targetable() {
        return TARGETABLE;
    }

    public static boolean isCombatTarget(@Nullable Entity entity, boolean pvpEnabled) {
        return isTargetableEntity(entity) && (pvpEnabled || !(entity instanceof EntityPlayer)) && COMBAT_ENTITY.apply(entity);
    }

    public static Predicate<Entity> combat(boolean pvpEnabled) {
        return entity -> isCombatTarget(entity, pvpEnabled);
    }

    public static boolean isCollectible(@Nullable Entity entity, boolean includeXp) {
        return isLiveEntity(entity) && ((entity instanceof EntityItem) || (includeXp && entity instanceof EntityXPOrb));
    }

    public static boolean isAiTarget(@Nullable Entity entity) {
        return isLiveEntity(entity) && EntitySelectors.CAN_AI_TARGET.apply(entity) && isNonDecorativeEntity(entity);
    }

    public static boolean isSupportTarget(@Nullable Entity entity) {
        return SUPPORT_TARGET.apply(entity);
    }

    public static boolean canApplySupportPotion(@Nullable Entity entity, @Nullable Potion potion, boolean pvpEnabled) {
        if (!isSupportTarget(entity) || !(entity instanceof EntityLivingBase) || potion == null || !((EntityLivingBase) entity).canBeHitWithPotion()) {
            return false;
        }
        return pvpEnabled || !(entity instanceof EntityPlayer) || !potion.isBadEffect();
    }

    public static boolean isPvpEnabled(@Nullable World world, @Nullable Entity owner) {
        try {
            MinecraftServer server = world == null ? null : world.getMinecraftServer();
            if ((server == null) && (owner instanceof EntityPlayerMP)) {
                server = ((EntityPlayerMP) owner).getServer();
            }
            return (server != null) && server.isPVPEnabled();
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isLiveEntity(@Nullable Entity entity) {
        return entity != null && !entity.isDead;
    }

    private static boolean isTargetableEntity(@Nullable Entity entity) {
        return isLiveEntity(entity) && isNonDecorativeEntity(entity);
    }

    private static boolean isNonDecorativeEntity(@Nullable Entity entity) {
        return !(entity instanceof EntityHanging) && !(entity instanceof EntityArmorStand);
    }
}
