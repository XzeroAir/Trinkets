package xzeroair.trinkets.races.dwarf;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceDwarfAttributes extends RaceAttributesWrapper {

	public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

	public RaceDwarfAttributes() {
		size = 75;
		width = size;
		height = size;
		color1 = 10832170;
		color2 = 7039851;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
		attributes = serverConfig.attributes;
	}

}
