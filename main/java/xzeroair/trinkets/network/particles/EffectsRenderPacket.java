package xzeroair.trinkets.network.particles;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.ThreadSafePacket;

public class EffectsRenderPacket extends ThreadSafePacket {

	public EffectsRenderPacket() {
	}

	double x;
	double y;
	double z;
	double x2;
	double y2;
	double z2;
	private int effectID;
	private int color;
	private float alpha;
	private float intensity;

	public EffectsRenderPacket(Entity entity, double x, double y, double z, double x2, double y2, double z2, int color, int effectID, float alpha, float intensity) {
		entityID = entity.getEntityId();
		this.effectID = effectID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.color = color;
		this.alpha = alpha;
		this.intensity = intensity;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(effectID);
		buf.writeInt(color);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(x2);
		buf.writeDouble(y2);
		buf.writeDouble(z2);
		buf.writeFloat(alpha);
		buf.writeFloat(intensity);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		effectID = buf.readInt();
		color = buf.readInt();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		x2 = buf.readDouble();
		y2 = buf.readDouble();
		z2 = buf.readDouble();
		alpha = buf.readFloat();
		intensity = buf.readFloat();
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {
		final EntityPlayerSP clientPlayer = Minecraft.getMinecraft().player;
		World world = clientPlayer.getEntityWorld();
		try {
			final Entity entity = clientPlayer.getEntityWorld().getEntityByID(entityID);
			if (entity != null) {
				world = entity.getEntityWorld();
			}
			Trinkets.proxy.renderEffect(effectID, world, x, y, z, x2, y2, z2, color, alpha, intensity);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final EntityPlayerMP serverPlayer = server.player;
		WorldServer world = serverPlayer.getServerWorld();
		try {
			Entity entity = serverPlayer.getEntityWorld().getEntityByID(entityID);
			if (entity != null) {
				if (entity instanceof EntityPlayerMP) {
					world = ((EntityPlayerMP) entity).getServerWorld();
				}
			} else {
				entity = serverPlayer;
			}
			NetworkHandler.sendToClients(
					world, new BlockPos(x, y, z), new EffectsRenderPacket(entity, x, y, z, x2, y2, z2, color, effectID, alpha, intensity)
			);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
