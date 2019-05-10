package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;

public class PacketConfigSync implements IMessage {

	int playerId;
	boolean setting = true;

	public PacketConfigSync() {}

	public PacketConfigSync(EntityPlayer p, boolean setting) {
		this.setting = setting;
		this.playerId = p.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.playerId);
		buffer.writeBoolean(this.setting);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.playerId = buffer.readInt();
		this.setting = buffer.readBoolean();
	}

	public static class Handler implements IMessageHandler<PacketConfigSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {
					TrinketsConfig.SERVER.C04_DE_Chests = message.setting;
				}
			});
			return null;
		}
	}
}