package xzeroair.trinkets.blocks.plants;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class MoonRose extends BlockBush implements IsModelLoaded {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public MoonRose(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(Trinkets.CREATIVE_TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.PLANT);
    }

    @SideOnly(Side.CLIENT)
    protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        return helper.formatAddVariables(translation);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> tooltips, @Nonnull ITooltipFlag flagIn) {
        super.addInformation(stack, world, tooltips, flagIn);
        final TranslationHelper helper = TranslationHelper.INSTANCE;
        for (int i = 1; i < 10; i++) {
            final int index = i;
            final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
            if (!helper.isStringEmpty(string)) {
                tooltips.add(string);
            }
        }
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID + "." + super.getTranslationKey();
    }

    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @Nonnull EnumHand hand) {
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
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    @Override
    public boolean hasTileEntity(@Nonnull IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getPlant(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return super.getPlant(world, pos);
    }

    @Override
    public EnumPlantType getPlantType(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return EnumPlantType.Plains;
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    /**
     * Return true if the block can sustain a Bush
     */
    @Override
    protected boolean canSustainBush(IBlockState state) {
        return (state.getBlock() == Blocks.GRASS) || (state.getBlock() == Blocks.DIRT) || (state.getBlock() == Blocks.FARMLAND);
    }

    @Override
    public boolean canBlockStay(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state) {
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        {
            IBlockState soil = worldIn.getBlockState(pos.down());
            return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
        //		return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMoonRose();
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        if (willHarvest) {
            return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, @Nonnull ItemStack tool) {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (stack.hasTagCompound()) {
            final TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityMoonRose) {
                Capabilities.getTEProperties(te, prop -> prop.loadFromNBT(stack.getTagCompound()));
            }
        }
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, @Nonnull BlockPos pos, IBlockState state, int fortune) {
        final ItemStack stack = new ItemStack(state.getBlock());
        final TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMoonRose) {
            stack.setTagCompound(Capabilities.getTEProperties(te, new NBTTagCompound(), (prop, tag) -> {
                prop.saveToNBT(tag);
                return tag;
            }));
        }
        drops.add(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        Trinkets.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

}
