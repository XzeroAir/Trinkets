package xzeroair.trinkets.util.config.race;

import net.minecraftforge.common.config.Config;

public class RaceMagicConfig {

    public RaceMagicConfig(int affinity) {
        this.affinity = affinity;
    }

    @Config.RequiresMcRestart
    @Config.Name("Magic Affinity")
    @Config.LangKey("xat.entityMagic.affinity")
    public int affinity;

}
