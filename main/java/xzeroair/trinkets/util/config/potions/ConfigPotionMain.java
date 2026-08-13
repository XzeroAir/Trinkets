package xzeroair.trinkets.util.config.potions;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigPotionMain {

    @Config.RequiresMcRestart
    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_REGISTRY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_REGISTRY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_REGISTRY)
    public boolean ENABLED = true;

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_SURVIVAL_WATER_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_SURVIVAL_WATER_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_SURVIVAL_WATER)
    public boolean THIRST = true;

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_ICE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_ICE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_ICE)
    public PotionConfig ICE_RESISTANCE = new PotionConfig("minecraft:snow", 3600);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_LIGHTNING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_LIGHTNING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RESISTANCE_LIGHTNING)
    public PotionConfig LIGHTNING_RESISTANCE = new PotionConfig("xat:spark_powder", 3600);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_HUMAN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_HUMAN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_HUMAN)
    public PotionConfig HUMAN = new PotionConfig("minecraft:apple", 3600);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_DWARF_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_DWARF_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_DWARF)
    public PotionConfig DWARF = new PotionConfig("minecraft:iron_block", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_ELF_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_ELF_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_ELF)
    public PotionConfig ELF = new PotionConfig("minecraft:leaves", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAIRY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAIRY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAIRY)
    public PotionConfig FAIRY = new PotionConfig("minecraft:ghast_tear", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_GOBLIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_GOBLIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_GOBLIN)
    public PotionConfig GOBLIN = new PotionConfig("minecraft:leather", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_TITAN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_TITAN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_TITAN)
    public PotionConfig TITAN = new PotionConfig("minecraft:golden_apple:0", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAELIS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAELIS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_FAELIS)
    public PotionConfig FAELIS = new PotionConfig("xat:faelis_claw", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON)
    public PotionConfig DRAGON = new PotionConfig("minecraft:dragon_breath", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_FIRE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_FIRE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_FIRE)
    public PotionConfig DRAGON_FIRE = new PotionConfig("minecraft:magma", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_ICE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_ICE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_ICE)
    public PotionConfig DRAGON_ICE = new PotionConfig("minecraft:packed_ice", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_LIGHTNING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_LIGHTNING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_DRAGON_LIGHTNING)
    public PotionConfig DRAGON_LIGHTNING = new PotionConfig("xat:spark_powder", 1200);

    @Config.Name(ConstantsConfigLang.CONFIG_POTIONS_RACE_TAURUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_POTIONS_RACE_TAURUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_POTIONS_RACE_TAURUS)
    public PotionConfig TAURUS = new PotionConfig("minecraft:milk_bucket", 1200);


}
