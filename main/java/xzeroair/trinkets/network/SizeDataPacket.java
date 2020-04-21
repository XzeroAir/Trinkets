package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.RaceProperties;

public class SizeDataPacket implements IMessage {
	// A default constructor is always required
	public SizeDataPacket() {
	}

	public int size = 100;
	public boolean trans = false;
	public int target = 100;
	public String food = "none";
	public float width = 0.6F;
	public float height = 1.8F;
	public float defaultWidth = 0.6F;
	public float defaultHeight = 1.8F;
	public float eyeHeight = 1.62F;
	public int entityID = 0;

	public SizeDataPacket(EntityLivingBase entity, RaceProperties cap) {
		entityID = entity.getEntityId();
		size = cap.getSize();
		trans = cap.getTrans();
		target = cap.getTarget();
		food = cap.getFood();
		width = entity.width;
		height = entity.height;
		defaultWidth = entity.width;
		defaultHeight = entity.height;
		eyeHeight = entity.getEyeHeight();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(size);
		buf.writeBoolean(trans);
		buf.writeInt(target);
		ByteBufUtils.writeUTF8String(buf, food);
		buf.writeFloat(width);
		buf.writeFloat(height);
		buf.writeFloat(defaultWidth);
		buf.writeFloat(defaultHeight);
		buf.writeFloat(eyeHeight);
		buf.writeInt(entityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		size = buf.readInt();
		trans = buf.readBoolean();
		target = buf.readInt();
		ByteBufUtils.readUTF8String(buf);
		width = buf.readFloat();
		height = buf.readFloat();
		defaultWidth = buf.readFloat();
		defaultHeight = buf.readFloat();
		eyeHeight = buf.readFloat();
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<SizeDataPacket, IMessage> {

		@Override
		public IMessage onMessage(SizeDataPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					final Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof EntityLivingBase) {
						if (entity.hasCapability(Capabilities.ENTITY_RACE, null)) {
							RaceProperties cap = Capabilities.getEntityRace((EntityLivingBase) entity);
							if (cap != null) {
								cap.setSize(message.size);
								cap.setTrans(message.trans);
								cap.setTarget(message.target);
								cap.setFood(message.food);
								cap.setWidth(message.width);
								cap.setHeight(message.height);
								cap.setDefaultWidth(message.defaultWidth);
								cap.setDefaultHeight(message.defaultHeight);
							}
						}
					}
				}
			});
			return null;
		}
	}

}