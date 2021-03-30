package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.entity.AlphaWolf;

public class AlphaWolfPacket implements IMessage {
	// A default constructor is always required
	public AlphaWolfPacket() {
	}

	private int entityID;

	public AlphaWolfPacket(EntityLivingBase entity) {
		entityID = entity.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<AlphaWolfPacket, IMessage> {

		@Override
		public IMessage onMessage(AlphaWolfPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof AlphaWolf) {
						AlphaWolf wolf = (AlphaWolf) entity;
						Entity oldWolf = EntityList.createEntityFromNBT(wolf.getPreviousWolf(), entity.getEntityWorld());
						if (oldWolf != null) {
							oldWolf.readFromNBT(wolf.getPreviousWolf());
							oldWolf.setLocationAndAngles(wolf.posX, wolf.posY + 1.1F, wolf.posZ, wolf.rotationYaw, 0F);
							if (oldWolf instanceof EntityLivingBase) {
								((EntityLivingBase) oldWolf).setHealth(wolf.getHealth());
							}
							entity.world.spawnEntity(oldWolf);
							wolf.setDead();
						}
					}
				}
			});
			return null;
		}
	}
}