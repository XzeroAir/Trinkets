package xzeroair.trinkets.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TempBlock extends BlockBreakable implements IsModelLoaded {

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 4);
    public static final PropertyBool HEATED_STONE = PropertyBool.create("heated_stone");

    public TempBlock(String name) {
        super(Material.ROCK, false);
        this.setRegistryName(Reference.MODID, name);
//        this.slipperiness = 0.98F;
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(HEATED_STONE, false));
        this.setTranslationKey(this.getRegistryName().toString());
        this.setHardness(0.5F);
        this.setLightLevel(0);
//        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Override
    public void onEntityWalk(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Entity entityIn) {
        if (!entityIn.isImmuneToFire() && (entityIn instanceof EntityLivingBase) && (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, (EntityLivingBase) entityIn) <= 0)) {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);

        if (state.getValue(HEATED_STONE)) {
            final List<ItemStack> items = new ArrayList<>();
            items.add(new ItemStack(Blocks.COBBLESTONE));
            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0F, false, player);
            for (final ItemStack item : items) {
                spawnAsEntity(worldIn, pos, item);
            }
            return;
        }

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            List<ItemStack> items = new ArrayList<>();
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
            this.harvesters.set(player);
            try {
                this.dropBlockAsItem(worldIn, pos, state, i);
            } finally {
                this.harvesters.remove();
            }
            Material material = worldIn.getBlockState(pos.down()).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
            }
        }
    }

    @Override
    public int quantityDropped(@Nonnull Random random) {
        return 0;
    }

//    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
//    {
//        if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - this.getDefaultState().getLightOpacity())
//        {
//            this.turnIntoLava(worldIn, pos);
//        }
//    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (state.getValue(HEATED_STONE)) {
            final int age = state.getValue(AGE);
            if (age == 4) {
                worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 3);
            } else {
                worldIn.setBlockState(pos, state.withProperty(AGE, age + 1), 2);
                worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
            }
            return;
        }
        if ((rand.nextInt(3) == 0 || this.countNeighbors(worldIn, pos) < 4) && worldIn.getLightFromNeighbors(pos) > 11 - state.getValue(AGE) - state.getLightOpacity()) {
            this.slightlyMelt(worldIn, pos, state, rand, true);
        } else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(rand, 20, 40));
        }
    }

    public void setHeatedStone(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(HEATED_STONE, true), 3);
        if (this.hasWaterNeighbor(worldIn, pos)) {
            this.coolHeatedStone(worldIn, pos);
        } else {
            worldIn.scheduleUpdate(pos, this, MathHelper.getInt(worldIn.rand, 20, 40));
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

    @Override
    public EnumPushReaction getPushReaction(@Nonnull IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(AGE) + (state.getValue(HEATED_STONE) ? 5 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        final int normalizedMeta = MathHelper.clamp(meta, 0, 9);
        return this.getDefaultState().withProperty(AGE, normalizedMeta % 5).withProperty(HEATED_STONE, normalizedMeta >= 5);
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if (state.getValue(HEATED_STONE) && this.hasWaterNeighbor(worldIn, pos)) {
            this.coolHeatedStone(worldIn, pos);
            return;
        }
        if (!state.getValue(HEATED_STONE) && blockIn == this) {
            int i = this.countNeighbors(worldIn, pos);

            if (i < 2) {
                this.turnIntoLava(worldIn, pos);
            }
        }
    }

    private boolean hasWaterNeighbor(World worldIn, BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.values()) {
            if (worldIn.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER) {
                return true;
            }
        }
        return false;
    }

    private void coolHeatedStone(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 3);
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

    protected void slightlyMelt(World worldIn, BlockPos pos, @Nonnull IBlockState state, Random rand, boolean meltNeighbors) {
        int i = state.getValue(AGE);

        if (i < 3) {
            worldIn.setBlockState(pos, state.withProperty(AGE, i + 1), 2);
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

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, HEATED_STONE);
    }

    @Override
    public ItemStack getItem(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(HEATED_STONE).build());
    }
}
