package xzeroair.trinkets.util;

public class TrinketsRegistryNames {

    public static final TrinketsRegistryNames INSTANCE = new TrinketsRegistryNames();
    public static final String MODID = Reference.MODID;

    public TrinketsRegistryNames() {
    }

    public static class ModElements {

        /// LITERALS
        //@formatter:off
        public static final String NEUTRAL                          = "neutral";
        public static final String EARTH                            = "earth";
        public static final String WATER                            = "water";
        public static final String FIRE                             = "fire";
        public static final String AIR                              = "air";
        public static final String ICE                              = "ice";
        public static final String LIGHTNING                        = "lightning";
        public static final String POISON                           = "poison";
        public static final String LIGHT                            = "light";
        public static final String DARK                             = "dark";
        public static final String VOID                             = "void";
        //@formatter:on

        public static class ModElementsReg {
            /// REG NAMES
            //@formatter:off
            public static final String REG_NEUTRAL                      = MODID + ":" + NEUTRAL;
            public static final String REG_EARTH                        = MODID + ":" + EARTH;
            public static final String REG_WATER                        = MODID + ":" + WATER;
            public static final String REG_FIRE                         = MODID + ":" + FIRE;
            public static final String REG_AIR                          = MODID + ":" + AIR;
            public static final String REG_ICE                          = MODID + ":" + ICE;
            public static final String REG_LIGHTNING                    = MODID + ":" + LIGHTNING;
            public static final String REG_POISON                       = MODID + ":" + POISON;
            public static final String REG_LIGHT                        = MODID + ":" + LIGHT;
            public static final String REG_DARK                         = MODID + ":" + DARK;
            public static final String REG_VOID                         = MODID + ":" + VOID;
            //@formatter:on
        }

    }

    public static class ModRaces {

        /// LITERALS
        //@formatter:off
        public static final String HUMAN                            = "human";
        public static final String ELF                              = "elf";    /// 1
        public static final String FAIRY                            = "fairy";  /// 2
        public static final String DWARF                            = "dwarf";  /// 3
        public static final String GOBLIN                           = "goblin"; /// 4
        public static final String TITAN                            = "titan";  /// 5
        public static final String FAELIS                           = "faelis"; /// 6
        public static final String DRAGON                           = "dragon"; /// 7
        public static final String TAURUS                           = "taurus"; /// 8
        //@formatter:on

        public static class ModRacesReg {
            /// REG NAMES
            //@formatter:off
            public static final String REG_HUMAN                        = MODID + ":" + HUMAN;
            public static final String REG_ELF                          = MODID + ":" + ELF;
            public static final String REG_FAIRY                        = MODID + ":" + FAIRY;
            public static final String REG_DWARF                        = MODID + ":" + DWARF;
            public static final String REG_GOBLIN                       = MODID + ":" + GOBLIN;
            public static final String REG_TITAN                        = MODID + ":" + TITAN;
            public static final String REG_FAELIS                       = MODID + ":" + FAELIS;
            public static final String REG_DRAGON                       = MODID + ":" + DRAGON;
            public static final String REG_TAURUS                       = MODID + ":" + TAURUS;
            //@formatter:on
        }
    }

    public static class ModAbilities {

        //@formatter:off
        public static final String BREATH_VOID					="breath_" + ModElements.VOID;
        public static final String BREATH_LIGHT					="breath_" + ModElements.LIGHT;
        public static final String BREATH_DARK					="breath_" + ModElements.DARK;
        public static final String BREATH_POISON				="breath_" + ModElements.POISON;
        public static final String BREATH_EARTH					="breath_" + ModElements.EARTH;
        public static final String BREATH_WATER					="breath_" + ModElements.WATER;
        public static final String BREATH_AIR					="breath_" + ModElements.AIR;
        public static final String BREATH_FIRE					="breath_" + ModElements.FIRE;
        public static final String BREATH_ICE					="breath_" + ModElements.ICE;
        public static final String BREATH_LIGHTNING				="breath_" + ModElements.LIGHTNING;
        public static final String BREATH_DRAGON					="breath_dragon";

