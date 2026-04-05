package xzeroair.trinkets.races.elf;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceElfDefaultInformation extends RaceDefaultInformationWrapper {

    public static final RaceElfDefaultInformation INSTANCE = new RaceElfDefaultInformation();
    private final ElfConfig serverConfig;

    public RaceElfDefaultInformation() {
        super(100, 100, 16374701, 11107684, 16374701);
        this.serverConfig = TrinketsConfig.SERVER.RACES.ELF;
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
