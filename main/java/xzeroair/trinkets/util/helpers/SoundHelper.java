package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class SoundHelper {

    private SoundHelper() {
    }

    public static void playSound(Entity entity, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        playSound(entity, entity.world, entity.posX, entity.posY, entity.posZ, sound, category, volume, pitch);
    }

    public static void playSound(Entity entity, World world, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (world == null) {
            return;
        }
        if (entity == null || !((entity instanceof EntityPlayer))) {
            world.playSound((EntityPlayer) null, x, y, z, sound, category, volume, pitch);
        }

        if (entity instanceof EntityPlayer && world.isRemote) {
            world.playSound((EntityPlayer) entity, x, y, z, sound, category, volume, pitch);
        } else {
            world.playSound((EntityPlayer) null, x, y, z, sound, category, volume, pitch);
        }
    }

    public static void playSound(World world, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        playSound(null, world, x, y, z, sound, category, volume, pitch);
    }

    public static void playSound(World world, BlockPos position, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        world.playSound((EntityPlayer) null, position, sound, category, volume, pitch);
    }
}
