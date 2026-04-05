package xzeroair.trinkets.races.dragon;

import xzeroair.trinkets.races.RaceDefaultInformationWrapper;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceDragonDefaultInformation extends RaceDefaultInformationWrapper {

    public enum DragonWingsVariant {
        //@formatter:off
        Normal(0),
        Angel(1),
        HIDDEN(99);
        //@formatter:on

        private final int id;

        DragonWingsVariant(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static int getMaxLength() {
            return values().length;
        }

        public static DragonWingsVariant cosmetic(int value) {
            if ((value < 0) || (value >= values().length)) {
                value = 0;
            }
            return values()[value];
        }
    }

    public static final RaceDragonDefaultInformation INSTANCE = new RaceDragonDefaultInformation();
    private final DragonConfig serverConfig;

    public RaceDragonDefaultInformation() {
        super(120, 120, 3289650, 9509561, 3289650);
        this.serverConfig = TrinketsConfig.SERVER.RACES.DRAGON;
    }

    @Override
    public int getPrimaryTraitMaxVariants() {
        return 0;
    }

    @Override
    public int getSecondaryTraitMaxVariants() {
        /// Horns
        /// Horns Inverted
        /// Dragon Horns
        return 3;
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
