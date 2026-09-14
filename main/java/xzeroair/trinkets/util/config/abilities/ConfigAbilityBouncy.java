package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilityBouncy {

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. Bounce Multiplier")
    @Config.Comment("Multiplier applied to landing velocity when bouncing. Lower values make each bounce decay faster.")
    @Config.RangeDouble(min = 0, max = 2)
    public float BOUNCE_MULTIPLIER = 0.8F;

    @Config.Name("02. Fall Distance Multiplier")
    @Config.Comment("Minimum bounce velocity gained per block of fall distance when landing velocity is unavailable.")
    @Config.RangeDouble(min = 0, max = 2)
    public float FALL_DISTANCE_MULTIPLIER = 0.1F;

    @Config.Name("03. Maximum Bounce Velocity")
    @Config.Comment("Maximum upward velocity a bounce can apply.")
    @Config.RangeDouble(min = 0, max = 10)
    public float MAX_BOUNCE_VELOCITY = 1.5F;

    @Config.Name("04. Minimum Repeat Bounce Velocity")
    @Config.Comment("A bounce weaker than this will not trigger another bounce when it lands.")
    @Config.RangeDouble(min = 0, max = 10)
    public float MINIMUM_REPEAT_BOUNCE_VELOCITY = 0.4F;

    @Config.Name("05. Minimum Fall Distance")
    @Config.Comment("Bounce only begins after falling farther than this distance.")
    @Config.RangeDouble(min = 0, max = 100)
    public float MINIMUM_FALL_DISTANCE = 2F;

    @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_BOUNCY_FALL_DAMAGE_MULTIPLIER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_BOUNCY_FALL_DAMAGE_MULTIPLIER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_BOUNCY_FALL_DAMAGE_MULTIPLIER)
    @Config.RangeDouble(min = 0, max = 1)
    public float FALL_DAMAGE_MULTIPLIER = 0F;
}
