package xzeroair.trinkets.util;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.particles.ParticleGreed;
import xzeroair.trinkets.particles.ParticleHeartBeat;

@Mod.EventBusSubscriber
public class ItemEffectHandler {
	
	
	
	public static void reverseMaterialAcceleration(World world, AxisAlignedBB aabb, Material material, Entity entity) {
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
						if (material == MaterialLiquid.WATER) {
							double liquidLevel = (double)((float)(l1 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer) state.getValue(BlockLiquid.LEVEL)).intValue()));
							if ((double)l >= liquidLevel) {
								vec3 = block.modifyAcceleration(world, pos, entity, vec3);
							}
						}
					}
				}
			}
			if (vec3.lengthVector() > 0.0D && entity.isPushedByWater()) {
				vec3 = vec3.normalize();
				double d1 = 0.014D;
				entity.motionX -= vec3.x * d1;
				entity.motionY -= vec3.y * d1;
				entity.motionZ -= vec3.z * d1;
				entity.motionX *= 0.85D;
				entity.motionY *= 0.85D;
				entity.motionZ *= 0.85D;
			}
	}


	public static void blockdetect(World world, AxisAlignedBB aabb, Entity entity) {
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
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						if (block == block.getBlockById(56)) {
							EntityPlayer player = (EntityPlayer) entity;
							double v1 = player.getPositionEyes(0.0F).x;
							double v2 = player.getPositionEyes(0.0F).y;
							double v3 = player.getPositionEyes(0.0F).z;

							double bv1 = player.getPosition().getX() - pos.getX();
							double bv2 = player.getPosition().getY() - pos.getY();
							double bv3 = player.getPosition().getZ() - pos.getZ();
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
						//	world.spawnParticle(EnumParticleTypes.HEART, testv1+testv4+plv1, testv2+testv5+plv2, testv3+testv6+plv3, 0, 0, 0, new int[0]);
						//	Main.proxy.spawnParticle(EnumParticleTypes.HEART, testv1+testv4, testv2+testv5+plv2, testv3+testv6, 0, 0, 0, 0);
						//	Main.proxy.spawnParticle(EnumParticleTypes.HEART, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 0);
							ParticleGreed effect = new ParticleGreed(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0, 0);
							Minecraft.getMinecraft().effectRenderer.addEffect(effect);
						}
					}
				}
			}
	}
	public static void enemyDetect(World world, AxisAlignedBB aabb, Entity entity) {
		
						EntityPlayer isPlayer = (EntityPlayer) entity;
						AxisAlignedBB bBox = isPlayer.getEntityBoundingBox().grow(12, 12, 12);
						List<EntityLiving> entLivList = entity.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, bBox);
						for(EntityLiving stuff : entLivList) {
							ParticleHeartBeat effect = new ParticleHeartBeat(world, stuff.posX, stuff.posY+1.3, stuff.posZ, 0, 0, 0);
							Minecraft.getMinecraft().effectRenderer.addEffect(effect);
						}
	}
	public static void pull(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

		double vel = 1.0 - dist / 15.0;
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX += dX / dist * vel * 0.1;
			ent.motionY += dY / dist * vel * 0.1;
			ent.motionZ += dZ / dist * vel * 0.1;
		}
	}
	public static void push(Entity ent, double x, double y, double z)
	{
		double dX = x - ent.posX;
		double dY = y - ent.posY;
		double dZ = z - ent.posZ;
		double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

		double vel = 1.0 - dist / 15.0;
		if (vel > 0.0D)
		{
			vel *= vel;
			ent.motionX -= dX / dist * vel * 0.1;
			ent.motionY -= dY / dist * vel * 0.1;
			ent.motionZ -= dZ / dist * vel * 0.1;
		}
	}
	
}
