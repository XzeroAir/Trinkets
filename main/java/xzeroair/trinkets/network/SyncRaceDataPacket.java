package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;

public class SyncRaceDataPacket extends ThreadSafePacket {
	//	// A default constructor is always required
	public SyncRaceDataPacket() {
	}

	private int entityID;
	private NBTTagCompound tag;

	public SyncRaceDataPacket(EntityLivingBase entity, NBTTagCompound tag) {
		entityID = entity.getEntityId();
		this.tag = tag;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final Minecraft mc = Minecraft.getMinecraft();
		final World world = mc.player.getEntityWorld();
		final Entity entity = world.getEntityByID(entityID);
		Capabilities.getEntityProperties(
				entity, prop -> {
					prop.getTag().merge(tag);
					prop.loadFromNBT(tag);
				}
		);
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final Entity entity = server.player.getEntityWorld().getEntityByID(entityID);
		Capabilities.getEntityProperties(entity, prop -> {
			prop.loadFromNBT(tag);
			prop.sendInformationToTracking();
		});
	}

}