package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
		if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
			return;
		}

		//		boolean moving = ((entity.getHorizontalFacing().getDirectionVec().getX() * entity.motionX) > 0) || ((entity.getHorizontalFacing().getDirectionVec().getZ() * entity.motionZ) > 0);
		final AxisAlignedBB aabb = entity.getEntityBoundingBox();
		float f = entity.width;
		boolean onGround = entity.onGround;
		//		final BlockPos pos = entity.getPosition();
		float width = entity.width;
		float height = entity.height;

		if (entity.isSneaking()) {
			width = TLWidth;
			height = TLHeight * 0.9F;
		} else if (entity.isElytraFlying()) {
			width = TLWidth;
			height = TLHeight * 0.33F;
		} else if (entity.isPlayerSleeping()) {
			width = 0.2F;
			height = 0.2F;
		} else if (entity.isRiding()) {
			width = TLWidth;
			height = TLHeight;
			//		} else if (moving && (entity.isInWater() || entity.isInLava())) {
			//			width = TLWidth;
			//			height = TLHeight * 0.33F;
		} else {
			width = TLWidth;
			height = TLHeight;
		}
		float Wclamp = 0.252F;
		float Hclamp = 0.45F;
		if (entity instanceof EntityPlayer) {
			Wclamp = 0.3f;
			Hclamp = 0.45f;
		} else {
			Wclamp = 0.3f;
			Hclamp = 0.45f;
		}
		width = MathHelper.clamp(width, Wclamp, 5.4F);
		height = MathHelper.clamp(height, Hclamp, 5.4F); // for some reason 5.000000099999F any higher then this and it breaks
		entity.width = width;
		entity.height = height;

		//		try {
		//			TrinketReflectionHelper.ENTITY_SETSIZE.invoke(entity, width, height);
		//		} catch (Exception e) {
		//		}
		double d0 = entity.width / 2.0D;
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
		//		if ((entity.width > f) && !entity.world.isRemote) {
		//			entity.move(MoverType.SELF, f - entity.width, 0.0D, f - entity.width);
		//		}
		//		}

	}

	public static void setSize(EntityLivingBase entity, EntityProperties properties) {
		if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
			return;
		}
		if (properties.getSize() < 100) {
			if (entity.isChild()) {
				return;
			}
		}

		final float TLHeight = (float) (properties.getDefaultHeight() * (properties.getSize() * 0.01));
		final float TLWidth = (float) (properties.getDefaultWidth() * (properties.getSize() * 0.01));

		//		float tw = 0.6F + (0 * (TLWidth - 0.6F));
		//		float th = 1.8F + (0 * (TLHeight - 1.8F));
		setSize(entity, TLHeight, TLWidth);
		//		test(entity, true, th, tw);
		//		test(entity, true, TLHeight, TLWidth);
	}

	public static void test(EntityLivingBase player, boolean setSize, float TLHeight, float TLWidth) {
		EntityProperties prop = Capabilities.getEntityRace(player);
		boolean isTransforming = (prop != null) && prop.isTransforming();
		if (!isTransforming) {
			double morphWidth = TLWidth;
			//			if ((Math.abs((player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX) - morphWidth) > 0.000001D) || (Math.abs((player.getEntityBoundingBox().maxY - player.getEntityBoundingBox().minY) - morphEnt.height) > 0.000001D)) {
			if (setSize) {
				player.width = TLWidth;//morphEnt.width;
				player.height = TLHeight;//morphEnt.height;
			}
			//	                double difference = ((player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX)) - morphWidth;
			//	                if(difference > 0)
			//	                {
			//	                    boolean collidedHorizontally = player.collidedHorizontally;
			//	                    boolean collidedVertically = player.collidedVertically;
			//	                    boolean onGround = player.onGround;
			//	                    boolean collided = player.collided;
			//	                    float distanceWalkedModified = player.distanceWalkedModified;
			//	                    float distanceWalkedOnStepModified = player.distanceWalkedOnStepModified;
			//	                    player.move(MoverType.SELF, difference, 0.0D, difference);
			//	                    player.distanceWalkedModified = distanceWalkedModified;
			//	                    player.distanceWalkedOnStepModified = distanceWalkedOnStepModified;
			//	                    player.collidedHorizontally = collidedHorizontally || player.collidedHorizontally;
			//	                    player.collidedVertically = collidedVertically || player.collidedVertically;
			//	                    player.onGround = onGround || player.onGround;
			//	                    player.collided = collided || player.collided;
			//	                }
			setSizeTest(player, (float) morphWidth, TLHeight);
			//			}
		} else {
			//			EntityLivingBase prevEnt = info.prevState.getEntInstance(player.getEntityWorld());
			//			EntityLivingBase nextEnt = info.nextState.getEntInstance(player.getEntityWorld());

			//			float morphTransition = info.getMorphTransitionProgress(0F);
			float width = player.width;
			float height = player.height;

			if (player.isSneaking()) {
				width = TLWidth;
				height = TLHeight * 0.9F;
			} else if (player.isElytraFlying()) {
				width = TLWidth;
				height = TLHeight * 0.33F;
			} else if (player.isPlayerSleeping()) {
				width = 0.2F;
				height = 0.2F;
			} else if (player.isRiding()) {
				width = TLWidth;
				height = TLHeight;
				//		} else if (moving && (entity.isInWater() || entity.isInLava())) {
				//			width = TLWidth;
				//			height = TLHeight * 0.33F;
			} else {
				width = TLWidth;
				height = TLHeight;
			}
			float Wclamp = 0.252F;
			float Hclamp = 0.45F;
			if (player instanceof EntityPlayer) {
				Wclamp = 0.3f;
				Hclamp = 0.45f;
			} else {
				Wclamp = 0.3f;
				Hclamp = 0.45f;
			}
			width = MathHelper.clamp(width, Wclamp, 5.4F);
			height = MathHelper.clamp(height, Hclamp, 5.4F);
			float newWidth = width;//EntityHelper.interpolateValues(prevEnt.width, nextEnt.width, morphTransition);
			float newHeight = height;//EntityHelper.interpolateValues(prevEnt.height, nextEnt.height, morphTransition);

			if ((Math.abs((player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX) - newWidth) > 0.000001D) || (Math.abs((player.getEntityBoundingBox().maxY - player.getEntityBoundingBox().minY) - newHeight) > 0.000001D)) {
				if (setSize) {
					player.width = newWidth;
					player.height = newHeight;
				}
				double difference = ((player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX)) - newWidth;
				if (difference > 0) {
					boolean collidedHorizontally = player.collidedHorizontally;
					boolean collidedVertically = player.collidedVertically;
					boolean onGround = player.onGround;
					boolean collided = player.collided;
					float distanceWalkedModified = player.distanceWalkedModified;
					float distanceWalkedOnStepModified = player.distanceWalkedOnStepModified;
					player.move(MoverType.SELF, difference, 0.0D, difference);
					player.distanceWalkedModified = distanceWalkedModified;
					player.distanceWalkedOnStepModified = distanceWalkedOnStepModified;
					player.collidedHorizontally = collidedHorizontally || player.collidedHorizontally;
					player.collidedVertically = collidedVertically || player.collidedVertically;
					player.onGround = onGround || player.onGround;
					player.collided = collided || player.collided;
				}
				setSizeTest(player, newWidth, newHeight);
			}
		}
	}

	public static void setSizeTest(EntityLivingBase player, float width, float height) {
		float f = (float) (player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX);

		if ((Math.abs(f - width) < 0.00001D) && (Math.abs((player.getEntityBoundingBox().maxY - player.getEntityBoundingBox().minY) - height) < 0.00001D)) {
			return;
		}

		double d0 = width / 2.0D;
		player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, player.posY, player.posZ - d0, player.posX + d0, player.posY + height, player.posZ + d0));

		if (((float) (player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX)) < f) {
			return;
		}

		boolean collidedHorizontally = player.collidedHorizontally;
		boolean collidedVertically = player.collidedVertically;
		boolean onGround = player.onGround;
		boolean collided = player.collided;

		float difference = ((float) (player.getEntityBoundingBox().maxX - player.getEntityBoundingBox().minX)) - f;
		float distanceWalkedModified = player.distanceWalkedModified;
		float distanceWalkedOnStepModified = player.distanceWalkedOnStepModified;
		EntityProperties prop = Capabilities.getEntityRace(player);
		boolean isTransforming = (prop != null) && prop.isTransforming();
		if (!player.world.isRemote || isTransforming) {
			player.move(MoverType.SELF, difference, 0.0D, difference);
			player.move(MoverType.SELF, -(difference + difference), 0.0D, -(difference + difference));
			player.move(MoverType.SELF, difference, 0.0D, difference);
		}
		player.distanceWalkedModified = distanceWalkedModified;
		player.distanceWalkedOnStepModified = distanceWalkedOnStepModified;

		player.collidedHorizontally = collidedHorizontally || player.collidedHorizontally;
		player.collidedVertically = collidedVertically || player.collidedVertically;
		player.onGround = onGround || player.onGround;
		player.collided = collided || player.collided;
	}
}
