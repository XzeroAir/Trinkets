package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.UUID;

public class AttributeHelper {

    public static void removeAttributes(@Nonnull EntityLivingBase entity, UUID uuid) {
        if (!entity.world.isRemote) {
            final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
            for (final IAttributeInstance attribute : attributes) {
                if (!attribute.getAttribute().equals(RaceAttribute.ENTITY_RACE)) {
                    if (attribute.getModifier(uuid) != null) {
                        attribute.removeModifier(uuid);
                    }
                }
            }
        }
    }

    public static void removeAttributesByUUID(@Nonnull EntityLivingBase entity, UUID uuid) {
        if (!entity.world.isRemote) {
            final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
            for (final IAttributeInstance attribute : attributes) {
                if (!attribute.getAttribute().equals(RaceAttribute.ENTITY_RACE)) {
                    if (attribute.getModifier(uuid) != null) {
                        attribute.removeModifier(uuid);
                    }
                }
            }
        }
    }

    public static void removeAttributesByUUID(@Nonnull EntityLivingBase entity, UUID... uuids) {
        if (!entity.world.isRemote) {
            final Collection<IAttributeInstance> attributes = entity.getAttributeMap().getAllAttributes();
            for (final IAttributeInstance attribute : attributes) {
                if (!attribute.getAttribute().equals(RaceAttribute.ENTITY_RACE)) {
                    for (UUID uuid : uuids) {
                        if (attribute.getModifier(uuid) != null) {
                            attribute.removeModifier(uuid);
                        }
                    }
                }
            }
        }
    }
}
