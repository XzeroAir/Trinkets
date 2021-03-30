package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.entity.AlphaWolf;

public class AlphaWolfAttackPacket implements IMessage {
	// A default constructor is always required
	public AlphaWolfAttackPacket() {
	}

	private int entityID;

	public AlphaWolfAttackPacket(EntityLivingBase entity) {
		entityID = entity.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID); //check carryon source for picking up entities
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<AlphaWolfAttackPacket, IMessage> {

		@Override
		public IMessage onMessage(AlphaWolfAttackPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof AlphaWolf) {
						//						AlphaWolf wolf = (AlphaWolf) entity;
						//						EntityWolf oldWolf = new EntityWolf(entity.getEntityWorld());
						//						if (oldWolf != null) {
						//							oldWolf.readFromNBT(wolf.getPreviousWolf());
						//							oldWolf.setLocationAndAngles(wolf.posX, wolf.posY, wolf.posZ, wolf.rotationYaw, 0F);
						//							oldWolf.setHealth(wolf.getHealth());
						//							entity.world.spawnEntity(oldWolf);
						//							wolf.setDead();
						//						}
					}
				}
			});
			return null;
		}
	}
}