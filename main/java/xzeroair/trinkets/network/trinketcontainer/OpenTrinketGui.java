package xzeroair.trinkets.network.trinketcontainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.network.ThreadSafePacket;

public class OpenTrinketGui extends ThreadSafePacket {

	private int guiID;

	public OpenTrinketGui() {
	}

	public OpenTrinketGui(int guiID) {
		this.guiID = guiID;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(guiID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		guiID = buf.readInt();
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		//		final EntityPlayerSP entity = Minecraft.getMinecraft().player;
		//		entity.openContainer.onContainerClosed(entity);
		//		entity.openGui(Trinkets.instance, Trinkets.GUI, entity.world, 0, 0, 0);
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final EntityPlayerMP entity = server.player;
		if (guiID == 99) {
			entity.openContainer.onContainerClosed(entity);
			entity.openContainer = entity.inventoryContainer;
		} else {
			entity.openContainer.onContainerClosed(entity);
			entity.openGui(Trinkets.instance, guiID, entity.world, 0, 0, 0);
		}
	}
}