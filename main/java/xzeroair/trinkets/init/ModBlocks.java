package xzeroair.trinkets.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.blocks.BlockTeddyBear;
import xzeroair.trinkets.blocks.TempBlock;
import xzeroair.trinkets.blocks.plants.MoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;
import xzeroair.trinkets.enums.CustomTeddyBearTypes;
import xzeroair.trinkets.util.TrinketsRegistryNames;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {

    public static class Flowers {
        public static final List<Block> BLOCKS = new ArrayList<>();

        public static final Block MOON_ROSE = new MoonRose(TrinketsRegistryNames.ModBlocks.MOON_ROSE);

        protected static void registerTileEntityBlock(Class<? extends TileEntity> clss, ResourceLocation key) {
            GameRegistry.registerTileEntity(clss, key);
        }

        protected static void registerBlockWithTE(Block block, Class<? extends TileEntity> clss) {
            registerBlock(block);
            registerTileEntityBlock(clss, block.getRegistryName());
        }

        protected static void registerBlocks() {
            registerBlockWithTE(MOON_ROSE, TileEntityMoonRose.class);
        }

        private static void registerBlock(Block block) {
            BLOCKS.add(block);
            ModItems.crafting.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    public static class Placeables {
        public static final List<Block> BLOCKS = new ArrayList<>();

        public static final BlockTeddyBear TEDDYBEAR = new BlockTeddyBear();
        public static final BlockTeddyBear REMBO = new BlockTeddyBear("rembo");
        public static final BlockTeddyBear SCARY = new BlockTeddyBear("scary");
        public static final BlockTeddyBear SHIVAXI = new BlockTeddyBear("shivaxi");
        public static final BlockTeddyBear BEE = new BlockTeddyBear("bee");
        public static final BlockTeddyBear PANDA = new BlockTeddyBear("panda");
        public static final BlockTeddyBear ARTSY = new BlockTeddyBear("artsy");
        public static final BlockTeddyBear TWILIGHT = new BlockTeddyBear("twilight");
        public static final BlockTeddyBear RYU = new BlockTeddyBear("ryu");
        public static final BlockTeddyBear KEN = new BlockTeddyBear("ken");
        public static final BlockTeddyBear NYAN = new BlockTeddyBear("nyan");
        public static final BlockTeddyBear NYAN_OLD = new BlockTeddyBear("nyan_old");
        public static final BlockTeddyBear BOOM = new BlockTeddyBear("boom");
        public static final BlockTeddyBear RIXXI = new BlockTeddyBear("rixxi");
        public static final BlockTeddyBear SNOWIE = new BlockTeddyBear("snowie");

        public static final TempBlock TEMP_BLOCK = new TempBlock(TrinketsRegistryNames.ModBlocks.COOLED_MAGMA);

        protected static void registerTileEntityBlock(Class<? extends TileEntity> clss, ResourceLocation key) {
            GameRegistry.registerTileEntity(clss, key);
        }

        protected static void registerBlockWithTE(Block block, Class<? extends TileEntity> clss) {
            registerBlock(block);
            registerTileEntityBlock(clss, block.getRegistryName());
        }

        protected static void registerBlocks() {
            registerBlockWithTE(TEDDYBEAR, TileEntityTeddyBear.class);
            registerBlock(REMBO);
            registerBlock(SCARY);
            registerBlock(SHIVAXI);
            registerBlock(BEE);
            registerBlock(PANDA);
            registerBlock(ARTSY);
            registerBlock(TWILIGHT);
            registerBlock(RYU);
            registerBlock(KEN);
            registerBlock(NYAN);
            registerBlock(NYAN_OLD);
            registerBlock(BOOM);
            registerBlock(RIXXI);
            registerBlock(SNOWIE);
            registerBlock(TEMP_BLOCK);
        }

        private static void registerBlock(Block block) {
            BLOCKS.add(block);
            //			ModItems.crafting.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }

        public static BlockTeddyBear getTeddy(CustomTeddyBearTypes variant) {
            switch (variant) {
                case REMBO:
                    return REMBO;
                case SCARY:
                    return SCARY;
                case SHIVAXI:
                    return SHIVAXI;
                case BEE:
                    return BEE;
                case PANDA:
                    return PANDA;
                case ARTSY:
                    return ARTSY;
                case TWILIGHT:
                    return TWILIGHT;
                case RYU:
                    return RYU;
                case KEN:
                    return KEN;
                case NYAN:
                    return NYAN;
                case NYAN_OLD:
                    return NYAN_OLD;
                case BOOM:
                    return BOOM;
                case RIXXI:
                    return RIXXI;
                case SNOWIE:
                    return SNOWIE;
                default:
                    return TEDDYBEAR;
            }
        }

        public static BlockTeddyBear getTeddy(int variant) {
            return getTeddy(CustomTeddyBearTypes.getType(variant));
//            switch (variant) {
//                case 0:
//                    return TEDDYBEAR;
//                case 1:
//                    return REMBO;
//                case 2:
//                    return SCARY;
//                case 3:
//                    return SHIVAXI;
//                case 4:
//                    return BEE;
//                case 5:
//                    return PANDA;
//                case 6:
//                    return ARTSY;
//                case 7:
//                    return TWILIGHT;
//                case 8:
//                    return RYU;
//                case 9:
//                    return KEN;
//                case 10:
//                    return NYAN;
//                case 11:
//                    return NYAN_OLD;
//                case 12:
//                    return BOOM;
//                case 13:
//                    return RIXXI;
//                default:
//                    return TEDDYBEAR;
//            }
        }
    }

    public static void registerBlocks() {
        Trinkets.LOGGER.info("Instantiating Flowers");
        Flowers.registerBlocks();
        Trinkets.LOGGER.info("Instantiating Placeables");
        Placeables.registerBlocks();
    }
}
