package xzeroair.trinkets.traits.abilities.elements.water;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityWater;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;

public class AbilityWaterImmunity extends Ability implements IAttackAbility {

    protected final ConfigAbilityImmunityWater CONFIG;

    public AbilityWaterImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.WATER_IMMUNITY);
    }

    public AbilityWaterImmunity(ConfigAbilityImmunityWater config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_WATER);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (DamageTypeConfigParser.isWaterDamage(source.getDamageType())) {
            return true;
        }
        return cancel;
    }
}