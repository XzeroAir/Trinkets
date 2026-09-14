package xzeroair.trinkets.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.traits.abilities.AbilitySummonSlime;

public class EntityWrapperLifecycleHandler {

    public static final EntityWrapperLifecycleHandler instance = new EntityWrapperLifecycleHandler();

    @SubscribeEvent
    public void playerLoggedOut(PlayerLoggedOutEvent event) {
        if (!event.player.world.isRemote) {
            this.ownerUnavailable(event.player);
        }
    }

    @SubscribeEvent
    public void playerTravelledToDimension(EntityTravelToDimensionEvent event) {
        if (!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayer) {
            this.ownerChangingDimension((EntityPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void playerChangedDimension(PlayerChangedDimensionEvent event) {
        if (!event.player.world.isRemote) {
            AbilitySummonSlime.restoreDimensionFollowingSummons(event.player);
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().world.isRemote) {
            return;
        }
        if (event.getEntityLiving() instanceof EntityPlayer) {
            this.ownerUnavailable((EntityPlayer) event.getEntityLiving());
        } else if (event.getEntityLiving() instanceof EntitySummonWrapper) {
            ((EntitySummonWrapper) event.getEntityLiving()).died();
        }
    }

    private void ownerUnavailable(EntityPlayer player) {
        for (Entity entity : player.world.loadedEntityList) {
            if (entity instanceof EntitySummonWrapper) {
                final EntitySummonWrapper wrapper = (EntitySummonWrapper) entity;
                if (!wrapper.isTemporarySummon() && wrapper.isSummonedBy(player)) {
                    wrapper.ownerUnavailable(player);
                }
            }
        }
    }

    private void ownerChangingDimension(EntityPlayer player) {
        for (Entity entity : player.world.loadedEntityList) {
            if (entity instanceof EntitySummonWrapper) {
                final EntitySummonWrapper wrapper = (EntitySummonWrapper) entity;
                if (!wrapper.isTemporarySummon() && wrapper.isSummonedBy(player)) {
                    wrapper.ownerChangingDimension(player);
                }
            }
        }
    }
}
