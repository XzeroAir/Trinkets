package xzeroair.trinkets.api;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class EntityApiHelper {

	public static String getEntityRace(Entity entity) {
		try {
			final EntityProperties raceProp = Capabilities.getEntityRace(entity);
			if (raceProp != null) {
				return raceProp.getCurrentRace().getName();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
