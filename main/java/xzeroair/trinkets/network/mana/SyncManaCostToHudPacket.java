package xzeroair.trinkets.network.mana;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;
import xzeroair.trinkets.network.ThreadSafePacket;

public class SyncManaCostToHudPacket extends ThreadSafePacket {

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

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		ScreenOverlayEvents.instance.SyncCost(cost);
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {

	}

}
