package xzeroair.trinkets.compatibility;

import net.minecraftforge.fml.common.Loader;

public class ModCompat {

    public static final ModCompat INSTANCE = new ModCompat();

    public ModCompat() {
        this.preInitChecks();
    }

    public boolean Baubles = false;
    public boolean ArtemisLib = false;
    public boolean ToughAsNails = false;
    public boolean SimpleDifficulty = false;
    public boolean FirstAid = false;
    public boolean ElenaiDodge1 = false;
    public boolean ElenaiDodge2 = false;
    public boolean EnhancedVisuals = false;
    public boolean IceAndFire = false;
    public boolean FireResistanceTiers = false;
    public boolean BetterDiving = false;
    public boolean SoManyEnchantments = false;
    public boolean MoBends = false;
    public boolean LycanitesMobs = false;
    public boolean DefiledLands = false;

    // Unnecessary fixes on my end.
    public boolean InventoryHUD = false;

    public void preInitChecks() {

        this.Baubles = Loader.isModLoaded("baubles");
        this.ArtemisLib = Loader.isModLoaded("artemislib");
        this.ToughAsNails = Loader.isModLoaded("toughasnails");
        this.SimpleDifficulty = Loader.isModLoaded("simpledifficulty");
        this.FirstAid = Loader.isModLoaded("firstaid");
        this.ElenaiDodge1 = Loader.isModLoaded("elenaidodge");
        this.ElenaiDodge2 = Loader.isModLoaded("elenaidodge2");
        this.EnhancedVisuals = Loader.isModLoaded("enhancedvisuals");
        this.IceAndFire = Loader.isModLoaded("iceandfire");
        this.FireResistanceTiers = Loader.isModLoaded("fireresistancetiers");
        this.BetterDiving = Loader.isModLoaded("better_diving");
        this.SoManyEnchantments = Loader.isModLoaded("somanyenchantments");
        this.MoBends = Loader.isModLoaded("mobends");
        this.LycanitesMobs = Loader.isModLoaded("lycanitesmobs");
        this.DefiledLands = Loader.isModLoaded("defiledlands");

        this.InventoryHUD = Loader.isModLoaded("inventoryhud");

    }

    public class ModNames {
        public static final String LANG_NAME_BAUBLES = "Baubles";
        public static final String LANG_NAME_ARTEMIS = "Artemis Lib";
        public static final String LANG_NAME_TOUGH_AS_NAILS = "itemGroup.tabToughAsNails";
        public static final String LANG_NAME_SIMPLE_DIFFICULTY = "itemGroup.tabSimpleDifficulty";
        public static final String LANG_NAME_FIRST_AID = "itemGroup.firstaid";
        public static final String LANG_NAME_ELENAI_DODGE = "Elenai Dodge";
        public static final String LANG_NAME_ELENAI_DODGE_2 = "Elenai Dodge 2";
        public static final String LANG_NAME_ENHANCED_VISUALS = "Enhanced Visuals";
        public static final String LANG_NAME_ICE_AND_FIRE = "Ice and Fire";
        public static final String LANG_NAME_BETTER_DIVING = "Better Diving";
        public static final String LANG_NAME_FIRE_RESISTANCE_TIERS = "Fire Resistance Tiers";
        public static final String LANG_NAME_SO_MANY_ENCHANTS = "So Many Enchantments";
        public static final String LANG_NAME_MO_BENDS = "Mo Bends";
        public static final String LANG_NAME_LYCANITES_MOBS = "Lycanites Mobs";
        public static final String LANG_NAME_INVENTORY_HUD = "Inventory Hud";
        public static final String LANG_NAME_DEFILED_LANDS = "Defiled Lands";
    }
}
