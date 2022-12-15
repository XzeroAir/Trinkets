package xzeroair.trinkets.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.blocks.materials.TrinketBlockMaterial;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class BlockTeddyBear extends BlockHorizontal implements IsModelLoaded {

	protected static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 0.8D, 0.8D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.15D, 0.8D, 0.8D, 0.85D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2D, 0.0D, 0.15D, 0.8D, 0.8D, 0.85D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.2D, 0.85D, 0.8D, 0.8D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.15D, 0.0D, 0.2D, 0.85D, 0.8D, 0.8D);
	public static final PropertyEnum<EnumTeddyType> VARIANT = PropertyEnum.<EnumTeddyType>create("variant", EnumTeddyType.class);

	public BlockTeddyBear() {
		super(TrinketBlockMaterial.ClothTeddyBear);
		this.setRegistryName(Reference.MODID, "teddy_bear");
		this.setTranslationKey(this.getRegistryName().toString());
		this.setLightLevel(0);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(VARIANT, EnumTeddyType.TEDDY));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTeddyBear();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;//super.hasTileEntity(state);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, world, tooltip, flagIn);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		IBlockState blockBelow = world.getBlockState(pos.down());
		//		System.out.println(
		//				blockBelow.isBlockNormalCube() + " | " +
		//						blockBelow.isFullBlock() + " | " +
		//						blockBelow.isFullCube() + " | " +
		//						blockBelow.isNormalCube() + " | " +
		//						blockBelow.isTranslucent() + " | " +
		//						blockBelow.getBlock().getLocalizedName()
		//		);
		if (blockBelow.isSideSolid(world, pos.down(), EnumFacing.UP)) {
			return super.canPlaceBlockAt(world, pos);
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		this.checkAndDropBlock(world, pos, state);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		this.checkAndDropBlock(world, pos, state);
	}

	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(world, pos, state)) {
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);//setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
		{
			IBlockState down = world.getBlockState(pos.down());
			if (!down.isSideSolid(world, pos, EnumFacing.UP)) {
				return false;
			}
		}
		return true;//this.canSustainBush(worldIn.getBlockState(pos.down()));
	}

	@Deprecated
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.getValue(VARIANT).getMapColor();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (willHarvest) {
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		//		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		//		System.out.println("Broke Block " + te);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		TileEntity TileEntity = worldIn.getTileEntity(pos);
		if (TileEntity instanceof TileEntityTeddyBear) {
			((TileEntityTeddyBear) TileEntity).setTeddyBear(stack);
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntity TileEntity = world.getTileEntity(pos);
		if (TileEntity instanceof TileEntityTeddyBear) {
			ItemStack stack = ((TileEntityTeddyBear) TileEntity).getTeddyBear();
			if ((stack != null) && !stack.isEmpty()) {
				//				System.out.println("Stacks not Null, Did it drop? " + stack + " | " + stack.getItem());
				drops.add(stack);
			} else {
				//				System.out.println("Stack is Null");
			}
		} else {
			System.out.println("Something went wrong, Please Report this with the circumstances prior to it happening");
		}
		//		} else {
		//			super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.trinkets.TrinketTeddyBear;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(VARIANT, EnumTeddyType.byMetadata(meta));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState()
				.withProperty(FACING, EnumFacing.byHorizontalIndex(meta))
				.withProperty(VARIANT, EnumTeddyType.byMetadata((meta & 15) >> 2));
		return state;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i |= state.getValue(FACING).getHorizontalIndex();
		i |= state.getValue(VARIANT).getMetadata() << 2;
		return i;
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		//		if (!world.isRemote && (entity instanceof EntityLivingBase)) {
		//			if (!((EntityLivingBase) entity).isPotionActive(MobEffects.NIGHT_VISION)) {
		//				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, false, true));
		//			}
		//		}
	}

	private static final AxisAlignedBB HITBOX = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (state.getValue(FACING)) {
		case UP:
		default:
			return DEFAULT_AABB;
		case NORTH:
			return NORTH_AABB;
		case SOUTH:
			return SOUTH_AABB;
		case WEST:
			return WEST_AABB;
		case EAST:
			return EAST_AABB;
		}
		//		return HITBOX;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		return Block.NULL_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 *
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Deprecated
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 *
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever
	 *             possible. Implementing/overriding is fine.
	 */
	@Deprecated
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return super.getLightValue(state, world, pos);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, VARIANT });
	}

	public enum EnumTeddyType implements IStringSerializable {
		TEDDY(0, MapColor.BROWN, "teddy"),
		REMBO(1, MapColor.RED, "rembo"),
		SCARY(2, MapColor.GRAY, "scary"),
		SHIVAXI(3, MapColor.LIGHT_BLUE, "shivaxi");

		/** Array of the Block's BlockStates */
		private static final EnumTeddyType[] META_LOOKUP = new EnumTeddyType[values().length];
		/** The BlockState's metadata. */
		private final int meta;
		/** The EnumType's name. */
		private final String name;
		private final String translationKey;
		private final MapColor mapColor;

		private EnumTeddyType(int meta, MapColor color, String name) {
			this(meta, color, name, name);
		}

		private EnumTeddyType(int meta, MapColor mapColor, String name, String translationKey) {
			this.meta = meta;
			this.name = name;
			this.translationKey = translationKey;
			this.mapColor = mapColor;
		}

		/**
		 * Returns the EnumType's metadata value.
		 */
		public int getMetadata() {
			return meta;
		}

		public MapColor getMapColor() {
			return mapColor;
		}

		@Override
		public String toString() {
			return name;
		}

		/**
		 * Returns an EnumType for the BlockState from a metadata value.
		 */
		public static EnumTeddyType byMetadata(int meta) {
			//			 return VALUES[MathHelper.abs(index % VALUES.length)];
			if ((meta < 0) || (meta >= META_LOOKUP.length)) {
				meta = 0;
			}

			return META_LOOKUP[MathHelper.abs(meta % META_LOOKUP.length)];
			//			return META_LOOKUP[meta];
		}

		@Override
		public String getName() {
			return name;
		}

		public String getTranslationKey() {
			return translationKey;
		}

		static {
			for (EnumTeddyType teddytype : values()) {
				META_LOOKUP[teddytype.getMetadata()] = teddytype;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().build());
		//		Trinkets.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		//		Trinkets.proxy.registerItemRenderer(Item.getItemFromBlock(this), 1, "inventory");
		//		Trinkets.proxy.registerItemRenderer(Item.getItemFromBlock(this), 2, "inventory");
	}
}
