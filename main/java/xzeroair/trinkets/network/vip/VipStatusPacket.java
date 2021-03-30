package xzeroair.trinkets.network.vip;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;

public class VipStatusPacket implements IMessage {
	// A default constructor is always required
	public VipStatusPacket() {
	}

	public int entityID;
	public boolean login;
	public int status;
	public boolean isBro;
	public boolean isPanda;
	public boolean isVip;

	public VipStatusPacket(EntityLivingBase entity, VipStatus status) {
		entityID = entity.getEntityId();
		status.getStatus();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeBoolean(login);
		buf.writeInt(status);
		buf.writeBoolean(isBro);
		buf.writeBoolean(isPanda);
		buf.writeBoolean(isVip);
		buf.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		login = buf.readBoolean();
		status = buf.readInt();
		isBro = buf.readBoolean();
		isPanda = buf.readBoolean();
		isVip = buf.readBoolean();
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<VipStatusPacket, IMessage> {

		@Override
		public IMessage onMessage(VipStatusPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).world != null)) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					if (Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID).getUniqueID().compareTo(Trinkets.proxy.getPlayer(ctx).getUniqueID()) == 0) {
						entity = Trinkets.proxy.getPlayer(ctx);
					}
					if (entity instanceof EntityLivingBase) {
						if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
							VipStatus cap = Capabilities.getVipStatus(entity);
							if (cap != null) {
								//TODO Fix VIP
								cap.setStatus(message.status);
								//								cap.setIsBro(message.isBro);
								//								cap.setIsBro(message.isPanda);
								//								cap.setIsBro(message.isVip);
								cap.setLogin(message.login);
							}
						}
					}
				}
			});
			return null;
		}
	}

}