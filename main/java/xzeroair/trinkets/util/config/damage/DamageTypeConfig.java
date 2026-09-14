package xzeroair.trinkets.util.config.damage;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class DamageTypeConfig {

    @Config.Name(ConstantsConfigLang.CONFIG_DAMAGE_INDIRECT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_DAMAGE_INDIRECT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_DAMAGE_INDIRECT)
    public String[] INDIRECT_DAMAGE_TYPES = {"*;isIndirect", "*;isExplosion", "*;isMagic", "*;isProjectile"};
}
