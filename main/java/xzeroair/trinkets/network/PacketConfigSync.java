package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
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
		buffer.writeInt(playerId);
		buffer.writeBoolean(setting);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		playerId = buffer.readInt();
		setting = buffer.readBoolean();
	}

	public static class Handler implements IMessageHandler<PacketConfigSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {

			//			if((Minecraft.getMinecraft().world != null) || (Minecraft.getMinecraft().player != null)) {
			Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Main.proxy.getPlayer(ctx) != null) {
					EntityPlayer p = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.playerId);
					TrinketsConfig.SERVER.C04_DE_Chests = message.setting;
					//if(ctx.side == Side.CLIENT) {
					//player.sendMessage(new TextComponentString("You Sent A Packet!"));
					//} else {
					//player.sendMessage(new TextComponentString("You Recieved A Packet!"));
					//}
				}
			});
			//			}
			return null;
		}
	}
}