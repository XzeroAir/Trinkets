package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.handlers.Counter;

public class AbilityFlying extends Ability implements ITickableAbility, IPotionAbility, IMiningAbility {

	protected boolean flightEnabled = false;
	protected boolean speedModified = false;
	protected float speed = 0F;
	protected float cost = 0F;
	//	protected float oldSpeed = 0.5F;
	//	protected boolean canFly = true;

	public AbilityFlying() {
		super(Abilities.creativeFlight);
	}

	//	public AbilityFlying setCanFly(boolean canFly) {
	//		if(this.canfly != canFly) {
	//			this.canFly = canFly;
	//		}
	//		return this;
	//	}

	public AbilityFlying setFlightEnabled(boolean enabled) {
		flightEnabled = enabled;
		return this;
	}

	public AbilityFlying setSpeedEnabled(boolean enabled) {
		speedModified = enabled;
		return this;
	}

	public AbilityFlying setFlightSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public AbilityFlying setFlightCost(float cost) {
		this.cost = cost;
		return this;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		// TODO Fix this so it's not reliant on the capability
		final boolean flag = Capabilities.getEntityProperties(entity, true, (prop, canFly) -> prop.getRaceHandler().canFly());
		if (flightEnabled && flag) {
			this.addFlyingAbility(entity);
		} else {
			this.removeFlyingAbility(entity);
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		if (effect.getPotion().getRegistryName().toString().contentEquals("minecraft:levitation")) {
			return true;
		}
		return cancel;
	}

	@Override
	public float breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		if (!entity.isInsideOfMaterial(Material.WATER)) {
			float speed = originalSpeed;
			if (!entity.onGround) {
				speed *= 5F;
			}
			if (newSpeed < speed) {
				return speed;
			}
		}
		return newSpeed;
	}

	protected void setFlyingSpeed(EntityLivingBase entity, float flightSpeed) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				if (speedModified && player.world.isRemote) {
					if (player.capabilities.getFlySpeed() != flightSpeed) {
						if ((flightSpeed > 0) && (flightSpeed != Float.NaN)) {
							player.capabilities.setFlySpeed(flightSpeed);
							player.sendPlayerAbilities();
						}
					}
				}
			} else {
				if (player.world.isRemote && (player.capabilities.getFlySpeed() == flightSpeed)) {
					player.capabilities.setFlySpeed(0.05F);
					player.sendPlayerAbilities();
				}
			}
		}
	}

	protected void addFlyingAbility(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if ((cost > 0) && (magic != null)) {
					final boolean flying = player.capabilities.isFlying;
					final float mp = magic.getMana();
					if (mp >= cost) {
						if ((player.capabilities.allowFlying != true)) {
							player.capabilities.allowFlying = true;
							this.setFlyingSpeed(entity, speed);
							if (player instanceof EntityPlayerMP) {
								player.sendPlayerAbilities();
							}
						}
					} else {
						if (flying) {
							this.removeFlyingAbility(player);
						}
					}
					if (player.capabilities.isFlying) {
						this.setFlyingSpeed(entity, speed);
						player.fallDistance = 0F;
						final Counter counter = tickHandler.getCounter("fly_timer", 20, true, true, true, true);
						if ((counter != null) && !player.isRiding()) {
							if (counter.Tick()) {
								if (magic.spendMana(cost)) {
									//									magic.setManaRegenTimeout(TrinketsConfig.SERVER.mana.mana_regen_timeout * 3);
								} else {
									this.removeFlyingAbility(player);
								}
							}
						}
					}
				} else {
					if ((player.capabilities.allowFlying != true)) {
						player.capabilities.allowFlying = true;
						if (player instanceof EntityPlayerMP) {
							player.sendPlayerAbilities();
						}
					}
					if (player.capabilities.isFlying) {
						player.fallDistance = 0F;
					}
				}
			}
		}

	}

	protected void removeFlyingAbility(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				if ((player.capabilities.allowFlying == true)) {
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
					if (player instanceof EntityPlayerMP) {
						player.sendPlayerAbilities();
					}
					if (player.world.isRemote) {
						if (player.capabilities.getFlySpeed() != 0.05F) {
							player.capabilities.setFlySpeed(0.05f);
							player.sendPlayerAbilities();
						}
					}
					player.fallDistance = 0F;
				}
			}
		}
	}

	@Override
	public void onAbilityAdded(EntityLivingBase entity) {
		this.addFlyingAbility(entity);
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		this.removeFlyingAbility(entity);
		tickHandler.removeCounter("fly_timer");
	}
}
