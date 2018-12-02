package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.compatibilities.ItemCap.ItemCap;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.helpers.TrinketHelper;

public class ItemCapDataMessage implements IMessage {
	// A default constructor is always required
	public ItemCapDataMessage(){}

	public int type = 0;
	public boolean on = false;
	public int entityID = 0;

	public ItemCapDataMessage(int type, boolean on, int entityID) {
		this.type = type;
		this.on = on;
		this.entityID = entityID;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(type);
		buf.writeBoolean(on);
		buf.writeInt(entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		this.type = buf.readInt();
		this.on = buf.readBoolean();
		this.entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<ItemCapDataMessage, IMessage> {

		@Override public IMessage onMessage(ItemCapDataMessage message, MessageContext ctx) {

			Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Main.proxy.getPlayer(ctx) != null) {
					if(Main.proxy.getPlayer(ctx).getEntityId() == message.entityID) {
						ItemCap itemNBT = TrinketHelper.getBaubleCap(TrinketHelper.getBaubleStack(Main.proxy.getPlayer(ctx), ModItems.dragons_eye));
						if(itemNBT.oreType() != message.type) {
							itemNBT.setOreType(message.type);
						}
						itemNBT.nightVisionOn(message.on);
					}
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