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

public class PolarizedStoneSyncPacket implements IMessage {
	// A default constructor is always required
	public PolarizedStoneSyncPacket() {
	}

	NBTTagCompound tag;
	int slot;
	int handler;
	int entityID;
	ItemStack item;
	int damage;

	public PolarizedStoneSyncPacket(EntityLivingBase player, ItemStack stack, TrinketProperties properties, int slot, int handler, int damage) {
		this.slot = slot;
		this.handler = handler;
		tag = new NBTTagCompound();
		properties.savedNBTData(tag);
		item = stack;
		entityID = player.getEntityId();
		this.damage = damage;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(slot);
		buf.writeInt(handler);
		buf.writeInt(entityID);
		ByteBufUtils.writeItemStack(buf, item);
		ByteBufUtils.writeTag(buf, tag);
		buf.writeInt(damage);
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
		damage = buf.readInt();
	}

	public static class Handler implements IMessageHandler<PolarizedStoneSyncPacket, IMessage> {

		@Override
		public IMessage onMessage(PolarizedStoneSyncPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getEntityLivingBase(ctx, message.entityID) != null) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					if (entity instanceof EntityPlayer) {
						if (message.handler == 1) {
							ITrinketContainerHandler trinket = TrinketHelper.getTrinketHandler((EntityPlayer) entity);
							ItemStack stack = trinket.getStackInSlot(message.slot);
							if (!stack.isEmpty()) {
								stack.setItemDamage(message.damage);
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
								stack.setItemDamage(message.damage);
								TrinketProperties properties = Capabilities.getTrinketProperties(stack);
								if (properties != null) {
									properties.loadNBTData(message.tag);
									properties.sendInformationToTracking(entity);
								}
							}
						} else {

						}
					}
				}
			});
			return null;
		}
	}
}