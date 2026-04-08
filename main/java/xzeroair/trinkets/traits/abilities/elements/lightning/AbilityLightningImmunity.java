package xzeroair.trinkets.traits.abilities.elements.lightning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ILightningStrikeAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityLightning;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;

public class AbilityLightningImmunity extends Ability implements ITickableAbility, IAttackAbility, ILightningStrikeAbility {

    protected final ConfigAbilityImmunityLightning CONFIG;
    private final Potion lightning_resist;

    public AbilityLightningImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.LIGHTNING_IMMUNITY);
    }

    public AbilityLightningImmunity(@Nonnull ConfigAbilityImmunityLightning config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_LIGHTNING);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.lightning_resist = ModPotionTypes.TrinketPotions.get(ModPotionTypes.lightningResist);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = this.getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("potion", ConstantsTextTranslations.LIGHTNING_IMMUNITY.getFormattedText());
        return helper.formatAddVariables(key, renderID, key1);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        // Immune to slowness?
        final AbilityHolder holder = this.getAbilityHolder();
        if (holder.getInfo().getHandlerType() == ItemHandlerType.POTION) {
            return;
        }
        if (this.lightning_resist != null) {
            final boolean client = entity.world.isRemote;
            if (!client) {
                if (!entity.isPotionActive(this.lightning_resist)) {
                    entity.addPotionEffect(new PotionEffect(this.lightning_resist, 400, 0, false, false));
                }
            }
            if (client) {
                if (entity.isPotionActive(this.lightning_resist)) {
                    PotionEffect pot = entity.getActivePotionEffect(this.lightning_resist);
                    if (pot != null) {
                        pot.setPotionDurationMax(true);
                    }
                }
            }
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, @Nonnull DamageSource source, float dmg, boolean cancel) {
        if (DamageTypeConfigParser.isLightningDamage(source.getDamageType())) {
            return true;
        }
        return cancel;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (this.lightning_resist != null) {
            entity.removePotionEffect(this.lightning_resist);
        }
    }

    @Override
    public boolean onStruckByLightning(EntityLivingBase entity, boolean cancel) {
        entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 1));
        return true;
    }
}