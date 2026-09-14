package xzeroair.trinkets.traits.abilities.elements;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;

/**
 * Common damage-cancellation behavior for elemental immunity families that do
 * not yet require additional environmental or potion behavior.
 */
public abstract class AbilityElementalImmunity extends Ability implements IAttackAbility {

    private final String damageTypeCheck;

    protected AbilityElementalImmunity(String registryName, String damageTypeCheck, boolean enabled) {
        super(registryName);
        this.damageTypeCheck = damageTypeCheck;
        this.setAbilityEnabled(enabled);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        return DamageTypeConfigParser.parseDamageTypeMatchesType(source, this.damageTypeCheck) || cancel;
    }
}
