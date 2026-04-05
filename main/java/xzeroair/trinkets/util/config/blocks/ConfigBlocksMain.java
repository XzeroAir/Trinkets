package xzeroair.trinkets.util.config.blocks;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigBlocksMain {

    public ConfigBlocksMain() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS_MOON_ROSE)
    public ConfigBlockMoonRose MOON_ROSE = new ConfigBlockMoonRose();

    @Config.Ignore
    @Config.Name(ConstantsConfigLang.CONFIG_BLOCKS_TEDDY_BEAR_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_BLOCKS_TEDDY_BEAR_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_BLOCKS_TEDDY_BEAR)
    public ConfigBlockTeddyBear TEDDY_BEAR = new ConfigBlockTeddyBear();

}
