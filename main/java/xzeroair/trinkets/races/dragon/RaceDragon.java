package xzeroair.trinkets.races.dragon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.dragon.RaceDragonRenderer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.traits.abilities.*;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceDragon extends EntityRacePropertiesHandler {

    public static final DragonConfig serverConfig = TrinketsConfig.SERVER.races.dragon;

    public RaceDragon(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.dragon);
    }

    public RaceDragon(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.dragon, element);
    }

    @Override
    public void startTransformation() {
        // Night Vision
        this.addAbility(new AbilityNightVision().toggleAbility(true));

        if (serverConfig.creative_flight) {
            this.addAbility(new AbilityFlying().setFlightCost(serverConfig.flight_cost));
        }
//        Element ele = getEntityProperties().getCurrentRace().getElement();
        Element ele = getRaceCache().getElement();
        // Elemental Features
        final boolean tanEnabled = (Trinkets.MOD_COMPAT.ToughAsNails && TrinketsConfig.getClientStore().MOD_COMPAT_TOUGHASNAILS);
        final boolean sdEnabled = (Trinkets.MOD_COMPAT.SimpleDifficulty && TrinketsConfig.getClientStore().MOD_COMPAT_SIMPLEDIFFICULTY);
        final boolean survival = tanEnabled || sdEnabled;
        if (ele == Elements.FIRE && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.FIRE_VARIANT) {
            this.addAbility(new AbilityFireImmunity().setRequiredElement(Elements.FIRE));
            if (survival && serverConfig.compat.tan.immuneToHeat) {
                this.addAbility(new AbilityHeatImmunity().setRequiredElement(Elements.FIRE));
            }
            if (serverConfig.elementConfig.fire.breath_damage > 0) {
                this.addAbility(new AbilityFireBreathing().setRequiredElement(Elements.FIRE));
            }
        } else if (ele == Elements.ICE && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
            this.addAbility(new AbilityIceImmunity().setRequiredElement(Elements.ICE));
            if (survival && serverConfig.compat.tan.immuneToCold) {
                this.addAbility(new AbilityColdImmunity().setRequiredElement(Elements.ICE));
            }
            if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.FROST_WALKER) {
                this.addAbility(new AbilityFrostWalker().setRequiredElement(Elements.ICE));
            }
            if (serverConfig.elementConfig.ice.breath_damage > 0) {
                this.addAbility(new AbilityIceBreathing().setRequiredElement(Elements.ICE));
            }
        } else if (ele == Elements.LIGHTNING && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
            this.addAbility(new AbilityLightningImmunity().setRequiredElement(Elements.LIGHTNING));
            this.addAbility(new AbilityLightningBolt().setRequiredElement(Elements.LIGHTNING));
            if (serverConfig.elementConfig.lightning.breath_damage > 0) {
                this.addAbility(new AbilityLightningBreathing().setRequiredElement(Elements.LIGHTNING));
            }
        } else {
            if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.DE_FIRE_RESIST) {
                this.addAbility(new AbilityFireImmunity().setRequiredElement(Elements.NEUTRAL));
                if (survival && serverConfig.compat.tan.immuneToHeat) {
                    this.addAbility(new AbilityHeatImmunity().setRequiredElement(Elements.NEUTRAL));
                }
                if (serverConfig.elementConfig.fire.breath_damage > 0) {
                    this.addAbility(new AbilityFireBreathing().setRequiredElement(Elements.NEUTRAL));
                }
            }
        }

        // Other Abilities
        if (TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder) {
            this.addAbility(new AbilityBlockFinder());
        }
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        if (!entity.world.isRemote) {
            String[] potEffects;
            if (getRaceCache().compareElement(Elements.FIRE)) {
                potEffects = serverConfig.elementConfig.fire.potEffects;
            } else if (getRaceCache().compareElement(Elements.ICE)) {
                potEffects = serverConfig.elementConfig.ice.potEffects;
            } else if (getRaceCache().compareElement(Elements.LIGHTNING)) {
                potEffects = serverConfig.elementConfig.lightning.potEffects;
            } else {
                potEffects = serverConfig.potEffects;
            }
            for (final String potID : potEffects) {
                final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
                if (potion.getPotion() != null) {
                    entity.addPotionEffect(potion.getPotionEffect());
                }
            }
        }
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (getRaceCache().compareElement(Elements.FIRE)) {
            DamageTypesConfig config = serverConfig.elementConfig.fire.dmgType;
            if ((source.isFireDamage() && config.fire) || (source.isExplosion() && config.explosion) || (source.isMagicDamage() && config.magic) || (source.isProjectile() && config.projectile) || (source instanceof EntityDamageSourceIndirect && config.indirect)) {
                return true;
            }
            for (String type : config.damageTypes) {
                if (source.damageType.contentEquals(type)) {
                    return true;
                }
            }
        } else if (getRaceCache().compareElement(Elements.ICE)) {
            DamageTypesConfig config = serverConfig.elementConfig.ice.dmgType;
            if ((source.isFireDamage() && config.fire) || (source.isExplosion() && config.explosion) || (source.isMagicDamage() && config.magic) || (source.isProjectile() && config.projectile) || (source instanceof EntityDamageSourceIndirect && config.indirect)) {
                return true;
            }
            for (String type : config.damageTypes) {
                if (source.damageType.contentEquals(type)) {
                    return true;
                }
            }
        } else if (getRaceCache().compareElement(Elements.LIGHTNING)) {
            DamageTypesConfig config = serverConfig.elementConfig.lightning.dmgType;
            if ((source.isFireDamage() && config.fire) || (source.isExplosion() && config.explosion) || (source.isMagicDamage() && config.magic) || (source.isProjectile() && config.projectile) || (source instanceof EntityDamageSourceIndirect && config.indirect)) {
                return true;
            }
            for (String type : config.damageTypes) {
                if (source.damageType.contentEquals(type)) {
                    return true;
                }
            }
        } else {
            DamageTypesConfig config = serverConfig.dmgType;
            if ((source.isFireDamage() && config.fire) || (source.isExplosion() && config.explosion) || (source.isMagicDamage() && config.magic) || (source.isProjectile() && config.projectile) || (source instanceof EntityDamageSourceIndirect && config.indirect)) {
                return true;
            }
            for (String type : config.damageTypes) {
                if (source.damageType.contentEquals(type)) {
                    return true;
                }
            }
        }
        return super.isAttacked(source, dmg);
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        final String e = effect.getPotion().getRegistryName().toString();
        Element ele = getRaceCache().getElement();
        if (ele == Elements.FIRE) {
            for (final String immunity : TrinketsConfig.SERVER.races.dragon.elementConfig.fire.resistances) {
                final Potion pot = Potion.getPotionFromResourceLocation(immunity);
                if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                    return true;
                }
            }
        } else if (ele == Elements.ICE) {
            for (final String immunity : TrinketsConfig.SERVER.races.dragon.elementConfig.ice.resistances) {
                final Potion pot = Potion.getPotionFromResourceLocation(immunity);
                if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                    return true;
                }
            }
        } else if (ele == Elements.LIGHTNING) {
            for (final String immunity : TrinketsConfig.SERVER.races.dragon.elementConfig.lightning.resistances) {
                final Potion pot = Potion.getPotionFromResourceLocation(immunity);
                if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                    return true;
                }
            }
        } else {
            for (final String immunity : TrinketsConfig.SERVER.races.dragon.resistances) {
                final Potion pot = Potion.getPotionFromResourceLocation(immunity);
                if ((pot != null) && e.contentEquals(pot.getRegistryName().toString())) {
                    return true;
                }
            }
        }
        return super.potionBeingApplied(effect);
    }

    @Override
    public void endTransformation() {
        String[] potEffects;
        if (getRaceCache().compareElement(Elements.FIRE)) {
            potEffects = serverConfig.elementConfig.fire.potEffects;
        } else if (getRaceCache().compareElement(Elements.ICE)) {
            potEffects = serverConfig.elementConfig.ice.potEffects;
        } else if (getRaceCache().compareElement(Elements.LIGHTNING)) {
            potEffects = serverConfig.elementConfig.lightning.potEffects;
        } else {
            potEffects = serverConfig.potEffects;
        }
        for (final String potID : potEffects) {
            final PotionHelper.PotionHolder potion = PotionHelper.getPotionHolder(potID);
            if (potion.getPotion() != null) {
                if (entity.isPotionActive(potion.getPotion())) {
                    entity.removePotionEffect(potion.getPotion());
                }
            }
        }
    }

    @Override
    public boolean canFly() {
        return super.canFly() && this.showTraits() && serverConfig.creative_flight;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Client~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceDragonRenderer(entity, this);
        }
        return RendererRace;
    }

}