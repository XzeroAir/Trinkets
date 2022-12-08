package xzeroair.trinkets.items.foods;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.FoodBase;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class Mana_Crystal extends FoodBase {

	public Mana_Crystal(String name) {
		super(name, 2, 1f);
		this.setAlwaysEdible();
		this.setUUID("5569090a-43b2-468e-918a-05df8570277c");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new OptionEntry("mpmax", "10");
		return helper.formatAddVariables(translation, key);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			magic.setBonusMana(magic.getBonusMana() + 1);
		}
		return super.onItemUseFinish(stack, worldIn, entity);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (TrinketsConfig.SERVER.mana.crystalExplodes) {
			final IBlockState state = worldIn.getBlockState(pos);
			if ((Reference.random.nextInt(10) == 0) && (state.getMaterial() == Material.ROCK) && state.isNormalCube() && (facing == EnumFacing.UP)) {
				if (!worldIn.isRemote) {
					//		System.out.println(facing + " | " + state.getMaterial().toString() + " | " + state.isNormalCube());
					final ItemStack itemstack = player.getHeldItem(hand);
					itemstack.shrink(1);
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, new ItemStack(ModItems.foods.mana_reagent, 1)));
					worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3F, false);
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return EnumActionResult.SUCCESS;
			} else {
				if (!worldIn.isRemote) {
					worldIn.playSound(null, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			}
		} else {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		return super.onItemRightClick(worldIn, player, handIn);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
	}
}
