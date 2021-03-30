package xzeroair.trinkets.network.mana;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;

public class SyncManaCostToHudPacket implements IMessage {

	public SyncManaCostToHudPacket() {
	}

	private float cost;

	@Override
	public void fromBytes(ByteBuf buf) {
		cost = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(cost);
	}

	public SyncManaCostToHudPacket(float cost) {
		this.cost = cost;
	}

	public static class Handler implements IMessageHandler<SyncManaCostToHudPacket, IMessage> {
		@Override
		public IMessage onMessage(SyncManaCostToHudPacket message, MessageContext ctx) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getPlayer(ctx) != null) {
					ScreenOverlayEvents.instance.SyncCost(message.cost);
				}
			});
			return null;
		}

	}

}
