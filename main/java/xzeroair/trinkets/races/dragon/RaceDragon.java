package xzeroair.trinkets.races.dragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.dragon.RaceDragonRenderer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dragon.config.*;
import xzeroair.trinkets.traits.abilities.*;
import xzeroair.trinkets.traits.abilities.compat.enhancedvisuals.AbilityEnhancedVisualsFireParticles;
import xzeroair.trinkets.traits.abilities.elements.air.AbilityAirImmunity;
import xzeroair.trinkets.traits.abilities.elements.dark.AbilityDarkImmunity;
import xzeroair.trinkets.traits.abilities.elements.earth.AbilityEarthImmunity;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireBreath;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityFrostWalker;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceBreath;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.elements.light.AbilityLightBreath;
import xzeroair.trinkets.traits.abilities.elements.light.AbilityLightImmunity;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningBolt;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningBreath;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningImmunity;
import xzeroair.trinkets.traits.abilities.elements.poison.AbilityPoisonImmunity;
import xzeroair.trinkets.traits.abilities.elements.voids.AbilityVoidImmunity;
import xzeroair.trinkets.traits.abilities.elements.water.AbilityWaterImmunity;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.enhancedvisuals.EnhancedVisualsCompat;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceDragon extends EntityRacePropertiesHandler {

    private final DragonConfig CONFIG = TrinketsConfig.SERVER.RACES.DRAGON;

    public RaceDragon(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.dragon));
    }

    public RaceDragon(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
    }

    @Override
    protected String[] getElementAttributes() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.ATTRIBUTES;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.ATTRIBUTES;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.ATTRIBUTES;
        } else if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.ATTRIBUTES;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.ATTRIBUTES;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.ATTRIBUTES;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.ATTRIBUTES;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.ATTRIBUTES;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.ATTRIBUTES;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.ATTRIBUTES;
        }
        return this.CONFIG.ATTRIBUTES;
    }

    @Override
    public void registerRaceAbilities() {
        this.addAbility(new AbilityCreativeFlight(this.CONFIG.ABILITIES.FLIGHT));
        this.addAbility(new AbilityElytraFlight(this.CONFIG.ABILITIES.ELYTRA_FLIGHT));
        this.addAbility(new AbilityNightVision(this.CONFIG.ABILITIES.NIGHT_VISION));
        if (EnhancedVisualsCompat.isModEnabled()) {
            this.addAbility(new AbilityEnhancedVisualsFireParticles(this.CONFIG.ABILITIES.EXTERNAL.CLEAR_FIRE_PARTICLES));
        }
        this.addElementAbilities();
        this.addAbility(new AbilityGreedyEyes(this.CONFIG.ABILITIES.GREEDY_EYES));
    }


    @Override
    protected void addNeutralAbilities() {
        this.addAbility(new AbilityFireImmunity(this.CONFIG.ABILITIES.FIRE_IMMUNITY).setRequiredElement(Elements.NEUTRAL));
        this.addSurvivalAbilities(this.CONFIG.COMPAT.SURVIVAL);
        this.addAbility(new AbilityDragonBreath(this.CONFIG.ABILITIES.DRAGON_BREATH).setRequiredElement(Elements.NEUTRAL));
    }

    @Override
    protected void addFireAbilities(Element element) {
        ConfigFireDragon cfg = this.CONFIG.ELEMENTS.FIRE;
        this.addAbility(new AbilityFireImmunity(cfg.ABILITIES.FIRE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityFireBreath(cfg.ABILITIES.FIRE_BREATH).setRequiredElement(element));
    }

    @Override
    protected void addIceAbilities(Element element) {
        ConfigIceDragon cfg = this.CONFIG.ELEMENTS.ICE;
        this.addAbility(new AbilityIceImmunity(cfg.ABILITIES.ICE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityFrostWalker(cfg.ABILITIES.FROST_WALKER).setRequiredElement(element));
        this.addAbility(new AbilityIceBreath(cfg.ABILITIES.ICE_BREATH).setRequiredElement(element));
    }

    @Override
    protected void addLightningAbilities(Element element) {
        ConfigLightningDragon cfg = this.CONFIG.ELEMENTS.LIGHTNING;
        this.addAbility(new AbilityLightningImmunity(cfg.ABILITIES.LIGHTNING_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityLightningBolt(cfg.ABILITIES.LIGHTNING_BOLT).setRequiredElement(element));
        this.addAbility(new AbilityLightningBreath(cfg.ABILITIES.LIGHTNING_BREATH).setRequiredElement(element));
    }

    @Override
    protected void addAirAbilities(Element element) {
        ConfigAirDragon cfg = this.CONFIG.ELEMENTS.AIR;
        this.addAbility(new AbilityAirImmunity(cfg.ABILITIES.AIR_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addDarkAbilities(Element element) {
        ConfigDarkDragon cfg = this.CONFIG.ELEMENTS.DARK;
        this.addAbility(new AbilityDarkImmunity(cfg.ABILITIES.DARK_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addEarthAbilities(Element element) {
        ConfigEarthDragon cfg = this.CONFIG.ELEMENTS.EARTH;
        this.addAbility(new AbilityEarthImmunity(cfg.ABILITIES.EARTH_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addLightAbilities(Element element) {
        ConfigLightDragon cfg = this.CONFIG.ELEMENTS.LIGHT;
        this.addAbility(new AbilityLightImmunity(cfg.ABILITIES.LIGHT_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityLightBreath(cfg.ABILITIES.LIGHT_BREATH).setRequiredElement(element));
    }

    @Override
    protected void addPoisonAbilities(Element element) {
        ConfigPoisonDragon cfg = this.CONFIG.ELEMENTS.POISON;
        this.addAbility(new AbilityPoisonImmunity(cfg.ABILITIES.POISON_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addVoidAbilities(Element element) {
        ConfigVoidDragon cfg = this.CONFIG.ELEMENTS.VOID;
        this.addAbility(new AbilityVoidImmunity(cfg.ABILITIES.VOID_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addWaterAbilities(Element element) {
        ConfigWaterDragon cfg = this.CONFIG.ELEMENTS.WATER;
        this.addAbility(new AbilityWaterImmunity(cfg.ABILITIES.WATER_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
    }

    @Override
    protected String[] getElementEffectsToAdd() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD;
        } else if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_ADD;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_ADD;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_ADD;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.EFFECTS_TO_ADD;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.EFFECTS_TO_ADD;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.EFFECTS_TO_ADD;
        }
        return this.CONFIG.EFFECTS_TO_ADD;
    }

    @Override
    protected String[] getElementEffectsToRemove() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE;
        } else if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_REMOVE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_REMOVE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_REMOVE;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.EFFECTS_TO_REMOVE;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.EFFECTS_TO_REMOVE;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.EFFECTS_TO_REMOVE;
        }
        return this.CONFIG.EFFECTS_TO_REMOVE;
    }

    @Override
    protected String[] getElementDamageTypesToIgnore() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.POISON) {
            return this.CONFIG.ELEMENTS.POISON.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.VOID) {
            return this.CONFIG.ELEMENTS.VOID.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.WATER) {
            return this.CONFIG.ELEMENTS.WATER.DAMAGE_TYPES_TO_IGNORE;
        }
        return this.CONFIG.DAMAGE_TYPES_TO_IGNORE;
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        this.applyEquipmentWeight(this.CONFIG.EQUIPMENT_WEIGHT.ENABLED, this.CONFIG.EQUIPMENT_WEIGHT.EQUIPMENT, false);
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.getElementEffectsToRemove());
        PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.getElementEffectsToAdd());
        this.checkMount();
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (!this.getEntity().world.isRemote) {
            boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore())
                    .getFirst();
            return !result;
        }
        return true;
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore())
                .getSecond();
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.getElementDamageTypesToIgnore())
                .getSecond();
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        return PotionHelper.isPotionEffect(effect, this.getElementEffectsToRemove());
    }

    @Override
    public void endTransformation() {
        this.clearEquipmentWeight();
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.getElementEffectsToAdd());
    }

    @Override
    public boolean mountEntity(Entity mount) {
        return this.isMountAllowed(mount, this.CONFIG.CAN_MOUNT, this.CONFIG.CAN_CONTROL_BOATS, this.CONFIG.MOUNT_WHITELIST, this.CONFIG.MOUNT_BLACKLIST);
    }

    @Override
    public boolean canFly() {
        return super.canFly() && this.showTraits() && this.CONFIG.ABILITIES.FLIGHT.ENABLED;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Client~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceDragonRenderer(this.getEntity(), this);
        }
        return this.RendererRace;
    }

}