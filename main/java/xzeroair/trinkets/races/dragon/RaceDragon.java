package xzeroair.trinkets.races.dragon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.dragon.RaceDragonRenderer;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dragon.config.ConfigFireDragon;
import xzeroair.trinkets.races.dragon.config.ConfigIceDragon;
import xzeroair.trinkets.races.dragon.config.ConfigLightningDragon;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.traits.abilities.AbilityCreativeFlight;
import xzeroair.trinkets.traits.abilities.AbilityDragonBreath;
import xzeroair.trinkets.traits.abilities.AbilityElytraFlight;
import xzeroair.trinkets.traits.abilities.AbilityGreedyEyes;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireBreath;
import xzeroair.trinkets.traits.abilities.elements.fire.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityFrostWalker;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceBreath;
import xzeroair.trinkets.traits.abilities.elements.ice.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningBolt;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningBreath;
import xzeroair.trinkets.traits.abilities.elements.lightning.AbilityLightningImmunity;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DamageTypeConfigParser;
import xzeroair.trinkets.util.helpers.EntityHelper;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RaceDragon extends EntityRacePropertiesHandler {

    private final DragonConfig CONFIG = TrinketsConfig.SERVER.RACES.DRAGON;

    public RaceDragon(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.dragon));
    }

    public RaceDragon(@Nonnull EntityLivingBase e, @Nonnull EntityProperties properties, @Nonnull RaceCache raceCache) {
        super(e, properties, raceCache);
    }

    @Override
    public String[] getAttributes() {
        Element element = this.getRaceCache().getPrimaryElement();
        // Elemental Features
        if (element == Elements.FIRE) {
            return this.CONFIG.ELEMENTS.FIRE.ATTRIBUTES;
        } else if (element == Elements.ICE) {
            return this.CONFIG.ELEMENTS.ICE.ATTRIBUTES;
        } else if (element == Elements.LIGHTNING) {
            return this.CONFIG.ELEMENTS.LIGHTNING.ATTRIBUTES;
        } else {
            return this.CONFIG.ATTRIBUTES;
        }
    }

    @Override
    public void registerRaceAbilities() {
        // Night Vision
        this.addAbility(new AbilityCreativeFlight(this.CONFIG.ABILITIES.FLIGHT));
        this.addAbility(new AbilityElytraFlight(this.CONFIG.ABILITIES.ELYTRA_FLIGHT));
        this.addAbility(new AbilityNightVision(this.CONFIG.ABILITIES.NIGHT_VISION));

        Element element = this.getRaceCache().getPrimaryElement();
        // Elemental Features
        if (element == Elements.FIRE) {
            this.addFireAbilities(element);
        } else if (element == Elements.ICE) {
            this.addIceAbilities(element);
        } else if (element == Elements.LIGHTNING) {
            this.addLightningAbilities(element);
        } else {
            this.addAbility(new AbilityFireImmunity(this.CONFIG.ABILITIES.FIRE_IMMUNITY));
            this.addAbility(new AbilityDragonBreath(this.CONFIG.ABILITIES.DRAGON_BREATH));
        }

        // Other Abilities
        this.addAbility(new AbilityGreedyEyes(this.CONFIG.ABILITIES.GREEDY_EYES));
    }

    private void addFireAbilities(Element element) {
        ConfigFireDragon cfg = this.CONFIG.ELEMENTS.FIRE;
        this.addAbility(new AbilityFireImmunity(cfg.ABILITIES.FIRE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityFireBreath(cfg.ABILITIES.FIRE_BREATH).setRequiredElement(element));
    }

    private void addIceAbilities(Element element) {
        ConfigIceDragon cfg = this.CONFIG.ELEMENTS.ICE;
        this.addAbility(new AbilityIceImmunity(cfg.ABILITIES.ICE_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityFrostWalker(cfg.ABILITIES.FROST_WALKER).setRequiredElement(element));
        this.addAbility(new AbilityIceBreath(cfg.ABILITIES.ICE_BREATH).setRequiredElement(element));
    }

    private void addLightningAbilities(Element element) {
        ConfigLightningDragon cfg = this.CONFIG.ELEMENTS.LIGHTNING;
        this.addAbility(new AbilityLightningImmunity(cfg.ABILITIES.LIGHTNING_IMMUNITY).setRequiredElement(element));
        this.addSurvivalAbilities(cfg.COMPAT.SURVIVAL, element);
        this.addAbility(new AbilityLightningBolt(cfg.ABILITIES.LIGHTNING_BOLT).setRequiredElement(element));
        this.addAbility(new AbilityLightningBreath(cfg.ABILITIES.LIGHTNING_BREATH).setRequiredElement(element));
    }

    @Override
    public void whileTransformed() {
        super.whileTransformed();
        if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_REMOVE);
            PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_REMOVE);
            PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE);
            PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD);
        } else {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_REMOVE);
            PotionHelper.addAllPotionEffectsFromConfig(this.getEntity(), true, this.CONFIG.EFFECTS_TO_ADD);
        }
        if (!this.getEntity().world.isRemote && this.getEntity().isRiding()) {
            final Entity mount = this.getEntity().getRidingEntity();
            if ((mount != null) && !this.mountEntity(mount)) {
                this.getEntity().dismountRidingEntity();
            }
        }
    }

    @Override
    public boolean isAttacked(DamageSource source, float dmg) {
        if (!this.getEntity().world.isRemote) {
            if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
                boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE).getFirst();
                return !result;
            } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
                boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE).getFirst();
                return !result;
            } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
                boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE).getFirst();
                return !result;
            } else {
                boolean result = DamageTypeConfigParser.parseDamageTypeConfig(0, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getFirst();
                return !result;
            }
        }
        return true;
    }

    @Override
    public float isHurt(DamageSource source, float dmg) {
        if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(1, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
        }
    }

    @Override
    public float isDamaged(DamageSource source, float dmg) {
        if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.FIRE.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.ICE.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.ELEMENTS.LIGHTNING.DAMAGE_TYPES_TO_IGNORE).getSecond();
        } else {
            return dmg * DamageTypeConfigParser.parseDamageTypeConfig(2, source, dmg, this.raceCache.getPrimaryElement(), this.CONFIG.DAMAGE_TYPES_TO_IGNORE).getSecond();
        }
    }

    @Override
    public boolean potionBeingApplied(PotionEffect effect) {
        if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
            return PotionHelper.isPotionEffect(effect, TrinketsConfig.SERVER.RACES.DRAGON.ELEMENTS.FIRE.EFFECTS_TO_REMOVE);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
            return PotionHelper.isPotionEffect(effect, TrinketsConfig.SERVER.RACES.DRAGON.ELEMENTS.ICE.EFFECTS_TO_REMOVE);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
            return PotionHelper.isPotionEffect(effect, TrinketsConfig.SERVER.RACES.DRAGON.ELEMENTS.LIGHTNING.EFFECTS_TO_REMOVE);
        } else {
            return PotionHelper.isPotionEffect(effect, TrinketsConfig.SERVER.RACES.DRAGON.EFFECTS_TO_REMOVE);
        }
    }

    @Override
    public void endTransformation() {
        if (this.getRaceCache().comparePrimaryElement(Elements.FIRE)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.FIRE.EFFECTS_TO_ADD);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.ICE)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.ICE.EFFECTS_TO_ADD);
        } else if (this.getRaceCache().comparePrimaryElement(Elements.LIGHTNING)) {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.ELEMENTS.LIGHTNING.EFFECTS_TO_ADD);
        } else {
            PotionHelper.removeAllPotionEffectsFromConfig(this.getEntity(), this.CONFIG.EFFECTS_TO_ADD);
        }
    }

    @Override
    public boolean mountEntity(Entity mount) {
        if (EntityHelper.isCreative(this.getEntity())) {
            return true;
        } else if (!this.CONFIG.CAN_MOUNT) {
            return false;
        } else if (this.CONFIG.MOUNT_BLACKLIST.length > 0) {
            List<String> disallowedMounts = Arrays.asList(this.CONFIG.MOUNT_BLACKLIST);
            try {
                final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final String modID = regName.getNamespace();
                final String entityID = regName.getPath();
                final boolean exists = disallowedMounts.contains(modID + ":*") || disallowedMounts.contains(regName.toString());
                if (exists) {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                    return this.CONFIG.MOUNT_WHITELIST;
                } else {
                    if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == this.getEntity())) {
                            return false;
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !this.CONFIG.MOUNT_WHITELIST;
        } else {
            if (!this.CONFIG.CAN_CONTROL_BOATS && (mount instanceof EntityBoat)) {
                final EntityBoat boat = (EntityBoat) mount;
                final Entity controller = boat.getControllingPassenger();
                return (controller != null) && (controller != this.getEntity());
            }
            return true;
        }
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