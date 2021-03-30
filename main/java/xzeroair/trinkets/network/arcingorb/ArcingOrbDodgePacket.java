package xzeroair.trinkets.network.arcingorb;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketStatusEffect;
import xzeroair.trinkets.util.TrinketsConfig;

public class ArcingOrbDodgePacket implements IMessage {

	public ArcingOrbDodgePacket() {
	}

	public int attackerEntityID = 0;
	public int direction;

	public ArcingOrbDodgePacket(EntityLivingBase entity, int direction) {
		attackerEntityID = entity.getEntityId();
		this.direction = direction;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// Writes the int into the buf
		buf.writeInt(attackerEntityID);
		buf.writeInt(direction);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		direction = buf.readInt();
	}

	public static class Handler implements IMessageHandler<ArcingOrbDodgePacket, IMessage> {

		@Override
		public IMessage onMessage(ArcingOrbDodgePacket message, MessageContext ctx) {

			Trinkets.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				if ((Trinkets.proxy.getPlayer(ctx) != null) && (Trinkets.proxy.getPlayer(ctx).getEntityWorld() != null)) {
					EntityLivingBase entity = Trinkets.proxy.getEntityLivingBase(ctx, message.attackerEntityID);
					if (ctx.side.isServer()) {
						if (TrinketsConfig.SERVER.Items.ARCING_ORB.dodgeStuns) {
							if (entity instanceof EntityPlayer) {
								EntityPlayer player = (EntityPlayer) entity;
								EntityProperties cap = Capabilities.getEntityRace(player);
								float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.dodgeCost;
								float MP = cap.getMagic().getMana();
								float cost = iCost;// * MathHelper.clamp(percentage, 0F, 1F);
								//							System.out.println("percentage :" + percentage + ", cost :" + cost + ", damage :" + finalDamage);
								if (cap.getMagic().spendMana(cost)) {
									TrinketArcingOrb.SyncLightningOrb(player, player.getPositionVector().add(0, player.height * 0.5F, 0));
									List<EntityLivingBase> stunTargets = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(2, 1, 2));
									for (EntityLivingBase targetEntity : stunTargets) {
										if (targetEntity != player) {
											EntityProperties tProp = Capabilities.getEntityRace(targetEntity);
											TrinketStatusEffect effect = new TrinketStatusEffect(StatusEffectsEnum.paralysis, 3 * 20, 1, entity);
											if (tProp != null) {
												tProp.getStatusHandler().apply(effect);
											}
											//											NetworkHandler.INSTANCE.sendTo(new StatusEffectPacket(player, targetEntity, effect), (EntityPlayerMP) player);
										}
									}
								}
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
