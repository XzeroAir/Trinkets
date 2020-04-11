package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.Overlays.OverlayRenderer;

public class PacketSendMana implements IMessage {

	public PacketSendMana() {
	}

	private float mana;
	private float playerMana;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.mana = buf.readFloat();
		this.playerMana = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(this.mana);
		buf.writeFloat(this.playerMana);
	}

	public PacketSendMana(float mana, float playerMana) {
		this.mana = mana;
		this.playerMana = playerMana;
	}

	public static class Handler implements IMessageHandler<PacketSendMana, IMessage> {
		@Override
		public IMessage onMessage(PacketSendMana message, MessageContext ctx) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getPlayer(ctx) != null) {
					OverlayRenderer.instance.setMana(message.mana, message.playerMana);
				}
			});
			return null;
		}

	}

}
