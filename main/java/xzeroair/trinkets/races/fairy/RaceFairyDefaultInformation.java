package xzeroair.trinkets.races.fairy;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFairyDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceFairyDefaultInformation INSTANCE = new RaceFairyDefaultInformation();
    private final FairyConfig serverConfig;

    public RaceFairyDefaultInformation() {
        super(25, 25, 12514535, 962222, 12514535);
        this.serverConfig = TrinketsConfig.SERVER.RACES.FAIRY;
    }

    @Override
    public int getPrimaryTraitMaxVariants() {
        return 2;
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
