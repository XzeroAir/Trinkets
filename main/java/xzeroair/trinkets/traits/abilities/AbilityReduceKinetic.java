package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityReduceKinetic extends Ability implements IAttackAbility, IJumpAbility {

    float fallMultiplier = 1F;

    public AbilityReduceKinetic() {
        super(Abilities.reduceKinetic);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("fdpercent", true, ((100F - (TrinketsConfig.SERVER.Items.GREATER_INERTIA.falldamage_amount * 100F)) + "%"));
        return helper.formatAddVariable(key, renderID, key1);
    }

    public AbilityReduceKinetic setFallMultiplier(float fallMultiplier) {
        this.fallMultiplier = fallMultiplier;
        return this;
    }

    public float getFallMultiplier() {
        return fallMultiplier;
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
        if ((fallMultiplier <= 0)) {
            return true;
        }
        return cancel;
    }

    @Override
    public float fallDamageMultiplier(EntityLivingBase entity, float multiplier) {
        if ((fallMultiplier != 1F)) {
            multiplier = fallMultiplier;
        }
        return multiplier;
    }

    @Override
    public float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
        if ((fallMultiplier != 1F)) {
            if (source.equals(DamageSource.FALL) || source.equals(DamageSource.FALLING_BLOCK) || source.equals(DamageSource.FLY_INTO_WALL)) {
                return dmg * fallMultiplier;
            }
        }
        return dmg;
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if ((fallMultiplier <= 0)) {
            if (source.equals(DamageSource.FALL) || source.equals(DamageSource.FALLING_BLOCK) || source.equals(DamageSource.FLY_INTO_WALL)) {
                return true;
            }
        }
        return cancel;
    }

}
