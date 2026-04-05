package xzeroair.trinkets.util.config.trinkets;

import net.minecraftforge.common.config.Config;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.config.trinkets.dragoneye.ConfigDragonsEye;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;

public class TrinketItemsConfig {

    public TrinketItemsConfig() {
    }

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_TRANSFORMATION_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_TRANSFORMATION_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_TRANSFORMATION)
    public ConfigTransformItems TRANSFORMATION = new ConfigTransformItems();

    public class ConfigTransformItems {

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_DWARF_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_DWARF_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_DWARF)
        public TransformationRingConfig DWARF_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_ELF_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_ELF_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_ELF)
        public TransformationRingConfig ELF_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_FAIRY_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_FAIRY_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_FAIRY)
        public TransformationRingConfig FAIRY_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_GOBLIN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_GOBLIN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_GOBLIN)
        public TransformationRingConfig GOBLIN_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_TITAN_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_TITAN_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_TITAN)
        public TransformationRingConfig TITAN_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_FAELIS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_FAELIS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_FAELIS)
        public TransformationRingConfig FAELIS_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_DRAGON_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_DRAGON_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_DRAGON)
        public TransformationRingConfig DRAGON_RING = new TransformationRingConfig();

        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_TAURUS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_TAURUS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_TAURUS)
        public TransformationRingConfig TAURUS_RING = new TransformationRingConfig();

        @Config.Ignore
        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_SUCCUBUS_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_SUCCUBUS_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_SUCCUBUS)
        public TransformationRingConfig SUCCUBUS_RING = new TransformationRingConfig();

        @Config.Ignore
        @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_RING_SLIME_NAME)
        @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_RING_SLIME_COMMENT)
        @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_RING_SLIME)
        public TransformationRingConfig SLIME_RING = new TransformationRingConfig();

    }

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_DRAGONS_EYE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_DRAGONS_EYE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_DRAGONS_EYE)
    public ConfigDragonsEye DRAGON_EYE = new ConfigDragonsEye();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_ENDER_CROWN)
    public ConfigEnderCrown ENDER_CROWN = new ConfigEnderCrown();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_SHIELD_OF_HONOR)
    public ConfigDamageShield DAMAGE_SHIELD = new ConfigDamageShield();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_GLOW_RING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_GLOW_RING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_GLOW_RING)
    public ConfigGlowRing GLOW_RING = new ConfigGlowRing();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_POISON_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_POISON_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_POISON_STONE)
    public ConfigPoisonStone POISON_STONE = new ConfigPoisonStone();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_WITHER_RING_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_WITHER_RING_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_WITHER_RING)
    public ConfigWitherRing WITHER_RING = new ConfigWitherRing();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_POLARIZED_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_POLARIZED_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_POLARIZED_STONE)
    public ConfigPolarizedStone POLARIZED_STONE = new ConfigPolarizedStone();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_SEA_STONE)
    public ConfigSeaStone SEA_STONE = new ConfigSeaStone();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_NULL_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_NULL_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_NULL_STONE)
    public ConfigInertiaNull INERTIA_NULL = new ConfigInertiaNull();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_INERTIA_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_INERTIA_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_INERTIA_STONE)
    public ConfigGreaterInertia GREATER_INERTIA = new ConfigGreaterInertia();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_WEIGHTLESS_STONE_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_WEIGHTLESS_STONE_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_WEIGHTLESS_STONE)
    public ConfigWeightlessStone WEIGHTLESS_STONE = new ConfigWeightlessStone();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_ARCING_ORB_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_ARCING_ORB_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_ARCING_ORB)
    public ConfigArcingOrb ARCING_ORB = new ConfigArcingOrb();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_TEDDY_BEAR_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_TEDDY_BEAR_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_TEDDY_BEAR)
    public ConfigTeddyBear TEDDY_BEAR = new ConfigTeddyBear();

    @Config.Name(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS_NAME)
    @Config.Comment(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS_COMMENT)
    @Config.LangKey(ConstantsConfigLang.CONFIG_ITEMS_FAELIS_CLAWS)
    public ConfigFaelisClaw FAELIS_CLAW = new ConfigFaelisClaw();

    // TODO Fix
    //		@Config.Name("Leveling Device Settings")
    //		@Config.Comment("")
    //		@Config.LangKey(cfgPrefix + "." + ConstantsRegistryIds.ModItems.ExpDevice)
    //		public ConfigExpDevice EXP_DEVICE = new ConfigExpDevice();
}
