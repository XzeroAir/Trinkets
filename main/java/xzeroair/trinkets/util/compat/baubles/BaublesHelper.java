package xzeroair.trinkets.util.compat.baubles;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BaublesHelper {

    private static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.Baubles;
    }

    public static ItemStack getBaubleInSlot(EntityLivingBase entity, final int slot) {
        if (slot < 0) {
            return ItemStack.EMPTY;
        }
        return getBaublesHandler(entity, ItemStack.EMPTY, (baubles, rtn) -> slot < baubles.getSlots() ? baubles.getStackInSlot(slot) : rtn);
    }

    @Nullable
    public static IBaublesItemHandler getBaublesHandler(EntityLivingBase entity) {
        if (!isModEnabled()) {
            return null;
        }
        return Capabilities.getCapabilityWithConsumer(entity, BaublesCapabilities.CAPABILITY_BAUBLES, handler -> handler.setPlayer(entity));
    }

    @Nullable
    public static IBaublesItemHandler getBaublesHandler(EntityLivingBase entity, Consumer<IBaublesItemHandler> consumer) {
        if (!isModEnabled()) {
            return null;
        }
        return Capabilities.getCapabilityWithConsumer(entity, BaublesCapabilities.CAPABILITY_BAUBLES, handler -> {
            handler.setPlayer(entity);
            if (consumer != null) {
                consumer.accept(handler);
            }
        });
    }

    public static <R> R getBaublesHandler(EntityLivingBase entity, R ret, BiFunction<IBaublesItemHandler, R, R> func) {
        if (!isModEnabled()) {
            return ret;
        }
        return Capabilities.getCapabilityWithReturn(entity, BaublesCapabilities.CAPABILITY_BAUBLES, ret, (handler, rtn) -> {
            handler.setPlayer(entity);
            return func.apply(handler, rtn);
        });
    }

    public static boolean baubleCheck(EntityLivingBase entity, Item item) {
        return !getBaubleStack(entity, item).isEmpty();
    }

    public static ItemStack getBaubleStack(EntityLivingBase entity, Item item) {
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return getBaubleStack(entity, stack -> !stack.isEmpty() && stack.getItem().getRegistryName().toString().contentEquals(item.getRegistryName().toString()));
    }

    public static ItemStack getBaubleStack(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (predicate == null) {
            return ItemStack.EMPTY;
        }
        return getBaublesHandler(entity, ItemStack.EMPTY, (baubles, rtn) -> {
            for (int i = 0; i < baubles.getSlots(); i++) {
                final ItemStack stack = baubles.getStackInSlot(i);
                if (predicate.test(stack)) {
                    return stack;
                }
            }
            return rtn;
        });
    }

    public static void applyToBaubles(EntityLivingBase entity, Consumer<ItemStack> consumer) {
        if (consumer == null) {
            return;
        }
        getBaublesHandler(entity, handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                final ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    consumer.accept(stack);
                }
            }
        });
    }

    public static int countBaubles(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (predicate == null) {
            return 0;
        }
        return getBaublesHandler(entity, 0, (baubles, count) -> {
            for (int i = 0; i < baubles.getSlots(); i++) {
                final ItemStack stack = baubles.getStackInSlot(i);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    count++;
                }
            }
            return count;
        });
    }

    @Nullable
    public static SlotInformation getBaubleSlotInformation(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        if (predicate == null) {
            return null;
        }
        return getBaublesHandler(entity, null, (baubles, rtn) -> {
            for (int i = 0; i < baubles.getSlots(); i++) {
                final ItemStack stack = baubles.getStackInSlot(i);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    return new SlotInformation(stack, ItemHandlerType.BAUBLES, i);
                }
            }
            return rtn;
        });
    }

    public static List<SlotInformation> getSlotInfoForBaubles(EntityLivingBase entity, Predicate<ItemStack> predicate) {
        final List<SlotInformation> list = new ArrayList<>();
        if (predicate == null) {
            return list;
        }
        getBaublesHandler(entity, handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                final ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty() && predicate.test(stack)) {
                    list.add(new SlotInformation(stack, ItemHandlerType.BAUBLES, i));
                }
            }
        });
        return list;
    }

    public static BaubleType getBaubleType() {
        return getBaubleType("");
    }

    public static BaubleType getBaubleType(String string) {
        if (string == null || string.isEmpty()) {
            return BaubleType.TRINKET;
        }
        string = string.toLowerCase();
        switch (string) {
            case "amulet":
            case "necklace":
            case "pendant":
                return BaubleType.AMULET;
            case "ring":
            case "rings":
                return BaubleType.RING;
            case "belt":
            case "waist":
                return BaubleType.BELT;
            case "head":
            case "hat":
                return BaubleType.HEAD;
            case "body":
            case "chest":
                return BaubleType.BODY;
            case "charm":
                return BaubleType.CHARM;
            default:
                return BaubleType.TRINKET;
        }
    }
}
