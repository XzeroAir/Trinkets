package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import xzeroair.trinkets.items.dragons_eye;
import xzeroair.trinkets.items.dwarf_ring;
import xzeroair.trinkets.items.ender_tiara;
import xzeroair.trinkets.items.fish_stone;
import xzeroair.trinkets.items.glow_ring;
import xzeroair.trinkets.items.glowing_ingot;
import xzeroair.trinkets.items.great_inertia_stone;
import xzeroair.trinkets.items.inertia_stone;
import xzeroair.trinkets.items.poison_stone;
import xzeroair.trinkets.items.polarized_stone;
import xzeroair.trinkets.items.rubber_stone;
import xzeroair.trinkets.items.small_ring;
import xzeroair.trinkets.items.weightless_stone;
import xzeroair.trinkets.items.wither_ring;

public class ModItems {

	public static final List<Item> ITEMS = new ArrayList<>();
	//top of mod class

	//Crafting Materials
	public static final Item glowing_ingot = new glowing_ingot("glowing_ingot");

	//Items With effects
	public static final Item weightless_stone = new weightless_stone("weightless_stone");
	public static final Item inertia_stone = new inertia_stone("inertia_stone");
	public static final Item great_inertia_stone = new great_inertia_stone("great_inertia_stone");
	public static final Item glow_ring = new glow_ring("glow_ring");
	public static final Item fish_stone = new fish_stone("fish_stone");
	public static final Item polarized_stone = new polarized_stone("polarized_stone");
	public static final Item dragons_eye = new dragons_eye("dragons_eye");
	public static final Item small_ring = new small_ring("small_ring");
	public static final Item dwarf_ring = new dwarf_ring("dwarf_ring");
	public static final Item wither_ring = new wither_ring("wither_ring");
	public static final Item rubber_stone = new rubber_stone("rubber_stone");
	public static final Item poison_stone = new poison_stone("poison_stone");
	public static final Item ender_tiara = new ender_tiara("ender_tiara");

}
