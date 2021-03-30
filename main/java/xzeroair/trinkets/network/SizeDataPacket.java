package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class SizeDataPacket implements IMessage {
	// A default constructor is always required
	public SizeDataPacket() {
	}

	private int entityID;
	private NBTTagCompound tag;

	public SizeDataPacket(EntityLivingBase entity, EntityProperties properties) {
		entityID = entity.getEntityId();
		tag = new NBTTagCompound();
		properties.savedNBTData(tag);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	public static class Handler implements IMessageHandler<SizeDataPacket, IMessage> {

		@Override
		public IMessage onMessage(SizeDataPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase) {
						if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) entity);
							if (cap != null) {
								cap.loadNBTData(message.tag);
								cap.sendInformationToTracking();
							}
						}
					}
				}
			});
			return null;
		}
	}

}