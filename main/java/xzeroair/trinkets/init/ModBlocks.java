package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import xzeroair.trinkets.items.plants.MoonRose;

public class ModBlocks {

	public static class Flowers {
		public static final List<Block> BLOCKS = new ArrayList<>();

		public static final Block moon_rose = new MoonRose("moon_rose");
	}
}
