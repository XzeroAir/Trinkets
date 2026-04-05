package xzeroair.trinkets.util.helpers;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import xzeroair.trinkets.Trinkets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class RayTraceHelper {

    public static RayTraceResult rayTrace(EntityLivingBase entity, boolean useLiquids) {
        double distance = 5.0D;
        if (entity instanceof EntityPlayer) {
            distance = entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        }
        return rayTrace(entity.world, entity, distance, useLiquids, !useLiquids);
    }

    public static RayTraceResult rayTrace(World world, EntityLivingBase entity, boolean useLiquids) {
        double distance = 5.0D;
        if (entity instanceof EntityPlayer) {
            distance = entity.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        }
        return rayTrace(world, entity, distance, useLiquids, !useLiquids);
    }

    public static RayTraceResult rayTrace(EntityLivingBase entity, double distance) {
        return rayTrace(entity.world, entity, distance, false, true);
    }

    public static RayTraceResult rayTrace(EntityLivingBase entity, double distance, boolean useLiquids) {
        return rayTrace(entity.world, entity, distance, useLiquids, !useLiquids);
    }

    public static RayTraceResult rayTrace(World world, EntityLivingBase entity, double distance, boolean useLiquids) {
        return rayTrace(world, entity, distance, useLiquids, !useLiquids);
    }

    public static RayTraceResult rayTrace(World world, @Nonnull EntityLivingBase entity, double distance, boolean useLiquids, boolean ignoreBlockWithoutBoundingBox) {
        final Vec3d start = entity.getPositionEyes(1F);
        final Vec3d lookVec = entity.getLookVec();
        final Vec3d end = start.add(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance);
        return getRaytraceResult(world, entity, start, end, distance, useLiquids, ignoreBlockWithoutBoundingBox, null);
    }

    @Nullable
    public static RayTraceResult getRaytraceResult(@Nonnull World world, @Nonnull EntityLivingBase entity, Vec3d start, Vec3d end, double distance, boolean useLiquids, boolean ignoreBlockWithoutBoundingBox, Predicate<Entity> excluding) {
        RayTraceResult result = world.rayTraceBlocks(start, end, useLiquids, ignoreBlockWithoutBoundingBox, false);
        //		RayTraceResult result = entity.world.rayTraceBlocks(start, end);
        final Vec3d lookVec = entity.getLookVec();
        if ((result != null) && (result.typeOfHit == Type.BLOCK)) {
            final BlockPos pos = result.getBlockPos();
            if (world.getBlockState(pos).getCollisionBoundingBox(world, pos) != Block.NULL_AABB) {
                distance = result.hitVec.distanceTo(start);
                end = start.add(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance);
            }
        }
        final Predicate<Entity> predicate = Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith());
        final Entity hitEntity = findEntityOnPath(entity, start, end, distance, excluding == null ? predicate : excluding);
        if (hitEntity != null) {
            result = new RayTraceResult(hitEntity);
        }
        return result;
    }

    private static final Predicate<Entity> Targets = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, ent -> (ent != null) && ent.canBeCollidedWith());

    @Nullable
    public static Entity findEntityOnPath(@Nonnull EntityLivingBase entity, Vec3d start, Vec3d end, double distance, Predicate<Entity> excluding) {
        final Vec3d lookVec = entity.getLookVec();
        final List<Entity> targets = entity.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance).grow(1.0D, 1.0D, 1.0D), excluding
                //				Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith())
        );
        final List<Pair<Entity, Double>> hitTargets = new ArrayList<>();
        for (final Entity target : targets) {
            final AxisAlignedBB targetBB = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());

            if (targetBB.contains(start)) {
                hitTargets.add(Pair.of(target, 0.0));
            } else {
                final RayTraceResult targetResult = targetBB.calculateIntercept(start, end);
                if (targetResult != null) {
                    final double d3 = start.distanceTo(targetResult.hitVec);
                    if (d3 < distance) {
                        hitTargets.add(Pair.of(target, d3));
                    }
                }
            }
        }

        if (!hitTargets.isEmpty()) {
            hitTargets.sort(Comparator.comparing(Pair::getRight));
            Optional<Pair<Entity, Double>> first = hitTargets.stream().findFirst();
            if (first.isPresent()) {
                return hitTargets.stream().findFirst().get().getLeft();
            }
            //			hitTargets.stream().filter(pair -> consumer.apply(pair.getLeft())).findFirst();
        }
        return null;
        //			consumer.apply(null);
    }

    // Used for Projectiles
    @Nullable
    public static Entity findEntityOnPath(@Nonnull EntityLivingBase entity, Vec3d start, Vec3d end) {
        Entity hitEntity = null;
        final List<Entity> list = entity.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(entity.motionX, entity.motionY, entity.motionZ).grow(1.0D), Targets);
        double d0 = 0.0D;

        for (final Entity entity1 : list) {
            if ((entity1 != entity)) {
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    final double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if ((d1 < d0) || (d0 == 0.0D)) {
                        hitEntity = entity1;
                        d0 = d1;
                    }
                }
            }
        }
        return hitEntity;
    }

    public static void rayTraceEntity(@Nonnull Beam beam, Function<Entity, Boolean> consumer) {
        final Vec3d start = beam.getStart();
        final Vec3d lookVec = beam.getLookVec();
        final Vec3d end = beam.getEnd();
        final double dist = beam.getDist();
        final World world = beam.getWorld();
        final EntityLivingBase player = beam.getEntity();
        final List<Entity> targets = world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().expand(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith()));
        final List<Pair<Entity, Double>> hitTargets = new ArrayList<>();
        for (final Entity target : targets) {
            final AxisAlignedBB targetBB = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());

            if (targetBB.contains(start)) {
                hitTargets.add(Pair.of(target, 0.0));
            } else {
                final RayTraceResult targetResult = targetBB.calculateIntercept(start, end);

                if (targetResult != null) {
                    final double d3 = start.distanceTo(targetResult.hitVec);

                    if (d3 < dist) {
                        hitTargets.add(Pair.of(target, d3));
                    }
                }
            }
        }

        if (!hitTargets.isEmpty()) {
            hitTargets.sort(Comparator.comparing(Pair::getRight));
            hitTargets.forEach(pair -> consumer.apply(pair.getLeft()));
        } else {
            consumer.apply(null);
        }

    }

    //	public static void drawAttackSweep(World world, Vec3d pos, EnumParticleTypes particle) {
    //		if (world.isRemote) {
    //			world.spawnParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
    //		}
    //	}
    //
    //	//Credits for this go to Zabi
    //	public static void drawLine(Vec3d start, Vec3d end, World world, Beam beam, double density, EnumParticleTypes particle) {
    //		//		RayTraceHelper.drawLine(beam.getStart(), beam.getEnd(), player.world, beam, 1D, EnumParticleTypes.SWEEP_ATTACK);
    //		final double dist = beam.getDist();
    //
    //		for (double done = 0; done < dist; done += density) {
    //			final double alpha = done / dist;
    //			final double x = interpolate(start.x - 0, end.x + 0.5, alpha);
    //			final double y = interpolate(start.y + 0, end.y + 0.5, alpha);
    //			final double z = interpolate(start.z - 0, end.z + 0.5, alpha);
    //
    //			if (world.isRemote) {
    //				world.spawnParticle(particle, x, y, z, 0, 0, 0);
    //			}
    //		}
    //	}

    public static void drawLightning(@Nonnull Vec3d start, @Nonnull Vec3d end, World world, double dist, double density, float intensity) {
        final int color = 2515356;
        Trinkets.proxy.renderEffect(1, world, start.x, start.y, start.z, end.x, end.y, end.z, color, 0.9F, intensity);
        Trinkets.proxy.renderEffect(2, world, start.x, start.y, start.z, end.x, end.y, end.z, color, 0.9F, intensity);
    }

    private static final List<Vec3d> points = new ArrayList<>();
    private static Vec3d first, second, third, forth;

    public static void drawPath(Vec3d start, Vec3d end, World world, @Nonnull Beam beam, int color, double density) {
        final Random rand = new Random();
        final double dist = beam.getDist();
        final double r1 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
        final double r2 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
        final double r3 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
        first = start;
        for (double done = 0; done < dist; done += density) {
            final double alpha = done / dist;
            //			double x = interpolate(first.x, end.x + 0.5, alpha);
            //			double y = interpolate(first.y, end.y + 0.5, alpha);
            //			double z = interpolate(first.z, end.z + 0.5, alpha);
            //			System.out.println(start.x + (x - (x * 0.9)));
            final double x = interpolate(end.x + 0.5, start.x, alpha);
            final double y = interpolate(end.y + 0.5, start.y, alpha);
            final double z = interpolate(end.z + 0.5, start.z, alpha);

            final int widthRange = rand.nextInt(6) * (rand.nextBoolean() ? -1 : 1);
            final int widthRange2 = rand.nextInt(3) * (rand.nextBoolean() ? -1 : 1);
            final int widthRange3 = rand.nextInt(6) * (rand.nextBoolean() ? -1 : 1);
            final double t1 = widthRange * 0.1f;
            final double t2 = widthRange2 * 0.1f;
            final double t3 = widthRange3 * 0.1f;
            //			second = new Vec3d(x + (t1 * (dist - done)), y + (t2 * (dist - done)), z + (t3 * (dist - done)));
            second = new Vec3d(x + r1, y + r2, z + r3);
            //			second = new Vec3d(x - ((first.x - x) * t1), y - ((first.y - y) * t2), z - ((first.z - z) * t3));
            if (world.isRemote) {
                //				if (Trinkets.proxy.getSide() == Side.CLIENT) {
                //					GlStateManager.pushMatrix();
                //					final ParticleSmell effect = new ParticleSmell(world, first, second, beam, color, (float) alpha, true);
                //					Minecraft.getMinecraft().effectRenderer.addEffect(effect);
                //					GlStateManager.popMatrix();
                //				}
            }
            first = second;
            //			if ((done > 0) && (done < (dist - 1))) {
            //				points.add(new Vec3d(x + t1, y + t2, z + t3));
            //			} else {
            //			points.add(new Vec3d(x, y, z));
            //			}

            //			if ((done > 0) && (done < (dist - 1))) {
            //				points.add(new Vec3d(x + t1, y + t2, z + t3));
            //			} else {
            //				points.add(new Vec3d(x, y, z));
            //			}
        }
        //		for (int p = 0; p < (points.size() - 1); p++) {
        //			if (world.isRemote) {
        //				GlStateManager.pushMatrix();
        //				Vec3d origin = start;
        //				if (p > 0) {
        //					origin = points.get(p - 1);
        //				}
        //				ParticleSmell effect = new ParticleSmell(world, origin, points.get(p), beam, color, 1F, true);
        //				//				final ParticleGreed effect = new ParticleGreed(world, new Vec3d(x, y, z), color, (float) alpha, true);
        //				Minecraft.getMinecraft().effectRenderer.addEffect(effect);
        //				GlStateManager.popMatrix();
        //			}
        //		}
    }

    public static double interpolate(double start, double end, double alpha) {
        return start + ((end - start) * alpha);
    }

    public static double interpolateReverse(double start, double end, double alpha) {
        //		return start + ((end - start) * alpha);
        return end + ((end - start) * alpha);
    }

    public static class Beam {
        private final World world;
        private final EntityLivingBase player;
        private final double maxDist;
        private Vec3d start;
        private Vec3d lookVec;
        private Vec3d end;
        private double dist;
        private final double accuracy;
        private final boolean canBreak;
        private final Random rand = new Random();

        public Beam(World world, EntityLivingBase player, double maxDist, double accuracy, boolean canBreak) {
            this.world = world;
            this.player = player;
            this.maxDist = maxDist;
            this.accuracy = accuracy / 2;
            this.canBreak = canBreak;
            this.calculate();
        }

        private void calculate() {
            final float prevYaw = this.player.rotationYaw;
            final float prevPitch = this.player.rotationPitch;

            this.start = this.player.getPositionEyes(1.0f);
            this.player.rotationYaw += (float) (((2 * this.accuracy) * this.rand.nextDouble()) - this.accuracy);
            this.player.rotationYaw = this.player.rotationYaw % 360;
            this.player.rotationPitch += (float) (((2 * this.accuracy) * this.rand.nextDouble()) - this.accuracy);
            this.lookVec = this.player.getLookVec();
            this.player.rotationYaw = prevYaw;
            this.player.rotationPitch = prevPitch;
            this.end = this.start.add(this.lookVec.x * this.maxDist, this.lookVec.y * this.maxDist, this.lookVec.z * this.maxDist);
            final RayTraceResult result = this.world.rayTraceBlocks(this.start, this.end);
            this.dist = this.maxDist;

            if ((result != null) && (result.typeOfHit == Type.BLOCK)) {
                final BlockPos pos = result.getBlockPos();

                if (this.world.getBlockState(pos).getCollisionBoundingBox(this.world, pos) != Block.NULL_AABB) {
                    //					if (canBreak && !world.isRemote) {
                    //						if (player.isAllowEdit()) {
                    //							if ((world.getBlockState(pos).getMaterial() == Material.GLASS) || (world.getBlockState(pos).getMaterial() == Material.ICE)) {
                    //								world.destroyBlock(pos, false);
                    //							}
                    //						}
                    //					}
                    this.dist = result.hitVec.distanceTo(this.start);
                    this.end = this.start.add(this.lookVec.x * this.dist, this.lookVec.y * this.dist, this.lookVec.z * this.dist);
                }
            }
        }

        public Vec3d getStart() {
            return this.start;
        }

        public Vec3d getLookVec() {
            return this.lookVec;
        }

        public Vec3d getEnd() {
            return this.end;
        }

        public double getDist() {
            return this.dist;
        }

        public World getWorld() {
            return this.world;
        }

        public EntityLivingBase getEntity() {
            return this.player;
        }

        public double getMaxDist() {
            return this.maxDist;
        }
    }
}
