package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import xzeroair.trinkets.blocks.BlockGlowing;

public class ModBlocks {

	public static final List<Block> BLOCKS = new ArrayList<>();
	//top of mod class


	public static final Block BLOCK_GLOWING = new BlockGlowing("block_glowing", Material.GLASS);

}