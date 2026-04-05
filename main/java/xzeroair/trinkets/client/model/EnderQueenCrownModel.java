package xzeroair.trinkets.client.model;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

@SideOnly(Side.CLIENT)
public class EnderQueenCrownModel {

    public static EnderQueenCrownModel INSTANCE = new EnderQueenCrownModel();

    @SideOnly(Side.CLIENT)
    private final BipedJsonModel HELMET_CROWN;
    @SideOnly(Side.CLIENT)
    private final BipedJsonModel CROWN;

    public EnderQueenCrownModel() {
        this.CROWN = new BipedJsonModel(new ResourceLocation(Reference.MODID, TrinketsRegistryNames.ModItems.ENDER_TIARA + "_model"));
        this.HELMET_CROWN = new BipedJsonModel(new ResourceLocation(Reference.MODID, TrinketsRegistryNames.ModItems.ENDER_TIARA + "_model"));
    }

    @SideOnly(Side.CLIENT)
    public BipedJsonModel getAccessory() {
        return this.CROWN;
    }

    @SideOnly(Side.CLIENT)
    public BipedJsonModel getHelmet() {
        return this.HELMET_CROWN;
    }
}
