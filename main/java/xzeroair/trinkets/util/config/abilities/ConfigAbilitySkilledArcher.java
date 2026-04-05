package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.enums.BowScalingMode;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySkilledArcher {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_ARCHER;

    public ConfigAbilitySkilledArcher() {
        this(20F);
    }

    public ConfigAbilitySkilledArcher(float cost) {
        this.CHARGE_SHOT_COST = cost;
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Comment("how much Mana should this ability initially cost")
    @Config.Name("01. Charge Shot Cost")
    @Config.LangKey(LANG_PREFIX + ".cost")
    public float CHARGE_SHOT_COST;

    @Config.Comment("If a fully charged shot should explode on impact causing splash damage")
    @Config.Name("02. Full Charge Explodes")
    @Config.LangKey(LANG_PREFIX + ".explode")
    public boolean CHARGE_SHOT_EXPLODES = true;

    @Config.Comment("Scaling mode to determine damage")
    @Config.Name("03. Scaling Mode")
    @Config.LangKey(LANG_PREFIX + ".scaling.mode")
    public BowScalingMode SCALING_MODE = BowScalingMode.KINETIC;

    @Config.Comment("How long in ticks before the bow is fully drawn. Modified by Bow weight")
    @Config.Name("04. Draw Time")
    @Config.LangKey(LANG_PREFIX + ".max.draw")
    @Config.RangeInt(min = 12, max = 72000)
    public int CHARGE_SHOT_TIME = 60;

    @Config.Comment("")
    @Config.Name("05. Damage Multiplier")
    @Config.LangKey(LANG_PREFIX + ".base.multiplier")
    public float CHARGE_SHOT_DAMAGE_MULTI = 1.5F;

    @Config.Comment("")
    @Config.Name("06. Minimum Damage Multiplier")
    @Config.LangKey(LANG_PREFIX + ".minimum.damage.multiplier")

    public float CHARGE_SHOT_MIN_DAMAGE_MULTI = 1F;

    @Config.Comment("")
    @Config.Name("07. Mana Usage Bonus")
    @Config.LangKey(LANG_PREFIX + ".mana.damage.multiplier")
    public float CHARGE_SHOT_MANA_DAMAGE_MULTI = 1F;

    @Config.Comment("")
    @Config.Name("08. Default Bow Weight")
    @Config.LangKey(LANG_PREFIX + ".bow.weights.default")
    public float DEFAULT_WEIGHT = 60f;

    @Config.Comment("Items that do not count as a bow")
    @Config.Name("09. Bow Blacklist")
    @Config.LangKey(LANG_PREFIX + ".bow.blacklist")
    public String[] BOW_BLACKLIST = {
            //@formatter:off
            "*:*crossbow*"
            //@formatter:on
    };

    @Config.Comment("Bow Weights to determine damage")
    @Config.Name("10. Bow Weights")
    @Config.LangKey(LANG_PREFIX + ".bow.weights")
    public String[] BOWS = {
            //@formatter:off
            "minecraft:bow;*;40.0",
            "spartanweaponry:longbow_wood;*;70.0"
            //@formatter:on
    };
}
