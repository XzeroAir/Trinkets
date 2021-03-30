package xzeroair.trinkets.util.helpers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.particles.ParticleSmell;

public class RayTraceHelper {

	public static void rayTraceEntity(Beam beam, Function<Entity, Boolean> consumer) {
		Vec3d start = beam.getStart();
		Vec3d lookVec = beam.getLookVec();
		Vec3d end = beam.getEnd();
		double dist = beam.getDist();
		World world = beam.getWorld();
		EntityLivingBase player = beam.getEntity();
		List<Entity> targets = world.getEntitiesInAABBexcluding(
				player, player.getEntityBoundingBox().expand(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist).grow(1.0D, 1.0D, 1.0D),
				Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> (ent != null) && ent.canBeCollidedWith())
		);
		List<Pair<Entity, Double>> hitTargets = new ArrayList<>();
		for (Entity target : targets) {
			AxisAlignedBB targetBB = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());

			if (targetBB.contains(start)) {
				hitTargets.add(Pair.of(target, 0.0));
			} else {
				RayTraceResult targetResult = targetBB.calculateIntercept(start, end);

				if (targetResult != null) {
					double d3 = start.distanceTo(targetResult.hitVec);

					if (d3 < dist) {
						hitTargets.add(Pair.of(target, d3));
					}
				}
			}
		}

		if (!hitTargets.isEmpty()) {
			hitTargets.sort(Comparator.comparing(Pair::getRight));
			hitTargets.stream().filter(pair -> consumer.apply(pair.getLeft())).findFirst();
		} else {
			consumer.apply(null);
		}

	}

	public static void drawAttackSweep(World world, Vec3d pos, EnumParticleTypes particle) {
		if (world.isRemote) {
			world.spawnParticle(particle, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	//Credits for this go to Zabi
	public static void drawLine(Vec3d start, Vec3d end, World world, Beam beam, double density, EnumParticleTypes particle) {
		//		RayTraceHelper.drawLine(beam.getStart(), beam.getEnd(), player.world, beam, 1D, EnumParticleTypes.SWEEP_ATTACK);
		double dist = beam.getDist();

		for (double done = 0; done < dist; done += density) {
			double alpha = done / dist;
			double x = interpolate(start.x - 0, end.x + 0.5, alpha);
			double y = interpolate(start.y + 0, end.y + 0.5, alpha);
			double z = interpolate(start.z - 0, end.z + 0.5, alpha);

			if (world.isRemote) {
				world.spawnParticle(particle, x, y, z, 0, 0, 0);
			}
		}
	}

	public static void drawLightning(Vec3d start, Vec3d end, World world, double dist, double density, float intensity) {
		int color = 2515356;
		Trinkets.proxy.renderEffect(1, world, start.x, start.y, start.z, end.x, end.y, end.z, color, 0.9F, intensity);
		Trinkets.proxy.renderEffect(2, world, start.x, start.y, start.z, end.x, end.y, end.z, color, 0.9F, intensity);
	}

	private static List<Vec3d> points = new ArrayList<>();
	private static Vec3d first, second, third, forth;

	public static void drawPath(Vec3d start, Vec3d end, World world, Beam beam, int color, double density) {
		Random rand = new Random();
		double dist = beam.getDist();
		double r1 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
		double r2 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
		double r3 = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
		first = start;
		for (double done = 0; done < dist; done += density) {
			double alpha = done / dist;
			//			double x = interpolate(first.x, end.x + 0.5, alpha);
			//			double y = interpolate(first.y, end.y + 0.5, alpha);
			//			double z = interpolate(first.z, end.z + 0.5, alpha);
			//			System.out.println(start.x + (x - (x * 0.9)));
			double x = interpolate(end.x + 0.5, start.x, alpha);
			double y = interpolate(end.y + 0.5, start.y, alpha);
			double z = interpolate(end.z + 0.5, start.z, alpha);

			int widthRange = rand.nextInt(6) * (rand.nextBoolean() ? -1 : 1);
			int widthRange2 = rand.nextInt(3) * (rand.nextBoolean() ? -1 : 1);
			int widthRange3 = rand.nextInt(6) * (rand.nextBoolean() ? -1 : 1);
			double t1 = widthRange * 0.1f;
			double t2 = widthRange2 * 0.1f;
			double t3 = widthRange3 * 0.1f;
			//			second = new Vec3d(x + (t1 * (dist - done)), y + (t2 * (dist - done)), z + (t3 * (dist - done)));
			second = new Vec3d(x + r1, y + r2, z + r3);
			//			second = new Vec3d(x - ((first.x - x) * t1), y - ((first.y - y) * t2), z - ((first.z - z) * t3));
			if (world.isRemote) {
				GlStateManager.pushMatrix();
				final ParticleSmell effect = new ParticleSmell(world, first, second, beam, color, (float) alpha, true);
				Minecraft.getMinecraft().effectRenderer.addEffect(effect);
				GlStateManager.popMatrix();
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
		private World world;
		//		private EntityPlayer player;
		private EntityLivingBase player;
		private double maxDist;
		private Vec3d start;
		private Vec3d lookVec;
		private Vec3d end;
		private double dist;
		private double accuracy;
		private boolean canBreak;
		private Random rand = new Random();

		public Beam(World world, EntityLivingBase player, double maxDist, double accuracy, boolean canBreak) {
			this.world = world;
			this.player = player;
			this.maxDist = maxDist;
			accuracy = accuracy / 2;
			this.canBreak = canBreak;

			this.calculate();
		}

		private void calculate() {
			float prevYaw = player.rotationYaw;
			float prevPitch = player.rotationPitch;

			start = player.getPositionEyes(1.0f);
			player.rotationYaw += ((2 * accuracy) * rand.nextDouble()) - accuracy;
			player.rotationYaw = player.rotationYaw % 360;
			player.rotationPitch += ((2 * accuracy) * rand.nextDouble()) - accuracy;
			lookVec = player.getLookVec();
			player.rotationYaw = prevYaw;
			player.rotationPitch = prevPitch;
			end = start.add(lookVec.x * maxDist, lookVec.y * maxDist, lookVec.z * maxDist);
			RayTraceResult result = world.rayTraceBlocks(start, end);
			dist = maxDist;

			if ((result != null) && (result.typeOfHit == Type.BLOCK)) {
				BlockPos pos = result.getBlockPos();

				if (world.getBlockState(pos).getCollisionBoundingBox(world, pos) != Block.NULL_AABB) {
					if (canBreak && !world.isRemote) {
						//						if (player.isAllowEdit()) {
						//							if ((world.getBlockState(pos).getMaterial() == Material.GLASS) || (world.getBlockState(pos).getMaterial() == Material.ICE)) {
						//								world.destroyBlock(pos, false);
						//							}
						//						}
					}

					dist = result.hitVec.distanceTo(start);
					end = start.add(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist);
				}
			}
		}

		public Vec3d getStart() {
			return start;
		}

		public Vec3d getLookVec() {
			return lookVec;
		}

		public Vec3d getEnd() {
			return end;
		}

		public double getDist() {
			return dist;
		}

		public World getWorld() {
			return world;
		}

		public EntityLivingBase getEntity() {
			return player;
		}

		public double getMaxDist() {
			return maxDist;
		}
	}
}
