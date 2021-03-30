package xzeroair.trinkets.network.arcingorb;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class ArcingOrbAttackPacket implements IMessage {

	public ArcingOrbAttackPacket() {
	}

	public int attackerEntityID = 0;
	public int chargeDuration;
	//	double x1;
	//	double y1;
	//	double z1;
	//	double x2;
	//	double y2;
	//	double z2;
	//	double distance;
	//	float intensity;

	public ArcingOrbAttackPacket(EntityLivingBase entity, int charge) {
		attackerEntityID = entity.getEntityId();
		chargeDuration = charge;
		//		x1 = start.x;
		//		y1 = start.y;
		//		z1 = start.z;
		//		x2 = end.x;
		//		y2 = end.y;
		//		z2 = end.z;
		//		this.distance = distance;
		//		this.intensity = intensity;
	}

	//	public ArcingOrbAttackPacket(EntityLivingBase entity, Vec3d start, Vec3d end, double distance, float intensity) {
	//		attackerEntityID = entity.getEntityId();
	//		x1 = start.x;
	//		y1 = start.y;
	//		z1 = start.z;
	//		x2 = end.x;
	//		y2 = end.y;
	//		z2 = end.z;
	//		this.distance = distance;
	//		this.intensity = intensity;
	//	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeInt(chargeDuration);
		//		buf.writeDouble(x1);
		//		buf.writeDouble(y1);
		//		buf.writeDouble(z1);
		//		buf.writeDouble(x2);
		//		buf.writeDouble(y2);
		//		buf.writeDouble(z2);
		//		buf.writeDouble(distance);
		//		buf.writeFloat(intensity);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		chargeDuration = buf.readInt();
		//		x1 = buf.readDouble();
		//		y1 = buf.readDouble();
		//		z1 = buf.readDouble();
		//		x2 = buf.readDouble();
		//		y2 = buf.readDouble();
		//		z2 = buf.readDouble();
		//		distance = buf.readDouble();
		//		intensity = buf.readFloat();
	}

	public static class Handler implements IMessageHandler<ArcingOrbAttackPacket, IMessage> {

		@Override
		public IMessage onMessage(ArcingOrbAttackPacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.attackerEntityID);
					if (ctx.side.isServer()) {
						if (entity instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) entity;
							EntityProperties cap = Capabilities.getEntityRace(player);
							//							float percentage = MathHelper.clamp(
							//									(((message.chargeDuration * TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost) / cap.getMagic().getMaxMana()) * 100) * 0.00005F,
							//									0F,
							//									1F
							//							);
							float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
							float dmg = TrinketsConfig.SERVER.Items.ARCING_ORB.attackDmg;
							float MP = cap.getMagic().getMana();
							float percentage = MathHelper.clamp(
									(((message.chargeDuration) / iCost)),
									0F,
									(MP / iCost)
							);
							float cost = iCost * MathHelper.clamp(percentage, 0F, 1F);
							float finalDamage = dmg * MathHelper.clamp(percentage, 0F, 1F);
							//							System.out.println("percentage :" + percentage + ", cost :" + cost + ", damage :" + finalDamage);
							if (cap.getMagic().spendMana(cost)) {
								RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, 15.0D, 1D, true);
								RayTraceHelper.rayTraceEntity(beam, target -> {
									boolean targetHit = target != null;
									Vec3d hitLoc = targetHit ? target.getPositionVector() : beam.getEnd();
									TrinketArcingOrb.SyncLightningBolt(entity, beam.getStart(), hitLoc);
									TrinketArcingOrb.SyncLightningOrb(entity, hitLoc);
									AxisAlignedBB bb1 = new AxisAlignedBB(new BlockPos(hitLoc)).grow(1);
									List<Entity> splash = entity.world.getEntitiesWithinAABB(Entity.class, bb1);
									for (Entity e : splash) {
										if ((e instanceof EntityLivingBase) && (e != entity)) {
											if ((e instanceof EntityPlayer) && !entity.getEntityWorld().getMinecraftServer().isPVPEnabled()) {
												continue;
											}
											if (((cost * 0.1F) > 3F) && (e instanceof EntityCreeper)) {
												EntityCreeper creep = (EntityCreeper) e;
												EntityLightningBolt bolt = new EntityLightningBolt(entity.getEntityWorld(), creep.posX, creep.posY, creep.posZ, true);
												creep.onStruckByLightning(bolt);
												creep.extinguish();
											}
											e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) entity), finalDamage);
										}
									}
									if (targetHit) {
										return true;
									} else {
										return false;
									}
								});
							}
						}
					} else {
						//						RayTraceHelper.drawLightning(new Vec3d(message.x1, message.y1, message.z1), new Vec3d(message.x2, message.y2, message.z2), entity.getEntityWorld(), message.distance, 1F, message.intensity);
					}
				}
			});
			return null;
		}
	}
}