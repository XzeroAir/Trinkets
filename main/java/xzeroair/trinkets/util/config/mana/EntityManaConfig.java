package xzeroair.trinkets.util.config.mana;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class EntityManaConfig {

    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ENABLED_COMMENT)
    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ENABLED_NAME)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ENABLED)
    public boolean mana_enabled = true;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_REGEN_FREQUENCY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_REGEN_FREQUENCY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_REGEN_FREQUENCY)
    public int mana_update_ticks = 20;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_REGEN_TIMEOUT_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_REGEN_TIMEOUT_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_REGEN_TIMEOUT)
    public int mana_regen_timeout = 60;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_BONUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_BONUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_BONUS)
    @Config.RangeDouble(min = 0)
    public float bonus = 10;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_BONUS_MAX_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_BONUS_MAX_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_BONUS_MAX)
    @Config.RangeInt(min = 0)
    public int bonus_max = 90;

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_RECOVERY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_RECOVERY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_RECOVERY)
    public String[] recovery = new String[]{
            //@formatter:off
			"xat:dwarf_stout;*;100%",
			"xat:elf_sap;*;100%",
			"xat:faelis_food;*;100%",
			"xat:fairy_dew;*;100%",
			"xat:goblin_soup;*;100%",
			"xat:titan_spirit;*;100%",
			"xat:taurus_tea;*;100%",
			"xat:dragon_gem;*;100%",
			"xat:mana_crystal;*;100%",
			"xat:mana_reagent;*;100%",
			"xat:mana_candy;*;50",
			"minecraft:golden_apple;0;20",
			"minecraft:golden_apple;1;50",
			"simpledifficulty:juice;0;10%",
			"simpledifficulty:juice;5;50%",
			"botania:manacookie;0;10%",
			//@formatter:on
    };

    @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS)
    public MagicItemsConfig MAGIC_ITEMS = new MagicItemsConfig();

    public class MagicItemsConfig {

        @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL)
        public ManaCrystalConfig MAGIC_CRYSTAL = new ManaCrystalConfig();

        public class ManaCrystalConfig {

            @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_CRYSTAL_EXPLODE)
            public boolean crystalExplodes = false;

        }

        @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT)
        public ManaReagentConfig MAGIC_REAGENT = new ManaReagentConfig();

        public class ManaReagentConfig {

            @Config.Name(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL_NAME)
            @Config.Comment(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL_COMMENT)
            @Config.LangKey(ConstantsConfigLang.CONFIG_MAGIC_ITEMS_REAGENT_HARMFUL)
            public boolean reagentHarmful = true;

        }

    }

}
