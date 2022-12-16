<<<<<<< Updated upstream
package xzeroair.trinkets.network.vip;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.vip.VIPHandler;

public class VipStatusPacket extends ThreadSafePacket {

	public VipStatusPacket() {
		super();
	}

	public VipStatusPacket(EntityPlayer entity, NBTTagCompound tag) {
		super(entity.getEntityId(), tag);
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

	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		if (!TrinketsConfig.SERVER.misc.retrieveVIP) {
			return;
		}
		final EntityPlayerMP serverPlayer = server.player;
		try {
			if (tag == null) {
				return;
			} else {
				if (!tag.hasKey("uuid")) {
					return;
				}
			}
			final String id = tag.getString("uuid");
			if (VIPHandler.Vips.containsKey(id.replaceAll("-", ""))) {
				final Entity entity = serverPlayer.getServerWorld().getEntityFromUuid(UUID.fromString(id));
				if ((entity != null) && (entity instanceof EntityPlayerMP)) {
					final EntityPlayerMP vip = (EntityPlayerMP) entity;
					final VipStatus status = Capabilities.getVipStatus(vip);
					if (status != null) {
						status.confirmedStatus();
					}
				}
			}
		} catch (final Exception e) {
		}
	}

=======
package xzeroair.trinkets.network.vip;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Vip.VipStatus;
import xzeroair.trinkets.network.ThreadSafePacket;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.vip.VIPHandler;

public class VipStatusPacket extends ThreadSafePacket {

	public VipStatusPacket() {
		super();
	}

	public VipStatusPacket(EntityPlayer entity, NBTTagCompound tag) {
		super(entity.getEntityId(), tag);
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

	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		if (!TrinketsConfig.SERVER.misc.retrieveVIP) {
			return;
		}
		final EntityPlayerMP serverPlayer = server.player;
		try {
			if (tag == null) {
				return;
			} else {
				if (!tag.hasKey("uuid")) {
					return;
				}
			}
			final String id = tag.getString("uuid");
			if (VIPHandler.Vips.containsKey(id.replaceAll("-", ""))) {
				final Entity entity = serverPlayer.getServerWorld().getEntityFromUuid(UUID.fromString(id));
				if ((entity != null) && (entity instanceof EntityPlayerMP)) {
					final EntityPlayerMP vip = (EntityPlayerMP) entity;
					final VipStatus status = Capabilities.getVipStatus(vip);
					if (status != null) {
						status.confirmedStatus();
					}
				}
			}
		} catch (final Exception e) {
		}
	}

>>>>>>> Stashed changes
}