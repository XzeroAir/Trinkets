package xzeroair.trinkets.traits.abilities.elements.ice;

import net.minecraft.entity.EntityLivingBase;
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
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityImmunityIce;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.TranslationHelper;

import javax.annotation.Nonnull;

public class AbilityIceImmunity extends Ability implements ITickableAbility, IAttackAbility {

    protected final ConfigAbilityImmunityIce CONFIG;
    private final Potion ice_resist;

    public AbilityIceImmunity() {
        this(TrinketsConfig.SERVER.ABILITIES.ICE_IMMUNITY);
    }

    public AbilityIceImmunity(@Nonnull ConfigAbilityImmunityIce config) {
        super(TrinketsRegistryNames.ModAbilities.IMMUNITY_ICE);
        this.CONFIG = config;
        this.setAbilityEnabled(config.ENABLED);
        this.ice_resist = ModPotionTypes.TrinketPotions.get(ModPotionTypes.iceResist);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(@Nonnull TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("potion", ConstantsTextTranslations.ICE_IMMUNITY.getFormattedText());
        return helper.formatAddVariables(key, renderID, key1);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        // Immune to slowness?
        final AbilityHolder holder = this.getAbilityHolder();
        if (holder.getInfo().getHandlerType() == ItemHandlerType.POTION) {
            return;
        }
        if (this.ice_resist != null) {
            final boolean client = entity.world.isRemote;
            if (!client) {
                if (!entity.isPotionActive(this.ice_resist)) {
                    entity.addPotionEffect(new PotionEffect(this.ice_resist, 400, 0, false, false));
                }
            }
            if (client) {
                if (entity.isPotionActive(this.ice_resist)) {
                    PotionEffect pot = entity.getActivePotionEffect(this.ice_resist);
                    if (pot != null) {
                        pot.setPotionDurationMax(true);
                    }
                }
            }
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, @Nonnull DamageSource source, float dmg, boolean cancel) {
        if (DamageTypeConfigParser.isIceDamage(source.getDamageType())) {
            return true;
        }
        return cancel;
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (this.ice_resist != null) {
            entity.removePotionEffect(this.ice_resist);
        }
    }
}
