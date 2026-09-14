package xzeroair.trinkets.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.ConstantsLang;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrinketsDamageSource extends EntityDamageSource {

    public static final TrinketsDamageSource poison = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.POISON).setIsPoisonDamage(true);
    public static final TrinketsDamageSource bleeding = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.BLEED);
    public static final TrinketsDamageSource water = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.WATER).setIsWaterDamage(true);
    public static final DamageSource linkedVitality = new DamageSource(TrinketsRegistryNames.ModDamageTypes.LINKED_VITALITY).setDamageBypassesArmor();

    private boolean isAirDamage;
    private boolean isDarkDamage;
    private boolean isEarthDamage;
    private boolean isIceDamage;
    private boolean isLightDamage;
    private boolean isLightningDamage;
    private boolean isPoisonDamage;
    private boolean isVoidDamage;
    private boolean isWaterDamage;

    public TrinketsDamageSource(String name) {
        this(name, null);
    }

    public TrinketsDamageSource(String name, @Nullable Entity damageSource) {
        super(name, damageSource);
        this.damageSourceEntity = damageSource;
    }

    public boolean isPoisonDamage() {
        return this.isPoisonDamage;
    }

    public TrinketsDamageSource setIsPoisonDamage(boolean isPoison) {
        this.isPoisonDamage = isPoison;
        return this;
    }

    public boolean isAirDamage() {
        return this.isAirDamage;
    }

    public TrinketsDamageSource setIsAirDamage(boolean isAir) {
        this.isAirDamage = isAir;
        return this;
    }

    public boolean isDarkDamage() {
        return this.isDarkDamage;
    }

    public TrinketsDamageSource setIsDarkDamage(boolean isDark) {
        this.isDarkDamage = isDark;
        return this;
    }

    public boolean isEarthDamage() {
        return this.isEarthDamage;
    }

    public TrinketsDamageSource setIsEarthDamage(boolean isEarth) {
        this.isEarthDamage = isEarth;
        return this;
    }

    public boolean isIceDamage() {
        return this.isIceDamage;
    }

    public TrinketsDamageSource setIsIceDamage(boolean isIce) {
        this.isIceDamage = isIce;
        return this;
    }

    public boolean isLightDamage() {
        return this.isLightDamage;
    }

    public TrinketsDamageSource setIsLightDamage(boolean isLight) {
        this.isLightDamage = isLight;
        return this;
    }

    public boolean isLightningDamage() {
        return this.isLightningDamage;
    }

    public TrinketsDamageSource setIsLightningDamage(boolean isLightning) {
        this.isLightningDamage = isLightning;
        return this;
    }

    public boolean isVoidDamage() {
        return this.isVoidDamage;
    }

    public TrinketsDamageSource setIsVoidDamage(boolean isVoid) {
        this.isVoidDamage = isVoid;
        return this;
    }

    public boolean isWaterDamage() {
        return this.isWaterDamage;
    }

    public TrinketsDamageSource setIsWaterDamage(boolean isWater) {
        this.isWaterDamage = isWater;
        return this;
    }

    public TrinketsDamageSource setDirectSource(Entity damageSource) {
        this.damageSourceEntity = damageSource;
        return this;
    }

    public static TrinketsDamageSource causeDamageFrom(String damageType, Entity damageSource) {
        return new TrinketsDamageSource(damageType, damageSource);
    }

    public static DamageSource causeElementDamage(Element element, Entity damageSource) {
        final String damageType;
        if (element == Elements.AIR) {
            damageType = TrinketsRegistryNames.ModDamageTypes.AIR;
        } else if (element == Elements.DARK) {
            damageType = TrinketsRegistryNames.ModDamageTypes.DARK;
        } else if (element == Elements.EARTH) {
            damageType = TrinketsRegistryNames.ModDamageTypes.EARTH;
        } else if (element == Elements.FIRE) {
            damageType = TrinketsRegistryNames.ModDamageTypes.FIRE;
        } else if (element == Elements.ICE) {
            damageType = TrinketsRegistryNames.ModDamageTypes.ICE;
        } else if (element == Elements.LIGHT) {
            damageType = TrinketsRegistryNames.ModDamageTypes.LIGHT;
        } else if (element == Elements.LIGHTNING) {
            damageType = TrinketsRegistryNames.ModDamageTypes.LIGHTNING;
        } else if (element == Elements.POISON) {
            damageType = TrinketsRegistryNames.ModDamageTypes.POISON;
        } else if (element == Elements.VOID) {
            damageType = TrinketsRegistryNames.ModDamageTypes.VOID;
        } else if (element == Elements.WATER) {
            damageType = TrinketsRegistryNames.ModDamageTypes.WATER;
        } else {
            return new EntityDamageSource(DamageSource.GENERIC.damageType, damageSource);
        }
        final TrinketsDamageSource source = causeDamageFrom(damageType, damageSource)
                .setIsAirDamage(element == Elements.AIR)
                .setIsDarkDamage(element == Elements.DARK)
                .setIsEarthDamage(element == Elements.EARTH)
                .setIsIceDamage(element == Elements.ICE)
                .setIsLightDamage(element == Elements.LIGHT)
                .setIsLightningDamage(element == Elements.LIGHTNING)
                .setIsPoisonDamage(element == Elements.POISON)
                .setIsVoidDamage(element == Elements.VOID)
                .setIsWaterDamage(element == Elements.WATER);
        if (element == Elements.FIRE) {
            source.setFireDamage();
        }
        return source;
    }

    @Override
    public ITextComponent getDeathMessage(@Nonnull EntityLivingBase entity) {
        final ITextComponent source = new TextComponentTranslation(ConstantsLang.DAMAGE_TYPE + "." + this.damageType);
        String type = ".attack";
        type += this.isCrownDeath(entity);
        final String s = ConstantsLang.DAMAGE_TYPE + "." + this.damageType + type;
        return new TextComponentTranslation(s, entity.getDisplayName(), source);
    }

    public String isCrownDeath(EntityLivingBase entity) {
        if (this.damageType.contentEquals(TrinketsRegistryNames.ModDamageTypes.WATER)) {
            boolean WATER_CROWN = TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketEnderTiara);
            if (WATER_CROWN) {
                return ".crown";
            }
        }
        return "";
    }

}
