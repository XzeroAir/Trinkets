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
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

import java.util.Map;

public class AbilityPoisonAffinity extends Ability implements IAttackAbility, IPotionAbility, ITickableAbility {

    private final ConfigPoisonStone serverConfig = TrinketsConfig.SERVER.Items.POISON_STONE;

    public AbilityPoisonAffinity() {
        super(Abilities.poisonAffinity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String addCustomDescriptionTags(TranslationHelper helper, String key, int rendMod, int renderID, int compatID) {
        final KeyEntry key1 = new OptionEntry("chance", serverConfig.poison, MathHelper.clamp((1F / serverConfig.poison_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
        final KeyEntry key2 = new OptionEntry("duration", serverConfig.poison, (serverConfig.poison_duration / 20) + "s");
        final KeyEntry key3 = new OptionEntry("bonus", serverConfig.bonus_damage, helper.translateAttributeValue(1, (serverConfig.bonus_damage_amount <= 0 ? serverConfig.bonus_damage_amount : serverConfig.bonus_damage_amount - 1)));
        return helper.formatAddVariables(key, renderID, key1, key2, key3);
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
        final Map<Potion, PotionEffect> potMap = entity.getActivePotionMap();
        if (!potMap.isEmpty()) {
            for (final String immunities : TrinketsConfig.SERVER.Items.POISON_STONE.immunities) {
                final Potion effect = Potion.getPotionFromResourceLocation(immunities);
                if (effect != null) {
                    entity.removePotionEffect(effect);
                }
            }
        }
    }

    @Override
    public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
        // Lycanites damage type = "acid"
        if (source.damageType.contentEquals("poison") || source.damageType.contentEquals("xat.poison")) {
            return true;
        }
        return cancel;
    }

    @Override
    public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
        if (serverConfig.poison) {
            if (!target.isPotionActive(MobEffects.POISON) && !source.isMagicDamage() && !source.isFireDamage() && !source.isExplosion()) {
                if ((serverConfig.poison_chance == 0) || ((serverConfig.poison_chance > 0) && (random.nextInt(serverConfig.poison_chance) == 0))) {
                    target.addPotionEffect(new PotionEffect(MobEffects.POISON, serverConfig.poison_duration, 0, false, true));
                }
            }
        }
        if (!target.isPotionActive(MobEffects.POISON) || source.isMagicDamage() || source.isFireDamage() || source.isExplosion()) {
            return dmg;
        }
        final float damage = dmg;
        if (serverConfig.bonus_damage) {
            final float multipliedDamage = damage * serverConfig.bonus_damage_amount;
            return multipliedDamage;
            //				if (multipliedDamage > 0) {
            //					float finalDamage = multipliedDamage - damage;
            //					if (finalDamage > 0) {
            //						if (target.attackEntityFrom(TrinketsDamageSource.poison.setDirectSource(source.getTrueSource()), finalDamage)) {
            //							target.hurtResistantTime = 0;
            //						}
            //					}
            //				}
        }
        return damage;
    }

    @Override
    public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
        final String e = effect.getPotion().getRegistryName().toString();
        for (final String immunity : TrinketsConfig.SERVER.Items.POISON_STONE.immunities) {
            final Potion pot = Potion.getPotionFromResourceLocation(immunity);
            if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                return true;
            }
        }
        return cancel;
    }
}
