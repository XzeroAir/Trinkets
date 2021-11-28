package xzeroair.trinkets.items.foods;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
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
		return 32;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		//		if (entityLiving instanceof EntityPlayer) {
		//			EntityProperties prop = Capabilities.getEntityRace(entityLiving);
		//			if (prop != null) {
		//				Random rand = new Random();
		//				int chance = rand.nextInt(10);
		//				float manaAmount = 25f;
		//				if (chance == 0) {
		//					manaAmount = 50f;
		//				}
		//				prop.getMagic().addMana(manaAmount);
		//			}
		//		}
		//		this.setCooldown(20);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		return super.onItemRightClick(worldIn, player, handIn);
		//		final ItemStack itemstack = player.getHeldItem(handIn);
		//		boolean flag = this.getEdible();
		//		System.out.println(flag);
		//		if (flag) {
		//			return super.onItemRightClick(worldIn, player, handIn);
		//			//		if (player.canEat(flag)) {
		//			//			player.setActiveHand(handIn);
		//			//			return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
		//		} else {
		//
		//			return new ActionResult<>(EnumActionResult.FAIL, itemstack);
		//		}
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
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				if (stack.getCount() > 1) {
					if (stack.getCount() >= 48) {
						return one_forth;
					} else if (stack.getCount() >= 32) {
						return one_half;
					} else if (stack.getCount() >= 16) {
						return three_forths;
					} else {
						return full_stack;
					}
				} else {
					return full_stack;
				}
			}
		});
	}
}