        public static final String IMMUNITY_VOID				="immunity_" + ModElements.VOID;
        public static final String IMMUNITY_LIGHT				="immunity_" + ModElements.LIGHT;
        public static final String IMMUNITY_DARK				="immunity_" + ModElements.DARK;
        public static final String IMMUNITY_POISON				="immunity_" + ModElements.POISON;
        public static final String IMMUNITY_EARTH				="immunity_" + ModElements.EARTH;
        public static final String IMMUNITY_WATER				="immunity_" + ModElements.WATER;
        public static final String IMMUNITY_AIR				    ="immunity_" + ModElements.AIR;
        public static final String IMMUNITY_FIRE				="immunity_" + ModElements.FIRE;
        public static final String IMMUNITY_ICE					="immunity_" + ModElements.ICE;
        public static final String IMMUNITY_LIGHTNING			="immunity_" + ModElements.LIGHTNING;

        public static final String AFFINITY_VOID				="affinity_" + ModElements.VOID;
        public static final String AFFINITY_LIGHT				="affinity_" + ModElements.LIGHT;
        public static final String AFFINITY_DARK				="affinity_" + ModElements.DARK;
        public static final String AFFINITY_POISON				="affinity_" + ModElements.POISON;
        public static final String AFFINITY_EARTH				="affinity_" + ModElements.EARTH;
        public static final String AFFINITY_WATER				="affinity_" + ModElements.WATER;
        public static final String AFFINITY_AIR				    ="affinity_" + ModElements.AIR;
        public static final String AFFINITY_FIRE				="affinity_" + ModElements.FIRE;
        public static final String AFFINITY_ICE					="affinity_" + ModElements.ICE;
        public static final String AFFINITY_LIGHTNING			="affinity_" + ModElements.LIGHTNING;

        public static final String CREATIVE_FLIGHT				="creative_flight";
        public static final String ELYTRA_FLIGHT					="elytra_flight";
        public static final String GREEDY_EYES					="greedy_eyes";
        public static final String NIGHT_VISION 				="night_vision";
        public static final String CLIMBING						="climbing";
        public static final String RESTORATION_FIELD				="mending_bloom";
        public static final String WEIGHTLESS					="weightless";
        public static final String WELL_RESTED					="well_rested";
        public static final String NULLIFY_KINETIC				="nullify_kinetic";
        public static final String REDUCE_KINETIC				="reduce_kinetic";
        public static final String SAFE_GUARD					="safe_guard";
        public static final String ENDER_QUEEN					="ender_queen";
        public static final String VICIOUS_STRIKE				="vicious_strike";
        public static final String MAGNETIC						="magnetic";
        public static final String REPEL						="repel";
        public static final String DODGING						="dodging";
        public static final String LIGHTNING_BOLT				="lightning_bolt";
        public static final String FROST_WALKER					="frost_walker";
        public static final String LARGE_HANDS					="large_hands";
        public static final String HEAVY						="heavy";
        public static final String WOLF_RIDER				    ="wolf_rider";
        public static final String SKILLED_ARCHER				="skilled_archer";
        public static final String SKILLED_MINER				="skilled_miner";
        public static final String SKILLED_RIDER				="skilled_rider";
        public static final String SKILLED_SWIMMER				="skilled_swimmer";

        /// UNUSED
        public static final String STAMPEDE						="stampede";
        public static final String FAST_REFLEXES				="fast_reflexes";
        public static final String LEVELING						="leveling";

        // External Mods
        public static final String SURVIVAL_HEAT_IMMUNITY		="immunity_heat";
        public static final String SURVIVAL_COLD_IMMUNITY		="immunity_cold";
        public static final String SURVIVAL_THIRST_IMMUNITY		="immunity_thirst";
        public static final String SURVIVAL_PARASITES_IMMUNITY	="immunity_parasites";
        public static final String SURVIVAL_THIRST_ABSORPTION	="absorption_thirst";
        public static final String FIRST_AID_HARD_HEAD    		="hard_head";
        public static final String ENHANCED_VISUALS_BLUR        ="clear_vision";
        public static final String ENHANCED_VISUALS_SPLASH      ="clear_splash";
        public static final String ENHANCED_VISUALS_STATIC      ="ender_eyes";
        //@formatter:on
    }

    public static class ModItems {

