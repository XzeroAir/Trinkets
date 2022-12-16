package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.StringUtils;
import xzeroair.trinkets.util.helpers.TrinketReflectionHelper;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
		if (Trinkets.ArtemisLib && TrinketsConfig.compat.artemislib) {
			return;
		}
		if (entity.isChild()) {
			return;
		}

		//		boolean moving = ((entity.getHorizontalFacing().getDirectionVec().getX() * entity.motionX) > 0) || ((entity.getHorizontalFacing().getDirectionVec().getZ() * entity.motionZ) > 0);
		//		final AxisAlignedBB aabb = entity.getEntityBoundingBox();
		//		final float f = entity.width;
		//		final BlockPos pos = entity.getPosition();
		boolean ground = entity.onGround;
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
		width = (float) MathHelper.clamp(StringUtils.getAccurateDouble(width, width), Wclamp, 5.4F);
		height = (float) MathHelper.clamp(StringUtils.getAccurateDouble(height, height), Hclamp, 5.4F); // for some reason 5.000000099999F any higher then this and it breaks

		//		if (!(entity instanceof EntityPlayer)) {
		if ((width != entity.width) || (height != entity.height)) {
			entity.width = width;
			entity.height = height;

			try {
				TrinketReflectionHelper.ENTITY_SETSIZE.invoke(entity, width, height);
			} catch (Exception e) {
			}
		}

		//		} else {
		//			entity.width = width;
		//			entity.height = height;
		final double d0 = StringUtils.getAccurateDouble(entity.width / 2.0D, width);
		double x1 = StringUtils.getAccurateDouble(entity.posX - d0, entity.posX - width);
		double z1 = StringUtils.getAccurateDouble(entity.posZ - d0, entity.posZ - width);
		double x2 = StringUtils.getAccurateDouble(entity.posX + d0, entity.posX + width);
		double z2 = StringUtils.getAccurateDouble(entity.posZ + d0, entity.posZ + width);
		double y = StringUtils.getAccurateDouble(entity.posY + entity.height, entity.posY + entity.height);
		entity.setEntityBoundingBox(new AxisAlignedBB(x1, entity.posY, z1, x2, y, z2));
		//		}
		entity.onGround = ground;
		// TODO Entities Still Slide in some cases

	}

	public static void setSize(EntityLivingBase entity, EntityProperties properties) {
		if (Trinkets.ArtemisLib && TrinketsConfig.compat.artemislib) {
			return;
		}
		float height = properties.getRaceHandler().getHeight();
		float width = properties.getRaceHandler().getWidth();
		//		if (properties.getSize() < 100) {
		if (entity.isChild()) {
			return;
		}
		//		}

		setSize(entity, height, width);
	}
}
