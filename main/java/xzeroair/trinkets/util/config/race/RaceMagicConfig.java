package xzeroair.trinkets.util.config.race;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class RaceMagicConfig {

    public RaceMagicConfig() {
        this(100);
    }

    public RaceMagicConfig(int affinity) {
        this.affinity = affinity;
    }

    @Config.RequiresMcRestart
    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_AFFINITY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_AFFINITY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_AFFINITY)
    public int affinity;

}
