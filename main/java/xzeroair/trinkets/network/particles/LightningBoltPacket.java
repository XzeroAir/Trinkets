package xzeroair.trinkets.network.particles;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;

public class LightningBoltPacket implements IMessage {

	public LightningBoltPacket() {
	}

	public int attackerEntityID = 0;
	double x1;
	double y1;
	double z1;
	double x2;
	double y2;
	double z2;
	double distance;
	float intensity;

	public LightningBoltPacket(EntityLivingBase entity, Vec3d start, Vec3d end, double distance, float intensity) {
		attackerEntityID = entity.getEntityId();
		x1 = start.x;
		y1 = start.y;
		z1 = start.z;
		x2 = end.x;
		y2 = end.y;
		z2 = end.z;
		this.distance = distance;
		this.intensity = intensity;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeDouble(x1);
		buf.writeDouble(y1);
		buf.writeDouble(z1);
		buf.writeDouble(x2);
		buf.writeDouble(y2);
		buf.writeDouble(z2);
		buf.writeDouble(distance);
		buf.writeFloat(intensity);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		x1 = buf.readDouble();
		y1 = buf.readDouble();
		z1 = buf.readDouble();
		x2 = buf.readDouble();
		y2 = buf.readDouble();
		z2 = buf.readDouble();
		distance = buf.readDouble();
		intensity = buf.readFloat();
	}

	public static class Handler implements IMessageHandler<LightningBoltPacket, IMessage> {

		@Override
		public IMessage onMessage(LightningBoltPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					EntityPlayer player = Trinkets.proxy.getPlayer(ctx);
					Vec3d start = new Vec3d(message.x1, message.y1, message.z1);
					Vec3d end = new Vec3d(message.x2, message.y2, message.z2);
					if (ctx.side.isServer()) {
						TrinketArcingOrb.SyncLightningBolt(Trinkets.proxy.getEntityLivingBase(ctx, message.attackerEntityID), start, end);
					} else {
						player.world.playSound(message.x1, message.y1, message.z1, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.2F, 0.6F, true);
						Trinkets.proxy.renderEffect(1, player.world, message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, 2515356, 0.9F, 3F);
					}
				}
			});
			return null;
		}
	}
}
