package xzeroair.trinkets.compatibility;

import net.minecraftforge.fml.common.Loader;

public class ModCompat {

    public static ModCompat instance = new ModCompat();

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

    public void preInitChecks() {

        Baubles = Loader.isModLoaded("baubles");
        ArtemisLib = Loader.isModLoaded("artemislib");
        ToughAsNails = Loader.isModLoaded("toughasnails");
        SimpleDifficulty = Loader.isModLoaded("simpledifficulty");
        FirstAid = Loader.isModLoaded("firstaid");
        ElenaiDodge1 = Loader.isModLoaded("elenaidodge");
        ElenaiDodge2 = Loader.isModLoaded("elenaidodge2");
        EnhancedVisuals = Loader.isModLoaded("enhancedvisuals");
        IceAndFire = Loader.isModLoaded("iceandfire");
        FireResistanceTiers = Loader.isModLoaded("fireresistancetiers");
        BetterDiving = Loader.isModLoaded("better_diving");
        SoManyEnchantments = Loader.isModLoaded("somanyenchantments");

    }
}
