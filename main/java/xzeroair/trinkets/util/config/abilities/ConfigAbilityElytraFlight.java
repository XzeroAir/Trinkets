package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityElytraFlight {

    public ConfigAbilityElytraFlight() {
        this(true, true, true, 0F, 10F, 0.42D);
    }

    public ConfigAbilityElytraFlight(boolean enabled, boolean liftEnabled, float cost, float liftCost, double liftStrength) {
        this(enabled, liftEnabled, true, cost, liftCost, liftStrength);
    }

    public ConfigAbilityElytraFlight(boolean enabled, boolean liftEnabled, boolean collisionDamage, float cost, float liftCost, double liftStrength) {
        this.ENABLED = enabled;
        this.LIFT_ENABLED = liftEnabled;
        this.COLLISION_DAMAGE = collisionDamage;
        this.COST = cost;
        this.LIFT_COST = liftCost;
        this.LIFT_STRENGTH = liftStrength;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_ENABLED)
    public boolean LIFT_ENABLED;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_MAGIC_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_COST)
    public float COST;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_COST)
    @Config.RangeDouble(min = 0)
    public float LIFT_COST;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_LIFT_STRENGTH)
    @Config.RangeDouble(min = 0)
    public double LIFT_STRENGTH;

    @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_ELYTRA_FLIGHT_COLLISION_DAMAGE)
    public boolean COLLISION_DAMAGE;

}