        //@formatter:off
        public static final String GLOW_RING                    = "glow_ring";
        public static final String WEIGHTLESS_STONE             = "weightless_stone";
        public static final String INERTIA_NULL_STONE           = "inertia_null_stone";
        public static final String GREATER_INERTIA_STONE        = "greater_inertia_stone";
        public static final String SEA_STONE                    = "sea_stone";
        public static final String POLARIZED_STONE              = "polarized_stone";
        public static final String DRAGONS_EYE                  = "dragons_eye";
        public static final String WITHER_RING                  = "wither_ring";
        public static final String POISON_STONE                 = "poison_stone";
        public static final String ENDER_TIARA                  = "ender_tiara";
        public static final String HONOR_SHIELD                 = "damage_shield";
        public static final String ARCING_ORB                   = "arcing_orb";
        public static final String TEDDY_BEAR                   = "teddy_bear";

        public static final String RING_FAIRY                   = "fairy_ring";
        public static final String RING_TITAN                   = "titan_ring";
        public static final String RING_DWARF                   = "dwarf_ring";
        public static final String RING_GOBLIN                  = "goblin_ring";
        public static final String RING_ELF                     = "elf_ring";
        public static final String RING_FAELIS                  = "faelis_ring";
        public static final String RING_DRAGON                  = "dragon_ring";
        public static final String RING_TAURUS                  = "taurus_ring";

        public static final String FAELIS_CLAWS                 = "faelis_claw";

        public static final String COSMETIC                     = "cosmetic";

        // UNUSED
        public static final String RING_SUCCUBUS                = "succubus_ring";
        public static final String RING_SLIME                   = "slime_ring";
        public static final String DRAGON_SHIELD                = "dragon_shield";
        public static final String LEVELING_DEVICE              = "leveling_device";
        public static final String HORN_OF_PLENTY               = "horn_of_plenty";
        public static final String GLITTER_BOW                  = "glitter_bow";
        public static final String RIBBON_BOW                   = "ribbon_bow";
        //@formatter:on

        public static class ModItemsReg {
            /// REG NAMES
            //@formatter:off
            public static final String REG_GLOW_RING                    = MODID + ":" + GLOW_RING;
            public static final String REG_WEIGHTLESS_STONE             = MODID + ":" + WEIGHTLESS_STONE;
            public static final String REG_INERTIA_NULL_STONE           = MODID + ":" + INERTIA_NULL_STONE;
            public static final String REG_GREATER_INERTIA_STONE        = MODID + ":" + GREATER_INERTIA_STONE;
            public static final String REG_SEA_STONE                    = MODID + ":" + SEA_STONE;
            public static final String REG_POLARIZED_STONE              = MODID + ":" + POLARIZED_STONE;
            public static final String REG_DRAGONS_EYE                  = MODID + ":" + DRAGONS_EYE;
            public static final String REG_WITHER_RING                  = MODID + ":" + WITHER_RING;
            public static final String REG_POISON_STONE                 = MODID + ":" + POISON_STONE;
            public static final String REG_ENDER_TIARA                  = MODID + ":" + ENDER_TIARA;
            public static final String REG_HONOR_SHIELD                 = MODID + ":" + HONOR_SHIELD;
            public static final String REG_ARCING_ORB                   = MODID + ":" + ARCING_ORB;
            public static final String REG_TEDDY_BEAR                   = MODID + ":" + TEDDY_BEAR;

            public static final String REG_RING_FAIRY                   = MODID + ":" + RING_FAIRY;
            public static final String REG_RING_TITAN                   = MODID + ":" + RING_TITAN;
            public static final String REG_RING_DWARF                   = MODID + ":" + RING_DWARF;
            public static final String REG_RING_GOBLIN                  = MODID + ":" + RING_GOBLIN;
            public static final String REG_RING_ELF                     = MODID + ":" + RING_ELF;
            public static final String REG_RING_FAELIS                  = MODID + ":" + RING_FAELIS;
            public static final String REG_RING_DRAGON                  = MODID + ":" + RING_DRAGON;
            public static final String REG_RING_TAURUS                  = MODID + ":" + RING_TAURUS;

            public static final String REG_FAELIS_CLAWS                 = MODID + ":" + FAELIS_CLAWS;

