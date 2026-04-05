package xzeroair.trinkets.util.compat.baubles;

import baubles.api.BaubleType;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.capabilities.Capabilities;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class BaublesHelper {

    public static ItemStack getBaubleInSlot(EntityLivingBase entity, final int slot) {
        return getBaublesHandler(entity, ItemStack.EMPTY, (baubles, rtn) -> {
            return baubles.getStackInSlot(slot);
        });
    }

    public static IBaublesItemHandler getBaublesHandler(EntityLivingBase entity) {
        return Capabilities.getCapabilityWithConsumer(entity, BaublesCapabilities.CAPABILITY_BAUBLES, (handler) -> handler.setPlayer(entity));
    }

    public static IBaublesItemHandler getBaublesHandler(EntityLivingBase entity, Consumer<IBaublesItemHandler> consumer) {
        return Capabilities.getCapabilityWithConsumer(entity, BaublesCapabilities.CAPABILITY_BAUBLES, (handler) -> {
            handler.setPlayer(entity);
            consumer.accept(handler);
        });
    }

    public static <R> R getBaublesHandler(EntityLivingBase entity, R ret, BiFunction<IBaublesItemHandler, R, R> func) {
        return Capabilities.getCapabilityWithReturn(entity, BaublesCapabilities.CAPABILITY_BAUBLES, ret, (handler, rtn) -> {
            handler.setPlayer(entity);
            return func.apply(handler, rtn);
        });
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
