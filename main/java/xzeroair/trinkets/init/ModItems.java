package xzeroair.trinkets.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import xzeroair.trinkets.util.Reference;

public class ModItems {
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	//top of mod class
	
	
	public static final ArmorMaterial ARMOUR_GLOWING = EnumHelper.addArmorMaterial("armour_glowing", Reference.MODID + ":glowing", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.0F);
	
	//Items
	public static final Item glowing_ingot = new xzeroair.trinkets.items.glowing_ingot("glowing_ingot");
	public static final Item weightless_stone = new xzeroair.trinkets.items.weightless_stone("weightless_stone");
	public static final Item inertia_stone = new xzeroair.trinkets.items.inertia_stone("inertia_stone");
    public static final Item glow_ring = new xzeroair.trinkets.items.glow_ring("glow_ring");
	
	
	//init
//	public static void init() {
		
//		glowing_ingot = new Itemglowing_ingot();
//		glow_ring = new Itemglow_ring();
		
//	}
//	public static void register() {
		
//		ForgeRegistries.ITEMS.register(glowing_ingot);
//		ForgeRegistries.ITEMS.register(glow_ring);
		
//	}
//	public static void registerRenders() {
		
//		registerRender(glowing_ingot);
//		registerRender(glow_ring);
		
//	}
//	public static void registerRender(Item item) {
		
//		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
		
//	}

}
