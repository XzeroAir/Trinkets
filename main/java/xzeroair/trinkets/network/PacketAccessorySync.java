package xzeroair.trinkets.network;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;

public class PacketAccessorySync implements IMessage {

	int entityID;
	byte slot;
	int handler;
	boolean equipped;
	ItemStack Accessory;
	NBTTagCompound tag;

	public PacketAccessorySync() {
	}

	public PacketAccessorySync(EntityPlayer player, ItemStack stack, NBTTagCompound nbt, int slot, int handler, boolean equipped) {
		Accessory = stack;
		this.slot = (byte) slot;
		this.equipped = equipped;
		entityID = player.getEntityId();
		tag = nbt;
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		buffer.writeInt(entityID);
		buffer.writeByte(slot);
		buffer.writeInt(handler);
		buffer.writeBoolean(equipped);
		ByteBufUtils.writeItemStack(buffer, Accessory);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		entityID = buffer.readInt();
		slot = buffer.readByte();
		handler = buffer.readInt();
		equipped = buffer.readBoolean();
		Accessory = ByteBufUtils.readItemStack(buffer);
		tag = ByteBufUtils.readTag(buffer);
	}

	public static class Handler implements IMessageHandler<PacketAccessorySync, IMessage> {
		@Override
		public IMessage onMessage(PacketAccessorySync message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).world != null)) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					if ((entity instanceof EntityPlayer)) {
						if (message.handler == 1) {
							ITrinketContainerHandler trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
							if (message.equipped) {
								trinket.setStackInSlot(message.slot, message.Accessory);
								ItemStack s = trinket.getStackInSlot(message.slot);
								if (!s.isEmpty()) {
									TrinketProperties properties = Capabilities.getTrinketProperties(s);
									if (properties != null) {
										properties.loadNBTData(message.tag);
										properties.sendInformationToTracking(entity);
									}
								}
							} else {
								trinket.setStackInSlot(message.slot, ItemStack.EMPTY);
							}
						} else if (message.handler == 2) {
							IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);
							if (message.equipped) {
								baubles.setStackInSlot(message.slot, message.Accessory);
								ItemStack s = baubles.getStackInSlot(message.slot);
								if (!s.isEmpty()) {
									TrinketProperties properties = Capabilities.getTrinketProperties(s);
									if (properties != null) {
										properties.loadNBTData(message.tag);
										properties.sendInformationToTracking(entity);
									}
								}
							} else {
								baubles.setStackInSlot(message.slot, ItemStack.EMPTY);
							}
						} else {

						}
						//						if (!message.isTrinket) {
						//							final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);
						//							if (message.slot >= 0) {
						//								if (message.equipped) {
						//									baubles.setStackInSlot(message.slot, message.Accessory);
						//									ItemStack s = baubles.getStackInSlot(message.slot);
						//									if (!s.isEmpty()) {
						//										TrinketProperties prop = Capabilities.getTrinketProperties(s);
						//										if (prop != null) {
						//											prop.loadNBTData(message.tag);
						//										}
						//									}
						//								} else {
						//									baubles.setStackInSlot(message.slot, ItemStack.EMPTY);
						//								}
						//							}
						//						} else {
						//							final ITrinketContainerHandler Trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
						//							if (message.slot >= 0) {
						//								if (message.equipped) {
						//									Trinket.setStackInSlot(message.slot, message.Accessory);
						//									ItemStack s = Trinket.getStackInSlot(message.slot);
						//									if (!s.isEmpty()) {
						//										TrinketProperties prop = Capabilities.getTrinketProperties(s);
						//										if (prop != null) {
						//											prop.loadNBTData(message.tag);
						//										}
						//									}
						//								} else {
						//									Trinket.setStackInSlot(message.slot, ItemStack.EMPTY);
						//								}
						//							}
						//
						//						}
					}
				}
			});
			return null;
		}
	}
}
