package xzeroair.trinkets.util.compat.simpledifficulty;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.SDPotions;
import com.charles445.simpledifficulty.api.temperature.ITemperatureCapability;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class SDCompat {

    private static boolean getSDEnabled() {
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        return sdEnabled;
    }

    public static void addThirst(EntityPlayer player, int amount, int saturation) {
        if (getSDEnabled()) {
            final IThirstCapability thirst = SDCapabilities.getThirstData(player);
            thirst.addThirstLevel(amount);
            thirst.addThirstSaturation(saturation);
        }
    }

    public static void ClearTempurature(EntityPlayer player) {
        if (getSDEnabled()) {
            final ITemperatureCapability temp = SDCapabilities.getTemperatureData(player);
            temp.setTemperatureLevel(12);
            if (player.isPotionActive(SDPotions.hyperthermia)) {
                player.removePotionEffect(SDPotions.hyperthermia);
            }
            if (player.isPotionActive(SDPotions.hypothermia)) {
                player.removePotionEffect(SDPotions.hypothermia);
            }
        }
    }

    public static void immuneToHeat(EntityPlayer player) {
        if (getSDEnabled()) {
            final ITemperatureCapability temp = SDCapabilities.getTemperatureData(player);
            if (temp.getTemperatureLevel() > 14) {
                temp.setTemperatureLevel(14);
                if (player.isPotionActive(SDPotions.hyperthermia)) {
                    player.removePotionEffect(SDPotions.hyperthermia);
                }
            }
        }
    }

    public static void immuneToCold(EntityPlayer player) {
        if (getSDEnabled()) {
            final ITemperatureCapability temp = SDCapabilities.getTemperatureData(player);
            if (temp.getTemperatureLevel() < 11) {
                temp.setTemperatureLevel(11);
                if (player.isPotionActive(SDPotions.hypothermia)) {
                    player.removePotionEffect(SDPotions.hypothermia);
                }
            }
        }
    }

    public static void clearThirst(EntityLivingBase entity) {
        if (getSDEnabled()) {
            if (entity.isPotionActive(SDPotions.thirsty)) {
                entity.removePotionEffect(SDPotions.thirsty);
            }
        }
    }

    public static void clearParasites(EntityLivingBase entity) {
        if (getSDEnabled()) {
            if (entity.isPotionActive(SDPotions.parasites)) {
                entity.removePotionEffect(SDPotions.parasites);
            }
        }
    }

    public static Potion getSDThirst() {
        return SDPotions.thirsty;
    }

    public static Potion getSDParasites() {
        return SDPotions.parasites;
    }

    public static Potion getSDHypothermia() {
        return SDPotions.hypothermia;
    }

    public static Potion getSDHyperthermia() {
        return SDPotions.hyperthermia;
    }

}
