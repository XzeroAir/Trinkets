package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MobCapDataMessage implements IMessage {
	// A default constructor is always required
	public MobCapDataMessage(){}

	public int size = 100;
	public boolean trans = false;
	public int target = 100;
	public float width = 0.6F;
	public float height = 1.8F;
	public float defaultWidth = 0.6F;
	public float defaultHeight = 1.8F;
	public int entityID = 0;

	public MobCapDataMessage(int size, boolean trans, int target, int entityID) {
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.entityID = entityID;
	}

	public MobCapDataMessage(int size, boolean trans, int target, float width, float height, float defaultWidth, float defaultHeight, int entityID) {
		this.size = size;
		this.trans = trans;
		this.target = target;
		this.width = width;
		this.height = height;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.entityID = entityID;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(size);
		buf.writeBoolean(trans);
		buf.writeInt(target);
		buf.writeFloat(width);
		buf.writeFloat(height);
		buf.writeFloat(defaultWidth);
		buf.writeFloat(defaultHeight);
		buf.writeInt(entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		this.size = buf.readInt();
		this.trans = buf.readBoolean();
		this.target = buf.readInt();
		this.width = buf.readFloat();
		this.height = buf.readFloat();
		this.defaultWidth = buf.readFloat();
		this.defaultHeight = buf.readFloat();
		this.entityID = buf.readInt();
	}
}