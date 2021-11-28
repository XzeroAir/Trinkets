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
			sourceNull = false;
			attackerEntityID = source.getEntityId();
		} else {
			sourceNull = true;
		}
		targetEntityID = targetEntity.getEntityId();
		effectID = ID;
		duration = dur;
		this.level = level;
		this.combine = combine;
	}

	public StatusEffectPacket(Entity source, EntityLivingBase targetEntity, TrinketStatusEffect effect) {
		this(source, targetEntity, effect.getEffectID(), effect.getDuration(), effect.getLevel(), false);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeBoolean(sourceNull);
		if (sourceNull) {
			buf.writeInt(attackerEntityID);
		}
		buf.writeInt(targetEntityID);
		buf.writeInt(effectID);
		buf.writeInt(duration);
		buf.writeInt(level);
		buf.writeBoolean(combine);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		sourceNull = buf.readBoolean();
		if (sourceNull) {
			attackerEntityID = buf.readInt();
		}
		targetEntityID = buf.readInt();
		effectID = buf.readInt();
		duration = buf.readInt();
		level = buf.readInt();
		combine = buf.readBoolean();
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
			final Entity entity = clientPlayer.getEntityWorld().getEntityByID(targetEntityID);
			final Entity source = sourceNull ? null : clientPlayer.getEntityWorld().getEntityByID(attackerEntityID);
			final StatusHandler status = Capabilities.getStatusHandler(entity);
			if (status != null) {
				if (combine) {
					status.combine(effectID, duration, level);
				} else {
					status.apply(effectID, duration, level, source);
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
			final Entity entity = serverPlayer.getEntityWorld().getEntityByID(attackerEntityID);
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