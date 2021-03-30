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

public class ItemCapDataMessage implements IMessage {
	// A default constructor is always required
	public ItemCapDataMessage() {
	}

	NBTTagCompound tag;
	int slot;
	int handler;
	int entityID;
	ItemStack item;

	public ItemCapDataMessage(EntityLivingBase player, ItemStack stack, TrinketProperties properties, int slot, int handler) {
		this.slot = slot;
		this.handler = handler;
		tag = new NBTTagCompound();
		properties.savedNBTData(tag);
		item = stack;
		entityID = player.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(slot);
		buf.writeInt(handler);
		buf.writeInt(entityID);
		ByteBufUtils.writeItemStack(buf, item);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		slot = buf.readInt();
		handler = buf.readInt();
		entityID = buf.readInt();
		item = ByteBufUtils.readItemStack(buf);
		tag = ByteBufUtils.readTag(buf);
	}

	public static class Handler implements IMessageHandler<ItemCapDataMessage, IMessage> {

		@Override
		public IMessage onMessage(ItemCapDataMessage message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getEntityLivingBase(ctx, message.entityID) != null) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					if ((entity instanceof EntityPlayer)) {
						if (message.handler == 1) {
							ITrinketContainerHandler trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
							ItemStack stack = trinket.getStackInSlot(message.slot);
							if (!stack.isEmpty()) {
								TrinketProperties properties = Capabilities.getTrinketProperties(stack);
								if (properties != null) {
									properties.loadNBTData(message.tag);
									properties.sendInformationToTracking(entity);
								}
							}
						} else if (message.handler == 2) {
							IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);
							ItemStack stack = baubles.getStackInSlot(message.slot);
							if (!stack.isEmpty()) {
								TrinketProperties properties = Capabilities.getTrinketProperties(stack);
								if (properties != null) {
									properties.loadNBTData(message.tag);
									properties.sendInformationToTracking(entity);
								}
							}
						} else {

						}
						//						ItemStack stack = TrinketHelper.getAccessory((EntityPlayer) entity, message.item.getItem());
						//						final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
						//						if (iCap != null) {
						//							iCap.setTarget(message.type);
						//							iCap.toggleMainAbility(message.on);
						//							iCap.toggleAltAbility(message.altOn);
						//							iCap.setSlot(message.slot);
						//							iCap.setCount(message.hits);
						//							iCap.setStoredExp(message.exp);
						//							iCap.setStoredMana(message.mana);
						//						}
					}
				}
			});
			return null;
		}
	}
}