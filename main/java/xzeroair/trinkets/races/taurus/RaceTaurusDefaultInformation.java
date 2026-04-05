package xzeroair.trinkets.races.taurus;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.taurus.config.TaurusConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceTaurusDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceTaurusDefaultInformation INSTANCE = new RaceTaurusDefaultInformation();
    private final TaurusConfig serverConfig;

    public RaceTaurusDefaultInformation() {
        super(120, 120, 1315860, 2894892, 16770876);
        this.serverConfig = TrinketsConfig.SERVER.RACES.TAURUS;
    }

    @Override
    public int getPrimaryTraitMaxVariants() {
        /// Female Horns
        /// Male Horns
        return 3;
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
