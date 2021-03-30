package xzeroair.trinkets.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;

public class IncreasedAttackRangePacket implements IMessage {

	public IncreasedAttackRangePacket() {
	}

	public int attackerEntityID = 0;
	public int targetEntityID = 0;

	public IncreasedAttackRangePacket(EntityLivingBase entity, Entity targetEntity, EntityProperties cap) {
		attackerEntityID = entity.getEntityId();
		targetEntityID = targetEntity.getEntityId();
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

	public static class Handler implements IMessageHandler<IncreasedAttackRangePacket, IMessage> {

		@Override
		public IMessage onMessage(IncreasedAttackRangePacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					final Entity player = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.attackerEntityID);
					if (player instanceof EntityPlayer) {
						if (player.hasCapability(Capabilities.ENTITY_RACE, null)) {
							EntityProperties cap = Capabilities.getEntityRace((EntityLivingBase) player);
							if (cap != null) {
								final Entity targetEntity = Trinkets.proxy.getPlayer(ctx).getEntityWorld().getEntityByID(message.targetEntityID);
								AxisAlignedBB bb = targetEntity.getEntityBoundingBox().grow(1);
								List<Entity> splash = player.world.getEntitiesWithinAABBExcludingEntity(targetEntity, bb);
								((EntityPlayer) player).attackTargetEntityWithCurrentItem(targetEntity);
								for (Entity e : splash) {
									if ((e instanceof EntityLivingBase) && (e != player)) {
										((EntityPlayer) player).attackTargetEntityWithCurrentItem(e);
									}
								}
							}
						}
					}
				}
			});
			return null;
		}
	}
}
