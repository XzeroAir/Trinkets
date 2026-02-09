package xzeroair.trinkets.util.helpers;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class NBTHelper {

    public static boolean hasKey(@Nullable NBTTagCompound tag, @Nullable String key) {
        return tag != null && !tag.isEmpty() && key != null && !key.isEmpty() && tag.hasKey(key);
    }

    public static boolean hasBoolean(@Nullable NBTTagCompound tag, @Nullable String key) {
        if (hasKey(tag, key)) {
            return tag.getBoolean(key);
        }
        return false;
    }

    public static void hasBoolean(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<Boolean> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            try {
                consumer.accept(tag.getBoolean(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getBoolean(@Nullable NBTTagCompound tag, @Nullable String key, boolean defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getBoolean(key);
        }
        return defaultReturn;
    }


    public static String getString(@Nullable NBTTagCompound tag, @Nullable String key, String defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getString(key);
        }
        return defaultReturn;
    }

    public static void hasString(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<String> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getString(key));
        }
    }

    public static int getInteger(@Nullable NBTTagCompound tag, @Nullable String key, int defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getInteger(key);
        }
        return defaultReturn;
    }

    public static void hasInteger(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<Integer> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getInteger(key));
        }
    }

    public static float getFloat(@Nullable NBTTagCompound tag, @Nullable String key, float defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getFloat(key);
        }
        return defaultReturn;
    }

    public static void hasFloat(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<Float> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getFloat(key));
        }
    }

    public static double getDouble(@Nullable NBTTagCompound tag, @Nullable String key, double defaultReturn) {
        if (hasKey(tag, key)) {
            return tag.getDouble(key);
        }
        return defaultReturn;
    }

    public static void hasDouble(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<Double> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getDouble(key));
        }
    }

    @Nonnull
    public static NBTTagCompound getTag(@Nullable NBTTagCompound tag, @Nullable String key) {
        if (hasKey(tag, key)) {
            return tag.getCompoundTag(key);
        }
        return new NBTTagCompound();
    }

    public static void hasTag(@Nullable NBTTagCompound tag, @Nullable String key, @Nullable Consumer<NBTTagCompound> consumer) {
        if (hasKey(tag, key) && consumer != null) {
            consumer.accept(tag.getCompoundTag(key));
        }
    }


}
