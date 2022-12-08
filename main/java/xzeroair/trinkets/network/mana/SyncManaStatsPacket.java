package xzeroair.trinkets.network.mana;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.network.ThreadSafePacket;

public class SyncManaStatsPacket extends ThreadSafePacket {

	public SyncManaStatsPacket() {
	}

	private int entityID;
	private NBTTagCompound tag;

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}

	public SyncManaStatsPacket(EntityLivingBase entity, MagicStats magic) {
		entityID = entity.getEntityId();
		tag = new NBTTagCompound();
		magic.saveToNBT(tag);
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final Entity player = Minecraft.getMinecraft().player.getEntityWorld().getEntityByID(entityID);
		final MagicStats cap = Capabilities.getMagicStats(player);
		if (cap != null) {
			cap.loadFromNBT(tag);
			//			ScreenOverlayEvents.instance.SyncMana(cap.getMana(), cap.getBonusMana(), cap.getMaxMana());
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
	}

}
