package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityFireImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility {

    public static ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
    protected int amplifier = 0;

    public AbilityFireImmunity() {
        super(Abilities.fireImmunity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        final TranslationHelper.KeyEntry key1 = new TranslationHelper.OptionEntry("potion", new TextComponentTranslation("effect.fireResistance").getFormattedText());
        final TranslationHelper.KeyEntry key2 = new TranslationHelper.OptionEntry("smouldering", Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.lycanites, new TextComponentTranslation("effect.smouldering").getFormattedText());
        return helper.formatAddVariables(key, renderID, key1, key2);
    }

    public int getAmplifier() {
        if (Trinkets.MOD_COMPAT.FireResistanceTiers) {
            AbilityHolder holder = this.getAbilityHolder();
            if (holder != null) {
                if (holder.getSourceID().equalsIgnoreCase("xat:" + ModItems.DragonsEye)) {
                    amplifier = TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.FRTiers.amplifier;
                } else if (holder.getSourceID().equalsIgnoreCase("xat:dragon")) {
                    amplifier = TrinketsConfig.SERVER.races.dragon.compat.FRTiers.amplifier;
                }
            }
        }
        if (amplifier < 0) {
            amplifier = 0;
        }
        return amplifier;
    }

    public AbilityFireImmunity setAmplifier(int amp) {
        if (amplifier != amp) {
            if (amp < 0) {
                amplifier = 0;
            } else {
                amplifier = amp;
            }
        }
        return this;
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        if (entity.isBurning()) {
            entity.extinguish();
        }
        final PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600, getAmplifier(), false, false);
        entity.addPotionEffect(fireResist);
        if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            if (entity.world.isRemote) {
                entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).setPotionDurationMax(true);
            }
        }
    }

    @Override
    public void onAbilityRemoved(EntityLivingBase entity) {
        if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
            entity.removePotionEffect(MobEffects.FIRE_RESISTANCE);
        }
    }

    /**
     * TODO add a config option for this, or remove it from this ability entirely and add it to it's own.
     *
     * @param entity
     * @param effect
     * @param cancel
     * @return
     */
    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        if (Trinkets.MOD_COMPAT.LycanitesMobs && TrinketsConfig.compat.lycanites) {
            final String e = effect.getPotion().getRegistryName().toString();
            final Potion smouldering = LycanitesCompat.getPotionEffectByName("smouldering");
            if ((smouldering != null) && e.contentEquals(smouldering.getRegistryName().toString())) {
                return true;
            }
        }
        return cancel;
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource damage, float dmg, boolean cancel) {
        if (damage.isFireDamage()) {
            return true;
        }
        return cancel;
    }

    @Override
    public float damaged(EntityLivingBase attacked, DamageSource source, float dmg) {
        if (Trinkets.MOD_COMPAT.FireResistanceTiers) {
            return dmg;
        }
        if (source.isFireDamage()) {
            return 0;
        }
        return dmg;
    }

}
