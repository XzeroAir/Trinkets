<<<<<<< Updated upstream
package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.blocks.BlockTeddyBear;
import xzeroair.trinkets.blocks.plants.MoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;

public class ModBlocks {

	public static class Flowers {
		public static final List<Block> BLOCKS = new ArrayList<>();

		public static final Block MOON_ROSE = new MoonRose("moon_rose");

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

		public static final Block TEDDYBEAR = new BlockTeddyBear();

		protected static void registerTileEntityBlock(Class<? extends TileEntity> clss, ResourceLocation key) {
			GameRegistry.registerTileEntity(clss, key);
		}

		protected static void registerBlockWithTE(Block block, Class<? extends TileEntity> clss) {
			registerBlock(block);
			registerTileEntityBlock(clss, block.getRegistryName());
		}

		protected static void registerBlocks() {
			registerBlockWithTE(TEDDYBEAR, TileEntityTeddyBear.class);
		}

		private static void registerBlock(Block block) {
			BLOCKS.add(block);
			//			ModItems.crafting.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
	}

	public static void registerBlocks() {
		Trinkets.log.info("Instantiating Flowers");
		Flowers.registerBlocks();
		Trinkets.log.info("Instantiating Placeables");
		Placeables.registerBlocks();
	}
}
=======
package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.blocks.BlockTeddyBear;
import xzeroair.trinkets.blocks.plants.MoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityMoonRose;
import xzeroair.trinkets.blocks.tileentities.TileEntityTeddyBear;

public class ModBlocks {

	public static class Flowers {
		public static final List<Block> BLOCKS = new ArrayList<>();

		public static final Block MOON_ROSE = new MoonRose("moon_rose");

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

		public static final Block TEDDYBEAR = new BlockTeddyBear();

		protected static void registerTileEntityBlock(Class<? extends TileEntity> clss, ResourceLocation key) {
			GameRegistry.registerTileEntity(clss, key);
		}

		protected static void registerBlockWithTE(Block block, Class<? extends TileEntity> clss) {
			registerBlock(block);
			registerTileEntityBlock(clss, block.getRegistryName());
		}

		protected static void registerBlocks() {
			registerBlockWithTE(TEDDYBEAR, TileEntityTeddyBear.class);
		}

		private static void registerBlock(Block block) {
			BLOCKS.add(block);
			//			ModItems.crafting.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
	}

	public static void registerBlocks() {
		Trinkets.log.info("Instantiating Flowers");
		Flowers.registerBlocks();
		Trinkets.log.info("Instantiating Placeables");
		Placeables.registerBlocks();
	}
}
>>>>>>> Stashed changes
