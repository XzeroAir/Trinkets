package xzeroair.trinkets.util.compat.lycanitesmobs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

public class LycanitesCompat {

    private static final String MODID = "lycanitesmobs";

    public static final String paralysis = "paralysis";
    public static final String penetration = "penetration";
    public static final String weight = "weight";
    public static final String fear = "fear";
    public static final String decay = "decay";
    public static final String insomnia = "insomnia";
    public static final String instability = "instability";
    public static final String lifeleak = "lifeleak";
    public static final String bleed = "bleed";
    public static final String plague = "plague";
    public static final String aphagia = "aphagia";
    public static final String smited = "smited";
    public static final String smouldering = "smouldering";
    public static final String leech = "leech";
    public static final String swiftswimming = "swiftswimming";
    public static final String fallresist = "fallresist";
    public static final String rejuvenation = "rejuvenation";
    public static final String immunization = "immunization";
    public static final String cleansed = "cleansed";
    public static final String repulsion = "repulsion";

    public static final String DAMAGE_TYPE_OOZE = "ooze";
    public static final String DAMAGE_TYPE_COLD_FIRE = "cold_fire";

    public static final String LANG_DAMAGE_COLD_FIRE = "tile.icefire.name";
    public static final String LANG_DAMAGE_OOZE = "tile.ooze.name";
    public static final String LANG_DAMAGE_SMOULDERING = "effect." + smouldering;
    public static final String LANG_DAMAGE_INSTABILITY = "effect." + instability;
    public static final TextComponentTranslation SMOULDERING = new TextComponentTranslation(LANG_DAMAGE_SMOULDERING);
    public static final TextComponentTranslation INSTABILITY = new TextComponentTranslation(LANG_DAMAGE_INSTABILITY);

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.LYCANITES_MOBS;
    }

    public static void removeParalysis(EntityLivingBase entity) {
        removeEffect(entity, paralysis);
    }

    public static void removePenetration(EntityLivingBase entity) {
        removeEffect(entity, penetration);
    }

    public static void removeWeight(EntityLivingBase entity) {
        removeEffect(entity, weight);
    }

    public static void removeFear(EntityLivingBase entity) {
        removeEffect(entity, fear);
    }

    public static void removeDecay(EntityLivingBase entity) {
        removeEffect(entity, decay);
    }

    public static void removeInsomnia(EntityLivingBase entity) {
        removeEffect(entity, insomnia);
    }

    public static void removeInstability(EntityLivingBase entity) {
        removeEffect(entity, instability);
    }

    public static void removeLifeleak(EntityLivingBase entity) {
        removeEffect(entity, lifeleak);
    }

    public static void removeBleed(EntityLivingBase entity) {
        removeEffect(entity, bleed);
    }

    public static void removePlague(EntityLivingBase entity) {
        removeEffect(entity, plague);
    }

    public static void removeAphagia(EntityLivingBase entity) {
        removeEffect(entity, aphagia);
    }

    public static void removeSmited(EntityLivingBase entity) {
        removeEffect(entity, smited);
    }

    public static void removeLeech(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, leech);
    }

    public static void removeSwiftSwimming(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, swiftswimming);
    }

    public static void removeFallResist(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, fallresist);
    }

    public static void removeRejuvenation(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, rejuvenation);
    }

    public static void removeImmunization(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, immunization);
    }

    public static void removeCleansed(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, cleansed);
    }

    public static void removeRepulsion(EntityLivingBase entity) {
        // Good Effect
        removeEffect(entity, repulsion);
    }

    public static void applyEffect(EntityLivingBase entity, String name, int duration, int amplifier) {
        if (isModEnabled()) {
            try {
                Potion effect = Potion.getPotionFromResourceLocation(MODID + ":" + name);
                if ((effect != null) && !entity.isPotionActive(effect)) {
                    entity.addPotionEffect(new PotionEffect(effect, duration, amplifier, false, false));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeEffect(EntityLivingBase entity, String name) {
        if (isModEnabled()) {
            try {
                Potion effect = Potion.getPotionFromResourceLocation(MODID + ":" + name);
                if ((effect != null) && entity.isPotionActive(effect)) {
                    entity.removePotionEffect(effect);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Potion getPotionByName(String name) {
        if (isModEnabled()) {
            return PotionHelper.getModPotion(MODID, name);
        }
        return null;
    }

    public static boolean isWeight(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffect(effect, MODID, weight);
    }

    public static boolean isWeightLazy(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffectLazy(effect, MODID, weight);
    }

    public static boolean isSmouldering(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffect(effect, MODID, smouldering);
    }

    public static boolean isSmoulderingLazy(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffectLazy(effect, MODID, smouldering);
    }

    public static boolean isParalysis(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffect(effect, MODID, paralysis);
    }

    public static boolean isParalysisLazy(final PotionEffect effect) {
        return isModEnabled() && PotionHelper.isPotionEffectLazy(effect, MODID, paralysis);
    }

    public static boolean isOozeDamage(DamageSource source) {
        return isModEnabled() && source != null && source.damageType.contentEquals(DAMAGE_TYPE_OOZE);
    }

    public static boolean isColdFireDamage(DamageSource source) {
        return isModEnabled() && source != null && source.damageType.contentEquals(DAMAGE_TYPE_COLD_FIRE);
    }

    public static void convertManaToSpirit(EntityLivingBase entity) {
        //		if (Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.lycanites) {
        //			if ((entity == null) || !(entity instanceof EntityPlayer)) {
        //				return;
        //			}
        //			try {
        //				ExtendedPlayer cap = ExtendedPlayer.getForPlayer((EntityPlayer) entity);
        //				int spirit = ExtendedPlayer.getForPlayer((EntityPlayer) entity).summonFocus;
        //				if (spirit < cap.summonFocusMax) {
        //					System.out.println(spirit);
        //					EntityProperties prop = Capabilities.getEntityRace(entity);
        //					if (prop.getMagic().spendMana(1f)) {
        //						if ((cap.summonFocus + 25) < cap.summonFocusMax) {
        //							cap.summonFocus = cap.summonFocus + 25;
        //						} else {
        //							cap.summonFocus = cap.summonFocusMax;
        //						}
        //					}
        //				}
        //				//TODO Lycanite Thing Shiv Requested
        //
        //			} catch (Exception e) {
        //				e.printStackTrace();
        //			}
        //		}
    }

}
