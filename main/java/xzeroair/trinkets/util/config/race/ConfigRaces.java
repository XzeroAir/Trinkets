package xzeroair.trinkets.util.config.race;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.races.human.config.HumanConfig;
import xzeroair.trinkets.races.taurus.config.TaurusConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.ConstantsConfigLang;

public class ConfigRaces {

    public ConfigRaces() {
    }

    @Config.RequiresWorldRestart
    @Config.Name("00. " + ConstantsConfigLang.CONFIG_RACES_MENU_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MENU_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MENU)
    public boolean SELECTION_MENU = false;

    @Config.RequiresWorldRestart
    @Config.Name("01. " + ConstantsConfigLang.CONFIG_RACES_MENU_BLACKLIST_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_MENU_BLACKLIST_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_MENU_BLACKLIST)
    public String[] BLACKLIST = {"Dragon", "Taurus"};

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_HUMAN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_HUMAN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_HUMAN)
    public HumanConfig HUMAN = new HumanConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_ELF_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_ELF_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_ELF)
    public ElfConfig ELF = new ElfConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_DWARF_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_DWARF_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_DWARF)
    public DwarfConfig DWARF = new DwarfConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_FAIRY_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAIRY_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAIRY)
    public FairyConfig FAIRY = new FairyConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_FAELIS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_FAELIS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_FAELIS)
    public FaelisConfig FAELIS = new FaelisConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_GOBLIN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_GOBLIN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_GOBLIN)
    public GoblinConfig GOBLIN = new GoblinConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_TITAN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_TITAN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_TITAN)
    public TitanConfig TITAN = new TitanConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_TAURUS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_TAURUS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_TAURUS)
    public TaurusConfig TAURUS = new TaurusConfig();

    @Config.Name(ConstantsConfigLang.CONFIG_RACES_DRAGON_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_RACES_DRAGON_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_RACES_DRAGON)
    public DragonConfig DRAGON = new DragonConfig();


}
