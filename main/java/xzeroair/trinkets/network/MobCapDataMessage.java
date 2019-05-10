package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;

public class MobCapDataMessage implements IMessage {
	// A default constructor is always required
	public MobCapDataMessage(){}

	int size = 100;
	boolean trans = false;
	int target = 100;
	float width = 0.6F;
	float height = 1.8F;
	float defaultWidth = 0.6F;
	float defaultHeight = 1.8F;
	int entityID = 0;

	public MobCapDataMessage(int size, boolean trans, int target, int entityID) {
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.entityID = entityID;
	}

	public MobCapDataMessage(int size, boolean trans, int target, float width, float height, float defaultWidth, float defaultHeight, int entityID) {
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.width = width;
		this.height = height;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.entityID = entityID;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.size);
		buf.writeBoolean(this.trans);
		buf.writeInt(this.target);
		buf.writeFloat(this.width);
		buf.writeFloat(this.height);
		buf.writeFloat(this.defaultWidth);
		buf.writeFloat(this.defaultHeight);
		buf.writeInt(this.entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		this.size = buf.readInt();
		this.trans = buf.readBoolean();
		this.target = buf.readInt();
		this.width = buf.readFloat();
		this.height = buf.readFloat();
		this.defaultWidth = buf.readFloat();
		this.defaultHeight = buf.readFloat();
		this.entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<MobCapDataMessage, IMessage> {

		@Override public IMessage onMessage(MobCapDataMessage message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID) != null) {
					final EntityLiving entity = (EntityLiving) Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
					final ISizeCap cap = entity.getCapability(SizeCapPro.sizeCapability, null);

					cap.setSize(message.size);
					cap.setTrans(message.trans);
					cap.setTarget(message.target);
					cap.setWidth(message.width);
					cap.setHeight(message.height);
					entity.width = message.width;
					entity.height = message.height;
					cap.setDefaultWidth(message.defaultWidth);
					cap.setDefaultHeight(message.defaultHeight);

					//if(ctx.side == Side.CLIENT) {
					//Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));
					//} else {
					//Main.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Recieved A Packet!"));
					//}
				}
			});
			return null;
		}
	}
}