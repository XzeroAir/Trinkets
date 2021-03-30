package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;

public class IncreasedReachPacket implements IMessage {

	public IncreasedReachPacket() {
	}

	public int attackerEntityID = 0;
	public int targetEntityID = 0;
	public double x = 0;
	public double y = 0;
	public double z = 0;

	public IncreasedReachPacket(EntityLivingBase entity, Entity targetEntity, Vec3d vec) {
		attackerEntityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public IncreasedReachPacket(EntityLivingBase entity, Entity targetEntity, double x, double y, double z) {
		attackerEntityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeInt(targetEntityID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		targetEntityID = buf.readInt();
	}

	public static class Handler implements IMessageHandler<IncreasedReachPacket, IMessage> {

		@Override
		public IMessage onMessage(IncreasedReachPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					final Entity player = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.attackerEntityID);
					Entity interacted = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.targetEntityID);
					if ((player != null) && (player instanceof EntityPlayer)) {
						EntityPlayer p = (EntityPlayer) player;
						if (interacted != null) {
							interacted.processInitialInteract((EntityPlayer) player, p.getActiveHand());
						} else {

						}
					}
				}
			});
			return null;
		}
	}
}