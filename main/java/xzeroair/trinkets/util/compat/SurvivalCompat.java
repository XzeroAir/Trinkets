package xzeroair.trinkets.util.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.util.FakePlayer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.compatibility.ModCompat;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.simpledifficulty.SDCompat;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;

import java.util.ArrayList;
import java.util.List;

public class SurvivalCompat {

    public static boolean isSurvivalModsActive() {
        return isSimpleDifficulty() || isToughAsNails();
    }

    public static boolean isSimpleDifficulty() {
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        return sdEnabled;
    }

    public static boolean isToughAsNails() {
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        return tanEnabled;
    }

    public static String getSurvivalMod() {
        if (isSimpleDifficulty()) {
            return ModCompat.ModNames.LANG_NAME_SIMPLE_DIFFICULTY;
        }
        if (isToughAsNails()) {
            return ModCompat.ModNames.LANG_NAME_TOUGH_AS_NAILS;
        }
        return "";
    }

    public static void addThirst(EntityLivingBase player, int amount, int saturation) {
        if (!(player instanceof EntityPlayer || player instanceof FakePlayer)) {
            return;
        }
        if (isSimpleDifficulty()) {
            try {
                SDCompat.addThirst((EntityPlayer) player, amount, saturation);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                TANCompat.addThirst((EntityPlayer) player, amount, saturation);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void ClearTempurature(EntityLivingBase player) {
        if (!(player instanceof EntityPlayer || player instanceof FakePlayer)) {
            return;
        }
        if (isSimpleDifficulty()) {
            try {
                SDCompat.ClearTempurature((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                TANCompat.ClearTempurature((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void immuneToHeat(EntityLivingBase player) {
        if (!(player instanceof EntityPlayer || player instanceof FakePlayer)) {
            return;
        }
        if (isSimpleDifficulty()) {
            try {
                SDCompat.immuneToHeat((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                TANCompat.immuneToHeat((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void immuneToCold(EntityLivingBase player) {
        if (!(player instanceof EntityPlayer || player instanceof FakePlayer)) {
            return;
        }
        if (isSimpleDifficulty()) {
            try {
                SDCompat.immuneToCold((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                TANCompat.immuneToCold((EntityPlayer) player);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearThirst(EntityLivingBase entity) {
        if (isSimpleDifficulty()) {
            try {
                SDCompat.clearThirst(entity);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                TANCompat.clearThirst(entity);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearParasites(EntityLivingBase entity) {
        if (isSimpleDifficulty()) {
            try {
                SDCompat.clearParasites(entity);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    static List<Potion> thirsts = new ArrayList();
    static List<Potion> cold = new ArrayList();
    static List<Potion> hot = new ArrayList();

    public static List<Potion> getThirstEffects() {
        if (isSimpleDifficulty()) {
            try {
                final Potion SDThirst = SDCompat.getSDThirst();
                if (!thirsts.contains(SDThirst)) {
                    thirsts.add(SDCompat.getSDThirst());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                final Potion TANThirst = TANCompat.getTANThirst();
                if (!thirsts.contains(TANThirst)) {
                    thirsts.add(TANCompat.getTANThirst());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return thirsts;
    }

    public static List<Potion> getHypothermiaEffects() {
        if (isSimpleDifficulty()) {
            try {
                final Potion SDHypo = SDCompat.getSDHypothermia();
                if (!cold.contains(SDHypo)) {
                    cold.add(SDCompat.getSDHypothermia());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                final Potion TANHypo = TANCompat.getTANHypothermia();
                if (!cold.contains(TANHypo)) {
                    cold.add(TANCompat.getTANHypothermia());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return cold;
    }

    public static List<Potion> getHyperthermiaEffects() {
        if (isSimpleDifficulty()) {
            try {
                final Potion SDHyper = SDCompat.getSDHyperthermia();
                if (!hot.contains(SDHyper)) {
                    hot.add(SDCompat.getSDHyperthermia());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (isToughAsNails()) {
            try {
                final Potion TANHyper = TANCompat.getTANHyperthermia();
                if (!hot.contains(TANHyper)) {
                    hot.add(TANCompat.getTANHyperthermia());
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return hot;
    }

    public static Potion getSDParasitesPotionEffect() {
        if (isSimpleDifficulty()) {
            try {
                return SDCompat.getSDParasites();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
