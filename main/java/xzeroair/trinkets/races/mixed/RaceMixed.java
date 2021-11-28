package xzeroair.trinkets.races.mixed;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;
import xzeroair.trinkets.util.helpers.ColorHelper;

public class RaceMixed extends EntityRacePropertiesHandler {

	protected EntityRace main, sub;
	private ColorHelper color;

	public RaceMixed(@Nonnull EntityLivingBase e, @Nonnull EntityRace mainRace, @Nonnull EntityRace subRace, EntityProperties properties) {
		super(e, properties, null);//EntityRaces.mixed);
		main = mainRace;
		sub = subRace;
		color = new ColorHelper().setColor(properties.getTraitColor());
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
