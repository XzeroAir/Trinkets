package xzeroair.trinkets.traits.abilities.elements.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityPoison;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;

public class AbilityPoisonImmunity extends Ability implements IAttackAbility {

    protected final ConfigAbilityImmunityPoison CONFIG;

    public AbilityPoisonImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.POISON_IMMUNITY);
    }

    public AbilityPoisonImmunity(ConfigAbilityImmunityPoison config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_POISON);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (DamageTypeConfigParser.isPoisonDamage(source.getDamageType())) {
            return true;
        }
        return cancel;
    }
}