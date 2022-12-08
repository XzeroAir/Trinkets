package xzeroair.trinkets.races.elf;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceElfAttributes extends RaceAttributesWrapper {

	public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

	public RaceElfAttributes() {
		size = 100;
		width = size;
		height = size;
		color1 = 40960;
		color2 = 962222;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
		attributes = serverConfig.attributes;
	}
}
