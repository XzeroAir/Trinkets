package xzeroair.trinkets.races.faelis;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFaelisAttributes extends RaceAttributesWrapper {

	public static final FaelisConfig serverConfig = TrinketsConfig.SERVER.races.faelis;

	public RaceFaelisAttributes() {
		size = 85;
		width = size;
		height = size;
		color1 = 16571252;
		color2 = 4465933;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
		attributes = serverConfig.attributes;
	}
}
