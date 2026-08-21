package xzeroair.trinkets.util.config.compat;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class CompatibilityConfigs {

    public CompatibilityConfigs() {

        this.ARTEMIS_LIB = false;
        this.ELENAI_DODGE = Trinkets.MOD_COMPAT.ElenaiDodge1;
        this.TOUGH_AS_NAILS = Trinkets.MOD_COMPAT.ToughAsNails;
        this.SIMPLE_DIFFICULTY = Trinkets.MOD_COMPAT.SimpleDifficulty;
        this.ENHANCED_VISUALS = Trinkets.MOD_COMPAT.EnhancedVisuals;
        this.LYCANITES_MOBS = Trinkets.MOD_COMPAT.LycanitesMobs;
        this.DEFILED_LANDS = Trinkets.MOD_COMPAT.DefiledLands;
        this.BETTER_DIVING = Trinkets.MOD_COMPAT.BetterDiving;
        this.MO_BENDS = Trinkets.MOD_COMPAT.MoBends;
        this.ICE_AND_FIRE = Trinkets.MOD_COMPAT.IceAndFire;
        this.FIRST_AID = Trinkets.MOD_COMPAT.FirstAid;
        this.IDO_SWIMMING = Trinkets.MOD_COMPAT.Ido;
        this.TROPICRAFT_SWIMMING = Trinkets.MOD_COMPAT.Tropicraft;

    }

    @Config.Name(ConstantsConfigLang.CONFIG_FIRE_TIERS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FIRE_TIERS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FIRE_TIERS)
    public boolean FIRE_RESISTANCE_TIERS;

    @Config.Name(ConstantsConfigLang.CONFIG_ARTEMIS_LIB_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ARTEMIS_LIB_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ARTEMIS_LIB)
    public boolean ARTEMIS_LIB;

    @Config.Name(ConstantsConfigLang.CONFIG_FIRST_AID_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_FIRST_AID_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_FIRST_AID)
    public boolean FIRST_AID;

    @Config.Name(ConstantsConfigLang.CONFIG_ICE_AND_FIRE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ICE_AND_FIRE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ICE_AND_FIRE)
    public boolean ICE_AND_FIRE;

    @Config.Name(ConstantsConfigLang.CONFIG_ELENAI_DODGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ELENAI_DODGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ELENAI_DODGE)
    public boolean ELENAI_DODGE;

    @Config.Name(ConstantsConfigLang.CONFIG_TOUGH_AS_NAILS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TOUGH_AS_NAILS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TOUGH_AS_NAILS)
    public boolean TOUGH_AS_NAILS;

    @Config.Name(ConstantsConfigLang.CONFIG_SIMPLE_DIIFFICULTY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_SIMPLE_DIIFFICULTY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_SIMPLE_DIIFFICULTY)
    public boolean SIMPLE_DIFFICULTY;

    @Config.Name(ConstantsConfigLang.CONFIG_LYCANITES_MOBS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_LYCANITES_MOBS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_LYCANITES_MOBS)
    public boolean LYCANITES_MOBS;

    @Config.Name(ConstantsConfigLang.CONFIG_DEFILED_LANDS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DEFILED_LANDS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DEFILED_LANDS)
    public boolean DEFILED_LANDS;

    @Config.Name(ConstantsConfigLang.CONFIG_ENHANCED_VISUALS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ENHANCED_VISUALS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ENHANCED_VISUALS)
    public boolean ENHANCED_VISUALS;

    @Config.Name(ConstantsConfigLang.CONFIG_BETTER_DIVING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BETTER_DIVING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BETTER_DIVING)
    public boolean BETTER_DIVING;

    @Config.Name(ConstantsConfigLang.CONFIG_MO_BENDS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MO_BENDS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MO_BENDS)
    public boolean MO_BENDS;

    @Config.Name(ConstantsConfigLang.CONFIG_IDO_SWIMMING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_IDO_SWIMMING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_IDO_SWIMMING)
    public boolean IDO_SWIMMING;

    @Config.Name(ConstantsConfigLang.CONFIG_TROPICRAFT_SWIMMING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_TROPICRAFT_SWIMMING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_TROPICRAFT_SWIMMING)
    public boolean TROPICRAFT_SWIMMING;
}
