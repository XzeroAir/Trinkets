package xzeroair.trinkets.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class IncreasedReachPacket extends ThreadSafePacket {

	public IncreasedReachPacket() {
	}

	private int targetEntityID;
	private int hand;
	private double x;
	private double y;
	private double z;

	public IncreasedReachPacket(EntityLivingBase entity, EnumHand hand, Entity targetEntity, Vec3d vec) {
		this(entity, hand, targetEntity, vec.x, vec.y, vec.z);
	}

	public IncreasedReachPacket(EntityLivingBase entity, EnumHand hand, Entity targetEntity, double x, double y, double z) {
		entityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
		this.hand = hand == EnumHand.MAIN_HAND ? 0 : 1;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(hand);
		buf.writeInt(targetEntityID);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		hand = buf.readInt();
		targetEntityID = buf.readInt();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
	}

	@Override
	public void handleClientSafe(NetHandlerPlayClient client) {

	}

	@Override
	public void handleServerSafe(NetHandlerPlayServer server) {
		final World world = server.player.getEntityWorld();
		final Entity entity = world.getEntityByID(entityID);
		final Entity interacted = world.getEntityByID(targetEntityID);
		if ((entity instanceof EntityPlayer) && (interacted != null)) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (hand == 1) {
				//				final EnumActionResult action = interacted.applyPlayerInteraction(player, new Vec3d(x, y, z), EnumHand.MAIN_HAND);
				if (!interacted.processInitialInteract(player, EnumHand.OFF_HAND)) {
				}
			} else {
				player.attackTargetEntityWithCurrentItem(interacted);
			}
		}
	}
}