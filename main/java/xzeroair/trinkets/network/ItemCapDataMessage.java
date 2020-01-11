package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.TrinketCap.TrinketProvider;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class ItemCapDataMessage implements IMessage {
	// A default constructor is always required
	public ItemCapDataMessage(){}

	int type;
	int slot;
	int hits;
	int exp;
	boolean on;
	boolean altOn;
	boolean bauble;
	int entityID;
	ItemStack item;

	public ItemCapDataMessage(EntityLivingBase player, ItemStack stack, IAccessoryInterface cap, boolean isBauble) {
		slot = cap.wornSlot();
		type = cap.oreTarget();
		on = cap.ability();
		altOn = cap.altAbility();
		hits = cap.hitCount();
		exp = cap.storedExp();
		bauble = isBauble;
		item = stack;
		entityID = player.getEntityId();
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(slot);
		buf.writeInt(type);
		buf.writeInt(hits);
		buf.writeInt(exp);
		buf.writeBoolean(on);
		buf.writeBoolean(altOn);
		buf.writeBoolean(bauble);
		buf.writeInt(entityID);
		ByteBufUtils.writeItemStack(buf, item);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		slot = buf.readInt();
		type = buf.readInt();
		hits = buf.readInt();
		exp = buf.readInt();
		on = buf.readBoolean();
		altOn = buf.readBoolean();
		bauble = buf.readBoolean();
		entityID = buf.readInt();
		item = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler implements IMessageHandler<ItemCapDataMessage, IMessage> {

		@Override public IMessage onMessage(ItemCapDataMessage message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getEntityLivingBase(ctx, message.entityID) != null) {
					final EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					final IAccessoryInterface iCap = TrinketHelper.getAccessory((EntityPlayer) entity, message.item.getItem()).getCapability(TrinketProvider.itemCapability, null);
					if(iCap != null) {
						iCap.setOreTarget(message.type);
						iCap.setAbility(message.on);
						iCap.setAltAbility(message.altOn);
						iCap.setWornSlot(message.slot);
						iCap.setHitCount(message.hits);
						iCap.setStoredExp(message.exp);
					}

					//					if(ctx.side == Side.CLIENT) {
					//						System.out.println("Packet Recieved");
					//						Trinkets.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Recieved A Packet!"));
					//					} else {
					//						System.out.println("Packet Sent");
					//						Trinkets.proxy.getPlayer(ctx).sendMessage(new TextComponentString("You Sent A Packet!"));
					//					}
				}
			});
			return null;
		}
	}
}