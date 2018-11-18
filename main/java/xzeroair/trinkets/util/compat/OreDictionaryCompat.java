package xzeroair.trinkets.util.compat;

import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.init.ModItems;

public class OreDictionaryCompat {

	public static void registerOres() {

		OreDictionary.registerOre("ingotGlowing", ModItems.glowing_ingot);

	}

}
