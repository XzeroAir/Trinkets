package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.entity.MovingThrownProjectile;

public class UpdateThrowable implements IMessage {
	// A default constructor is always required
	public UpdateThrowable() {
	}

	private int entityID;
	private NBTTagCompound tag;

	public UpdateThrowable(MovingThrownProjectile movingThrownProjectile, NBTTagCompound tag) {
		entityID = movingThrownProjectile.getEntityId();
		this.tag = tag;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	public static class Handler implements IMessageHandler<UpdateThrowable, IMessage> {

		@Override
		public IMessage onMessage(UpdateThrowable message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					Entity entity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.entityID);
					if (entity instanceof MovingThrownProjectile) {
						((MovingThrownProjectile) entity).readFromNBT(message.tag);
						NBTTagCompound tag = message.tag;
						if (ctx.side.isServer()) {
							((MovingThrownProjectile) entity).spawnParticle();
						} else {
							EntityPlayer player = Trinkets.proxy.getPlayer(ctx);
							Trinkets.proxy.spawnParticle(3, player.world, tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"), 0, 0, 0, tag.getInteger("BreathColor"), tag.getInteger("frame"));
						}
					}
				}
			});
			return null;
		}
	}

}