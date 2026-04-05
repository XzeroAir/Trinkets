package xzeroair.trinkets.traits.abilities.elements.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityAffinityPoison;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityPoisonAffinity extends Ability implements IAttackAbility {

    protected final ConfigAbilityAffinityPoison CONFIG;
    protected int CHANCE;
    protected int DURATION;
    protected float DAMAGE_MULTIPLIER;

    public AbilityPoisonAffinity() {
        this(TrinketsConfig.SERVER.ABILITIES.AFFINITY_POISON);
    }

    public AbilityPoisonAffinity(ConfigAbilityAffinityPoison config) {
        super(TrinketsRegistryNames.ModAbilities.AFFINITY_POISON);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.CHANCE = config.CHANCE;
        this.DURATION = config.DURATION;
        this.DAMAGE_MULTIPLIER = config.DAMAGE_MULTI;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final KeyEntry key1 = new OptionEntry("chance", this.CHANCE > 0, this.CHANCE < 1 ? 0 : (1F / this.CHANCE) * 100 + "%");
        final KeyEntry key2 = new OptionEntry("duration", this.DURATION > 1, (this.DURATION / 20F));
        final KeyEntry key3 = new OptionEntry("bonus", this.DAMAGE_MULTIPLIER > 1, helper.translateAttributeValue(1, (this.DAMAGE_MULTIPLIER <= 0 ? this.DAMAGE_MULTIPLIER : this.DAMAGE_MULTIPLIER - 1)));
        return helper.formatAddVariables(key, renderID, key1, key2, key3);
    }

    @Override
    public float hurtEntity(@Nonnull EntityLivingBase target, DamageSource source, float dmg) {
        if (!target.isPotionActive(MobEffects.POISON) && !source.isMagicDamage() && !source.isFireDamage() && !source.isExplosion()) {
            if (((this.CHANCE > 0) && (this.random.nextInt(this.CHANCE) == 0))) {
                target.addPotionEffect(new PotionEffect(MobEffects.POISON, this.DURATION, 0, false, true));
            }
        }
        if (this.DAMAGE_MULTIPLIER <= 1 || !target.isPotionActive(MobEffects.POISON) || !DamageTypeConfigParser.isPoisonDamage(source.getDamageType())) {
            return dmg;
        }
        return dmg * (this.DAMAGE_MULTIPLIER);
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("CHANCE", this.CHANCE);
        tag.setInteger("DURATION", this.DURATION);
        tag.setFloat("MULTI", this.DAMAGE_MULTIPLIER);
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        NBTHelper.hasInteger(tag, "CHANCE", (value) -> this.CHANCE = value);
        NBTHelper.hasInteger(tag, "DURATION", (value) -> this.DURATION = value);
        NBTHelper.hasFloat(tag, "MULTI", (value) -> this.DAMAGE_MULTIPLIER = value);
    }
}
