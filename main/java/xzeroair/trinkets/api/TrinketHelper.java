package xzeroair.trinkets.api;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.enums.TargetOreType;

public class TrinketHelper {

	public static IContainerHandler getTrinketHandler(EntityPlayer player) {
		//		return player.getCapability(TrinketContainerProvider.containerCap, null);
		final IContainerHandler handler = player.getCapability(TrinketContainerProvider.containerCap, null);
		//		handler.setPlayer(player);
		return handler;
	}

	public static boolean AccessoryCheck(EntityLivingBase player, Item item) {
		if(TrinketCheck(player, item)) {
			return true;
		} else if(baubleCheck(player, item)){
			return true;
		} else {
			return false;
		}
	}

	private static boolean TrinketCheck(EntityLivingBase player, Item item) {
		if(player instanceof EntityPlayer) {
			final IContainerHandler Trinket = getTrinketHandler((EntityPlayer) player);
			if(Trinket != null) {
				for (int i = 0; i < Trinket.getSlots(); i++) {
					if(!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean baubleCheck(EntityLivingBase player, Item item) {
		if(Loader.isModLoaded("baubles") && (player instanceof EntityPlayer)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) player);
			if(baubles != null) {
				for(int i = 0; i < baubles.getSlots(); i++) {
					if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ItemStack getAccessoryByRegistryName(EntityPlayer player, String string) {
		boolean skip = false;
		final IContainerHandler Trinket = getTrinketHandler(player);
		if(Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if(!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getRegistryName().toString().equalsIgnoreCase(string))) {
					skip = true;
					return Trinket.getStackInSlot(i);
				}
			}
		}
		if(Loader.isModLoaded("baubles") && (skip != true)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if(baubles != null) {
				for(int i = 0; i < baubles.getSlots(); i++) {
					if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getRegistryName().toString().contentEquals(string))) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getAccessory(EntityPlayer player, Item item) {
		boolean skip = false;
		final IContainerHandler Trinket = getTrinketHandler(player);
		if(Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				if(!Trinket.getStackInSlot(i).isEmpty() && (Trinket.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
					skip = true;
					return Trinket.getStackInSlot(i);
				}
			}
		}
		if(Loader.isModLoaded("baubles") && (skip != true)) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if(baubles != null) {
				for(int i = 0; i < baubles.getSlots(); i++) {
					if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem().getTranslationKey().contentEquals(item.getTranslationKey()))) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack getBaubleStack(EntityPlayer player, Item item) {
		if(Loader.isModLoaded("baubles")) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			if(baubles != null) {
				for(int i = 0; i < baubles.getSlots(); i++) {
					if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem() == item)) {
						return baubles.getStackInSlot(i);
					}
				}
			}
		}
		return ItemStack.EMPTY;
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

	public static float getColor(String name, int index) {
		return TargetOreType.Color(name, index);
	}
}
