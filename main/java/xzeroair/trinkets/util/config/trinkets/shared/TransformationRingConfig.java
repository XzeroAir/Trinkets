package xzeroair.trinkets.util.config.trinkets.shared;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class TransformationRingConfig {


    public TransformationRingConfig() {
        this("ring");
    }

    public TransformationRingConfig(String type) {
        this.COMPAT = new Compatability(new BaubleCompat(type));
    }

    @Config.RequiresMcRestart
    @Config.Name("00. " + ConstantsConfigLang.REGISTRY_ENABLED_NAME)
    @Config.Comment(ConstantsConfigLang.REGISTRY_ENABLED_COMMENT)
    @Config.LangKey(ConstantsConfigLang.REGISTRY_ENABLED)
    public boolean ENABLED = true;

    @Config.Name(ConstantsConfigLang.CONFIG_COMPAT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_COMPAT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_COMPAT)
    public Compatability COMPAT;

    public class Compatability {

        public Compatability(BaubleCompat BAUBLES) {
            this.BAUBLES = BAUBLES;
        }

        @Config.Name(ConstantsConfigLang.CONFIG_BAUBLES_NAME)
        @Config.Comment({
                //@formatter:off
                "If the mod Baubles is installed what bauble slot should it use",
                "Available Types:",
                "Trinket, Any, All",
                "Amulet, Necklace, Pendant",
                "Ring, Rings",
                "Belt",
                "Head, Hat",
                "Body, Chest",
                "Charm"
                //@formatter:on
        })
        @Config.LangKey(ConstantsConfigLang.CONFIG_BAUBLES)
        public BaubleCompat BAUBLES;
    }
}
