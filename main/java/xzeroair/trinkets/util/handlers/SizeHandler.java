package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
		if (Trinkets.ArtemisLib && TrinketsConfig.compat.artemislib) {
			return;
		}

		//		boolean moving = ((entity.getHorizontalFacing().getDirectionVec().getX() * entity.motionX) > 0) || ((entity.getHorizontalFacing().getDirectionVec().getZ() * entity.motionZ) > 0);
		//		final AxisAlignedBB aabb = entity.getEntityBoundingBox();
		//		final float f = entity.width;
		//		final BlockPos pos = entity.getPosition();
		float width = entity.width;
		float height = entity.height;

		if (entity.isSneaking()) {
			width = TLWidth;
			height = TLHeight * 0.92F;
		} else if (entity.isElytraFlying()) {
			width = TLWidth;
			height = TLHeight * 0.2F;
		} else if (entity.isPlayerSleeping()) {
			width = 0.2F;
			height = 0.2F;
		} else if (entity.isRiding()) {
			// TODO Try to Fix this Somehow?
			//			final Entity mount = entity.getRidingEntity();
			//			final float mountHeight = mount == null ? 0 : mount.height;
			//			final double mountOffset = mount == null ? 0 : mount.getMountedYOffset();
			//			final double t = mountHeight - mountOffset;
			//			System.out.println(mountHeight + " | " + mountOffset + " | " + t);
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
		final double d0 = entity.width / 2.0D;
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));

		// TODO Entities Still Slide in some cases

	}

	public static void setSize(EntityLivingBase entity, EntityProperties properties) {
		if (Trinkets.ArtemisLib && TrinketsConfig.compat.artemislib) {
			return;
		}
		//		if (properties.getSize() < 100) {
		if (entity.isChild()) {
			return;
		}
		//		}

		setSize(entity, properties.getRaceHandler().getHeight(), properties.getRaceHandler().getWidth());
	}

	//	public static void updateSize(EntityLivingBase entity) {
	//		Capabilities.getEntityRace(entity, (properties) -> {
	//
	//			float width;
	//			float height;
	//
	//			final float TLHeight = (float) (properties.getDefaultHeight() * (properties.getRaceHandler().getHeightValue() * 0.01));
	//			final float TLWidth = (float) (properties.getDefaultWidth() * (properties.getRaceHandler().getWidthValue() * 0.01));
	//
	//			if (entity.isSneaking()) {
	//				width = TLWidth;
	//				height = TLHeight * 0.92F;
	//			} else if (entity.isElytraFlying()) {
	//				width = TLWidth;
	//				height = TLHeight * 0.2F;
	//			} else if (entity.isPlayerSleeping()) {
	//				width = 0.2F;
	//				height = 0.2F;
	//			} else if (entity.isRiding()) {
	//				//				width = properties.getDefaultWidth();//TLWidth;
	//				width = TLWidth;
	//				//				height = properties.getDefaultHeight();//TLHeight;
	//				height = TLHeight;
	//				//		} else if (moving && (entity.isInWater() || entity.isInLava())) {
	//				//			width = TLWidth;
	//				//			height = TLHeight * 0.33F;
	//			} else {
	//				width = TLWidth;
	//				height = TLHeight;
	//			}
	//			float Wclamp = 0.252F;
	//			float Hclamp = 0.45F;
	//			if (entity instanceof EntityPlayer) {
	//				Wclamp = 0.3f;
	//				Hclamp = 0.45f;
	//			} else {
	//				Wclamp = 0.3f;
	//				Hclamp = 0.45f;
	//			}
	//			width = MathHelper.clamp(width, Wclamp, 5.4F);
	//			height = MathHelper.clamp(height, Hclamp, 5.4F);
	//			//		if (entity.isElytraFlying()) {
	//			//			width = 0.6F;
	//			//			height = 0.6F;
	//			//		} else if (entity.isPlayerSleeping()) {
	//			//			width = 0.2F;
	//			//			height = 0.2F;
	//			//		} else if (entity.isSneaking()) {
	//			//			width = 0.6F;
	//			//			height = 1.65F;
	//			//		} else {
	//			//			width = 0.6F;
	//			//			height = 1.8F;
	//			//		}
	//
	//			if ((width != entity.width) || (height != entity.height)) {
	//				final double d0 = entity.width / 2.0D;
	//
	//				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
	//				//				axisalignedbb = new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0);
	//				axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + width, axisalignedbb.minY + height, axisalignedbb.minZ + width);
	//
	//				if (!entity.world.collidesWithAnyBlock(axisalignedbb)) {
	//					setSizeVanilla(entity, width, height);
	//				}
	//			}
	//		});
	//	}

	//	public static void setSizeVanilla(EntityLivingBase entity, float width, float height) {
	//		if ((width != entity.width) || (height != entity.height)) {
	//			final float f = entity.width;
	//			entity.width = width;
	//			entity.height = height;
	//
	//			if (entity.width < f) {
	//				final double d0 = width / 2.0D;
	//				entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
	//				return;
	//			}
	//
	//			final AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
	//			entity.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + entity.width, axisalignedbb.minY + entity.height, axisalignedbb.minZ + entity.width));
	//
	//			if ((entity.width > f) && !entity.world.isRemote) {
	//				entity.move(MoverType.SELF, f - entity.width, 0.0D, f - entity.width);
	//			}
	//		}
	//	}
}
