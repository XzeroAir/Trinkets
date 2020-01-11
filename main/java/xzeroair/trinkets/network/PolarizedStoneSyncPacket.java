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

public class PolarizedStoneSyncPacket implements IMessage {
	// A default constructor is always required
	public PolarizedStoneSyncPacket(){}

	int type;
	int hits;
	int exp;
	boolean on;
	boolean altOn;
	boolean bauble;
	int entityID;
	int damage;
	ItemStack item;

	public PolarizedStoneSyncPacket(EntityLivingBase player, ItemStack stack, IAccessoryInterface cap, boolean isBauble, int damage) {
		type = cap.oreTarget();
		on = cap.ability();
		altOn = cap.altAbility();
		hits = cap.hitCount();
		exp = cap.storedExp();
		bauble = isBauble;
		item = stack;
		entityID = player.getEntityId();
		this.damage = damage;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(type);
		buf.writeInt(hits);
		buf.writeInt(exp);
		buf.writeBoolean(on);
		buf.writeBoolean(altOn);
		buf.writeBoolean(bauble);
		buf.writeInt(entityID);
		buf.writeInt(damage);
		ByteBufUtils.writeItemStack(buf, item);
	}

	@Override public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		type = buf.readInt();
		hits = buf.readInt();
		exp = buf.readInt();
		on = buf.readBoolean();
		altOn = buf.readBoolean();
		bauble = buf.readBoolean();
		entityID = buf.readInt();
		damage = buf.readInt();
		item = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler implements IMessageHandler<PolarizedStoneSyncPacket, IMessage> {

		@Override public IMessage onMessage(PolarizedStoneSyncPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getEntityLivingBase(ctx, message.entityID) != null) {
					final EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					final ItemStack stack = TrinketHelper.getAccessory((EntityPlayer) entity, message.item.getItem());
					final IAccessoryInterface iCap = stack.getCapability(TrinketProvider.itemCapability, null);
					if(iCap != null) {
						iCap.setOreTarget(message.type);
						iCap.setAbility(message.on);
						iCap.setAltAbility(message.altOn);
						iCap.setHitCount(message.hits);
						iCap.setStoredExp(message.exp);
					}

					stack.setItemDamage(message.damage);

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