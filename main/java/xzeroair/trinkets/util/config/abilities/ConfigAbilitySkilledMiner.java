package xzeroair.trinkets.util.config.abilities;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigAbilitySkilledMiner {

    private final String LANG_PREFIX = ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER;

    public ConfigAbilitySkilledMiner() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE)
    public boolean fortune = true;

    @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_STACKS)
    public boolean fortune_mix = true;

    @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_REDUCED_REQUIREMENT)
    public boolean skilled_miner = true;

    @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_STATIC_MINING)
    public boolean static_mining = true;

    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS)
    public Blocks BLOCKS = new Blocks();

    public class Blocks {

        @Config.Name("00. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_FORTUNE_WHITELIST)
        public String[] Blocks = new String[]{
                //@formatter:off
                "minecraft:coal_ore",
                "minecraft:lapis_ore",
                "minecraft:diamond_ore",
                "minecraft:redstone_ore",
                "minecraft:lit_redstone_ore",
                "minecraft:emerald_ore",
                "minecraft:quartz_ore"
                //@formatter:on
        };

        @Config.Name("01. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS)
        public boolean bonus_exp = true;

        @Config.Name("02. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MAX)
        public int bonus_exp_max = 2;

        @Config.Name("03. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_BONUS_MIN)
        public int bonus_exp_min = 0;

        @Config.Name("04. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_WHITELIST)
        public String[] xPBlocks = new String[]{
                //@formatter:off
                "minecraft:coal_ore",
                "minecraft:iron_ore",
                "minecraft:gold_ore",
                "minecraft:lapis_ore",
                "minecraft:redstone_ore",
                "minecraft:diamond_ore",
                "minecraft:emerald_ore",
                "minecraft:quartz_ore"
                //@formatter:on
        };

        @Config.Name("05. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN)
        public boolean minXpBlocks = true;

        @Config.Name("06. " + ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ABILITIES_SKILLED_MINER_EXP_GAIN_MIN_WHITELIST)
        public String[] MinBlocks = new String[]{"minecraft:stone", "minecraft:end_stone"};
    }
}
