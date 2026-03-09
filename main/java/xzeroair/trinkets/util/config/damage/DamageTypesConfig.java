package xzeroair.trinkets.util.config.damage;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.Reference;

public class DamageTypesConfig {

    public DamageTypesConfig() {
        this(false, false, false, false, false, new String[0]);
    }

    public DamageTypesConfig(String... types) {
        this(false, false, false, false, false, types);
    }

    public DamageTypesConfig(boolean fireImmune, boolean explosionImmune, boolean magicImmune, boolean projectImmune, boolean indirectImmune) {
        this(fireImmune, explosionImmune, magicImmune, projectImmune, indirectImmune, new String[0]);
    }

    public DamageTypesConfig(boolean fireImmune, boolean explosionImmune, boolean magicImmune, boolean projectImmune, boolean indirectImmune, String... types) {
        if (types == null) {
            types = new String[0];
        }
        damageTypes = types;
        fire = fireImmune;
        explosion = explosionImmune;
        magic = magicImmune;
        projectile = projectImmune;
        indirect = indirectImmune;
    }

    @Config.Comment("Immune to Fire Damage Types")
    @Config.Name("00. Fire")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.fire")
    public boolean fire;

    @Config.Comment("Immune to Explosion Damage Types")
    @Config.Name("01. Explosion")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.explosion")
    public boolean explosion;

    @Config.Comment("Immune to Magic Damage Types")
    @Config.Name("02. Magic")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.magic")
    public boolean magic;

    @Config.Comment("Immune to All Projectile Damage Types")
    @Config.Name("03. Projectile")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.projectile")
    public boolean projectile;

    @Config.Comment("Immune to All Indirect Damage Types")
    @Config.Name("04. Indirect")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.indirect")
    public boolean indirect;

    @Config.Comment("Immune Damage Types")
    @Config.Name("05. Types")
    @Config.LangKey(Reference.MODID + ".config.races" + ".immunities.types")
    public String[] damageTypes;
}
