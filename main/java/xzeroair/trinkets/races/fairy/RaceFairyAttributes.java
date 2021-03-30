package xzeroair.trinkets.races.fairy;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFairyAttributes extends RaceAttributesWrapper {

	public static final FairyConfig serverConfig = TrinketsConfig.SERVER.races.fairy;

	public RaceFairyAttributes() {
		super(serverConfig.Attributes);
		size = 25;
		width = size;
		height = size;
		color1 = 12514535;
		color2 = 962222;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
	}
}
