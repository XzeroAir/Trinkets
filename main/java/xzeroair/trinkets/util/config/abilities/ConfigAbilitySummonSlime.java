package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.entity.wrapper.ShoulderDismountMode;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySummonSlime {

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. Health Cost Percentage")
    @Config.Comment("Fraction of the summoner's maximum health paid when summoning a Slime. 0.25 costs 25%.")
    @Config.RangeDouble(min = 0, max = 1)
    public float HEALTH_COST_PERCENTAGE = 0.25F;

    @Config.Name("02. Shoulder Dismount Mode")
    @Config.Comment({"How a perched Slime summon dismounts. SNEAK releases while sneaking; VANILLA releases after a fall, in water, while riding, or while flying."})
    public ShoulderDismountMode SHOULDER_DISMOUNT_MODE = ShoulderDismountMode.SNEAK;

    @Config.Name("03. Health Per Kill")
    @Config.Comment("Maximum and current health gained when the Slime summon deals a killing blow.")
    @Config.RangeDouble(min = 0, max = 100)
    public float HEALTH_PER_KILL = 2.0F;

    @Config.Name("04. Maximum Summon Health")
    @Config.Comment("Maximum health the Slime summon can reach through direct kills.")
    @Config.RangeDouble(min = 1, max = 1000)
    public float MAXIMUM_SUMMON_HEALTH = 40.0F;

    @Config.Name("05. Follow Teleport Distance")
    @Config.Comment("Distance in blocks at which a Slime summon safely teleports near its summoner.")
    @Config.RangeDouble(min = 8, max = 128)
    public float FOLLOW_TELEPORT_DISTANCE = 24.0F;

    @Config.Name("06. Kill Regeneration Duration")
    @Config.Comment("Regeneration duration in ticks granted after a Slime summon deals a killing blow.")
    @Config.RangeInt(min = 0, max = 1200)
    public int KILL_REGENERATION_DURATION = 100;

    @Config.Name("07. Slimeball Healing")
    @Config.Comment("Health restored when the summoner uses the configured food item.")
    @Config.RangeDouble(min = 0, max = 100)
    public float SLIMEBALL_HEALING = 4.0F;

    @Config.Name("09. Maximum Retained Summons")
    @Config.Comment("Maximum number of Slime summons a player may retain, whether active or unsummoned. A Slime that dies frees its slot.")
    @Config.RangeInt(min = 1, max = 12)
    public int MAXIMUM_ACTIVE_SUMMONS = 3;

    @Config.Name("10. Summon Food Item")
    @Config.Comment("ConfigObject item entry used to heal the Slime summon. Format: namespace:item;metadata, with * for any metadata.")
    public String SUMMON_FOOD_ITEM = "minecraft:slime_ball;*";
}
