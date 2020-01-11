package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class BlocklistSyncPacket implements IMessage {

	int playerId;
	int index = 0;
	boolean setting = true;
	int HR;
	int VR;
	String string;

	public BlocklistSyncPacket() {}

	public BlocklistSyncPacket(EntityPlayer p, boolean setting, String string, int index, int HR, int VR) {
		this.setting = setting;
		this.string = string;
		this.index = index;
		this.HR = HR;
		this.VR = VR;
		playerId = p.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(playerId);
		buffer.writeBoolean(setting);
		ByteBufUtils.writeUTF8String(buffer, string);
		buffer.writeInt(index);
		buffer.writeInt(HR);
		buffer.writeInt(VR);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		playerId = buffer.readInt();
		setting = buffer.readBoolean();
		string = ByteBufUtils.readUTF8String(buffer);
		index = buffer.readInt();
		HR = buffer.readInt();
		VR = buffer.readInt();
	}

	public static class Handler implements IMessageHandler<BlocklistSyncPacket, IMessage>
	{
		@Override
		public IMessage onMessage(BlocklistSyncPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {

					final String[] blocklist = message.string.split(", ");
					if(TrinketsConfig.getBlockListArray(message.setting) != blocklist.clone()) {
						TrinketsConfig.setBlocklist(blocklist, message.setting);
					}
					TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C001_HD = message.HR;
					TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.DR.C00_VD = message.VR;

				}
			});
			return null;
		}
	}
}