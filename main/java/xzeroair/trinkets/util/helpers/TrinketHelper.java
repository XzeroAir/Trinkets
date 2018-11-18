package xzeroair.trinkets.util.helpers;

import java.util.ArrayList;
import java.util.List;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketHelper {

	private static Item getBaubleRings(EntityPlayer player) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for(int i = 1; i < 2; i++) {
			if(!baubles.getStackInSlot(i).isEmpty()) {
				return baubles.getStackInSlot(i).getItem();
			}
		}
		return null;
	}

	private static ItemStack getBaubleRingsStack(EntityPlayer player) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for(int i = 1; i < 2; i++) {
			if(!baubles.getStackInSlot(i).isEmpty()) {
				return baubles.getStackInSlot(i);
			}
		}
		return null;
	}

	public static Item getBaubleType(EntityPlayer player, TrinketType type) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		switch (type) {
		case amulet:
			return baubles.getStackInSlot(type.slot()).getItem();
		case upperRing:
			return baubles.getStackInSlot(type.slot()).getItem();
		case lowerRing:
			return baubles.getStackInSlot(type.slot()).getItem();
		case rings:
			return getBaubleRings(player);
		case belt:
			return baubles.getStackInSlot(type.slot()).getItem();
		case head:
			return baubles.getStackInSlot(type.slot()).getItem();
		case body:
			return baubles.getStackInSlot(type.slot()).getItem();
		case charm:
			return baubles.getStackInSlot(type.slot()).getItem();
		default:
			return baubles.getStackInSlot(type.slot()).getItem();
		}
	}

	public static ItemStack getBaubleTypeStack(EntityPlayer player, TrinketType type) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		switch (type) {
		case amulet:
			return baubles.getStackInSlot(type.slot());
		case upperRing:
			return baubles.getStackInSlot(type.slot());
		case lowerRing:
			return baubles.getStackInSlot(type.slot());
		case rings:
			return getBaubleRingsStack(player);
		case belt:
			return baubles.getStackInSlot(type.slot());
		case head:
			return baubles.getStackInSlot(type.slot());
		case body:
			return baubles.getStackInSlot(type.slot());
		case charm:
			return baubles.getStackInSlot(type.slot());
		default:
			return baubles.getStackInSlot(type.slot());
		}
	}

	public static enum TrinketType {

		amulet, upperRing, lowerRing, rings, belt, head, body, charm;
		public int slot() {
			switch (this) {
			case amulet:
				return 0;
			case upperRing:
				return 1;
			case lowerRing:
				return 2;
			case belt:
				return 3;
			case head:
				return 4;
			case body:
				return 5;
			case charm:
				return 6;
			case rings:
				return 7;
			default:
				return 0;
			}
		}
	}

	public static int getOre(int i) {
		switch (i) {
		case 1:
			return targetOreType.coal.id();
		case 2:
			return targetOreType.iron.id();
		case 3:
			return targetOreType.gold.id();
		case 4:
			return targetOreType.diamond.id();
		case 5:
			return targetOreType.emerald.id();
		case 6:
			return targetOreType.lapis.id();
		case 7:
			return targetOreType.redstone.id();
		case 8:
			return targetOreType.quarts.id();
		case 9:
			return targetOreType.chest.id();
		default:
			return 92;
		}
	}

	public static List<Float> getColor(int type) {
		//		float[] color = new float[] {0F, 0F, 0F};
		List<Float> color = new ArrayList<>();
		color.add(0, 0.0F);//red
		color.add(1, 0.0F);//green
		color.add(2, 0.0F);//blue
		if(type == targetOreType.coal.id()) {
			color.clear();
			color.add(0, 0.1F);//red
			color.add(1, 0.1F);//green
			color.add(2, 0.1F);//blue
			return color;
		}
		if(type == targetOreType.iron.id()) {
			color.clear();
			color.add(0, 0.9F);
			color.add(1, 0.74F);
			color.add(2, 0.6F);
			return color;
		}
		if(type == targetOreType.gold.id()) {
			color.clear();
			color.add(0, 1.0F);
			color.add(1, 0.9F);
			color.add(2, 0.3F);
			return color;
		}
		if(type == targetOreType.diamond.id()) {
			color.clear();
			color.add(0, 0.0F);
			color.add(1, 0.9F);
			color.add(2, 1.0F);
			return color;
		}
		if(type == targetOreType.emerald.id()) {
			color.clear();
			color.add(0, 0.0F);
			color.add(1, 1.0F);
			color.add(2, 0.3F);
			return color;
		}
		if(type == targetOreType.lapis.id()) {
			color.clear();
			color.add(0, 0.0F);
			color.add(1, 0.0F);
			color.add(2, 0.8F);
			return color;
		}
		if(type == targetOreType.redstone.id()) {
			color.clear();
			color.add(0, 1.0F);
			color.add(1, 0.1F);
			color.add(2, 0.1F);
			return color;
		}
		if(type == targetOreType.quarts.id()) {
			color.clear();
			color.add(0, 1.0F);
			color.add(1, 1.0F);
			color.add(2, 1.0F);
			return color;
		}
		if(type == targetOreType.chest.id()) {
			color.clear();
			color.add(0, 1.0F);
			color.add(1, 0.9F);
			color.add(2, 0.3F);
			return color;
		}
		return color;

	}

	public static enum targetOreType {
		coal, iron, gold, diamond, emerald, lapis, redstone, quarts, chest;
		public int id() {
			switch (this) {
			case coal:
				return 16;
			case iron:
				return 15;
			case gold:
				return 14;
			case diamond:
				return 56;
			case emerald:
				return 129;
			case lapis:
				return 21;
			case redstone:
				return 73;
			case quarts:
				return 153;
			case chest:
				return 54;
			default:
				return 92;
			}
		}
	}

	public static String level(int i) {
		switch(i){
		case 0:
			return "pickaxe";
		case 1:
			return "axe";
		case 2:
			return "shovel";
		default:
			return "";
		}
	}

	public static void harvestLevel(EntityPlayer player) {
		Item heldItem = player.inventory.getCurrentItem().getItem();
		ItemStack heldItemStack = player.inventory.getCurrentItem();

		for(int i = 0; i < 3; i++) {
			String toolLevel = level(i);
			if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) > 0) {
				if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 0) {
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, 5, false, false));
				}
				if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 1) {
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, 3, false, false));
				}
				if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) == 2) {
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, 1, false, false));
				}
				if(heldItem.getHarvestLevel(heldItemStack, toolLevel, player, null) >= 3) {
					player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 120, 0, false, false));
				}
			}
		}
	}
}
