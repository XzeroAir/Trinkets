package xzeroair.trinkets.races.fairy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.fairy.RaceFairyRenderer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.AbilityCreativeFlight;
import xzeroair.trinkets.traits.abilities.AbilityElytraFlight;
import xzeroair.trinkets.traits.abilities.AbilityHealCloud;
import xzeroair.trinkets.traits.abilities.elements.air.AbilityAirImmunity;
import xzeroair.trinkets.traits.abilities.elements.dark.AbilityDarkImmunity;
import xzeroair.trinkets.traits.abilities.elements.earth.AbilityEarthImmunity;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.elements.light.AbilityLightImmunity;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningImmunity;
import xzeroair.trinkets.traits.abilities.elements.poison.AbilityPoisonImmunity;
import xzeroair.trinkets.traits.abilities.elements.voids.AbilityVoidImmunity;
import xzeroair.trinkets.traits.abilities.elements.water.AbilityWaterImmunity;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;

public class RaceFairy extends EntityRacePropertiesHandler {

    private final FairyConfig CONFIG = TrinketsConfig.SERVER.RACES.FAIRY;

    public RaceFairy(EntityLivingBase e, EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.fairy));
    }

    public RaceFairy(@Nonnull EntityLivingBase e, EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
    }

    @Override
    public void registerRaceAbilities() {
        this.addAbility(new AbilityCreativeFlight(this.CONFIG.ABILITIES.FLIGHT));
        this.addAbility(new AbilityElytraFlight(this.CONFIG.ABILITIES.ELYTRA_FLIGHT));
        this.addAbility(new AbilityClimbing(this.CONFIG.ABILITIES.CLIMBING));
        this.addAbility(new AbilityHealCloud(this.CONFIG.ABILITIES.RESTORATION_FIELD));
        this.addElementAbilities();
    }

    @Override
    protected void addAirAbilities(Element element) {
        this.addAbility(new AbilityAirImmunity(this.CONFIG.ELEMENTS.AIR.ABILITIES.AIR_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.AIR.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addDarkAbilities(Element element) {
        this.addAbility(new AbilityDarkImmunity(this.CONFIG.ELEMENTS.DARK.ABILITIES.DARK_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.DARK.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addEarthAbilities(Element element) {
        this.addAbility(new AbilityEarthImmunity(this.CONFIG.ELEMENTS.EARTH.ABILITIES.EARTH_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.EARTH.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addFireAbilities(Element element) {
        this.addAbility(new AbilityFireImmunity(this.CONFIG.ELEMENTS.FIRE.ABILITIES.FIRE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.FIRE.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addIceAbilities(Element element) {
        this.addAbility(new AbilityIceImmunity(this.CONFIG.ELEMENTS.ICE.ABILITIES.ICE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.ICE.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addLightAbilities(Element element) {
        this.addAbility(new AbilityLightImmunity(this.CONFIG.ELEMENTS.LIGHT.ABILITIES.LIGHT_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.LIGHT.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addLightningAbilities(Element element) {
        this.addAbility(new AbilityLightningImmunity(this.CONFIG.ELEMENTS.LIGHTNING.ABILITIES.LIGHTNING_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.LIGHTNING.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addPoisonAbilities(Element element) {
        this.addAbility(new AbilityPoisonImmunity(this.CONFIG.ELEMENTS.POISON.ABILITIES.POISON_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.POISON.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addVoidAbilities(Element element) {
        this.addAbility(new AbilityVoidImmunity(this.CONFIG.ELEMENTS.VOID.ABILITIES.VOID_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.VOID.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addWaterAbilities(Element element) {
        this.addAbility(new AbilityWaterImmunity(this.CONFIG.ELEMENTS.WATER.ABILITIES.WATER_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(this.CONFIG.ELEMENTS.WATER.COMPAT.SURVIVAL, element);
    }

    @Override
    protected void addNeutralAbilities() {
        this.addSurvivalAbilities(this.CONFIG.COMPAT.SURVIVAL);
    }

    @Override
    protected String[] getElementAttributes() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.ATTRIBUTES;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.ATTRIBUTES;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.ATTRIBUTES;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.ATTRIBUTES;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.ATTRIBUTES;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.ATTRIBUTES;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.ATTRIBUTES;
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
    protected String[] getElementEffectsToAdd() {
        Element element = this.getRaceCache().getPrimaryElement();
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_ADD;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_ADD;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_ADD;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_ADD;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD;
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
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.EFFECTS_TO_REMOVE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.EFFECTS_TO_REMOVE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.EFFECTS_TO_REMOVE;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.EFFECTS_TO_REMOVE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE;
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
        if (element == Elements.AIR) {
            return this.CONFIG.ELEMENTS.AIR.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.DARK) {
            return this.CONFIG.ELEMENTS.DARK.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.EARTH) {
            return this.CONFIG.ELEMENTS.EARTH.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHT) {
            return this.CONFIG.ELEMENTS.LIGHT.DAMAGE_TYPES_TO_IGNORE;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE;
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
    public boolean canFly() {
        return super.canFly() && this.showTraits() && this.CONFIG.ABILITIES.FLIGHT.ENABLED;
    }

    @Override
    public boolean mountEntity(Entity mount) {
        return this.isMountAllowed(mount, this.CONFIG.CAN_MOUNT, this.CONFIG.CAN_CONTROL_BOATS, this.CONFIG.MOUNT_WHITELIST, this.CONFIG.MOUNT_BLACKLIST);
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
    public void endTransformation() {
        this.clearEquipmentWeight();
        PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.getElementEffectsToAdd());
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        return PotionHelper.isPotionEffect(effect, this.getElementEffectsToRemove());
    }


    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceFairyRenderer(this.getEntity(), this);
        }
        return this.RendererRace;
    }

}
