package xzeroair.trinkets.races.titan;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceTitanAttributes extends RaceAttributesWrapper {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	public RaceTitanAttributes() {
		super(serverConfig.Attributes);
		size = 300;
		width = size;
		height = size;
		color1 = 10066329;
		color2 = 3223595;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
	}
}
