package xzeroair.trinkets.races.human;

import xzeroair.trinkets.races.RaceAttributesWrapper;
import xzeroair.trinkets.races.human.config.HumanConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceHumanAttributes extends RaceAttributesWrapper {

    public static final HumanConfig serverConfig = TrinketsConfig.SERVER.races.human;

    public RaceHumanAttributes() {
        size = 100;
        width = size;
        height = size;
        color1 = 11107684;
        color2 = 16374701;
        color3 = color1;
        opacity = 1f;
        trait_opacity = 1F;
    }

    @Override
    public String[] getAttributes() {
        return serverConfig.attributes;
    }
}
