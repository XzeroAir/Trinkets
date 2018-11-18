package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ItemCapDataMessage implements IMessage {
	// A default constructor is always required
	public ItemCapDataMessage(){}

	public int type = 0;
	public boolean on = false;
	public int entityID = 0;

	public ItemCapDataMessage(int type, boolean on, int entityID) {
		this.type = type;
		this.on = on;
		this.entityID = entityID;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(type);
		buf.writeBoolean(on);
		buf.writeInt(entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		this.type = buf.readInt();
		this.on = buf.readBoolean();
		this.entityID = buf.readInt();
	}
}