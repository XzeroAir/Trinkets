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
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.StringUtils;

public class AbilityLightningBolt extends Ability implements ITickableAbility, IKeyBindInterface {

	protected Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith());

	public AbilityLightningBolt() {
		super(Abilities.lightningBolt);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		LycanitesCompat.removeParalysis(entity);
	}

	protected boolean prepareBolt(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
		final float cfgCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
		final int length = Math.min((int) (300 * Math.min(MathHelper.pct(magic.getMana(), 0, cfgCost), 1F)), 300);
		final Counter counter = tickHandler.getCounter("heldCounter", length, false, true, false, true, false);
		counter.setLength(length);
		return def;
	}

	protected boolean chargeBolt(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
		final float cfgCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
		final float maxMP = magic.getMaxMana();
		final float mp = magic.getMana();
		final Counter counter = tickHandler.getCounter("heldCounter");
		final int tick = counter.getTick();
		final float multi = (float) MathHelper.pct(tick, 0, counter.getLength());
		final float realCost = (float) (cfgCost * (MathHelper.pct(tick, 0, 300)));//(float) StringUtils.getAccurateDouble((Math.min(cfgCost, mp)) * multi);
		if (!counter.Tick()) {
			if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
				entity.world.playSound(
						(EntityPlayer) entity,
						entity.getPosition(),
						SoundEvents.BLOCK_NOTE_BASS,
						SoundCategory.PLAYERS,
						0.5F,
						(float) StringUtils.getAccurateDouble((Math.min(0.6F + (0.4F * multi), 1F)))
				);
				ScreenOverlayEvents.instance.SyncCost(realCost);
			}
			if (realCost == mp) {
				return false;
			}
		} else {
			if ((entity instanceof EntityPlayer) && entity.world.isRemote) {
				entity.world.playSound(
						(EntityPlayer) entity,
						entity.getPosition(),
						SoundEvents.BLOCK_END_GATEWAY_SPAWN,
						SoundCategory.PLAYERS,
						0.5F,
						0.5F
				);
				ScreenOverlayEvents.instance.SyncCost(realCost);
			}
			return false;
		}
		return def;
	}

	protected boolean castBolt(EntityLivingBase entity, MagicStats magic, boolean def, boolean aux) {
		final float cfgCost = TrinketsConfig.SERVER.Items.ARCING_ORB.attackCost;
		final float cfgDmg = TrinketsConfig.SERVER.Items.ARCING_ORB.attackDmg;
		final float maxMP = magic.getMaxMana();
		final float mp = magic.getMana();
		final Counter counter = tickHandler.getCounter("heldCounter");
		final int tick = counter.getTick();
		final float multi = (float) MathHelper.pct(tick, 0, 300);
		final float realCost = (float) StringUtils.getAccurateDouble(cfgCost * multi);
		if (tick > (counter.getLength() * 0.1)) {
			final float finalDamage = cfgDmg * multi;
			if (magic.spendMana(realCost)) {
				final double maxDist = 15D;
				final Vec3d start = entity.getPositionEyes(1F);
				final Vec3d lookVec = entity.getLookVec();
				final Vec3d end = start.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
				final RayTraceResult result = RayTraceHelper.rayTrace(entity, maxDist);
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

					final List<Entity> splash = entity.world.getEntitiesInAABBexcluding(entity, bb1, Targets);
					for (final Entity e : splash) {
						if ((e instanceof EntityPlayer) && !pvpEnabled) {

						} else {
							if ((multi > 0.33F) && (e instanceof EntityCreeper)) {
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
			}
		}
		this.reset(entity);
		return def;
	}

	protected void reset(Entity entity) {
		final Counter counter = tickHandler.getCounter("heldCounter");
		if (counter != null) {
			counter.resetTick();
			counter.setLength(300);
		}
		if (entity.world.isRemote) {
			ScreenOverlayEvents.instance.SyncCost(0);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		return ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName();
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {

		return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.prepareBolt((EntityLivingBase) entity, magic, ret, Aux));
	}

	@Override
	public boolean onKeyDown(Entity entity, boolean Aux) {
		return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.chargeBolt((EntityLivingBase) entity, magic, ret, Aux));
	}

	@Override
	public boolean onKeyRelease(Entity entity, boolean Aux) {
		return Capabilities.getMagicStats(entity, true, (magic, ret) -> this.castBolt((EntityLivingBase) entity, magic, ret, Aux));
	}
}
