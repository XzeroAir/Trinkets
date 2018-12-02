package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.sizeCap.CapPro;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class MobCapDataMessage implements IMessage {
	// A default constructor is always required
	public MobCapDataMessage(){}

	public int size = 100;
	public boolean trans = false;
	public int target = 100;
	public float width = 0.6F;
	public float height = 1.8F;
	public float defaultWidth = 0.6F;
	public float defaultHeight = 1.8F;
	public int entityID = 0;

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
		buf.writeInt(size);
		buf.writeBoolean(trans);
		buf.writeInt(target);
		buf.writeFloat(width);
		buf.writeFloat(height);
		buf.writeFloat(defaultWidth);
		buf.writeFloat(defaultHeight);
		buf.writeInt(entityID);
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

			Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID) != null) {
					EntityLiving entity = (EntityLiving) Main.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
					ICap cap = entity.getCapability(CapPro.sizeCapability, null);

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