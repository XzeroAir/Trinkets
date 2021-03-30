package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;

public class MoveHudMessage implements IMessage, IMessageHandler<MoveHudMessage, IMessage> {

	public MoveHudMessage() {
	}

	@Override
	public void toBytes(ByteBuf buffer) {
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
	}

	@Override
	public IMessage onMessage(MoveHudMessage message, MessageContext ctx) {

		Trinkets.proxy.getThreadListener(ctx).addScheduledTask(new Runnable() {
			@Override
			public void run() {

				ctx.getServerHandler().player.openContainer.onContainerClosed(ctx.getServerHandler().player);
				ctx.getServerHandler().player.openGui(Trinkets.instance, 1, ctx.getServerHandler().player.world, 0, 0, 0);

			}
		});
		return null;
	}
}