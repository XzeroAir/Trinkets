package xzeroair.trinkets.traits.abilities.elements.dark;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityAffinityDark;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import javax.annotation.Nullable;

public class AbilityAffinityDark extends Ability implements ITickableAbility, IAttackAbility {

    protected final ConfigAbilityAffinityDark CONFIG;
    protected int CHANCE, DURATION, COOLDOWN;
    protected float LEECH_AMOUNT;
    protected boolean TRUE_LEECH;
    protected final String TRUE_LEECH_TAG = "true_leech";
    protected final String LEECH_AMOUNT_TAG = "leech_amount";
    protected final String WITHER_CHANCE_TAG = "wither_chance";
    protected final String WITHER_DURATION_TAG = "wither_duration";

    public AbilityAffinityDark() {
        this(TrinketsConfig.SERVER.ABILITIES.AFFINITY_DARK);
    }

    public AbilityAffinityDark(ConfigAbilityAffinityDark config) {
        super(TrinketsRegistryNames.ModAbilities.AFFINITY_DARK);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.CHANCE = config.CHANCE;
        this.DURATION = config.DURATION;
        this.TRUE_LEECH = config.TRUE_LEECH;
        this.LEECH_AMOUNT = config.LEECH_AMOUNT;
        this.COOLDOWN = 0;
        this.setChanged(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final KeyEntry key1 = new OptionEntry(this.WITHER_CHANCE_TAG, this.CHANCE > 0, this.CHANCE < 1 ? 0 : (1F / this.CHANCE) * 100 + "%");
        final KeyEntry key2 = new OptionEntry(this.WITHER_DURATION_TAG, this.DURATION > 1, (this.DURATION / 20F) + "");
        final KeyEntry key3 = new OptionEntry(this.LEECH_AMOUNT_TAG, !this.TRUE_LEECH, (this.LEECH_AMOUNT / 2) + "");
        final KeyEntry key4 = new OptionEntry(this.TRUE_LEECH_TAG, this.TRUE_LEECH, "");
        final KeyEntry key5 = new OptionEntry("wither", this.CHANCE > 0, ConstantsTextTranslations.TextMinecraft.MINECRAFT_WITHER.getFormattedText());
        final KeyEntry key6 = new OptionEntry("amount", true, "100.0%");
        return helper.formatAddVariables(key, renderID, key1, key2, key3, key4, key5, key6);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (this.COOLDOWN > 0) {
            this.COOLDOWN--;
        }
    }

    @Override
    public boolean attackEntity(EntityLivingBase target, DamageSource source, float dmg, boolean cancel) {
        if (this.CHANCE > 0 && !target.isPotionActive(MobEffects.WITHER)) {
            if (this.random.nextInt(this.CHANCE) == 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, this.DURATION, 0, false, true));
            }
        }
        return cancel;
    }

    @Override
    public float damageEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (this.COOLDOWN < 1 && target.isPotionActive(MobEffects.WITHER)) {
            Entity attacker = source.getTrueSource();
            if (attacker instanceof EntityLivingBase) {
                if (this.TRUE_LEECH) {
                    ((EntityLivingBase) attacker).heal(dmg);
                    System.out.println("Healing: " + dmg);
                } else {
                    if (this.LEECH_AMOUNT > 0) {
                        if (dmg >= this.LEECH_AMOUNT) {
                            ((EntityLivingBase) attacker).heal(this.LEECH_AMOUNT);
                        } else {
                            ((EntityLivingBase) attacker).heal(dmg);
                        }
                    }
                }
            }
            this.COOLDOWN = (20 * 1);
        }
        return dmg;
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(this.WITHER_CHANCE_TAG, this.CHANCE);
        if (this.DURATION > 0) {
            tag.setInteger(this.WITHER_DURATION_TAG, this.DURATION);
        }
        tag.setBoolean(this.TRUE_LEECH_TAG, this.TRUE_LEECH);
        if (!this.TRUE_LEECH) {
            tag.setFloat(this.LEECH_AMOUNT_TAG, this.LEECH_AMOUNT);
        }
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        super.loadDataCache(tag);
        NBTHelper.hasBoolean(tag, this.TRUE_LEECH_TAG, (bool) -> {
            this.TRUE_LEECH = bool;
        });
        NBTHelper.hasFloat(tag, this.LEECH_AMOUNT_TAG, (value) -> {
            this.LEECH_AMOUNT = value;
        });
        NBTHelper.hasInteger(tag, this.WITHER_CHANCE_TAG, (value) -> {
            this.CHANCE = value;
        });
        NBTHelper.hasInteger(tag, this.WITHER_DURATION_TAG, (value) -> {
            this.DURATION = value;
        });
    }
}
