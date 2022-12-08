package xzeroair.trinkets.traits.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

	public AbilityFlying() {
		super(Abilities.creativeFlight);
	}

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
			if (speedModified) {
				if ((speed != 0) && (speed != Float.NaN)) {
					this.setFlyingSpeed(entity, speed);
				}
			}
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
		if ((entity != null) && (entity instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				if (player.world.isRemote && (flightSpeed > 0F)) {
					player.capabilities.setFlySpeed(flightSpeed);
				}
			} else {
				if (player.capabilities.getFlySpeed() == flightSpeed) {
					player.capabilities.setFlySpeed(0.05F);
				}
			}
		}
	}

	protected void addFlyingAbility(EntityLivingBase entity) {
		if ((entity != null) && (entity instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				final MagicStats magic = Capabilities.getMagicStats(entity);
				if ((cost > 0) && (magic != null)) {
					final boolean flying = player.capabilities.isFlying;
					final float mp = magic.getMana();
					if (mp >= cost) {
						if ((player.capabilities.allowFlying != true)) {
							player.capabilities.allowFlying = true;
						}
					} else {
						if (flying) {
							this.removeFlyingAbility(player);
						}
					}
					if (player.capabilities.isFlying) {
						final Counter counter = tickHandler.getCounter("fly_timer", 20, true, true, true, true);
						if (counter != null) {
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
					}
				}
			}
		}
	}

	protected void removeFlyingAbility(EntityLivingBase entity) {
		if ((entity != null) && (entity instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) entity;
			if (!this.isCreativePlayer(player)) {
				if ((player.capabilities.allowFlying == true)) {
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					if (player.world.isRemote) {
						if (player.capabilities.getFlySpeed() != 0.05F) {
							player.capabilities.setFlySpeed(0.05f);
						}
					}
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
