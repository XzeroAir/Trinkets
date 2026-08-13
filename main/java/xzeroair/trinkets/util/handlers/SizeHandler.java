package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TrinketReflectionHelper;

public class SizeHandler {
    private static final float MIN_PLAYER_WIDTH = 0.3F;
    private static final float MIN_WIDTH = 0.252F;
    private static final float MIN_HEIGHT = 0.45F;
    private static final float MAX_SIZE = 5.4F;
    private static final float SIZE_PRECISION = 1000.0F;

    public static void setSizeForEntity(EntityLivingBase entity, float TLHeight, float TLWidth) {
        if (entity instanceof EntityPlayer) {
            setSize(entity, TLHeight, TLWidth);
            return;
        }
        setLivingSize(entity, TLHeight, TLWidth);
    }

    public static void setSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }
        if (entity.isChild()) {
            return;
        }
        boolean flying = (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying;
        float width;
        float height;

        if (entity.isSneaking()) {
            width = TLWidth;
            height = !flying ? TLHeight * 0.92F : TLHeight;
        } else if (entity.isElytraFlying()) {
            width = TLWidth;
            height = TLHeight * 0.2F;
        } else if (entity.isPlayerSleeping()) {
            width = 0.2F;
            height = 0.2F;
        } else if (entity.isRiding()) {
            width = TLWidth;
            height = TLHeight;
        } else {
            width = TLWidth;
            height = TLHeight;
        }
        final float minWidth = entity instanceof EntityPlayer ? MIN_PLAYER_WIDTH : MIN_WIDTH;
        width = MathHelper.clamp(roundSize(width), minWidth, MAX_SIZE);
        height = MathHelper.clamp(roundSize(height), MIN_HEIGHT, MAX_SIZE);

        if ((width != entity.width) || (height != entity.height)) {
            try {
                TrinketReflectionHelper.ENTITY_SETSIZE.invoke(entity, width, height);
            } catch (Exception ignored) {
            }
        }
        final double d0 = entity.width / 2.0D;
        double x1 = entity.posX - d0;
        double z1 = entity.posZ - d0;
        double x2 = entity.posX + d0;
        double z2 = entity.posZ + d0;
        double y = entity.posY + entity.height;
        entity.setEntityBoundingBox(new AxisAlignedBB(x1, entity.posY, z1, x2, y, z2));
    }

    private static void setPlayerSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }

        boolean flying = (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying;
        float width;
        float height;

        if (entity.isSneaking()) {
            width = TLWidth;
            height = !flying ? TLHeight * 0.92F : TLHeight;
        } else if (entity.isElytraFlying()) {
            width = TLWidth;
            height = TLHeight * 0.2F;
        } else if (entity.isPlayerSleeping()) {
            width = 0.2F;
            height = 0.2F;
        } else {
            width = TLWidth;
            height = TLHeight;
        }

        width = MathHelper.clamp(roundSize(width), MIN_PLAYER_WIDTH, MAX_SIZE);
        height = MathHelper.clamp(roundSize(height), MIN_HEIGHT, MAX_SIZE);

        final double halfWidth = width / 2.0D;
        final AxisAlignedBB nextBox = new AxisAlignedBB(entity.posX - halfWidth, entity.posY, entity.posZ - halfWidth, entity.posX + halfWidth, entity.posY + height, entity.posZ + halfWidth);

        entity.width = width;
        entity.height = height;
        entity.setEntityBoundingBox(nextBox);
    }

    private static void setLivingSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }
        if (entity.isChild()) {
            return;
        }

        float width;
        float height;

        if (entity.isSneaking()) {
            width = TLWidth;
            height = TLHeight * 0.92F;
        } else if (entity.isElytraFlying()) {
            width = TLWidth;
            height = TLHeight * 0.2F;
        } else if (entity.isPlayerSleeping()) {
            width = 0.2F;
            height = 0.2F;
        } else {
            width = TLWidth;
            height = TLHeight;
        }

        width = MathHelper.clamp(roundSize(width), MIN_PLAYER_WIDTH, MAX_SIZE);
        height = MathHelper.clamp(roundSize(height), MIN_HEIGHT, MAX_SIZE);

        final double halfWidth = width / 2.0D;
        final AxisAlignedBB nextBox = new AxisAlignedBB(entity.posX - halfWidth, entity.posY, entity.posZ - halfWidth, entity.posX + halfWidth, entity.posY + height, entity.posZ + halfWidth);

        entity.width = width;
        entity.height = height;
        entity.setEntityBoundingBox(nextBox);
    }

    public static void setSize(EntityLivingBase entity, EntityProperties properties) {
        if (Trinkets.MOD_COMPAT.ArtemisLib && TrinketsConfig.compat.ARTEMIS_LIB) {
            return;
        }
        float height = properties.getRaceHandler().getHeight();
        float width = properties.getRaceHandler().getWidth();
        //		if (properties.getSize() < 100) {
        if (entity.isChild()) {
            return;
        }
        //		}

        setSize(entity, height, width);
    }

    private static float roundSize(float value) {
        return Math.round(value * SIZE_PRECISION) / SIZE_PRECISION;
    }
}
