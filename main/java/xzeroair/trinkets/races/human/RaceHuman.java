package xzeroair.trinkets.races.human;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.handlers.SizeHandler;

public class RaceHuman extends EntityRacePropertiesHandler {

	public RaceHuman(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.human);
	}

	@Override
	public void onTick() {
		if (properties.isTransforming()) {
			SizeHandler.setSize(entity, properties);
		} else {
			if (firstUpdate) {
				if (entity instanceof EntityPlayer) {

				} else {
					if (entity.width != properties.getDefaultWidth()) {
						entity.width = properties.getDefaultWidth();
					}
					if (entity.height != properties.getDefaultHeight()) {
						entity.height = properties.getDefaultHeight();
					}
				}
				firstUpdate = false;
			}
		}
	}

	@Override
	public void whileTransformed() {

	}
}
