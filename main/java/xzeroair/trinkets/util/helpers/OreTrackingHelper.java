package xzeroair.trinkets.util.helpers;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.oredict.OreDictionary;

public class OreTrackingHelper {

	private static List target = new ArrayList<>();

	public static List getTargetOres(String s) {
		List ores = getOresLoaded();
		if(!target.isEmpty()) {
			target.clear();
		}
		for(int i = 0;i < ores.size();i++) {
			if(ores.get(i).toString().contains(s)) {
				target.add(ores.get(i));
			}
		}
		return target;
	}

	private static List OresLoaded = new ArrayList<>();
	private static int ID = 0;
	public static final List oreTypesLoaded() {
		if((ID < 19)) {
			String s = "None";
			List ore = getOresLoaded();
			if((ID == 0)) {
				s = "Coal";
			}
			if((ID == 1)) {
				s = "Tin";
			}
			if((ID == 2)) {
				s = "Copper";
			}
			if((ID == 3)) {
				s = "Iron";
			}
			if((ID == 4)) {
				s = "Aluminum";
			}
			if((ID == 5)) {
				s = "Lapis";
			}
			if((ID == 6)) {
				s = "Gold";
			}
			if((ID == 7)) {
				s = "Silver";
			}
			if((ID == 8)) {
				s = "Lead";
			}
			if((ID == 9)) {
				s = "Diamond";
			}
			if((ID == 10)) {
				s = "Redstone";
			}
			if((ID == 11)) {
				s = "Emerald";
			}
			if((ID == 12)) {
				s = "Quartz";
			}
			if((ID == 13)) {
				s = "Cobalt";
			}
			if((ID == 14)) {
				s = "Ardite";
			}
			if((ID == 15)) {
				s = "Nickel";
			}
			if((ID == 16)) {
				s = "Platinum";
			}
			if((ID == 17)) {
				s = "Iridium";
			}
			if((ID == 18)) {
				s = "Chest";
			}
			if(ore.toString().contains(s) && !OresLoaded.contains(s)) {
				OresLoaded.add(s);
			} else {
				ID++;
			}
		}
		return OresLoaded;
	}

	private static List totalOresLoaded = new ArrayList<>();
	private static boolean finished = false;

	public static final List getOresLoaded() {
		if(finished != true) {
			for(String ore : OreDictionary.getOreNames()) {
				if(ore.contains("ore")) {
					if(!(ore.contains("Nugget") || ore.contains("Poor") || ore.contains("Clathrate"))) {
						if(ore.contains("Coal")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Tin")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Copper")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Iron")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Aluminum")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Lapis")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Gold")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Silver")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Lead")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Diamond")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Redstone")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Emerald")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Quartz")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Cobalt")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Ardite")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Nickel")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Platinum")) {
							totalOresLoaded.add(ore);
						}
						if(ore.contains("Iridium")) {
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
}
