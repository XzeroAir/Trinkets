package xzeroair.trinkets.races.dwarf;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceDwarfDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceDwarfDefaultInformation INSTANCE = new RaceDwarfDefaultInformation();
    private final DwarfConfig serverConfig;

    public RaceDwarfDefaultInformation() {
        super(75, 75, 10832170, 7039851, 10832170);
        this.serverConfig = TrinketsConfig.SERVER.RACES.DWARF;
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
