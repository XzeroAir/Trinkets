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
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;

public class ItemCapDataMessage implements IMessage {
	// A default constructor is always required
	public ItemCapDataMessage() {
	}

	int type;
	int slot;
	int hits;
	int exp;
	int mana;
	boolean on;
	boolean altOn;
	boolean bauble;
	int entityID;
	ItemStack item;

	public ItemCapDataMessage(EntityLivingBase player, ItemStack stack, TrinketProperties cap, boolean isBauble) {
		this.slot = cap.Slot();
		this.type = cap.Target();
		this.on = cap.mainAbility();
		this.altOn = cap.altAbility();
		this.hits = cap.Count();
		this.exp = cap.StoredExp();
		this.mana = cap.StoredMana();
		this.bauble = isBauble;
		this.item = stack;
		this.entityID = player.getEntityId();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(this.slot);
		buf.writeInt(this.type);
		buf.writeInt(this.hits);
		buf.writeInt(this.exp);
		buf.writeInt(this.mana);
		buf.writeBoolean(this.on);
		buf.writeBoolean(this.altOn);
		buf.writeBoolean(this.bauble);
		buf.writeInt(this.entityID);
		ByteBufUtils.writeItemStack(buf, this.item);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		// Reads the int back from the buf. Note that if you have multiple values, you
		// must read in the same order you wrote.
		this.slot = buf.readInt();
		this.type = buf.readInt();
		this.hits = buf.readInt();
		this.exp = buf.readInt();
		this.mana = buf.readInt();
		this.on = buf.readBoolean();
		this.altOn = buf.readBoolean();
		this.bauble = buf.readBoolean();
		this.entityID = buf.readInt();
		this.item = ByteBufUtils.readItemStack(buf);
	}

	public static class Handler implements IMessageHandler<ItemCapDataMessage, IMessage> {

		@Override
		public IMessage onMessage(ItemCapDataMessage message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if (Trinkets.proxy.getEntityLivingBase(ctx, message.entityID) != null) {
					final EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.entityID);
					ItemStack stack = TrinketHelper.getAccessory((EntityPlayer) entity, message.item.getItem());
					final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
					if (iCap != null) {
						iCap.setTarget(message.type);
						iCap.toggleMainAbility(message.on);
						iCap.toggleAltAbility(message.altOn);
						iCap.setSlot(message.slot);
						iCap.setCount(message.hits);
						iCap.setStoredExp(message.exp);
						iCap.setStoredMana(message.mana);
					}
				}
			});
			return null;
		}
	}
}