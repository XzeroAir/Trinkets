package xzeroair.trinkets.traits.abilities;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.events.ScreenOverlayEvents;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.particles.EffectsRenderPacket;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class AbilityLightningBolt extends Ability implements ITickableAbility, IKeyBindInterface {

	public AbilityLightningBolt() {
		super(Abilities.lightningBolt);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		LycanitesCompat.removeParalysis(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		return ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName();
	}

	boolean cast, check = false;
	private int heldDuration, charge = 0;

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		final float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
		final MagicStats magic = Capabilities.getMagicStats(entity);
		this.reset(entity);
		cast = true;
		if (magic != null) {
			final float maxMP = magic.getMaxMana();
			final float MP = magic.getMana();
			final float limit = iCost * 0.1F;
			if ((iCost >= (limit)) && (MP < limit)) {
				this.reset(entity);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(Entity entity, boolean Aux) {
		final float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
		final MagicStats magic = Capabilities.getMagicStats(entity);
		final int bonusTime = 50;
		final int maxHoldTime = (int) iCost + bonusTime;
		if (heldDuration <= maxHoldTime) {
			cast = true;
			heldDuration++;
		} else
			//			this.reset(entity);
			return false;
		if (magic != null) {
			final float maxMP = magic.getMaxMana();
			final float MP = magic.getMana();
			final float limit = iCost * 0.1F;
			if (MP < limit) {
				this.reset(entity);
				return false;
			}
			if (heldDuration <= 300) {
				charge++;
			}
			final float dur = ((charge * 1F) / ((maxHoldTime - bonusTime) * 1F));
			final float percentage = MathHelper.clamp(dur, 0, MP / iCost);
			final float realCost = iCost * percentage;
			if (heldDuration <= (maxHoldTime - bonusTime)) {
				if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
					entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.1F, 1f - dur);
					ScreenOverlayEvents.instance.SyncCost(realCost);
				}
			} else {
				if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
					ScreenOverlayEvents.instance.SyncCost(iCost);
					entity.world.playSound((EntityPlayer) entity, entity.getPosition(), SoundEvents.BLOCK_NOTE_XYLOPHONE, SoundCategory.PLAYERS, 0.1F, 1f - dur);
				}
			}
		}
		return true;

	}

	@Override
	public boolean onKeyRelease(Entity entity, boolean Aux) {
		if (cast) {
			final MagicStats magic = Capabilities.getMagicStats(entity);
			if (magic != null) {
				final float iCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
				final float dmg = TrinketsConfig.SERVER.Items.ARCING_ORB.attackDmg;
				final float MP = magic.getMana();
				final int bonusTime = 50;
				final int maxHoldTime = (int) iCost + bonusTime;
				final float dur = ((charge * 1F) / ((maxHoldTime - bonusTime) * 1F));
				final float percentage = MathHelper.clamp(dur, 0, MP / iCost);
				final float realCost = iCost * percentage;
				final float finalDamage = dmg * MathHelper.clamp(percentage, 0F, 1F);
				if (magic.spendMana(realCost)) {
					final double maxDist = 15D;
					final Vec3d start = entity.getPositionEyes(1F);
					final Vec3d lookVec = entity.getLookVec();
					final Vec3d end = start.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
					final RayTraceResult result = RayTraceHelper.rayTrace((EntityLivingBase) entity, maxDist);
					//						this.getRaytraceResult(entity, start, end);
					Vec3d hitLoc = end;
					if (result != null) {
						if (result.typeOfHit == Type.ENTITY) {
							final Entity hit = result.entityHit;
							hitLoc = result.hitVec.add(0, hit.height * 0.5F, 0);
						} else if (result.typeOfHit == Type.BLOCK) {
							hitLoc = result.hitVec;
						} else {

						}
					}
					//					if (entity.getEntityWorld().isRemote) {
					if (Trinkets.proxy.getSide() == Side.CLIENT) {
						NetworkHandler.sendToServer(
								new EffectsRenderPacket(entity, start.x, start.y, start.z, hitLoc.x, hitLoc.y, hitLoc.z, 2515356, 1, 0.9F, 3F)
						);
						NetworkHandler.sendToServer(
								new EffectsRenderPacket(entity, hitLoc.x, hitLoc.y, hitLoc.z, hitLoc.x, hitLoc.y, hitLoc.z, 2515356, 2, 0.9F, 3F)
						);
					}
					if (!entity.getEntityWorld().isRemote) {
						boolean pvpEnabled = false;
						try {
							if (entity instanceof EntityPlayerMP) {
								pvpEnabled = ((EntityPlayerMP) entity).getServer().isPVPEnabled();
							}
						} catch (final Exception e) {
							e.printStackTrace();
						}
						final AxisAlignedBB bb1 = new AxisAlignedBB(new BlockPos(hitLoc)).grow(1);
						final Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith());

						final List<Entity> splash = entity.world.getEntitiesInAABBexcluding(entity, bb1, Targets);
						for (final Entity e : splash) {
							if ((e instanceof EntityPlayer) && !pvpEnabled) {

							} else {
								if ((dur > 0.33F) && (e instanceof EntityCreeper)) {
									final EntityCreeper creep = (EntityCreeper) e;
									final EntityLightningBolt bolt = new EntityLightningBolt(entity.getEntityWorld(), creep.posX, creep.posY, creep.posZ, true);
									creep.onStruckByLightning(bolt);
									creep.extinguish();
								}
								e.attackEntityFrom(new EntityDamageSource("lightningBolt", entity), finalDamage);
							}
						}
					}
					this.reset(entity);
					return true;
				} else {
					this.reset(entity);
					return false;
				}
			}
			this.reset(entity);
			return true;
		} else {
			this.reset(entity);
			return false;
		}
	}

	private void reset(Entity entity) {
		heldDuration = 0;
		charge = 0;
		cast = false;
		if (entity.world.isRemote) {
			ScreenOverlayEvents.instance.SyncCost(0);
		}
	}
}
