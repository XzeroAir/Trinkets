package xzeroair.trinkets.util.config.blocks;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigBlockMoonRose {

    public ConfigBlockMoonRose() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE)
    public int essence_amount = 10;

    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_ESSENCE_COOLDOWN)
    public int essence_cooldown = 300;

}
