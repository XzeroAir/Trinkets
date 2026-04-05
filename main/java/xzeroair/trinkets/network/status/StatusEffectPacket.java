package xzeroair.trinkets.network.status;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.network.ThreadSafePacket;

public class StatusEffectPacket extends ThreadSafePacket {

    public StatusEffectPacket() {
    }

    boolean sourceNull = false;
    private int attackerEntityID = 0;
    private int targetEntityID = 0;
    private int effectID = 0;
    private int duration = 0;
    private int level = 0;
    private boolean combine;

    public StatusEffectPacket(Entity source, EntityLivingBase targetEntity, int ID, int dur, int level, boolean combine) {
        if (source != null) {
            this.sourceNull = false;
            this.attackerEntityID = source.getEntityId();
        } else {
            this.sourceNull = true;
        }
        this.targetEntityID = targetEntity.getEntityId();
        this.effectID = ID;
        this.duration = dur;
        this.level = level;
        this.combine = combine;
    }

    public StatusEffectPacket(Entity source, EntityLivingBase targetEntity, TrinketStatusEffect effect) {
        this(source, targetEntity, effect.getEffectID(), effect.getDuration(), effect.getLevel(), false);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        buf.writeBoolean(this.sourceNull);
        if (this.sourceNull) {
            buf.writeInt(this.attackerEntityID);
        }
        buf.writeInt(this.targetEntityID);
        buf.writeInt(this.effectID);
        buf.writeInt(this.duration);
        buf.writeInt(this.level);
        buf.writeBoolean(this.combine);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.sourceNull = buf.readBoolean();
        if (this.sourceNull) {
            this.attackerEntityID = buf.readInt();
        }
        this.targetEntityID = buf.readInt();
        this.effectID = buf.readInt();
        this.duration = buf.readInt();
        this.level = buf.readInt();
        this.combine = buf.readBoolean();
    }

    //	public static class Handler implements IMessageHandler<StatusEffectPacket, IMessage> {
    //
    //		@Override
    //		public IMessage onMessage(StatusEffectPacket message, MessageContext ctx) {
    //
    //			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
    //				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
    //					final Entity player = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.attackerEntityID);
    //					if (player instanceof EntityPlayer) {
    //						if (player.hasCapability(Capabilities.ENTITY_RACE, null)) {
    //							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) player);
    //							if (cap != null) {
    //								final Entity targetEntity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.targetEntityID);
    //								if (targetEntity instanceof EntityLivingBase) {
    //									if ((targetEntity instanceof EntityPlayer) && !player.getEntityWorld().getMinecraftServer().isPVPEnabled()) {
    //										return;
    //									}
    //									StatusHandler status = Capabilities.getStatusHandler((EntityLivingBase) targetEntity);
    //									if (status != null) {
    //										status.apply(message.effectID, message.duration, message.level, (EntityLivingBase) player);
    //									}
    //								}
    //
    //							}
    //						}
    //					}
    //				}
    //			});
    //			return null;
    //		}
    //	}

    @Override
    public void handleClientSafe(NetHandlerPlayClient client) {
        final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
        try {
            final Entity entity = clientPlayer.getEntityWorld().getEntityByID(this.targetEntityID);
            final Entity source = this.sourceNull ? null : clientPlayer.getEntityWorld().getEntityByID(this.attackerEntityID);
            final StatusHandler status = Capabilities.getStatusHandler(entity);
            if (status != null) {
                if (this.combine) {
                    status.combine(this.effectID, this.duration, this.level);
                } else {
                    status.apply(this.effectID, this.duration, this.level, source);
                }
            }
        } catch (

                final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleServerSafe(NetHandlerPlayServer server) {
        final EntityPlayerMP serverPlayer = server.player;
        WorldServer world = serverPlayer.getServerWorld();
        try {
            final Entity entity = serverPlayer.getEntityWorld().getEntityByID(this.attackerEntityID);
            if (entity != null) {
                if (entity instanceof EntityPlayerMP) {
                    world = ((EntityPlayerMP) entity).getServerWorld();
                }
                //				if (player instanceof EntityPlayer) {
                //						if (player.hasCapability(Capabilities.ENTITY_RACE, null)) {
                //							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) player);
                //							if (cap != null) {
                //								final Entity targetEntity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.targetEntityID);
                //								if (targetEntity instanceof EntityLivingBase) {
                //									if ((targetEntity instanceof EntityPlayer) && !player.getEntityWorld().getMinecraftServer().isPVPEnabled()) {
                //										return;
                //									}
                //									StatusHandler status = Capabilities.getStatusHandler((EntityLivingBase) targetEntity);
                //									if (status != null) {
                //										status.apply(message.effectID, message.duration, message.level, (EntityLivingBase) player);
                //									}
                //								}
                //
                //							}
                //						}
                //				NetworkHandler.sendToClients(
                //						world, entity.getPosition(),
                //						new StatusEffectPacket(entity, targetEntity, effectID, duration, level, combine);
                ////						new EffectsRenderPacket(entity, x, y, z, x2, y2, z2, color, effectID, alpha, intensity)
                //				);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}