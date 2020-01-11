package xzeroair.trinkets.util.compat.baubles;

import baubles.api.BaubleType;

public class BaubleHelper {

	public static BaubleType getBaubleType(String string) {
		string.toLowerCase();
		if(string.contentEquals("trinket") || string.contentEquals("any") || string.contentEquals("all")) {
			return BaubleType.TRINKET;
		}
		else if(string.contentEquals("amulet") || string.contentEquals("necklace") || string.contentEquals("pendant")) {
			return BaubleType.AMULET;
		}
		else if(string.contentEquals("ring") || string.contentEquals("rings")) {
			return BaubleType.RING;
		}
		else if(string.contentEquals("belt")) {
			return BaubleType.BELT;
		}
		else if(string.contentEquals("head") || string.contentEquals("hat")) {
			return BaubleType.HEAD;
		}
		else if(string.contentEquals("body") || string.contentEquals("chest")) {
			return BaubleType.BODY;
		}
		else if(string.contentEquals("charm")) {
			return BaubleType.CHARM;
		}
		else {
			return BaubleType.TRINKET;
		}
	}

}
