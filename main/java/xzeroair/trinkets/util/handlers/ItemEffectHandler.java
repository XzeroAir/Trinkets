package xzeroair.trinkets.util.handlers;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xzeroair.trinkets.util.Reference;

public class ItemEffectHandler {

	public static void increasedWaterSpeed(World world, AxisAlignedBB aabb, Material material, Entity entity) {
		int i = MathHelper.floor(aabb.minX);
		int j = MathHelper.floor(aabb.maxX + 1.0D);
		int k = MathHelper.floor(aabb.minY);
		int l = MathHelper.floor(aabb.maxY + 1.0D);
		int i1 = MathHelper.floor(aabb.minZ);
		int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		Vec3d vec3 = new Vec3d(0.0D, 0.0D, 0.0D);
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					BlockPos pos = new BlockPos(k1, l1, i2);
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					if (state.getMaterial() == material) {
						//	System.out.println("Uhm Your in Water!?");
						double liquidLevel = (l1 + 1) - BlockLiquid.getLiquidHeightPercent(state.getValue(BlockLiquid.LEVEL).intValue());
						if (l >= liquidLevel) {
							vec3 = block.modifyAcceleration(world, pos, entity, vec3);
						}
					}
				}
			}
		}
		if ((vec3.lengthVector() >= 0.0D) && (entity.isInWater() && entity.isPushedByWater())) {
			vec3 = vec3.normalize();
			//	((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			double d1 = 0.01D;
			entity.motionX -= vec3.x * d1;
			entity.motionY -= vec3.y * d1;
			entity.motionZ -= vec3.z * d1;
			entity.motionX *= 1.1D;
			entity.motionY *= 1.05D;
			entity.motionZ *= 1.1D;
		}
	}

	public static void increasedLavaSpeed(World world, AxisAlignedBB aabb, Material material, Entity entity) {
		int i = MathHelper.floor(aabb.minX);
		int j = MathHelper.floor(aabb.maxX + 1.0D);
		int k = MathHelper.floor(aabb.minY);
		int l = MathHelper.floor(aabb.maxY + 1.0D);
		int i1 = MathHelper.floor(aabb.minZ);
		int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		Vec3d vec3 = new Vec3d(0.0D, 0.0D, 0.0D);
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					BlockPos pos = new BlockPos(k1, l1, i2);
					IBlockState state = world.getBlockState(pos);
					Block block = state.getBlock();
					if (state.getMaterial() == material) {
						//	System.out.println("Uhm Your in Lava!?");
						double liquidLevel = (l1 + 1) - BlockLiquid.getLiquidHeightPercent(state.getValue(BlockLiquid.LEVEL).intValue());
						if (l >= liquidLevel) {
							vec3 = block.modifyAcceleration(world, pos, entity, vec3);
						}
					}
				}
			}
		}
		if ((vec3.lengthVector() >= 0.0D) && (entity.isInLava())) {
			//	System.out.println("Yep your in Lava!");
			vec3 = vec3.normalize();
			double d1 = 0.01D;
			//	entity.motionX -= vec3.x * d1;
			//	entity.motionY -= vec3.y * d1;
			//	entity.motionZ -= vec3.z * d1;
			entity.motionX *= 1.3D;
			entity.motionY *= 1.3D;
			entity.motionZ *= 1.3D;
		}
	}

	public static void blockdetect(Entity entity, AxisAlignedBB aabb, int blockID) {
		int i = MathHelper.floor(aabb.minX);
		int j = MathHelper.floor(aabb.maxX + 1.0D);
		int k = MathHelper.floor(aabb.minY);
		int l = MathHelper.floor(aabb.maxY + 1.0D);
		int i1 = MathHelper.floor(aabb.minZ);
		int j1 = MathHelper.floor(aabb.maxZ + 1.0D);;
		Random rand = new Random();
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					BlockPos pos = new BlockPos(k1, l1, i2);
					IBlockState state = entity.world.getBlockState(pos);
					Block block = state.getBlock();
					if (block == block.getBlockById(blockID)) {
						double v1 = entity.getPositionEyes(0.0F).x;
						double v2 = entity.getPositionEyes(0.0F).y;
						double v3 = entity.getPositionEyes(0.0F).z;

						double bv1 = entity.getPosition().getX() - pos.getX();
						double bv2 = entity.getPosition().getY() - pos.getY();
						double bv3 = entity.getPosition().getZ() - pos.getZ();
						double plv1 = rand.nextDouble() * -bv1;
						double testv1 = Math.min(v1, +bv1);
						double testv4 = Math.max(v1, -bv1);
						double plv2 = rand.nextDouble() * -bv2;
						double testv2 = Math.min(v2, -bv2);
						double testv5 = Math.max(v2, +bv2);
						double plv3 = rand.nextDouble() * -bv3;
						double testv3 = Math.min(v3, -bv3);
						double testv6 = Math.max(v3, +bv3);
						//	world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.VOICE, 0.1f, 0.6f, false);
						//	world.spawnParticle(EnumParticleTypes.HEART, v1, v2+1, v3, pos.getX(), pos.getY(), pos.getZ(), new int[0]);
						//	Main.proxy.spawnParticle(EnumParticleTypes.HEART, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 0);
						//	ParticleGreed effect = new ParticleGreed(entity.world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0, 0, 0.0f, 1.0f, 0.0f);
						//	Minecraft.getMinecraft().effectRenderer.addEffect(effect);
					}
				}
			}
		}
	}

	public static void mobDetection(EntityPlayer player, AxisAlignedBB aabb, String entity) {
		List<Entity> entLootList = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, aabb);
		if(!entLootList.isEmpty()) {
			for(Entity e : entLootList) {
				if(e.getName().matches(entity)) {
					double X = (Reference.random.nextDouble() + e.posX) - 0.5;
					double Y = Reference.random.nextDouble() + e.posY;
					double Z = (Reference.random.nextDouble() + e.posZ) - 0.5;
					if(((e.ticksExisted % 40) == 0)) {
						//Do Stuff
					}
				}
			}
		}
	}


	public static void pull(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX += (dX / dist) * vel * 0.1;
			ent.motionY += (dY / dist) * vel * 0.1;
			ent.motionZ += (dZ / dist) * vel * 0.1;
		}
	}

	public static void push(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));

		double vel = 1.0 - (dist / 15.0);
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX -= (dX / dist) * vel * 0.1;
			ent.motionY -= (dY / dist) * vel * 0.1;
			ent.motionZ -= (dZ / dist) * vel * 0.1;
		}
	}
}
