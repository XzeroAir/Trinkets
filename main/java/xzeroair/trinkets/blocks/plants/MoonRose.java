package xzeroair.trinkets.blocks.plants;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class MoonRose extends BlockBush implements IsModelLoaded {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public MoonRose(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setSoundType(SoundType.PLANT);
	}

	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		return helper.formatAddVariables(translation);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltips, ITooltipFlag flagIn) {
		super.addInformation(stack, world, tooltips, flagIn);
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		for (int i = 1; i < 10; i++) {
			final int index = i;
			final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
			if (!helper.isStringEmpty(string)) {
				tooltips.add(
						string
				);
			}
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		//		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	//
	//	/**
	//	 * Convert the given metadata into a BlockState for this Block
	//	 */
	//	@Override
	//	public IBlockState getStateFromMeta(int meta) {
	//		IBlockState state = this.getDefaultState()
	//				.withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
	//		return state;
	//	}
	//
	//	@Override
	//	public int getMetaFromState(IBlockState state) {
	//		int i = 0;
	//		i |= state.getValue(FACING).getHorizontalIndex();
	//		return i;
	////		return super.getMetaFromState(state);
	//	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.byIndex(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// TODO Remove this method, unused
		super.updateTick(worldIn, pos, state, rand);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return super.getPlant(world, pos);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMoonRose();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		if (stack.hasTagCompound()) {
			final TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityMoonRose) {
				Capabilities.getTEProperties(te, prop -> prop.loadFromNBT(stack.getTagCompound()));
			}
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		final ItemStack stack = new ItemStack(state.getBlock());
		final TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityMoonRose) {
			NBTTagCompound compound = Capabilities.getTEProperties(te, new NBTTagCompound(), (prop, tag) -> {
				prop.saveToNBT(tag);
				return tag;
			});
			stack.setTagCompound(compound);
		}
		drops.add(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

}
