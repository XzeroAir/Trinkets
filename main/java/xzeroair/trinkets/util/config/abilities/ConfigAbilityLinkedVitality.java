package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityLinkedVitality {

    public ConfigAbilityLinkedVitality() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DAMAGE_SHARE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DAMAGE_SHARE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DAMAGE_SHARE)
    @Config.RangeDouble(min = 0, max = 1)
    public float DAMAGE_SHARE = 0.5F;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_BALANCE_HEALTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_BALANCE_HEALTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_BALANCE_HEALTH)
    public boolean BALANCE_HEALTH = false;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PROTECT_PARTNER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PROTECT_PARTNER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PROTECT_PARTNER)
    public boolean PROTECT_PARTNER = false;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_EXPERIENCE_BONUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_EXPERIENCE_BONUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_EXPERIENCE_BONUS)
    @Config.RangeDouble(min = 0, max = 10)
    public float EXPERIENCE_BONUS = 0.25F;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_DAMAGE)
    public boolean DIRECT_DAMAGE = false;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_REQUIRE_NEARBY_PARTNER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_REQUIRE_NEARBY_PARTNER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_REQUIRE_NEARBY_PARTNER)
    public boolean REQUIRE_NEARBY_PARTNER = false;

    @Config.Name("07. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PARTNER_DAMAGE_MULTIPLIER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PARTNER_DAMAGE_MULTIPLIER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_PARTNER_DAMAGE_MULTIPLIER)
    public float PARTNER_DAMAGE_MULTIPLIER = 1.5F;

    @Config.Name("08. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_SHARE_HEALING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_SHARE_HEALING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_SHARE_HEALING)
    public boolean SHARE_HEALING = false;

    @Config.Name("09. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_HEAL_SHARE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_HEAL_SHARE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_HEAL_SHARE)
    @Config.RangeDouble(min = 0, max = 1)
    public float HEAL_SHARE = 0.5F;

    @Config.Name("10. " + ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_HEAL_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_HEAL_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_LINKED_VITALITY_DIRECT_HEAL)
    public boolean DIRECT_HEAL = false;

    public static class ClientSettings {

        @Config.Name(ConstantsConfigLang.CONFIG_RENDERING_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_RENDERING_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_RENDERING)
        public boolean SHOWN = true;

        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_X_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_X_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_X)
        public double translatedX = 0.88D;

        @Config.Name(ConstantsConfigLang.CONFIG_LOCATION_Y_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_LOCATION_Y_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_LOCATION_Y)
        public double translatedY = 0.62D;

        @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_MODE)
        public boolean usePixelPosition = false;

        @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_X)
        public int xPixels = 0;

        @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_HUD_POSITION_PIXELS_Y)
        public int yPixels = 0;
    }
}
