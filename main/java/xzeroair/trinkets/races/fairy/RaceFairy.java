package xzeroair.trinkets.races.fairy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.fairy.RaceFairyRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.traits.abilities.AbilityClimbing;
import xzeroair.trinkets.traits.abilities.AbilityFlying;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigFairyRing;
import xzeroair.trinkets.util.helpers.EntityHelper;

import java.util.Arrays;
import java.util.List;

public class RaceFairy extends EntityRacePropertiesHandler {

    public static final FairyConfig serverConfig = TrinketsConfig.SERVER.races.fairy;
    public static final ClientConfigFairyRing clientConfig = TrinketsConfig.CLIENT.items.FAIRY_RING;

    public RaceFairy(EntityLivingBase e, Element element) {
        super(e, EntityRaces.fairy, element);
    }

    public RaceFairy(EntityLivingBase e) {
        super(e, EntityRaces.fairy);
    }

    @Override
    public void startTransformation() {
        if (serverConfig.creative_flight) {
            this.addAbility(new AbilityFlying().setFlightCost(serverConfig.flight_cost));
        }
        if (TrinketsConfig.getClientStore().CLIMBING_ENABLED) {
            this.addAbility(new AbilityClimbing());
        }
    }

    @Override
    public void whileTransformed() {
        if (!entity.world.isRemote && entity.isRiding()) {
            final Entity mount = entity.getRidingEntity();
            if ((mount != null)) {
                if (!this.mountEntity(mount)) {
                    entity.dismountRidingEntity();
                }
            }
        }
    }

    @Override
    public boolean canFly() {
        return super.canFly() && this.showTraits() && serverConfig.creative_flight;
    }

    @Override
    public boolean mountEntity(Entity mount) {
        if (EntityHelper.isCreative(entity)) {
            return true;
        } else if (!serverConfig.canMount) {
            return false;
        } else if (serverConfig.mountBlacklist.length > 0) {
            List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
            try {
                final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
                final String modID = regName.getNamespace();
                final String entityID = regName.getPath();
                final boolean exists = disallowedMounts.contains(modID + ":*") || disallowedMounts.contains(regName.toString());
                if (exists) {
                    if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == entity)) {
                            return false;
                        }
                    }
                    return serverConfig.whitelist;
                } else {
                    if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
                        final EntityBoat boat = (EntityBoat) mount;
                        final Entity controller = boat.getControllingPassenger();
                        if ((controller == null) || (controller == entity)) {
                            return false;
                        }
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return !serverConfig.whitelist;
        } else {
            if (!serverConfig.canControlBoats && (mount instanceof EntityBoat)) {
                final EntityBoat boat = (EntityBoat) mount;
                final Entity controller = boat.getControllingPassenger();
                if ((controller == null) || (controller == entity)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceFairyRenderer(entity, this);
        }
        return RendererRace;
    }

}
