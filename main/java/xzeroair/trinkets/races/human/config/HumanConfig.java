package xzeroair.trinkets.races.human.config;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.damage.DamageTypesConfig;
import xzeroair.trinkets.util.config.race.RaceMagicConfig;
import xzeroair.trinkets.util.config.race.RaceSizeConfig;

public class HumanConfig {

    private final String name = "human";
    private final String PREFIX = Reference.MODID + ".config.races." + name;

    @Config.Comment("What potion effects is the player immune to")
    @Config.Name("Potion Resistances")
    @Config.LangKey(Reference.MODID + ".config.races" + ".resistances")
    public String[] resistances = {};

    @Config.Comment("What DamageTypes is the player immune to")
    @Config.Name("Damage Immunity")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities")
    public DamageTypesConfig dmgType = new DamageTypesConfig();

    @Config.Comment("What effects to add to the player")
    @Config.Name("Potion Effects")
    @Config.LangKey(Reference.MODID + ".config.races" + ".effects")
    public String[] potEffects = {};

    @Config.Name("Magic")
    @Config.LangKey(Reference.MODID + ".config.magic")
    public final RaceMagicConfig magic = new RaceMagicConfig(100);

    @Config.Name("Size")
    @Config.LangKey(Reference.MODID + ".config.race.size")
    public final RaceSizeConfig size = new RaceSizeConfig(100, 100);

    @Config.Comment({"For More Information on Attributes", "https://minecraft.gamepedia.com/Attribute"})
    @Config.Name("Attributes")
    @Config.LangKey(Reference.MODID + ".config.attributes")
    public String[] attributes = {

    };
}
