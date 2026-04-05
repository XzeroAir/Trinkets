package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class NBTHelper {

    @Nonnull
    public static NBTTagCompound getEntityTag(@Nonnull Entity entity) {
        return getEntityTag(entity, new NBTTagCompound());
    }

    public static NBTTagCompound getEntityTag(@Nonnull Entity entity, NBTTagCompound returnTag) {
        NBTTagCompound tag = entity.getEntityData();
        if (tag != null) {
            final NBTTagCompound persistentData;
            if (entity instanceof EntityPlayer) {
                if (!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                    tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
                }
                persistentData = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            } else {
                persistentData = tag;
            }
            return persistentData;
        }
        return returnTag;
    }

    @Nonnull
    public static NBTTagCompound getTagCompoundSafe(@Nonnull ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public static boolean hasKey(@Nullable NBTTagCompound tag, @Nullable String key) {
        return tag != null && !tag.isEmpty() && key != null && !key.isEmpty() && tag.hasKey(key);
    }

    public static boolean hasBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key) {
        if (hasKey(tag, key)) {
            return tag.getBoolean(key);
        }
        return false;
    }

    public static void hasBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Boolean> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getBoolean(key));
        }
    }

    public static boolean getBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key, boolean defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getBoolean(key);
        }
        return defaultReturn;
    }


    public static String getString(@Nonnull NBTTagCompound tag, @Nonnull String key, String defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getString(key);
        }
        return defaultReturn;
    }

    public static void hasString(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<String> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getString(key));
        }
    }

    public static int getInteger(@Nonnull NBTTagCompound tag, @Nonnull String key, int defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getInteger(key);
        }
        return defaultReturn;
    }

    public static void hasInteger(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Integer> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getInteger(key));
        }
    }

    public static float getFloat(@Nonnull NBTTagCompound tag, @Nonnull String key, float defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getFloat(key);
        }
        return defaultReturn;
    }

    public static void hasFloat(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Float> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getFloat(key));
        }
    }

    public static double getDouble(@Nonnull NBTTagCompound tag, @Nonnull String key, double defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getDouble(key);
        }
        return defaultReturn;
    }

    public static void hasDouble(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Double> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getDouble(key));
        }
    }

    @Nonnull
    public static NBTTagCompound getTag(@Nonnull NBTTagCompound tag, @Nonnull String key) {
        if (hasKey(tag, key)) {
            return tag.getCompoundTag(key);
        }
        return new NBTTagCompound();
    }

    public static void hasTag(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<NBTTagCompound> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getCompoundTag(key));
        }
    }


}
