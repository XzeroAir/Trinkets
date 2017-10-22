package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

public class ModItems {
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	//top of mod class
	

	
	//Crafting Materials
	public static final Item glowing_ingot = new xzeroair.trinkets.items.glowing_ingot("glowing_ingot");
	
	
	//Items With effects
	public static final Item weightless_stone = new xzeroair.trinkets.items.weightless_stone("weightless_stone");
	public static final Item inertia_stone = new xzeroair.trinkets.items.inertia_stone("inertia_stone");
	public static final Item great_inertia_stone = new xzeroair.trinkets.items.great_inertia_stone("great_inertia_stone");
    public static final Item glow_ring = new xzeroair.trinkets.items.glow_ring("glow_ring");
    public static final Item fish_stone = new xzeroair.trinkets.items.fish_stone("fish_stone");
    public static final Item polarized_stone = new xzeroair.trinkets.items.polarized_stone("polarized_stone");
    public static final Item dragons_eye = new xzeroair.trinkets.items.dragons_eye("dragons_eye");


}
