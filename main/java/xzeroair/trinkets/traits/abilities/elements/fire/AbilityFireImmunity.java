package xzeroair.trinkets.traits.abilities.elements.fire;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
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
import xzeroair.trinkets.util.compat.fireresisttiers.FireResistTiersCompat;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityFire;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityFireImmunity extends Ability implements ITickableAbility, IAttackAbility {

    protected final ConfigAbilityImmunityFire CONFIG;
    protected int AMPLIFIER, DURATION;

    public AbilityFireImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.FIRE_IMMUNITY);
    }

    public AbilityFireImmunity(ConfigAbilityImmunityFire config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_FIRE);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.DURATION = config.DURATION;
        if (FireResistTiersCompat.isModEnabled() && config.COMPAT.TIERS.amplifier > 0) {
            this.AMPLIFIER = config.COMPAT.TIERS.amplifier;
        } else {
            this.AMPLIFIER = 0;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("potion", this.DURATION > 0, ConstantsTextTranslations.TextMinecraft.MINECRAFT_FIRE_RESISTANCE.getFormattedText());
        return helper.formatAddVariables(key, renderID, key1);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (entity.isBurning()) {
            entity.extinguish();
        }
        if (this.DURATION > 0) {
            final boolean client = entity.world.isRemote;
            if (!client) {
                if (!entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    final PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE, this.DURATION, this.AMPLIFIER, false, false);
                    entity.addPotionEffect(fireResist);
                }
            }
            if (client) {
                if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    PotionEffect pot = entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE);
                    if (pot != null) {
                        pot.setPotionDurationMax(true);
                    }
                }
            }
        }
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            entity.removePotionEffect(MobEffects.FIRE_RESISTANCE);
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (FireResistTiersCompat.isModEnabled()) {
            return cancel;
        }
        if (source.isFireDamage() || DamageTypeConfigParser.isFireDamage(source.getDamageType())) {
            return true;
        }
        return cancel;
    }

    @Override
    public float damaged(EntityLivingBase attacked, DamageSource source, float dmg) {
        if (FireResistTiersCompat.isModEnabled()) {
            return dmg;
        }
        if (source.isFireDamage() || DamageTypeConfigParser.isFireDamage(source.getDamageType())) {
            return 0;
        }
        return dmg;
    }

}
