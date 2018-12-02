package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.items.dwarf_ring;
import xzeroair.trinkets.items.small_ring;

public class PacketBaubleSync implements IMessage {

	int playerId;
	byte slot=0;
	ItemStack bauble;

	public PacketBaubleSync() {}

	public PacketBaubleSync(EntityPlayer p, int slot) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(p);
		this.slot = (byte) slot;
		this.bauble = baubles.getStackInSlot(slot);
		this.playerId = p.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(playerId);
		buffer.writeByte(slot);
		ByteBufUtils.writeItemStack(buffer, bauble);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		playerId = buffer.readInt();
		slot = buffer.readByte();
		bauble = ByteBufUtils.readItemStack(buffer);
	}

	public static class Handler implements IMessageHandler<PacketBaubleSync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketBaubleSync message, MessageContext ctx) {

			if((Minecraft.getMinecraft().world != null) || (Minecraft.getMinecraft().player != null)) {
				Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
					if(Main.proxy.getPlayer(ctx) != null) {
						EntityPlayer p = (EntityPlayer) Main.proxy.getPlayer(ctx).world.getEntityByID(message.playerId);
						IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) p);
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
