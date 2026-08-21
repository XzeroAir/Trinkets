package xzeroair.trinkets.util.compat.iceandfire;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

public class IceAndFireCompat {

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.IceAndFire && TrinketsConfig.getClientStore().MOD_COMPAT_ICE_AND_FIRE;
    }

    private static final String MODID = "iceandfire";

    public static final String DRAGON_BREATH_DAMAGE_TYPE_FIRE = "dragon_fire";
    public static final String DRAGON_BREATH_DAMAGE_TYPE_ICE = "dragon_ice";
    public static final String DRAGON_BREATH_DAMAGE_TYPE_LIGHTNING = "dragon_lightning";
    public static final String LANG_BREATH_ICE = "tile.iceandfire.dragon_ice.name";
    public static final String LANG_BREATH_FIRE = "tile.iceandfire.dragon_fire.name";
    public static final String LANG_BREATH_LIGHTNING = "tile.iceandfire.dragon_lightning.name";

    public static final String PARALYSIS = "paralysis";


    public static Potion getPotionByName(String name) {
        if (isModEnabled()) {
            return PotionHelper.getModPotion(MODID, name);
        }
        return null;
    }

    public static boolean isParalysis(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffect(effect, MODID, PARALYSIS);
    }

    public static boolean isParalysisLazy(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffectLazy(effect, MODID, PARALYSIS);
    }

    public static boolean isDragonLightningDamage(DamageSource source) {
        return isModEnabled() && source != null && source.damageType.contentEquals(DRAGON_BREATH_DAMAGE_TYPE_LIGHTNING);
    }

    public static boolean isDragonIceDamage(DamageSource source) {
        return isModEnabled() && source != null && source.damageType.contentEquals(DRAGON_BREATH_DAMAGE_TYPE_ICE);
    }

}