            public static final String REG_COSMETIC                     = MODID + ":" + COSMETIC;

            // UNUSED
            public static final String REG_RING_SUCCUBUS                = MODID + ":" + RING_SUCCUBUS;
            public static final String REG_DRAGON_SHIELD                = MODID + ":" + DRAGON_SHIELD;
            public static final String REG_LEVELING_DEVICE              = MODID + ":" + LEVELING_DEVICE;
            public static final String REG_HORN_OF_PLENTY               = MODID + ":" + HORN_OF_PLENTY;
            public static final String REG_GLITTER_BOW                  = MODID + ":" + GLITTER_BOW;
            public static final String REG_RIBBON_BOW                   = MODID + ":" + RIBBON_BOW;
            //@formatter:on
        }
    }

    public static class ModCrafting {

        //@formatter:off
        public static final String glowing_powder               = "glowing_powder";
        public static final String glowing_ingot                = "glowing_ingot";
        public static final String glowing_gem                  = "glowing_gem";

        public static final String spark_powder                 = "spark_powder";
        //@formatter:on

    }

    public static class ModFood {

        //@formatter:off
        public static final String FOOD_DWARF                   = ModRaces.DWARF + "_stout";
        public static final String FOOD_ELF                     = ModRaces.ELF + "_sap";
        public static final String FOOD_FAELIS                  = ModRaces.FAELIS + "_food";
        public static final String FOOD_FAIRY                   = ModRaces.FAIRY + "_dew";
        public static final String FOOD_GOBLIN                  = ModRaces.GOBLIN + "_soup";
        public static final String FOOD_TITAN                   = ModRaces.TITAN + "_spirit";
        public static final String FOOD_DRAGON                  = ModRaces.DRAGON + "_gem";
        public static final String FOOD_TAURUS                  = ModRaces.TAURUS + "_tea";

        public static final String FOOD_MANA_CANDY              = "mana_candy";
        public static final String FOOD_MANA_CRYSTAL            = "mana_crystal";
        public static final String FOOD_MANA_REAGENT            = "mana_reagent";

        public static final String FOOD_RESTORATION_SERUM       = "restoration_serum";
        //@formatter:on

    }

    public static class ModPotions {

        //@formatter:off
        public static final String POTION_GLOWING               = "glowing";
        public static final String POTION_sparkling             = "sparkling";
        public static final String POTION_glittering            = "glittering";
        //@formatter:on

    }

    public static class ModDamageTypes {

        //@formatter:off
        public static final String POISON                       = "damage_poison";
        public static final String WATER                        = "damage_water";
        public static final String BLEED                        = "damage_bleed";
        //@formatter:on

    }

    public static class ModBlocks {

        //@formatter:off
        public static final String MOON_ROSE                    = "moon_rose";
        public static final String TEDDY_BEAR                   = "teddy_bear";
        public static final String COOLED_MAGMA                 = "cooled_magma";

        public static final String REG_MOON_ROSE                = MODID + ":" + MOON_ROSE;
        public static final String REG_TEDDY_BEAR               = MODID + ":" + TEDDY_BEAR;
        public static final String REG_COOLED_MAGMA             = MODID + ":" + COOLED_MAGMA;
        //@formatter:on

    }

    public static class ModEntities {

        //@formatter:off
        public static final String ALPHA_WOLF                   = "AlphaWolf";
        public static final String DRAGON_BREATH                = "DragonBreath";
        public static final String AREA_EFFECT                  = "AreaEffect";
        public static final String REG_ALPHA_WOLF               = MODID + ":" + ALPHA_WOLF;
        public static final String REG_DRAGON_BREATH            = MODID + ":" + DRAGON_BREATH;
        public static final String REG_AREA_EFFECT              = MODID + ":" + AREA_EFFECT;
        //@formatter:on

    }

    public static class ModSounds {

        //@formatter:off
        public static final String ARA_ARA                      = "ara_ara";
        public static final String UWU                          = "uwu";
        public static final String REG_ARA_ARA                  = MODID + ":" + ARA_ARA;
        public static final String REG_UWU                      = MODID + ":" + UWU;
        //@formatter:on

    }

}
