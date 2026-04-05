package xzeroair.trinkets.races.titan;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceTitanDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceTitanDefaultInformation INSTANCE = new RaceTitanDefaultInformation();
    private final TitanConfig serverConfig;

    public RaceTitanDefaultInformation() {
        super(300, 300, 10066329, 3223595, 10066329);
        this.serverConfig = TrinketsConfig.SERVER.RACES.TITAN;
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
