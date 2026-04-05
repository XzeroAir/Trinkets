package xzeroair.trinkets.util.compat.toughasnails;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import toughasnails.api.TANPotions;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;
import toughasnails.api.thirst.ThirstHelper;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class TANCompat {

    private static boolean getTanEnabled() {
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        return tanEnabled;
    }

    public static void addThirst(EntityPlayer player, int amount, int saturation) {
        if (getTanEnabled()) {
            final IThirst thirst = ThirstHelper.getThirstData(player);
            thirst.addStats(amount, saturation);
        }
    }

    public static void ClearTempurature(EntityPlayer player) {
        if (getTanEnabled()) {
            final ITemperature temp = TemperatureHelper.getTemperatureData(player);
            temp.setTemperature(new Temperature(TemperatureScale.getScaleMidpoint()));
        }
    }

    public static void immuneToHeat(EntityPlayer player) {
        if (getTanEnabled()) {
            final ITemperature temp = TemperatureHelper.getTemperatureData(player);
            if (temp.getTemperature().getRange() == TemperatureRange.HOT) {
                temp.setTemperature(new Temperature(19));
            }
        }
    }

    public static void immuneToCold(EntityPlayer player) {
        if (getTanEnabled()) {
            final ITemperature temp = TemperatureHelper.getTemperatureData(player);
            if (temp.getTemperature().getRange() == TemperatureRange.ICY) {
                temp.setTemperature(new Temperature(6));
            }

        }
    }

    public static void clearThirst(EntityLivingBase entity) {
        if (getTanEnabled()) {
            if (entity.isPotionActive(TANPotions.thirst)) {
                entity.removePotionEffect(TANPotions.thirst);
            }
        }
    }

    public static Potion getTANThirst() {
        return TANPotions.thirst;
    }

    public static Potion getTANHypothermia() {
        return TANPotions.hypothermia;
    }

    public static Potion getTANHyperthermia() {
        return TANPotions.hyperthermia;
    }

}
