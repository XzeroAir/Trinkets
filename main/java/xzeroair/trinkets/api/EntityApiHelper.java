package xzeroair.trinkets.api;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.capabilities.Capabilities;

/**
 * Self Note
 * Do not Remove, Change or Rename this method, or Class
 */
public class EntityApiHelper {

    public static String getEntityRace(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRace().getRace().getName());
    }

    public static String getEntityRaceRegistryName(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRace().getRace().getRegistryName().toString());
    }

    public static String getEntityPrimaryElement(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRace().getElement().getName());
    }

    public static String getEntityPrimaryElementRegistryName(Entity entity) {
        return Capabilities.getEntityProperties(entity, "", (prop, name) -> prop.getCurrentRace().getElement().getRegistryName().toString());
    }

}
