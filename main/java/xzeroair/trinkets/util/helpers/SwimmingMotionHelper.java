package xzeroair.trinkets.util.helpers;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xzeroair.trinkets.capabilities.race.KeybindHandler;

public final class SwimmingMotionHelper {

    public static final double BUOYANCY = 0.02D;
    public static final double NORMAL_BUOYANCY = 0.016D;

    private SwimmingMotionHelper() {
    }

    public static void apply(EntityLivingBase entity, KeybindHandler keybindHandler, boolean classicLookControls) {
        apply(entity, keybindHandler, classicLookControls, 1D);
    }

    public static void apply(EntityLivingBase entity, KeybindHandler keybindHandler, boolean classicLookControls, double thrustProgress) {
        final Vec3d look = entity.getLookVec();
        double strafe = 0D;
        if (keybindHandler.isMovementInputDown(KeybindHandler.keyEnum.LEFT)) {
            strafe++;
        }
        if (keybindHandler.isMovementInputDown(KeybindHandler.keyEnum.RIGHT)) {
            strafe--;
        }
        final double speedScale = 1D / Math.sqrt(1D + (strafe * strafe));
        final double swimmingSpeed = getSwimmingSpeed(entity) * thrustProgress * speedScale;
        double verticalMotion = BUOYANCY * thrustProgress;
        if (classicLookControls) {
            verticalMotion += MathHelper.clamp(look.y, -0.25D, 0.25D) * thrustProgress;
        } else {
            verticalMotion += look.y * swimmingSpeed;
        }
        if (keybindHandler.isMovementInputDown(KeybindHandler.keyEnum.JUMP)) {
            verticalMotion += swimmingSpeed;
        } else if (keybindHandler.isMovementInputDown(KeybindHandler.keyEnum.SNEAK)) {
            verticalMotion -= swimmingSpeed + (BUOYANCY * thrustProgress);
        }
        final double yaw = entity.rotationYaw * 0.017453292D;
        entity.motionX += (look.x * swimmingSpeed) + (strafe * Math.cos(yaw) * swimmingSpeed);
        entity.motionY += verticalMotion;
        entity.motionZ += (look.z * swimmingSpeed) + (strafe * Math.sin(yaw) * swimmingSpeed);
        entity.velocityChanged = true;
    }

    public static double getSwimmingSpeed(EntityLivingBase entity) {
        final IAttributeInstance movement = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        final IAttributeInstance swimMovement = entity.getEntityAttribute(EntityLivingBase.SWIM_SPEED);
        final double swimMultiplier = swimMovement == null ? 1D : swimMovement.getAttributeValue() * 0.2D;
        final double movementSpeed = movement == null ? 0.1D : movement.getBaseValue();
        final double swimSpeedBase = movementSpeed * Math.max(swimMultiplier, 0D);
        double swimmingSpeed = MathHelper.clamp(swimSpeedBase, swimSpeedBase * 0.01D, swimSpeedBase);
        final ItemStack feet = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        final int depthStrider = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, feet);
        if (depthStrider > 0) {
            swimmingSpeed += (swimSpeedBase * 0.667D) * depthStrider;
        }
        return swimmingSpeed;
    }
}
