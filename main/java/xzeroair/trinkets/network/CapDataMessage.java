package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;
import xzeroair.trinkets.capabilities.sizeCap.SizeCapPro;

public class CapDataMessage implements IMessage {
	// A default constructor is always required
	public CapDataMessage(){}

	public int size = 100;
	public boolean trans = false;
	public int target = 100;
	public float width = 0.6F;
	public float height = 1.8F;
	public float defaultWidth = 0.6F;
	public float defaultHeight = 1.8F;
	public float eyeHeight = 1.62F;
	public int entityID = 0;

	public CapDataMessage(int size, boolean trans, int target, float width, float height, float defaultWidth, float defaultHeight, float eyeHeight, int entityID) {
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.width = width;
		this.height = height;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.eyeHeight = eyeHeight;
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
		buf.writeFloat(this.eyeHeight);
		buf.writeInt(this.entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		this.size = buf.readInt();
		this.trans = buf.readBoolean();
		this.target = buf.readInt();
		this.width = buf.readFloat();
		this.height = buf.readFloat();
		this.defaultWidth = buf.readFloat();
		this.defaultHeight = buf.readFloat();
		this.eyeHeight = buf.readFloat();
		this.entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<CapDataMessage, IMessage> {

		@Override public IMessage onMessage(CapDataMessage message, MessageContext ctx) {

			//			if((Minecraft.getMinecraft().world != null) || (Minecraft.getMinecraft().player != null)) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {
					final EntityPlayer player = (EntityPlayer) Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.entityID);
					final ISizeCap cap = player.getCapability(SizeCapPro.sizeCapability, null);

					cap.setSize(message.size);
					cap.setTrans(message.trans);
					cap.setTarget(message.target);
					cap.setWidth(message.width);
					cap.setHeight(message.height);
					cap.setDefaultWidth(message.defaultWidth);
					cap.setDefaultHeight(message.defaultHeight);

					player.eyeHeight = message.eyeHeight;

					//if(ctx.side == Side.CLIENT) {
					//player.sendMessage(new TextComponentString("You Sent A Packet!"));
					//} else {
					//player.sendMessage(new TextComponentString("You Recieved A Packet!"));
					//}

				}
			});
			//			}
			return null;
		}
	}

}