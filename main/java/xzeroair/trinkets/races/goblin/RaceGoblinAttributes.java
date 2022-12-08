package xzeroair.trinkets.races.goblin;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceGoblinAttributes extends RaceAttributesWrapper {

	public static final GoblinConfig serverConfig = TrinketsConfig.SERVER.races.goblin;

	public RaceGoblinAttributes() {
		size = 50;
		width = size;
		height = size;
		color1 = 6588004;
		color2 = 3096367;
		color3 = color1;
		opacity = 1f;
		trait_opacity = 1F;
		attributes = serverConfig.attributes;
	}
}
