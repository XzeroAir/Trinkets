package xzeroair.trinkets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nullable;
import java.util.Random;

public class TempBlock extends BlockBreakable implements IsModelLoaded {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    public TempBlock(String name) {
        super(Material.ROCK, false);
        this.setRegistryName(Reference.MODID, name);
//        this.slipperiness = 0.98F;
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setTranslationKey(this.getRegistryName().toString());
        this.setLightLevel(0);
//        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
            items.add(this.getSilkTouchDrop(state));

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack is : items)
                spawnAsEntity(worldIn, pos, is);
        } else {
            if (worldIn.provider.doesWaterVaporize()) {
                worldIn.setBlockToAir(pos);
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
            Material material = worldIn.getBlockState(pos.down()).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
            }
        }
    }

    public int quantityDropped(Random random) {
        return 0;
    }

//    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
//    {
//        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getDefaultState().getLightOpacity())
//        {
//            this.turnIntoLava(worldIn, pos);
//        }
//    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - ((Integer) state.getValue(AGE)).intValue() - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        } else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }

    protected void turnIntoLava(World worldIn, BlockPos pos) {
        if (worldIn.provider.doesWaterVaporize()) {
            worldIn.setBlockToAir(pos);
        } else {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockState(pos, Blocks.LAVA.getDefaultState());
            worldIn.neighborChanged(pos, Blocks.LAVA, pos);
        }
    }

    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(AGE)).intValue();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(MathHelper.clamp(meta, 0, 3)));
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (blockIn == this) {
            int i = this.countNeighbors(worldIn, pos);

            if (i < 2) {
                this.turnIntoLava(worldIn, pos);
            }
        }
    }

    private int countNeighbors(World worldIn, BlockPos pos) {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this) {
                ++i;

                if (i >= 4) {
                    return i;
                }
            }
        }

        return i;
    }

    protected void slightlyMelt(World worldIn, BlockPos pos, IBlockState state, Random rand, boolean meltNeighbors) {
        int i = ((Integer) state.getValue(AGE)).intValue();

        if (i < 3) {
            worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        } else {
            this.turnIntoLava(worldIn, pos);

            if (meltNeighbors) {
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    BlockPos blockpos = pos.offset(enumfacing);
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);

                    if (iblockstate.getBlock() == this) {
                        this.slightlyMelt(worldIn, blockpos, iblockstate, rand, false);
                    }
                }
            }
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{AGE});
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().build());
    }
}
