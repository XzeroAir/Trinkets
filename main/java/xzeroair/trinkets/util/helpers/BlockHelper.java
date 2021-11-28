package xzeroair.trinkets.util.helpers;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xzeroair.trinkets.util.config.ConfigHelper;

public class BlockHelper {

	public static boolean isBlockInList(World world, IBlockState state, BlockPos pos, String string, String meta) {
		if (state.getBlock().isAir(state, world, pos) || string.isEmpty() || string.trim().isEmpty()) {
			return false;
		}
		if (string.contains(":")) {
			if (meta.isEmpty() || meta.trim().isEmpty()) {
				return state.getBlock().getRegistryName().toString().equalsIgnoreCase(string);
			}
			if (state.getBlock().getRegistryName().toString().equalsIgnoreCase(string) && !meta.isEmpty()) {
				if (state.getBlock().getMetaFromState(state) == Integer.parseInt(meta)) {
					return true;
				}
			}
		} else {
			final ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			if (!blockStack.isEmpty()) {
				for (final String oreDictionary : OreTrackingHelper.getOreNames(blockStack)) {
					if (oreDictionary.equalsIgnoreCase(string)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isBlockInList(World world, IBlockState state, BlockPos pos, List<String> list) {
		return isBlockInList(world, state, pos, list);
	}

	public static boolean isBlockInList(World world, IBlockState state, BlockPos pos, String[] list) {
		boolean check = false;
		for (final String s : list) {
			if (ConfigHelper.checkBlockInConfig(state, s)) {
				check = true;
				return true;
			}
			//			if (isBlockInList(world, state, pos, s, getMeta(s)) == true) {
			//				check = true;
			//			}
		}
		return check;
	}

	private static String getMeta(String target) {
		String meta = "";
		if ((target.contains("[") && target.contains("]"))) {//|| (target.contains("{") && target.contains("}")
			final int metaStart = target.indexOf("[");
			final int metaEnd = target.lastIndexOf("]");
			meta = target.substring(metaStart + 1, metaEnd);
		}
		return meta;
	}
}
