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

public class LightningOrbPacket implements IMessage {

	public LightningOrbPacket() {
	}

	public int attackerEntityID = 0;
	double x1;
	double y1;
	double z1;

	public LightningOrbPacket(EntityLivingBase entity, Vec3d start) {
		attackerEntityID = entity.getEntityId();
		x1 = start.x;
		y1 = start.y;
		z1 = start.z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeDouble(x1);
		buf.writeDouble(y1);
		buf.writeDouble(z1);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		x1 = buf.readDouble();
		y1 = buf.readDouble();
		z1 = buf.readDouble();
	}

	public static class Handler implements IMessageHandler<LightningOrbPacket, IMessage> {

		@Override
		public IMessage onMessage(LightningOrbPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					EntityPlayer player = Trinkets.proxy.getPlayer(ctx);
					Vec3d loc = new Vec3d(message.x1, message.y1, message.z1);
					if (ctx.side.isServer()) {
						TrinketArcingOrb.SyncLightningOrb(Trinkets.proxy.getEntityLivingBase(ctx, message.attackerEntityID), loc);
					} else {
						player.world.playSound(loc.x, loc.y, loc.z, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 0.2F, 0.6F, true);
						Trinkets.proxy.renderEffect(2, player.world, loc.x, loc.y, loc.z, loc.x, loc.y, loc.z, 2515356, 1, 1);
					}
				}
			});
			return null;
		}
	}
}
