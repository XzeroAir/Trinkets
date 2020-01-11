package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.IContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;

public class PacketAccessorySync implements IMessage {

	int playerId;
	byte slot;
	boolean isTrinket;
	boolean equipped;
	ItemStack Accessory;

	public PacketAccessorySync() {}

	public PacketAccessorySync(EntityPlayer player, int slot, boolean isTrinket, boolean equipped) {
		if(!isTrinket) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			Accessory = baubles.getStackInSlot(slot);
		} else {
			final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
			Accessory = Trinket.getStackInSlot(slot);
		}
		this.slot = (byte) slot;
		this.isTrinket = isTrinket;
		this.equipped = equipped;
		playerId = player.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(playerId);
		buffer.writeByte(slot);
		buffer.writeBoolean(isTrinket);
		buffer.writeBoolean(equipped);
		ByteBufUtils.writeItemStack(buffer, Accessory);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		playerId = buffer.readInt();
		slot = buffer.readByte();
		isTrinket = buffer.readBoolean();
		equipped = buffer.readBoolean();
		Accessory = ByteBufUtils.readItemStack(buffer);
	}

	public static class Handler implements IMessageHandler<PacketAccessorySync, IMessage>
	{
		@Override
		public IMessage onMessage(PacketAccessorySync message, MessageContext ctx) {

			//			if((Minecraft.getMinecraft().world != null) || (Minecraft.getMinecraft().player != null)) {
			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {
					final EntityPlayer player = (EntityPlayer) Trinkets.proxy.getPlayer(ctx).world.getEntityByID(message.playerId);
					if(!message.isTrinket) {
						final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
						if(message.slot >= 0) {
							if(message.equipped) {
								baubles.setStackInSlot(message.slot, message.Accessory);
							} else {
								baubles.setStackInSlot(message.slot, ItemStack.EMPTY);
							}
						}
					} else {
						final IContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
						if(message.slot >= 0) {
							if(message.equipped) {
								Trinket.setStackInSlot(message.slot, message.Accessory);
							} else {
								Trinket.setStackInSlot(message.slot, ItemStack.EMPTY);
							}
						}

					}
				}
			});
			//			}
			return null;
		}
	}
}
