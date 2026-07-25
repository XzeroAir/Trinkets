package xzeroair.trinkets.util.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.abilities.*;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsBlur;
import xzeroair.trinkets.util.config.abilities.external.enhancedvisuals.ConfigAbilityEnhancedVisualsStatic;
import xzeroair.trinkets.util.config.abilities.external.firstaid.ConfigAbilityHardHead;
import xzeroair.trinkets.util.config.abilities.external.survival.*;

public class ConfigDefaultAbilityStorage {

    public ConfigDefaultAbilityStorage() {
    }

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_GREEDY_EYES)
    public ConfigAbilityGreedyEyes GREEDY_EYES = new ConfigAbilityGreedyEyes();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CREATIVE_FLIGHT)
    public ConfigAbilityFlight CREATIVE_FLIGHT = new ConfigAbilityFlight();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT)
    public ConfigAbilityElytraFlight ELYTRA_FLIGHT = new ConfigAbilityElytraFlight();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_NIGHT_VISION)
    public ConfigAbilityNightVision NIGHT_VISION = new ConfigAbilityNightVision();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENDER_QUEEN)
    public ConfigAbilityEnderQueen ENDER_QUEEN = new ConfigAbilityEnderQueen();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_CLIMBING)
    public ConfigAbilityClimbing CLIMBING = new ConfigAbilityClimbing();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_RESTORATION_FIELD)
    public ConfigAbilityHealCloud RESTORATION_FIELD = new ConfigAbilityHealCloud();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_WATER)
    public ConfigAbilityAffinityWater AFFINITY_WATER = new ConfigAbilityAffinityWater();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_POISON)
    public ConfigAbilityAffinityPoison AFFINITY_POISON = new ConfigAbilityAffinityPoison();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_AFFINITY_DARK)
    public ConfigAbilityAffinityDark AFFINITY_DARK = new ConfigAbilityAffinityDark();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_VICIOUS_STRIKE)
    public ConfigAbilityViciousStrike VICIOUS_STRIKE = new ConfigAbilityViciousStrike();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_SWIMMER)
    public ConfigAbilitySkilledSwimmer SKILLED_SWIMMER = new ConfigAbilitySkilledSwimmer();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER)
    public ConfigAbilitySkilledMiner SKILLED_MINER = new ConfigAbilitySkilledMiner();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_ARCHER)
    public ConfigAbilitySkilledArcher SKILLED_ARCHER = new ConfigAbilitySkilledArcher();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_MAGNETIC)
    public ConfigAbilityMagnetic MAGNETIC = new ConfigAbilityMagnetic();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_REPEL)
    public ConfigAbilityRepel REPEL = new ConfigAbilityRepel();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WELL_RESTED)
    public ConfigAbilityWellRested WELL_RESTED = new ConfigAbilityWellRested();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_DODGE)
    public ConfigAbilityDodge DODGE = new ConfigAbilityDodge();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_FIRE)
    public ConfigAbilityImmunityFire FIRE_IMMUNITY = new ConfigAbilityImmunityFire();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_ICE)
    public ConfigAbilityImmunityIce ICE_IMMUNITY = new ConfigAbilityImmunityIce();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_LIGHTNING)
    public ConfigAbilityImmunityLightning LIGHTNING_IMMUNITY = new ConfigAbilityImmunityLightning();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_POISON)
    public ConfigAbilityImmunityPoison POISON_IMMUNITY = new ConfigAbilityImmunityPoison();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_DARK)
    public ConfigAbilityImmunityDark DARK_IMMUNITY = new ConfigAbilityImmunityDark();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_IMMUNITY_WATER)
    public ConfigAbilityImmunityWater WATER_IMMUNITY = new ConfigAbilityImmunityWater();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_FIRE)
    public ConfigAbilityBreath FIRE_BREATH = new ConfigAbilityBreath();
    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_ICE)
    public ConfigAbilityBreath ICE_BREATH = new ConfigAbilityBreath();
    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BREATH_LIGHTNING)
    public ConfigAbilityBreath LIGHTNING_BREATH = new ConfigAbilityBreath();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LIGHTNING_BOLT)
    public ConfigAbilityLightningBolt LIGHTNING_BOLT = new ConfigAbilityLightningBolt();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_FROST_WALKER)
    public ConfigAbilityFrostWalker FROST_WALKER = new ConfigAbilityFrostWalker();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_NULL_KINETIC)
    public ConfigAbilityKinetic NULL_KINETIC = new ConfigAbilityKinetic();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_REDUCE_KINETIC)
    public ConfigAbilityKinetic REDUCE_KINETIC = new ConfigAbilityKinetic(0.25F);

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SAFE_GUARD)
    public ConfigAbilitySafeGuard SAFE_GUARD = new ConfigAbilitySafeGuard();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WEIGHTLESS)
    public ConfigAbilityWeightless WEIGHTLESS = new ConfigAbilityWeightless();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_WOLF_RIDER)
    public ConfigAbilityWolfRider WOLF_RIDER = new ConfigAbilityWolfRider();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LARGE_HANDS)
    public ConfigAbilityLargeHands LARGE_HANDS = new ConfigAbilityLargeHands();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_HEAVY)
    public ConfigAbilityHeavy HEAVY = new ConfigAbilityHeavy();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_STAMPEDE)
    public ConfigAbilityStampede STAMPEDE = new ConfigAbilityStampede();

    @Config.Ignore
    @Config.LangKey(ConstantsConfigLang.CONFIG_EXTERNAL)
    public ExternalAbilities EXTERNAL = new ExternalAbilities();

    public class ExternalAbilities {

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_FIRST_AID_HARD_HEAD)
        public ConfigAbilityHardHead HARD_HEAD = new ConfigAbilityHardHead();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_IMMUNITY_HEAT)
        public ConfigAbilitySurvivalHeat IMMUNITY_HEAT = new ConfigAbilitySurvivalHeat();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_IMMUNITY_COLD)
        public ConfigAbilitySurvivalCold IMMUNITY_COLD = new ConfigAbilitySurvivalCold();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST)
        public ConfigAbilitySurvivalThirst IMMUNITY_THIRST = new ConfigAbilitySurvivalThirst();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_PARASITES)
        public ConfigAbilitySurvivalParasites IMMUNITY_PARASITES = new ConfigAbilitySurvivalParasites();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SURVIVAL_THIRST_ABSORPTION)
        public ConfigAbilitySurvivalThirstAbsorption THIRST_ABSORPTION = new ConfigAbilitySurvivalThirstAbsorption();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_ENDER_EYES)
        public ConfigAbilityEnhancedVisualsStatic ENDER_EYES = new ConfigAbilityEnhancedVisualsStatic();

        @Config.Ignore
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ENHANCED_VISUALS_CLEAR_VISION)
        public ConfigAbilityEnhancedVisualsBlur CLEAR_VISION = new ConfigAbilityEnhancedVisualsBlur();

    }
}
