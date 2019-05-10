package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import xzeroair.trinkets.items.Greater_inertia_stone;
import xzeroair.trinkets.items.Inertia_null_stone;
import xzeroair.trinkets.items.dragons_eye;
import xzeroair.trinkets.items.dwarf_ring;
import xzeroair.trinkets.items.ender_tiara;
import xzeroair.trinkets.items.fairy_ring;
import xzeroair.trinkets.items.glow_ring;
import xzeroair.trinkets.items.glowing_ingot;
import xzeroair.trinkets.items.poison_stone;
import xzeroair.trinkets.items.polarized_stone;
import xzeroair.trinkets.items.sea_stone;
import xzeroair.trinkets.items.weightless_stone;
import xzeroair.trinkets.items.wither_ring;

public class ModItems {

	public static final List<Item> ITEMS = new ArrayList<>();
	//top of mod class

	//Crafting Materials
	public static final Item glowing_ingot = new glowing_ingot("glowing_ingot");

	//Items With effects
	public static final Item weightless_stone = new weightless_stone("weightless_stone");
	public static final Item inertia_null_stone = new Inertia_null_stone("inertia_null_stone");
	public static final Item greater_inertia_stone = new Greater_inertia_stone("greater_inertia_stone");
	public static final Item glow_ring = new glow_ring("glow_ring");
	public static final Item sea_stone = new sea_stone("sea_stone");
	public static final Item polarized_stone = new polarized_stone("polarized_stone");
	public static final Item dragons_eye = new dragons_eye("dragons_eye");
	public static final Item fairy_ring = new fairy_ring("fairy_ring");
	public static final Item dwarf_ring = new dwarf_ring("dwarf_ring");
	public static final Item wither_ring = new wither_ring("wither_ring");
	public static final Item poison_stone = new poison_stone("poison_stone");
	public static final Item ender_tiara = new ender_tiara("ender_tiara");

}
