package xzeroair.trinkets.network.status;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketStatusEffect;

public class StatusEffectPacket implements IMessage {

	public StatusEffectPacket() {
	}

	private int attackerEntityID = 0;
	private int targetEntityID = 0;
	private int effectID = 0;
	private int duration = 0;
	private int level = 0;

	public StatusEffectPacket(EntityLivingBase entity, EntityLivingBase targetEntity, int ID, int dur, int level) {
		attackerEntityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
		effectID = ID;
		duration = dur;
		this.level = level;
	}

	public StatusEffectPacket(EntityLivingBase entity, EntityLivingBase targetEntity, TrinketStatusEffect effect) {
		attackerEntityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
		effectID = effect.getEffectID();
		duration = effect.getDuration();
		level = effect.getLevel();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeInt(targetEntityID);
		buf.writeInt(effectID);
		buf.writeInt(duration);
		buf.writeInt(level);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		targetEntityID = buf.readInt();
		effectID = buf.readInt();
		duration = buf.readInt();
		level = buf.readInt();
	}

	public static class Handler implements IMessageHandler<StatusEffectPacket, IMessage> {

		@Override
		public IMessage onMessage(StatusEffectPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					final Entity player = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.attackerEntityID);
					if (player instanceof EntityPlayer) {
						if (player.hasCapability(Capabilities.ENTITY_RACE, null)) {
							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) player);
							if (cap != null) {
								final Entity targetEntity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.targetEntityID);
								if (targetEntity instanceof EntityLivingBase) {
									if ((targetEntity instanceof EntityPlayer) && !player.getEntityWorld().getMinecraftServer().isPVPEnabled()) {
										return;
									}
									EntityProperties tprop = Capabilities.getEntityRace((EntityLivingBase) targetEntity);
									tprop.getStatusHandler().apply(message.effectID, message.duration, message.level, (EntityLivingBase) player);
								}

							}
						}
					}
				}
			});
			return null;
		}
	}
}