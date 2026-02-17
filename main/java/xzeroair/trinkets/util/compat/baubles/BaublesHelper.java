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

    public static BaubleType getBaubleType(String string) {
        string = string.toLowerCase();
        //		if(string.contentEquals("trinket") || string.contentEquals("any") || string.contentEquals("all")) {
        //			return BaubleType.TRINKET;
        //		}
        //		else
        if (string.contentEquals("amulet") || string.contentEquals("necklace") || string.contentEquals("pendant")) {
            return BaubleType.AMULET;
        } else if (string.contentEquals("ring") || string.contentEquals("rings")) {
            return BaubleType.RING;
        } else if (string.contentEquals("belt")) {
            return BaubleType.BELT;
        } else if (string.contentEquals("head") || string.contentEquals("hat")) {
            return BaubleType.HEAD;
        } else if (string.contentEquals("body") || string.contentEquals("chest")) {
            return BaubleType.BODY;
        } else if (string.contentEquals("charm")) {
            return BaubleType.CHARM;
        } else {
            return BaubleType.TRINKET;
        }
    }

}
