<<<<<<< Updated upstream
package xzeroair.trinkets.races.mixed;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class RaceMixed extends EntityRacePropertiesHandler {

	protected EntityRace main, sub;

	public RaceMixed(@Nonnull EntityLivingBase e, @Nonnull EntityRace mainRace, @Nonnull EntityRace subRace) {
		super(e, null);//EntityRaces.mixed);
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
		AttributeHelper.removeAttributes(entity, race.getUUID());
	}

	@Override
	public void onTick() {

	}

	@Override
	public void whileTransformed() {

	}
}
=======
package xzeroair.trinkets.races.mixed;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class RaceMixed extends EntityRacePropertiesHandler {

	protected EntityRace main, sub;

	public RaceMixed(@Nonnull EntityLivingBase e, @Nonnull EntityRace mainRace, @Nonnull EntityRace subRace) {
		super(e, null);//EntityRaces.mixed);
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
		AttributeHelper.removeAttributes(entity, race.getUUID());
	}

	@Override
	public void onTick() {

	}

	@Override
	public void whileTransformed() {

	}
}
>>>>>>> Stashed changes
