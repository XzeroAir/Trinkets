package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.compat.defiledlands.CompatDefiledLands;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityViciousStrike;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbilityViciousStrike extends Ability implements IAttackAbility {

    protected ConfigAbilityViciousStrike CONFIG;
    protected int CHANCE;
    protected int DURATION;

    public AbilityViciousStrike() {
        this(TrinketsConfig.SERVER.ABILITIES.VICIOUS_STRIKE);
    }

    public AbilityViciousStrike(@Nonnull ConfigAbilityViciousStrike config) {
        super(TrinketsRegistryNames.ModAbilities.VICIOUS_STRIKE);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.DURATION = config.DURATION;
        this.CHANCE = config.CHANCE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final KeyEntry key1 = new OptionEntry("chance", this.CHANCE > 0, this.CHANCE < 1 ? 0 : (1F / this.CHANCE) * 100 + "%");
        final KeyEntry key2 = new OptionEntry("duration", this.DURATION > 1, (this.DURATION / 20F));
        final KeyEntry key3 = new OptionEntry("reduceddur", this.DURATION > 1, (this.DURATION / 3) / 20F);
        return helper.formatAddVariables(key, renderID, key1, key2, key3);
    }

    @Override
    public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (!this.isAbilityEnabled() || this.isIndirectDamage(source)) return dmg;
        int chance = this.CHANCE > 0 ? this.random.nextInt(this.CHANCE) : 0;
        if (chance == 0) {
            int max = 3;
            int amplifier = Capabilities.getEntityProperties(source.getTrueSource(), 0, (prop, rtn) -> prop.getCurrentRaceCache().compareRace(EntityRaces.faelis) ? 1 : rtn);
            final int duration = (amplifier > 0 ? this.DURATION : this.DURATION / 3);
            final Potion potion = CompatDefiledLands.getPotionBleeding();
            if (potion != null && this.CONFIG.COMPAT.DEFILED_LANDS.BLEED) {
                final boolean hasPot = target.isPotionActive(potion);
                PotionEffect bleed;
                if (hasPot) {
                    final PotionEffect bleeding = target.getActivePotionEffect(potion);
                    if ((bleeding.getAmplifier() + amplifier) <= max) {
                        amplifier = bleeding.getAmplifier() + 1 + amplifier;
                    }
                }
                target.addPotionEffect(new PotionEffect(potion, duration, Math.max(amplifier, max), false, false));
            } else {
                final Potion bleed = ModPotionTypes.TrinketPotions.get(ModPotionTypes.bleed);
                if (bleed != null) {
                    final int finalAmplifier = Math.max(0, amplifier);
                    final PotionEffect activeBleed = target.getActivePotionEffect(bleed);
                    if (activeBleed != null) {
                        target.addPotionEffect(new PotionEffect(bleed, activeBleed.getDuration() + duration, activeBleed.getAmplifier() + finalAmplifier, false, false));
                    } else {
                        target.addPotionEffect(new PotionEffect(bleed, duration, finalAmplifier, false, false));
                    }
                }
            }
        }
        return dmg;
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("CHANCE", this.CHANCE);
        tag.setInteger("DURATION", this.DURATION);
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        super.loadDataCache(tag);
        NBTHelper.hasInteger(tag, "CHANCE", (value) -> this.CHANCE = value);
        NBTHelper.hasInteger(tag, "DURATION", (value) -> this.DURATION = value);
    }
}
