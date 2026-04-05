package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityKinetic;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;

public class AbilityReduceKinetic extends Ability implements IAttackAbility, IJumpAbility {

    protected final ConfigAbilityKinetic CONFIG;

    protected float AMOUNT, COST;

    public AbilityReduceKinetic() {
        this(TrinketsConfig.SERVER.ABILITIES.REDUCE_KINETIC);
    }

    public AbilityReduceKinetic(ConfigAbilityKinetic config) {
        super(TrinketsRegistryNames.ModAbilities.REDUCE_KINETIC);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.AMOUNT = config.MULTIPLIER;
        this.COST = config.COST;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("fdpercent", true, ((100F - (this.AMOUNT * 100F)) + "%"));
        return helper.formatAddVariable(key, renderID, key1);
    }

    @Override
    public boolean fall(EntityLivingBase entity, float distance, float multiplier, boolean cancel) {
        if (this.AMOUNT <= 0) {
            if (this.COST > 0) {
                boolean spent = Capabilities.getMagicStats(entity, true, (magic, rtn) -> magic.spendMana(this.COST));
                if (!spent) {
                    return false;
                }
            }
            return true;
        }
        return cancel;
    }

    @Override
    public float fallDamageMultiplier(EntityLivingBase entity, float multiplier) {
        if (this.AMOUNT != 1F) {
            if (this.COST > 0) {
                boolean spent = Capabilities.getMagicStats(entity, true, (magic, rtn) -> magic.spendMana(this.COST));
                if (!spent) {
                    return multiplier;
                }
            }
            multiplier = AMOUNT;
        }
        return multiplier;
    }

    @Override
    public float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
        if (this.AMOUNT != 1F) {
            if (source.equals(DamageSource.FALL) || source.equals(DamageSource.FALLING_BLOCK) || source.equals(DamageSource.FLY_INTO_WALL)) {
                if (this.COST > 0) {
                    boolean spent = Capabilities.getMagicStats(attacked, true, (magic, rtn) -> magic.spendMana(this.COST));
                    if (!spent) {
                        return dmg;
                    }
                }
                return dmg * this.AMOUNT;
            }
        }
        return dmg;
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (this.AMOUNT <= 0) {
            if (source.equals(DamageSource.FALL) || source.equals(DamageSource.FALLING_BLOCK) || source.equals(DamageSource.FLY_INTO_WALL)) {
                if (this.COST > 0) {
                    return Capabilities.getMagicStats(attacked, true, (magic, rtn) -> magic.spendMana(this.COST));
                }
                return true;
            }
        }
        return cancel;
    }

    @Override
    public void loadStorage(NBTTagCompound compound) {
        super.loadStorage(compound);
        if (compound.hasKey("AMOUNT")) {
            this.AMOUNT = compound.getFloat("AMOUNT");
        }
        if (compound.hasKey("COST")) {
            this.COST = compound.getFloat("COST");
        }
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        if (CONFIG.ENABLED) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setFloat("AMOUNT", this.AMOUNT);
            if (this.COST > 0F) {
                tag.setFloat("COST", this.COST);
            }
            return tag;
        }
        return super.sendAbilityData();
    }

}
