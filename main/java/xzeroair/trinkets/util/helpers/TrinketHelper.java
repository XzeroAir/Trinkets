package xzeroair.trinkets.util.helpers;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.compatibilities.ItemCap.ItemProvider;
import xzeroair.trinkets.enums.TargetOreType;

public class TrinketHelper {

	public static boolean baubleCheck(EntityPlayer player, Item item) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for(int i = 0; i < baubles.getSlots(); i++) {
			if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem() == item)) {
				return true;
			}
		}
		return false;
	}

	public static Item getBauble(EntityPlayer player, Item item) {
		return getBaubleStack(player, item).getItem();
	}

	public static ItemStack getBaubleStack(EntityPlayer player, Item item) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for(int i = 0; i < baubles.getSlots(); i++) {
			if(!baubles.getStackInSlot(i).isEmpty() && (baubles.getStackInSlot(i).getItem() == item)) {
				return baubles.getStackInSlot(i);
			}
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasCap(ItemStack itemStack) {
		if((itemStack.hasCapability(ItemProvider.itemCapability, null))) {
			return true;
		} else {
			return false;
		}
	}

	public static ItemCap getBaubleCap(ItemStack itemStack) {
		return itemStack.getCapability(ItemProvider.itemCapability, null);
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

	public static int getOre(int i) {
		return TargetOreType.Type(i).id();
	}

	public static float getColor(String name, int index) {
		return TargetOreType.Color(name, index);
	}
}
