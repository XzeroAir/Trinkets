package xzeroair.trinkets.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;

public class Itemglowing_ingot extends Item {
	
	public Itemglowing_ingot() {
		
		setUnlocalizedName(Reference.TrinketsItems.GLOWINGINGOT.getUnlocalizedName());
		setRegistryName(Reference.TrinketsItems.GLOWINGINGOT.getRegistryName());
		
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}