package xzeroair.trinkets.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.attributes.FlyingAttribute;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.MagicAttributes;
import xzeroair.trinkets.attributes.RaceAttribute.RaceAttribute;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.configsync.PacketConfigSync;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;

public class OnWorldJoinHandler {

    @SubscribeEvent
    public void attachAttributes(@Nonnull EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            final AbstractAttributeMap map = entity.getAttributeMap();

            map.registerAttribute(RaceAttribute.ENTITY_RACE);
            map.registerAttribute(JumpAttribute.Jump);
            map.registerAttribute(JumpAttribute.stepHeight);
            map.registerAttribute(FlyingAttribute.Fly_Speed);
            map.registerAttribute(MagicAttributes.MAX_MANA);
            map.registerAttribute(MagicAttributes.regen);
            map.registerAttribute(MagicAttributes.regenCooldown);
            map.registerAttribute(MagicAttributes.affinity);


        }
    }

    /*
     * Fired on Logical Server and Fired After EntityJoinedWorldEvent
     */
    @SubscribeEvent
    public void onPlayerLogin(@Nonnull PlayerLoggedInEvent event) {
        final EntityPlayer player = event.player;
        if ((player instanceof EntityPlayerMP)) {
            final EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
            // config Sync
            Trinkets.LOGGER.info("Syncing Config to " + playerMP.getName());
            final NBTTagCompound config = TrinketsConfig.writeConfigMap();
            if (!config.isEmpty()) {
                NetworkHandler.sendTo(new PacketConfigSync(config), playerMP);
            }
            if (TrinketsConfig.SERVER.MISC.VIPS) {
                Capabilities.getVipStatus(player, VipStatus::onLogin);
            }
            Capabilities.getEntityProperties(playerMP, EntityProperties::onLogin);
            Capabilities.getMagicStats(playerMP, MagicStats::onLogin);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(@Nonnull PlayerLoggedOutEvent event) {
        final EntityPlayer player = event.player;
        if ((player instanceof EntityPlayerMP)) {
            final EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
            if (TrinketsConfig.SERVER.MISC.VIPS) {
                Capabilities.getVipStatus(playerMP, VipStatus::onJoinWorld);
            }
            Capabilities.getEntityProperties(playerMP, EntityProperties::onLogoff);
            Capabilities.getMagicStats(playerMP, MagicStats::onLogoff);
        }
    }

    /**
     * Send Capability Data To the player from Server, Because Client is Incorrect
     */
    @SubscribeEvent
    public void entityJoinWorld(@Nonnull EntityJoinWorldEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (TrinketsConfig.SERVER.MISC.VIPS) {
                Capabilities.getVipStatus(player, VipStatus::onJoinWorld);
            }
            Capabilities.getEntityProperties(player, EntityProperties::onJoinWorld);
            Capabilities.getMagicStats(player, MagicStats::onJoinWorld);
        }
    }

    /**
     * Runs Server Side Only, Runs After EntityjoinWorld
     */
    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        final EntityPlayer player = event.player;
        if (TrinketsConfig.SERVER.MISC.VIPS) {
            Capabilities.getVipStatus(player, (vip) -> vip.onChangedDimension(event.fromDim, event.toDim));
        }
        Capabilities.getEntityProperties(player, (prop) -> prop.onChangedDimension(event.fromDim, event.toDim));
        Capabilities.getMagicStats(player, (magic) -> magic.onChangedDimension(event.fromDim, event.toDim));
    }

}
