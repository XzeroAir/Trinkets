package xzeroair.trinkets.network.trinketcontainer;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;

public class OpenDefaultInventory implements IMessage, IMessageHandler<OpenDefaultInventory, IMessage> {

	public OpenDefaultInventory() {}

	@Override
	public void toBytes(ByteBuf buffer) {}

	@Override
	public void fromBytes(ByteBuf buffer) {}

	@Override
	public IMessage onMessage(OpenDefaultInventory message, MessageContext ctx) {

		Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
			ctx.getServerHandler().player.openContainer.onContainerClosed(ctx.getServerHandler().player);
			ctx.getServerHandler().player.openContainer = ctx.getServerHandler().player.inventoryContainer;
		});
		return null;
	}
}