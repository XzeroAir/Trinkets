package xzeroair.trinkets.traits.abilities.elements.dark;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityDark;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.NBTHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nullable;

public class AbilityDarkImmunity extends Ability implements IAttackAbility {

    protected final ConfigAbilityImmunityDark CONFIG;

    protected int DURATION;
    protected float DARK_HEAL_MULTI;
    protected boolean DARK_HEAL;

    protected final String DARK_HEAL_TAG = "dark_heal";
    protected final String DARK_HEAL_MULTI_TAG = "dark_heal_multi";

    public AbilityDarkImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.DARK_IMMUNITY);
    }

    public AbilityDarkImmunity(ConfigAbilityImmunityDark config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_DARK);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.DURATION = config.DURATION;
        this.DARK_HEAL = config.HEAL_FROM_DARK;
        this.DARK_HEAL_MULTI = config.HEAL_FROM_DARK_MULTI;
    }


    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final boolean isDarkHealActive = this.DARK_HEAL && this.DARK_HEAL_MULTI > 0;
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry(this.DARK_HEAL_TAG, isDarkHealActive, (this.DARK_HEAL_MULTI * 100) + "%");
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("heal", isDarkHealActive, "");
        final TranslationHelper.KeyEntry key3 = new TranslationHelper.OptionEntry("amount", isDarkHealActive, (Math.min(this.DARK_HEAL_MULTI, 1F) * 100) + "%");
        final TranslationHelper.KeyEntry key4 = new TranslationHelper.OptionEntry("wither", true, ConstantsTextTranslations.TextMinecraft.MINECRAFT_WITHER.getFormattedText());
        final TranslationHelper.KeyEntry key5 = new TranslationHelper.OptionEntry("not_dark_heal", !isDarkHealActive, "");
        return helper.formatAddVariables(key, renderID, key1, key2, key3, key4, key5);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (this.DARK_HEAL && this.DARK_HEAL_MULTI > 0) {
            if (dmg > 0 && source.getDamageType().compareTo(DamageSource.WITHER.getDamageType()) == 0) {
                attacked.heal(dmg);
                return true;
            }
        } else {
            if (DamageTypeConfigParser.isDarkDamage(source.getDamageType())) {
                return true;
            }
        }
        return cancel;
    }

    @Override
    public float hurt(EntityLivingBase attacked, DamageSource source, float dmg) {
        if (dmg > 0 && this.DARK_HEAL && this.DARK_HEAL_MULTI > 0 && DamageTypeConfigParser.isDarkDamage(source.getDamageType())) {
            final float finalDamage = dmg;
            float healAmount = finalDamage * this.DARK_HEAL_MULTI;
            attacked.heal(healAmount);
            return Math.max(finalDamage - healAmount, 0);
        }
        return dmg;
    }

    @Nullable
    @Override
    public NBTTagCompound sendAbilityData() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(this.DARK_HEAL_TAG, this.DARK_HEAL);
        if (this.DARK_HEAL) {
            tag.setFloat(this.DARK_HEAL_MULTI_TAG, this.DARK_HEAL_MULTI);
        }
        return tag;
    }

    @Override
    public void loadDataCache(NBTTagCompound tag) {
        super.loadDataCache(tag);
        NBTHelper.hasBoolean(tag, this.DARK_HEAL_TAG, (bool) -> {
            this.DARK_HEAL = bool;
        });
        NBTHelper.hasFloat(tag, this.DARK_HEAL_MULTI_TAG, (value) -> {
            this.DARK_HEAL_MULTI = value;
        });
    }

}