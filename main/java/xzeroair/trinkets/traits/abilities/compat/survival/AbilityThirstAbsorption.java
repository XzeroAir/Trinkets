package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.config.abilities.external.survival.ConfigAbilitySurvivalThirstAbsorption;

public class AbilityThirstAbsorption extends AbilitySurvivalMod implements ITickableAbility {

    private final ConfigAbilitySurvivalThirstAbsorption CONFIG;
    protected float AMOUNT;
    protected int FREQUENCY;

    public AbilityThirstAbsorption() {
        this(TrinketsConfig.SERVER.ABILITIES.EXTERNAL.THIRST_ABSORPTION);
    }

    public AbilityThirstAbsorption(ConfigAbilitySurvivalThirstAbsorption config) {
        super(TrinketsRegistryNames.ModAbilities.SURVIVAL_THIRST_ABSORPTION);
        this.CONFIG = config;
        this.AMOUNT = config.AMOUNT;
        this.FREQUENCY = config.FREQUENCY;
        this.setAbilityEnabled(config.ENABLED);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (!entity.world.isRemote && entity.isInWater() && entity.ticksExisted % FREQUENCY == 0) {
            SurvivalCompat.addThirst(entity, 1, 0);
        }
    }

}
