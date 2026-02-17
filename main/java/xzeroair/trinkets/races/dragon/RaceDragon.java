package xzeroair.trinkets.races.dragon;

import net.minecraft.entity.EntityLivingBase;
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
            if (serverConfig.breath_damage > 0) {
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
        } else if (ele == Elements.LIGHTNING && TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
            this.addAbility(new AbilityLightningImmunity().setRequiredElement(Elements.LIGHTNING));
            this.addAbility(new AbilityLightningBolt().setRequiredElement(Elements.LIGHTNING));
        } else {
            if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.DE_FIRE_RESIST) {
                this.addAbility(new AbilityFireImmunity().setRequiredElement(Elements.NEUTRAL));
                if (survival && serverConfig.compat.tan.immuneToHeat) {
                    this.addAbility(new AbilityHeatImmunity().setRequiredElement(Elements.NEUTRAL));
                }
                if (serverConfig.breath_damage > 0) {
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