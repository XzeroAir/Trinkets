package xzeroair.trinkets.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.util.ConstantsLang;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TrinketsDamageSource extends EntityDamageSource {

    public static final TrinketsDamageSource poison = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.POISON).setIsPoisonDamage(true);
    public static final TrinketsDamageSource bleeding = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.BLEED);
    public static final TrinketsDamageSource water = new TrinketsDamageSource(TrinketsRegistryNames.ModDamageTypes.WATER);

    private boolean isPoisonDamage;

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

    public TrinketsDamageSource setDirectSource(Entity damageSource) {
        this.damageSourceEntity = damageSource;
        return this;
    }

    public static TrinketsDamageSource causeDamageFrom(String damageType, Entity damageSource) {
        return new TrinketsDamageSource(damageType, damageSource);
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
