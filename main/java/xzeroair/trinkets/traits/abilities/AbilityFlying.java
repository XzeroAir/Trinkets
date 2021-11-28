package xzeroair.trinkets.traits.abilities;

import org.apache.commons.lang3.tuple.ImmutablePair;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IMiningAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.handlers.TickHandler;

public class AbilityFlying extends AbilityBase implements ITickableAbility, IPotionAbility, IMiningAbility {

	protected boolean flightEnabled = false;
	protected boolean speedModified = false;
	protected float speed = 0F;
	protected float cost = 0F;

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
		final EntityProperties prop = Capabilities.getEntityRace(entity);
		final boolean flag = (prop != null) && prop.showTraits();
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
	public ImmutablePair<Boolean, Float> breakingBlock(EntityLivingBase entity, IBlockState state, BlockPos pos, float originalSpeed, float newSpeed) {
		if (!entity.isInsideOfMaterial(Material.WATER)) {
			float speed = originalSpeed;
			if (!entity.onGround) {
				speed *= 5F;
			}
			if (newSpeed < speed) {
				return new ImmutablePair<>(false, speed);
			}
		}
		return null;
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
						final TickHandler counter = this.getCounter("fly_timer", true);
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
	public void addAbility(EntityLivingBase entity) {
		this.addFlyingAbility(entity);
	}

	@Override
	public void removeAbility(EntityLivingBase entity) {
		this.removeFlyingAbility(entity);
		this.removeCounter("fly_timer");
	}
}
