package xzeroair.trinkets.races.slime;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.slime.config.SlimeConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceSlimeDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceSlimeDefaultInformation INSTANCE = new RaceSlimeDefaultInformation();
    private final SlimeConfig serverConfig;

    public RaceSlimeDefaultInformation() {
        super(100, 100, 5349437, 5349437, 5349437);
        this.serverConfig = TrinketsConfig.SERVER.RACES.SLIME;
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
