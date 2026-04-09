package xzeroair.trinkets.api;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.capabilities.Capabilities;

/**
 * Self Note
 * Do not Remove, Change or Rename this method, or Class
 */
public class EntityApiHelper {

    public static String getEntityRace(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRaceCache().getRace().getName());
    }

    public static String getEntityRaceRegistryName(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRaceCache().getRace().getRegistryName().toString());
    }

    public static String getEntityPrimaryElement(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRaceCache().getPrimaryElement().getName());
    }

    public static String getEntityPrimaryElementRegistryName(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRaceCache().getPrimaryElement().getRegistryName().toString());
    }

}
