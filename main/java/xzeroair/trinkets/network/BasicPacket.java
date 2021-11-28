package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class BasicPacket implements IMessage {

	public BasicPacket() {
	}

	public BasicPacket(int entityID, NBTTagCompound tag) {
		this();
		this.entityID = entityID;
		this.tag = tag;
	}

	protected abstract IMessage handleClient(NetHandlerPlayClient ctx);

	protected abstract IMessage handleServer(NetHandlerPlayServer ctx);

	protected int entityID;
	protected NBTTagCompound tag;

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

}
