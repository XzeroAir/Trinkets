package xzeroair.trinkets.races.human;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.human.config.HumanConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceHumanDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceHumanDefaultInformation INSTANCE = new RaceHumanDefaultInformation();
    private final HumanConfig serverConfig;

    public RaceHumanDefaultInformation() {
        super(100, 100, 11107684, 16374701, 11107684);
        this.serverConfig = TrinketsConfig.SERVER.RACES.HUMAN;
    }

    @Override
    public int getHeight() {
        return this.serverConfig.SIZE.height;
    }

    @Override
    public int getWidth() {
        return this.serverConfig.SIZE.width;
    }
    
    @Override
    public String[] getAttributes() {
        return this.serverConfig.ATTRIBUTES;
    }
}
