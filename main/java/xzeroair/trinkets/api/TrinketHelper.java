package xzeroair.trinkets.api;

import java.util.ArrayList;
import java.util.List;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.enums.TargetOreType;

public class TrinketHelper {

	public static List<Item> SizeTrinkets = new ArrayList<>();
	public static List<String> bannedFoodItems = new ArrayList<>();

	public static List getIncompatibleRaceTrinket(Item item) {
		List<Item> CheckList = new ArrayList<>();
		for (Item notItem : SizeTrinkets) {
			if (!notItem.getTranslationKey().contentEquals(item.getTranslationKey())) {
				CheckList.add(notItem);
			}
		}
		return CheckList;
	}

	public static List BannedCompatFoods(String string) {
		if (!bannedFoodItems.contains("fairy_dew")) {
			bannedFoodItems.add("fairy_dew");
		}
		if (!bannedFoodItems.contains("dwarf_stout")) {
			bannedFoodItems.add("dwarf_stout");
		}
		if (!bannedFoodItems.contains("titan_spirit")) {
			bannedFoodItems.add("titan_spirit");
		}
		if (bannedFoodItems.contains(string)) {
			bannedFoodItems.remove(string);
			return bannedFoodItems;

		}
		return bannedFoodItems;
	}

	public static ITrinketContainerHandler getTrinketHandler(EntityPlayer player) {
		final ITrinketContainerHandler handler = player.getCapability(TrinketContainerProvider.containerCap, null);
		handler.setPlayer(player);
		return handler;
	}

	public static TrinketProperties getTrinketItemHandler(ItemStack stack) {
		TrinketProperties handler = Capabilities.getTrinketProperties(stack);
		return handler;
	}

	public static boolean AccessoryCheck(EntityLivingBase player, Item item) {
		if (TrinketCheck(player, item)) {
			return true;
		} else if (baubleCheck(player, item)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean AccessoryCheck(EntityLivingBase player, List<Item> items) {
		boolean found = false;
		for (Item item : items) {
			if (TrinketCheck(player, item)) {
				found = true;
			} else if (baubleCheck(player, item)) {
				found = true;
			}
		}
		return found;
	}

	private static boolean TrinketCheck(EntityLivingBase player, Item item) {
		if (player instanceof EntityPlayer) {
			final ITrinketContainerHandler Trinket = getTrinketHandler((EntityPlayer) player);
			if (Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if (!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean baubleCheck(EntityLivingBase player, Item item) {
		if (Loader.isModLoaded("baubles") && (player instanceof EntityPlayer)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ItemStack getAccessoryByRegistryName(EntityPlayer player, String string) {
		boolean skip = false;
		final ITrinketContainerHandler Trinket = getTrinketHandler(player);
		if (Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if (!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getRegistryName().toString().equalsIgnoreCase(string))) {
					skip = true;
					return Trinket.getStackInSlot(i);
				}
			}
		}
		if (Loader.isModLoaded("baubles") && (skip != true)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getRegistryName().toString().contentEquals(string))) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getAccessory(EntityPlayer player, Item item) {
		boolean skip = false;
		final ITrinketContainerHandler Trinket = getTrinketHandler(player);
		if (Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if (!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
					skip = true;
					return Trinket.getStackInSlot(i);
				}
			}
		}
		if (Loader.isModLoaded("baubles") && (skip != true)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getBaubleStack(EntityPlayer player, Item item) {
		if (Loader.isModLoaded("baubles")) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if (baubles != null) {
				for (int i = 0; i < baubles.getSlots(); i++) {
					if (!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem() == item)) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static String level(int i) {
		switch (i) {
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

	public static float getColor(String name, int index) {
		return TargetOreType.Color(name, index);
	}
}
