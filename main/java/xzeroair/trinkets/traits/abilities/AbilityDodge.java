package xzeroair.trinkets.traits.abilities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IMovementAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;
import xzeroair.trinkets.util.handlers.TickHandler;

public class AbilityDodge extends AbilityBase implements ITickableAbility, IMovementAbility {

	public final ConfigArcingOrb serverConfig = TrinketsConfig.SERVER.Items.ARCING_ORB;

	int keyPresses = 0;
	String keyPressed;
	int direction = -1;
	Long lastKeyPress = -1L;
	boolean trigger;

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final TickHandler counter = this.getCounter("lastKeyPress");
		if (counter != null) {
			if (counter.Tick()) {
				this.reset();
			}
		}
		//		long time = System.currentTimeMillis();
		//		if (time > (lastKeyPress + 200)) {
		//		trigger = false;
		//		if (!entity.onGround) {
		//			this.reset();
		//		}
	}

	public void handleDodge(Entity entity, int direction) {
		if (entity == null) {
			return;
		}
		final float yaw = entity.rotationYaw;

		final Vec3d look = entity.getLookVec();
		final EnumFacing.AxisDirection facing = entity.getHorizontalFacing().getAxisDirection();
		final Axis axis = entity.getHorizontalFacing().getAxis();

		double x = 0;
		double z = 0;
		if ((axis == Axis.Z)) {
			if ((facing == AxisDirection.POSITIVE)) {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			} else {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			}
		} else {
			if ((facing == AxisDirection.POSITIVE)) {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			} else {
				x = entity.getLookVec().z;
				z = -entity.getLookVec().x;
			}
		}
		final double spd = 1.25;
		double xV = 0;
		double yV = 0;
		double zV = 0;
		if (direction == 1) {
			//Left
			xV = look.z * spd;
			yV = 0.3;
			zV = -look.x * spd;
		} else if (direction == 3) {
			//Right
			xV = -look.z * spd;
			yV = 0.3;
			zV = look.x * spd;
		} else if (direction == 2) {
			//forwards
			xV = look.x * spd;
			yV = 0.3;
			zV = look.z * spd;
		} else if (direction == 0) {
			//backwards
			xV = -look.x * 2;
			yV = 0.5;
			zV = -look.z * 2;
		} else {
			xV = 0;
			yV = 0;
			zV = 0;
		}
		if (entity.onGround) {
			if (entity.world.isRemote) {
				entity.setVelocity(xV, yV, zV);
			} else {
				this.dodge(entity);
			}
		}
	}

	public void dodge(Entity entity) {
		if (!entity.getEntityWorld().isRemote) {
			final World w = entity.getEntityWorld();
			if (w instanceof WorldServer) {
				final WorldServer world = (WorldServer) w;
				//			NetworkHandler.sendToServer(
				NetworkHandler.sendToClients(
						world, entity.getPosition(),
						new EffectsRenderPacket(entity, entity.posX, entity.posY + (entity.height * 0.5F), entity.posZ, entity.posX, entity.posY, entity.posZ, 2515356, 2, 1, 1)
				);
			}
		}
		final double distance = TrinketsConfig.SERVER.Items.ARCING_ORB.stunDistance;
		final List<EntityLivingBase> stunTargets = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(distance, 1, distance));
		for (final EntityLivingBase targetEntity : stunTargets) {
			if (targetEntity != entity) {
				final StatusHandler status = Capabilities.getStatusHandler(targetEntity);
				if (status != null) {
					final TrinketStatusEffect effect = new TrinketStatusEffect(StatusEffectsEnum.paralysis, 3 * 20, 1, entity);
					status.apply(effect);
				}
			}
		}
	}

	public boolean triggerDodge(Entity entity) {
		if (trigger) {
			trigger = false;
			if (entity.world.isRemote) {
				if (!entity.onGround || entity.isSneaking()) {
					return false;
				}
			}
			if (TrinketsConfig.compat.elenaiDodge) {
				return false;
			}
			boolean trig = true;
			if (trig) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				final float cost = serverConfig.dodgeCost;
				if ((magic != null) && magic.spendMana(cost)) {
				} else {
					trig = false;
				}
			}
			return trig;
		} else {
			return false;
		}
	}

	@Override
	public boolean left(Entity entity, int state) {
		if (state == 0) {
			if (this.handleKeys(EnumDirection.Left)) {
				if (this.triggerDodge(entity)) {
					this.handleDodge(entity, 1);
				} else {
					return false;
				}
			} else {

			}
			return true;
		} else if (state == 1) {
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean right(Entity entity, int state) {
		if (state == 0) {
			if (this.handleKeys(EnumDirection.Right)) {
				if (this.triggerDodge(entity)) {
					this.handleDodge(entity, 3);
				} else {
					return false;
				}
			} else {

			}
			return true;
		} else if (state == 1) {
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean forward(Entity entity, int state) {
		if (state == 0) {
			if (this.handleKeys(EnumDirection.Forward)) {
				if (this.triggerDodge(entity)) {
					this.handleDodge(entity, 2);
				} else {
					return false;
				}
			} else {

			}
			return true;
		} else if (state == 1) {
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean back(Entity entity, int state) {
		if (state == 0) {
			if (this.handleKeys(EnumDirection.Back)) {
				if (this.triggerDodge(entity)) {
					this.handleDodge(entity, 0);
				} else {
					return false;
				}
			} else {

			}
			return true;
		} else if (state == 1) {
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean jump(Entity entity, int state) {
		return false;
	}

	@Override
	public boolean sneak(Entity entity, int state) {
		return false;
	}

	private void reset() {
		trigger = false;
		keyPresses = 0;
		lastKeyPress = -1L;
		direction = -1;
		this.removeCounter("lastKeyPress");
	}

	private boolean handleKeys(int direction) {
		if (this.direction != direction) {
			this.reset();
		}
		final TickHandler counter = this.getCounter("lastKeyPress");
		if (counter != null) {
			keyPresses++;
		} else {
			final TickHandler newCounter = this.getCounter("lastKeyPress", 3, true, true);
			this.direction = direction;
			keyPresses = 1;
		}
		if (keyPresses >= 2) {
			this.reset();
			trigger = true;
		}
		return trigger;
	}

	private boolean handleKeys(EnumDirection direction) {
		return this.handleKeys(direction.getID());
	}

	private enum EnumDirection {

		Left(1),
		Right(3),
		Forward(2),
		Back(0),
		Up(4),
		Down(5);

		private final int ID;

		private EnumDirection(int direction) {
			ID = direction;
		}

		public int getID() {
			return ID;
		}
	}

}
