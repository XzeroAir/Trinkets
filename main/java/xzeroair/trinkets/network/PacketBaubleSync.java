package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;

public class PacketBaubleSync implements IMessage {

	int playerId;
	byte slot=0;
	ItemStack bauble;

	public PacketBaubleSync() {}

	public PacketBaubleSync(EntityPlayer p, int slot) {
		final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(p);
		this.slot = (byte) slot;
		this.bauble = baubles.getStackInSlot(slot);
		this.playerId = p.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(this.playerId);
		buffer.writeByte(this.slot);
		ByteBufUtils.writeItemStack(buffer, this.bauble);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		this.playerId = buffer.readInt();
		this.slot = buffer.readByte();
		this.bauble = ByteBufUtils.readItemStack(buffer);
	}

	public static class Handler implements IMessageHandler<PacketBaubleSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketBaubleSync message, MessageContext ctx) {

			if((Minecraft.getMinecraft().world != null) || (Minecraft.getMinecraft().player != null)) {
				Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
					if(Trinkets.proxy.getPlayer(ctx) != null) {
						final EntityPlayer p = (EntityPlayer) Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.playerId);
						final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(p);
						baubles.setStackInSlot(message.slot, message.bauble);

						//if(ctx.side == Side.CLIENT) {
						//player.sendMessage(new TextComponentString("You Sent A Packet!"));
						//} else {
						//player.sendMessage(new TextComponentString("You Recieved A Packet!"));
						//}
					}
				});
			}
			return null;
		}
	}
}
