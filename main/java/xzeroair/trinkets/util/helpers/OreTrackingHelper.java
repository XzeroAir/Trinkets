package xzeroair.trinkets.util.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.enums.TargetOreType;

public class OreTrackingHelper {

	private String block;

	public OreTrackingHelper(String block) {
		this.block = block;
	}

	public static String translateOreName(String name) {
		String target = name;
		String meta = "";
		if (name.contains("[")) {
			final int metaStart = name.indexOf("[");
			final int metaEnd = name.lastIndexOf("]");
			target = name.substring(0, metaStart);
			meta = name.substring(metaStart + 1, metaEnd);
		}
		if (name.contains(":")) {
			final Item itemTarget = Item.getByNameOrId(target);
			ItemStack parseName = new ItemStack(itemTarget, 1);
			if (itemTarget == null) {
				if (Block.getBlockFromName(target) != null) {
					target = Block.getBlockFromName(target).getRegistryName().toString();
				}
			}
			target = parseName.getTextComponent().getUnformattedText();

			if (!meta.isEmpty() && (itemTarget != null) && itemTarget.getHasSubtypes()) {
				final NonNullList<ItemStack> parseMeta = NonNullList.create();
				itemTarget.getSubItems(CreativeTabs.SEARCH, parseMeta);
				for (final ItemStack t : parseMeta) {
					if (t.getMetadata() == Integer.parseInt(meta)) {
						parseName = new ItemStack(itemTarget, 1, Integer.parseInt(meta));
						target = parseName.getTextComponent().getUnformattedText();
					}
				}
			}
		}
		return target
				.replace(" Ore", "")
				.replace("ore", "")
				.replace(" ore", "")
				.replace("_", " ")
				.replace("tile.", "")
				.replace("[", "")
				.replace("]", "");
	}

	private static List OresLoaded = new ArrayList<>();
	private static int ID = 0;

	public static final List oreTypesLoaded() {
		if ((ID < 19)) {
			String s = "None";
			final List ore = getOresLoaded();
			if ((ID == 0)) {
				s = "Coal";
			}
			if ((ID == 1)) {
				s = "Tin";
			}
			if ((ID == 2)) {
				s = "Copper";
			}
			if ((ID == 3)) {
				s = "Iron";
			}
			if ((ID == 4)) {
				s = "Aluminum";
			}
			if ((ID == 5)) {
				s = "Lapis";
			}
			if ((ID == 6)) {
				s = "Gold";
			}
			if ((ID == 7)) {
				s = "Silver";
			}
			if ((ID == 8)) {
				s = "Lead";
			}
			if ((ID == 9)) {
				s = "Diamond";
			}
			if ((ID == 10)) {
				s = "Redstone";
			}
			if ((ID == 11)) {
				s = "Emerald";
			}
			if ((ID == 12)) {
				s = "Quartz";
			}
			if ((ID == 13)) {
				s = "Cobalt";
			}
			if ((ID == 14)) {
				s = "Ardite";
			}
			if ((ID == 15)) {
				s = "Nickel";
			}
			if ((ID == 16)) {
				s = "Platinum";
			}
			if ((ID == 17)) {
				s = "Iridium";
			}
			if ((ID == 18)) {
				s = "Chest";
			}
			if (ore.toString().contains(s) && !OresLoaded.contains(s)) {
				OresLoaded.add(s);
			} else {
				ID++;
			}
		}
		return OresLoaded;
	}

	public static String[] getOreNames(@Nonnull ItemStack stack) {
		final int[] ids = OreDictionary.getOreIDs(stack);
		final String[] list = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			list[i] = OreDictionary.getOreName(ids[i]);
		}
		return list;
	}

	private static List totalOresLoaded = new ArrayList<>();
	private static boolean finished = false;

	public static final List getOresLoaded() {
		if (finished != true) {
			for (final String ore : OreDictionary.getOreNames()) {
				if (ore.contains("ore")) {
					if (!(ore.contains("Nugget") || ore.contains("Poor") || ore.contains("Clathrate"))) {
						if (ore.contains("Coal")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Tin")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Copper")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Iron")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Aluminum")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Lapis")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Gold")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Silver")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Lead")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Diamond")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Redstone")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Emerald")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Quartz")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Cobalt")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Ardite")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Nickel")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Platinum")) {
							totalOresLoaded.add(ore);
						}
						if (ore.contains("Iridium")) {
							totalOresLoaded.add(ore);
						}
					}
				}
			}
			totalOresLoaded.add("Chest");
			finished = true;
		}
		return totalOresLoaded;
	}

	public static float getColor(String name, int index) {
		return TargetOreType.Color(name, index);
	}

	public static int getColor(String name) {
		return TargetOreType.Color(name);
	}
}
