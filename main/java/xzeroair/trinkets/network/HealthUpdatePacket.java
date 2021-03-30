package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;

public class HealthUpdatePacket implements IMessage {
	// A default constructor is always required
	public HealthUpdatePacket() {
	}

	public float health;
	public int entityID;

	public HealthUpdatePacket(EntityLivingBase entity) {
		health = entity.getHealth();
		entityID = entity.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeFloat(health);
		buf.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		health = buf.readFloat();
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<HealthUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(HealthUpdatePacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).world != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
					if (Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID).getUniqueID().compareTo(Trinkets.proxy.getPlayer(ctx).getUniqueID()) == 0) {
						entity = Trinkets.proxy.getPlayer(ctx);
					}
					if (entity instanceof EntityLivingBase) {
						((EntityLivingBase) entity).setHealth(message.health);
					}
				}
			});
			return null;
		}
	}

}