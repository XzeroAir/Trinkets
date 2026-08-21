package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import xzeroair.trinkets.Trinkets;

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
                if (!hasTagCompound(tag, EntityPlayer.PERSISTED_NBT_TAG)) {
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

    public static boolean hasKey(@Nullable NBTTagCompound tag, @Nullable String key, int expectedType) {
        if (tag == null || tag.isEmpty() || key == null || key.isEmpty() || !tag.hasKey(key)) {
            return false;
        }
        if (tag.hasKey(key, expectedType)) {
            return true;
        }
        final int actualType = tag.getTagId(key);
        Trinkets.LOGGER.warn("NBT type mismatch for key `{}`: expected {} ({}), found {} ({}). Value ignored.", key, getTypeName(expectedType), expectedType, getTypeName(actualType), actualType);
        return false;
    }

    private static String getTypeName(int type) {
        if (type == Constants.NBT.TAG_ANY_NUMERIC) {
            return "ANY_NUMERIC";
        }
        if (type >= Constants.NBT.TAG_END && type < NBTBase.NBT_TYPES.length) {
            return NBTBase.NBT_TYPES[type];
        }
        return "UNKNOWN";
    }

    public static boolean hasTagCompound(@Nullable NBTTagCompound tag, @Nullable String key) {
        return hasKey(tag, key, Constants.NBT.TAG_COMPOUND);
    }

    public static boolean hasAnyNumeric(@Nullable NBTTagCompound tag, @Nullable String key) {
        return hasKey(tag, key, Constants.NBT.TAG_ANY_NUMERIC);
    }

    public static boolean hasBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key) {
        if (hasKey(tag, key, Constants.NBT.TAG_BYTE)) {
            return tag.getBoolean(key);
        }
        return false;
    }

    public static void hasBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Boolean> consumer) {
        if (hasKey(tag, key, Constants.NBT.TAG_BYTE) && consumer != null) {
            consumer.accept(tag.getBoolean(key));
        }
    }

    public static boolean getBoolean(@Nonnull NBTTagCompound tag, @Nonnull String key, boolean defaultReturn) {
        if (hasKey(tag, key, Constants.NBT.TAG_BYTE)) {
            return tag.getBoolean(key);
        }
        return defaultReturn;
    }


    public static String getString(@Nonnull NBTTagCompound tag, @Nonnull String key, String defaultReturn) {
        if (hasKey(tag, key, Constants.NBT.TAG_STRING)) {
            return tag.getString(key);
        }
        return defaultReturn;
    }

    public static void hasString(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<String> consumer) {
        if (hasKey(tag, key, Constants.NBT.TAG_STRING) && consumer != null) {
            consumer.accept(tag.getString(key));
        }
    }

    public static int getInteger(@Nonnull NBTTagCompound tag, @Nonnull String key, int defaultReturn) {
        if (hasKey(tag, key, Constants.NBT.TAG_INT)) {
            return tag.getInteger(key);
        }
        return defaultReturn;
    }

    public static void hasInteger(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Integer> consumer) {
        if (hasKey(tag, key, Constants.NBT.TAG_INT) && consumer != null) {
            consumer.accept(tag.getInteger(key));
        }
    }

    public static float getFloat(@Nonnull NBTTagCompound tag, @Nonnull String key, float defaultReturn) {
        if (hasKey(tag, key, Constants.NBT.TAG_FLOAT)) {
            return tag.getFloat(key);
        }
        return defaultReturn;
    }

    public static void hasFloat(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Float> consumer) {
        if (hasKey(tag, key, Constants.NBT.TAG_FLOAT) && consumer != null) {
            consumer.accept(tag.getFloat(key));
        }
    }

    public static double getDouble(@Nonnull NBTTagCompound tag, @Nonnull String key, double defaultReturn) {
        if (hasKey(tag, key, Constants.NBT.TAG_DOUBLE)) {
            return tag.getDouble(key);
        }
        return defaultReturn;
    }

    public static void hasDouble(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<Double> consumer) {
        if (hasKey(tag, key, Constants.NBT.TAG_DOUBLE) && consumer != null) {
            consumer.accept(tag.getDouble(key));
        }
    }

    @Nonnull
    public static NBTTagCompound getTag(@Nonnull NBTTagCompound tag, @Nonnull String key) {
        if (hasTagCompound(tag, key)) {
            return tag.getCompoundTag(key);
        }
        return new NBTTagCompound();
    }

    public static void hasTag(@Nonnull NBTTagCompound tag, @Nonnull String key, @Nullable Consumer<NBTTagCompound> consumer) {
        if (hasTagCompound(tag, key) && consumer != null) {
            consumer.accept(tag.getCompoundTag(key));
        }
    }


}
