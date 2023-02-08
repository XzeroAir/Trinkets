package xzeroair.trinkets.items.foods;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.items.base.FoodBase;

public class Mana_Candy extends FoodBase {

	public Mana_Candy(String name) {
		super(name, 2, 1f);
		this.setAlwaysEdible();
		this.setUUID("892f3d8b-cd9a-4efd-9c90-c06e9930eff2");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 16;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelResourceLocation full_stack = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		ModelResourceLocation three_forths = new ModelResourceLocation(this.getRegistryName().toString() + "2", "inventory");
		ModelResourceLocation one_half = new ModelResourceLocation(this.getRegistryName().toString() + "3", "inventory");
		ModelResourceLocation one_forth = new ModelResourceLocation(this.getRegistryName().toString() + "4", "inventory");
		ModelBakery.registerItemVariants(this, full_stack, three_forths, one_half, one_forth);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			if (stack.getCount() >= (stack.getMaxStackSize() * 0.75)) {
				return one_forth;
			} else if (stack.getCount() >= (stack.getMaxStackSize() * 0.5)) {
				return one_half;
			} else if (stack.getCount() >= (stack.getMaxStackSize() * 0.25)) {
				return three_forths;
			} else {
				return full_stack;
			}
		});
	}
}
