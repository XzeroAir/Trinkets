package xzeroair.trinkets.races.mixed;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;

import javax.annotation.Nonnull;

public class RaceMixed extends EntityRacePropertiesHandler {

    protected EntityRace main, sub;

    public RaceMixed(@Nonnull EntityLivingBase e, @Nonnull EntityRace mainRace, @Nonnull EntityRace subRace) {
        super(e, EntityRaces.none);//EntityRaces.mixed);
        main = mainRace;
        sub = subRace;
    }

    public boolean isMixed() {
        return true;
    }

    @Override
    public void startTransformation() {
    }

    @Override
    public void endTransformation() {
        AttributeHelper.removeAttributes(entity, raceCache.getRace().getUUID());
    }

    @Override
    public void onTick() {

    }

    @Override
    public void whileTransformed() {

    }
}
