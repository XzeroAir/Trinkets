package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.TrinketsDamageSource;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.Utils;
import xzeroair.trinkets.util.config.ConfigHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DamageTypeConfigParser {


    public static boolean parseDamageTypeConfig(DamageSource source, @Nonnull String... configs) {
        return parseDamageTypeConfig(source, 0F, configs);
    }

    public static boolean parseDamageTypeConfig(DamageSource source, float dmg, @Nonnull String... configs) {
        for (String config : configs) {
            if (parseDamageType(source, dmg, config)) {
                return true;
            }
        }
        return false;
    }


    public static boolean parseDamageType(DamageSource source, String config) {
        return parseDamageType(source, 0F, config);
    }

    public static Utils.TempCache<Boolean, Float> parseDamageTypeConfig(int event, DamageSource source, float dmg, Element element, @Nonnull String... configs) {
        final String replaceEleRegex = "(ele(.*?):)";
        for (String config : configs) {
            if (event == 0 && config.startsWith("onAttacked:")) {
                String[] array = config.replaceFirst("onAttacked:", "").split(replaceEleRegex, 2);
                String myArrayString = array[0];
                String eleString = "";
                if (array.length > 1) {
                    myArrayString = array[1];
                    eleString = array[0];
                }
                Utils.TempCache<Boolean, Float> result = parseDamageTypeMultiplier(source, dmg, myArrayString.replaceFirst(replaceEleRegex, ""));
                if (eleString.isEmpty() ? result.getFirst() : result.getFirst() && compareElement(eleString, element)) {
                    return result;
                }
            } else if (event == 1 && config.startsWith("onHurt:")) {
                String[] array = config.replaceFirst("onHurt:", "").split(replaceEleRegex, 2);
                String myArrayString = array[0];
                String eleString = "";
                if (array.length > 1) {
                    myArrayString = array[1];
                    eleString = array[0];
                }
                Utils.TempCache<Boolean, Float> result = parseDamageTypeMultiplier(source, dmg, myArrayString.replaceFirst(replaceEleRegex, ""));
                if (eleString.isEmpty() ? result.getFirst() : result.getFirst() && compareElement(eleString, element)) {
                    return result;
                }
            } else if (event == 2 && config.startsWith("onDamaged:")) {
                String[] array = config.replaceFirst("onDamaged:", "").split(replaceEleRegex, 2);
                String myArrayString = array[0];
                String eleString = "";
                if (array.length > 1) {
                    myArrayString = array[1];
                    eleString = array[0];
                }
                Utils.TempCache<Boolean, Float> result = parseDamageTypeMultiplier(source, dmg, myArrayString.replaceFirst(replaceEleRegex, ""));
                if (eleString.isEmpty() ? result.getFirst() : result.getFirst() && compareElement(eleString, element)) {
                    return result;
                }
            } else if (!config.startsWith("onDamaged:") && !config.startsWith("onHurt:") && !config.startsWith("onAttacked:")) {
                String[] array = config.split(replaceEleRegex, 2);
                String myArrayString = array[0];
                String eleString = "";
                if (array.length > 1) {
                    myArrayString = array[1];
                    eleString = array[0];
                }
                Utils.TempCache<Boolean, Float> result = parseDamageTypeMultiplier(source, dmg, myArrayString.replaceFirst(replaceEleRegex, ""));
                if (eleString.isEmpty() ? result.getFirst() : result.getFirst() && compareElement(eleString, element)) {
                    return result;
                }
            }
        }
        return fail();
    }

    public static Utils.TempCache<Boolean, Float> parseDamageTypeMultiplier(DamageSource source, float dmg, String config) {
        if (source == null || config.isEmpty()) {
            return fail();
        }

        String string = ConfigHelper.cleanConfigEntry(config);

        if (!string.contains(";")) {
            return new Utils.TempCache<>(string.matches(source.getDamageType()), 0.0F);
        }

        String[] args = string.split(";");
        String arg0 = get(args, 0);

        // Step 1: damage type match
        if (!(arg0.equals("*") || arg0.matches(source.getDamageType()))) {
            return fail();
        }

        int index = 1;

        // Step 2: try damage check OR type/entity
        if (index < args.length) {
            String arg = args[index];
            float dmgLimit = StringUtils.getFloat(arg.replaceFirst("isMin:", ""));

            // ---- DAMAGE CHECK PATH ----
            if (dmgLimit > 0) {
                if (!matchesDamage(dmg, dmgLimit)) {
                    return fail();
                }
                index++;
                return successWithOptionalMultiplier(args, index);
            }

            // ---- TYPE CHECK PATH ----
            if (parseDamageTypeMatchesType(source, arg)) {
                index++;
                return handlePostType(source, dmg, args, index);
            }

            // ---- ENTITY CHECK PATH ----
            if (matchesEntity(source, arg)) {
                index++;
                return handlePostEntity(source, dmg, args, index);
            }

            return fail();
        }
        return successDefault();
    }

    public static boolean parseDamageType(DamageSource source, float dmg, String config) {
        if (source != null && !config.isEmpty()) {
            final String string = ConfigHelper.cleanConfigEntry(config);
            if (string.contains(";")) {
                final String[] array = string.split(";");
                final String arg1 = StringUtils.getStringFromArray(array, 0);
                final boolean matches = arg1.contentEquals("*") || arg1.matches(source.getDamageType());
                if (matches && array.length > 1) {
                    final String arg2 = StringUtils.getStringFromArray(array, 1).replace("isMin:", "");
                    final boolean matchesType = parseDamageTypeMatchesType(source, arg2);
                    float cfgDmg = StringUtils.getFloat(arg2);
                    final Entity attacker;
                    if (arg2.startsWith("isTrue:")) {
                        attacker = source.getTrueSource();
                    } else {
                        attacker = source.getImmediateSource();
                    }
                    if (matchesType && array.length > 2) {
                        final boolean matchesEntity = parseEntityByRegName(attacker, arg2);
                        if (matchesEntity && array.length > 3) {
                            final String arg3 = StringUtils.getStringFromArray(array, 2).replace("isMin:", "");
                            float cfgDmg2 = StringUtils.getFloat(arg3);
                            return (dmg > 0 && cfgDmg2 > 0 && dmg < cfgDmg2);
                        }
                        return matchesEntity || (dmg > 0 && cfgDmg > 0 && dmg < cfgDmg);
                    }
                    return matchesType || parseEntityByRegName(attacker, arg2) || (dmg > 0 && cfgDmg > 0 && dmg < cfgDmg);
                }
                return matches;
            } else {
                return string.matches(source.getDamageType());
            }
        }
        return false;
    }

    public static boolean parseEntityByRegName(@Nullable Entity entity, String entityid) {
        if (entity != null) {
            return StringUtils.parseResourceLocationSafely(EntityList.getKey(entity), entityid.replace("isTrue:", ""));
        }
        return false;
    }

    private static Utils.TempCache<Boolean, Float> handlePostType(DamageSource source, float dmg, String[] args, int index) {
        if (index >= args.length) return successDefault();

        String arg = get(args, index);
        float dmgLimit = StringUtils.getFloat(arg.replaceFirst("isMin:", ""));

        if (dmgLimit > 0) {
            if (!matchesDamage(dmg, dmgLimit)) return fail();
            return successWithOptionalMultiplier(args, index + 1);
        }

        if (matchesEntity(source, arg)) {
            return handlePostEntity(source, dmg, args, index + 1);
        }
        return fail();
    }

    private static Utils.TempCache<Boolean, Float> handlePostEntity(DamageSource source, float dmg, String[] args, int index) {
        if (index >= args.length) return successDefault();

        String arg = get(args, index);
        float dmgLimit = StringUtils.getFloat(arg.replaceFirst("isMin:", ""));

        if (dmgLimit > 0) {
            if (!matchesDamage(dmg, dmgLimit)) return fail();
            return successWithOptionalMultiplier(args, index + 1);
        }
        return fail();
    }

    private static boolean matchesDamage(float dmg, float limit) {
        return dmg > 0 && dmg < limit;
    }

    private static boolean matchesEntity(DamageSource source, @Nonnull String arg) {
        Entity attacker = arg.startsWith("isTrue:") ? source.getTrueSource() : source.getImmediateSource();

        String cleaned = arg.replaceFirst("isTrue:", "");
        return cleaned.equals("*") || parseEntityByRegName(attacker, cleaned);
    }

    private static Utils.TempCache<Boolean, Float> successWithOptionalMultiplier(@Nonnull String[] args, int index) {
        if (index < args.length) {
            float multi = StringUtils.getFloat(get(args, index));
            if (multi > 0) {
                return new Utils.TempCache<>(true, multi);
            }
            return fail();
        }
        return successDefault();
    }

    private static Utils.TempCache<Boolean, Float> successDefault() {
        return new Utils.TempCache<>(true, 0.0F);
    }

    private static Utils.TempCache<Boolean, Float> fail() {
        return new Utils.TempCache<>(false, 1.0F);
    }

    private static String get(String[] arr, int i) {
        return i < arr.length ? arr[i] : "";
    }

    public static boolean compareElement(@Nonnull String configElement, Element element) {
        switch (configElement) {
            case "eleFire":
                return element.equals(Elements.FIRE);
            case "eleIce":
                return element.equals(Elements.ICE);
            case "eleLightning":
                return element.equals(Elements.LIGHTNING);
            case "eleLight":
                return element.equals(Elements.LIGHT);
            case "eleDark":
                return element.equals(Elements.DARK);
            case "eleWater":
                return element.equals(Elements.WATER);
            case "eleEarth":
                return element.equals(Elements.EARTH);
            case "eleAir":
                return element.equals(Elements.AIR);
            case "elePoison":
                return element.equals(Elements.POISON);
            case "eleVoid":
                return element.equals(Elements.VOID);
            default:
                return false;
        }
    }

    public static <D extends DamageSource> boolean parseDamageTypeMatchesType(D source, String type) {
        if (source == null) {
            return false;
        }
        switch (type) {
            case "isFire":
                return source.isFireDamage() || isFireDamage(source.getDamageType());
            case "isMagic":
                return source.isMagicDamage();
            case "isExplosion":
                return source.isExplosion();
            case "isProjectile":
                return source.isProjectile();
            case "isAbsolute":
                return source.isDamageAbsolute();
            case "isUnblockable":
                return source.isUnblockable();
            case "isCreativePlayer":
                return source.isCreativePlayer();
            case "isBleed":
                return source instanceof TrinketsDamageSource && source.getDamageType().equals(TrinketsDamageSource.bleeding.getDamageType());
            case "isPoison":
                return source instanceof TrinketsDamageSource && source.getDamageType().equals(TrinketsDamageSource.poison.getDamageType()) || isPoisonDamage(source.getDamageType());
            case "isWater":
                return source instanceof TrinketsDamageSource && source.getDamageType().equals(TrinketsDamageSource.water.getDamageType()) || isWaterDamage(source.getDamageType());
            case "isIce":
                return isIceDamage(source.getDamageType());
            case "isLightning":
                return isLightningDamage(source.getDamageType());
            case "isLight":
                return isLightDamage(source.getDamageType());
            case "isDark":
                return isDarkDamage(source.getDamageType());
            case "isEarth":
                return isEarthDamage(source.getDamageType());
            case "isAir":
                return isAirDamage(source.getDamageType());
            case "isVoid":
                return isVoidDamage(source.getDamageType());
            case "isNeutral":
            default:
                return false;
        }
    }

    public static boolean isFireDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Fire_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIceDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Ice_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLightningDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Lightning_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLightDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Light_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDarkDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Dark_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVoidDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Void_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPoisonDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Poison_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWaterDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Water_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEarthDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Earth_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAirDamage(String string) {
        for (String damage : TrinketsConfig.SERVER.ELEMENTS.Air_Damage) {
            if (string.contentEquals(damage)) {
                return true;
            }
        }
        return false;
    }
}
