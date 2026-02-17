package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import java.util.Random;

public class AbilityWitherAffinity extends Ability implements IAttackAbility, IPotionAbility {

    private static final ConfigWitherRing serverConfig = TrinketsConfig.SERVER.Items.WITHER_RING;

    public AbilityWitherAffinity() {
        super(Abilities.witherImmunity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        String langKey = getTranslationKey();
        final KeyEntry key1 = new OptionEntry("chance", serverConfig.wither, MathHelper.clamp((1F / serverConfig.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        final KeyEntry key2 = new OptionEntry("amount", serverConfig.leech, (serverConfig.leech_amount * 0.5));
        return helper.formatAddVariables(key, renderID, key1, key2);
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        if (source.equals(DamageSource.WITHER)) {
            return true;
        }
        return cancel;
    }

    @Override
    public boolean attackEntity(EntityLivingBase target, DamageSource source, float dmg, boolean cancel) {
        if (serverConfig.wither && !target.isPotionActive(MobEffects.WITHER)) {
            final Random rand = new Random();
            if (rand.nextInt(serverConfig.wither_chance) == 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, serverConfig.wither_duration, 0, false, true));
            }
        }
        return cancel;
    }

    @Override
    public float damageEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (serverConfig.leech) {
            if (target.isPotionActive(MobEffects.WITHER) && (serverConfig.leech_amount > 0)) {
                if (dmg >= serverConfig.leech_amount) {
                    ((EntityLivingBase) source.getTrueSource()).heal(serverConfig.leech_amount);
                } else {
                    ((EntityLivingBase) source.getTrueSource()).heal(dmg);
                }
            }
        }
        return dmg;
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String e = effect.getPotion().getRegistryName().toString();
        for (final String immunity : TrinketsConfig.SERVER.Items.WITHER_RING.immunities) {
            final Potion pot = Potion.getPotionFromResourceLocation(immunity);
            if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                return true;
            }
        }
        return cancel;
    }

}
