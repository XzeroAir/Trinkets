package xzeroair.trinkets.util.compat;

import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.init.ModItems;

public class OreDictionaryCompat {

	public static void registerOres() {

		OreDictionary.registerOre("ingotGlowing", ModItems.crafting.glowing_ingot);
		OreDictionary.registerOre("gemGlowing", ModItems.crafting.glowing_gem);
		OreDictionary.registerOre("dustGlowing", ModItems.crafting.glowing_powder);

	}

}
