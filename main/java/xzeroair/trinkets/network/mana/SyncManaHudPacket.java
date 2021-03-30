package xzeroair.trinkets.network.mana;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;

public class SyncManaHudPacket implements IMessage {

	public SyncManaHudPacket() {
	}

	private float mana;
	private float maxMana;
	private int bonusMana;

	@Override
	public void fromBytes(ByteBuf buf) {
		mana = buf.readFloat();
		maxMana = buf.readFloat();
		bonusMana = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(mana);
		buf.writeFloat(maxMana);
		buf.writeInt(bonusMana);
	}

	public SyncManaHudPacket(float mana, int bonusMana, float maxMana) {
		this.mana = mana;
		this.maxMana = maxMana;
		this.bonusMana = bonusMana;
	}

	public static class Handler implements IMessageHandler<SyncManaHudPacket, IMessage> {
		@Override
		public IMessage onMessage(SyncManaHudPacket message, MessageContext ctx) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getPlayer(ctx) != null) {
					ScreenOverlayEvents.instance.SyncMana(message.mana, message.bonusMana, message.maxMana);
				}
			});
			return null;
		}

	}

}
