package xzeroair.trinkets.races.faelis;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.faelis.config.FaelisConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFaelisDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceFaelisDefaultInformation INSTANCE = new RaceFaelisDefaultInformation();
    private final FaelisConfig serverConfig;

    public RaceFaelisDefaultInformation() {
        super(85, 85, 16571252, 4465933, 16571252);
        this.serverConfig = TrinketsConfig.SERVER.RACES.FAELIS;
    }

    @Override
    public int getSecondaryTraitMaxVariants() {
        return 1;
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
