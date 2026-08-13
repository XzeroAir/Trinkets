package xzeroair.trinkets.util.compat;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.init.ModItems;

public class OreDictionaryCompat {

	public static int wildcard = OreDictionary.WILDCARD_VALUE;

	public static void registerOres() {

		OreDictionary.registerOre("ingotGlowing", ModItems.crafting.glowing_ingot);
		OreDictionary.registerOre("gemGlowing", ModItems.crafting.glowing_gem);
		OreDictionary.registerOre("dustGlowing", ModItems.crafting.glowing_powder);

	}

	public static boolean existsInOreDictionary(String entry) {
		return OreDictionary.doesOreNameExist(entry);
	}

	public static NonNullList<ItemStack> getOreDictEntries(String entry) {
		return OreDictionary.getOres(entry, false);
	}

	public static String[] getOreNames(@Nonnull ItemStack stack) {
		final int[] ids = OreDictionary.getOreIDs(stack);
		final String[] list = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			list[i] = OreDictionary.getOreName(ids[i]);
		}
		return list;
	}
}
