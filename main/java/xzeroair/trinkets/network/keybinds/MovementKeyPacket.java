package xzeroair.trinkets.network.keybinds;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.network.ThreadSafePacket;

public class MovementKeyPacket extends ThreadSafePacket {

	private int entityID;
	private String key;
	private int state;

	public MovementKeyPacket() {
	}

	public MovementKeyPacket(EntityLivingBase entity, String key, int state) {
		entityID = entity.getEntityId();
		this.key = key;
		this.state = state;
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buffer, key);
		buffer.writeInt(state);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		entityID = buffer.readInt();
		key = ByteBufUtils.readUTF8String(buffer);
		state = buffer.readInt();
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		//		final Entity entity = Minecraft.getMinecraft().world.getEntityByID(entityID);
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final Entity entity = server.player.getEntityWorld().getEntityByID(entityID);
		Capabilities.getEntityProperties(entity, prop -> {
			prop.getKeybindHandler().pressKey(entity, key, state);
		});
	}
}
