package xzeroair.trinkets.util.compat.firstaid;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.baubles.BaubleDwarfRing;
import xzeroair.trinkets.items.baubles.BaubleFairyRing;

public class FirstAidSyncHPPacket implements IMessage {
	// A default constructor is always required
	public FirstAidSyncHPPacket(){}

	public boolean reset = false;
	ItemStack stack;
	public int entityID = 0;

	public FirstAidSyncHPPacket(EntityLivingBase entity, boolean reset, ItemStack stack) {

		this.stack = stack;
		entityID = entity.getEntityId();
		this.reset = reset;
	}

	@Override public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeBoolean(reset);
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeInt(entityID);
	}

	@Override public void fromBytes(ByteBuf buf) {
		reset = buf.readBoolean();
		ByteBufUtils.readItemStack(buf);
		entityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<FirstAidSyncHPPacket, IMessage> {

		@Override public IMessage onMessage(FirstAidSyncHPPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if(Trinkets.proxy.getPlayer(ctx) != null) {
					final EntityPlayer SyncPlayer = (EntityPlayer) Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if(message.reset == false) {
						if((message.stack.getItem() instanceof BaubleFairyRing) || (message.stack.getItem() instanceof BaubleDwarfRing)) {
							FirstAidCompat.resetHP(SyncPlayer);
						}
					} else {
						FirstAidCompat.resetHP(SyncPlayer);
					}

				}
			});
			return null;
		}
	}

}