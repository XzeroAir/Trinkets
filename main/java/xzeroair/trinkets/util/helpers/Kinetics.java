package xzeroair.trinkets.util.helpers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class Kinetics {

    public static void applyChargedPush(@Nonnull EntityPlayer player, int chargeTicks, int maxChargeTicks, double minVelocity, double maxVelocity) {

        // Clamp charge
        int clamped = Math.min(chargeTicks, maxChargeTicks);

        // Normalize to 0.0 → 1.0
        double t = (double) clamped / (double) maxChargeTicks;

        // Optional easing (makes it feel better than linear)
        // Try: t = t * t;            // ease-in
        // Or:  t = 1 - Math.pow(1 - t, 2); // ease-out
        t = t * t;

        // Interpolate velocity
        double velocity = minVelocity + (maxVelocity - minVelocity) * t;

//        Vec3d look = player.getLookVec();
        Vec3d look = player.getLookVec();
        look = new Vec3d(look.x, 0, look.z).normalize(); // horizontal only

        player.motionX += look.x * velocity;
        player.motionY += look.y * velocity;
//        player.motionY += look.y * velocity * 0.5D;
        player.motionZ += look.z * velocity;
//        player.motionX = look.x * velocity;
//        player.motionY = look.y * velocity;
//        player.motionZ = look.z * velocity;

        player.velocityChanged = true;
    }

    public static void applyForce(@Nonnull Entity origin, @Nonnull Entity targetEntity, boolean isPull, double force, double maxSpeed, double damping) {
        Vec3d from = targetEntity.getPositionVector();
        Vec3d to = origin.getPositionVector();

        Vec3d delta = to.subtract(from);
        double distance = delta.length();

        if ((distance > 0.001D && (isPull ? distance < 10D : distance < (origin.width + 3D)))) {
            Vec3d dir = delta.normalize();

//            double force = CONFIG.FORCE;
//            double maxSpeed = 0.8D;
//            double damping = 0.85D;
            if (isPull) {
                double scaledForce = force * Math.max(distance, 2.0D);

                targetEntity.motionX += dir.x * scaledForce;
                targetEntity.motionY += dir.y * scaledForce;
                targetEntity.motionZ += dir.z * scaledForce;

                targetEntity.motionX *= damping;
                targetEntity.motionY *= damping;
                targetEntity.motionZ *= damping;

            } else {
                Vec3d pushDir = from.subtract(to).normalize();

                double speed = Math.min(force * 4.0D, maxSpeed);

                // Overwrite motion to fully redirect
                targetEntity.motionX = pushDir.x * speed;
                targetEntity.motionY = pushDir.y * speed;
                targetEntity.motionZ = pushDir.z * speed;

                if (true) {
                    double horiz = MathHelper.sqrt(targetEntity.motionX * targetEntity.motionX + targetEntity.motionZ * targetEntity.motionZ);

                    if (horiz > 0.0001D) {
                        float yaw = (float) (MathHelper.atan2(targetEntity.motionX, targetEntity.motionZ) * (180D / Math.PI));
                        float pitch = (float) (MathHelper.atan2(targetEntity.motionY, horiz) * (180D / Math.PI));

                        targetEntity.rotationYaw = yaw;
                        targetEntity.rotationPitch = pitch;
                        targetEntity.prevRotationYaw = yaw;
                        targetEntity.prevRotationPitch = pitch;
                    }
                }
            }

            // Clamp velocity
            double speed = Math.sqrt(targetEntity.motionX * targetEntity.motionX + targetEntity.motionY * targetEntity.motionY + targetEntity.motionZ * targetEntity.motionZ);

            if (speed > maxSpeed) {
                double scale = maxSpeed / speed;
                targetEntity.motionX *= scale;
                targetEntity.motionY *= scale;
                targetEntity.motionZ *= scale;
            }

            targetEntity.velocityChanged = true;
        }
    }


    public static void pull(@Nonnull Entity ent, double x, double y, double z, double speed) {
        final double dX = (x - 0.5) - ent.posX;
        final double dY = y - ent.posY;
        final double dZ = (z - 0.5) - ent.posZ;
        final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

        double vel = 1.0 - (dist / 15.0);
        if ((vel > 0.0D) && (vel < 0.95D)) {
            vel *= vel;
            ent.motionX += (dX / dist) * vel * (speed * MathHelper.clamp(dist - 0.5, 0, 1));
            ent.motionY += (dY / dist) * vel * ((speed * 1.25) * MathHelper.clamp(dist - 0.5, 0, 1));
            ent.motionZ += (dZ / dist) * vel * (speed * MathHelper.clamp(dist - 0.5, 0, 1));
            ent.velocityChanged = true;
        }
    }

    public static void push(@Nonnull Entity ent, double x, double y, double z, double speed) {
        final double dX = x - ent.posX;
        final double dY = y - ent.posY;
        final double dZ = z - ent.posZ;
        final double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

        double vel = 1.0 - (dist / 15.0);
        if (vel > 0.0D) {
            vel *= vel;
            ent.motionX -= (dX / dist) * vel * speed;
            ent.motionY -= (dY / dist) * vel * speed;
            ent.motionZ -= (dZ / dist) * vel * speed;
            ent.velocityChanged = true;
        }
    }
}
