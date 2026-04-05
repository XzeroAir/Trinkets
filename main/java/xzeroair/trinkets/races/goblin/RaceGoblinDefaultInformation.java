package xzeroair.trinkets.races.goblin;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.goblin.config.GoblinConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceGoblinDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceGoblinDefaultInformation INSTANCE = new RaceGoblinDefaultInformation();
    private final GoblinConfig serverConfig;

    public RaceGoblinDefaultInformation() {
        super(50, 50, 6588004, 3096367, 6588004);
        this.serverConfig = TrinketsConfig.SERVER.RACES.GOBLIN;
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
