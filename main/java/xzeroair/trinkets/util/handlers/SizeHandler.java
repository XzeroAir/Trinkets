package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.capabilities.race.RaceProperties;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, RaceProperties properties) {

		float width = entity.width;
		float height = entity.height;

		if ((entity.height != properties.getHeight()) || (entity.width != properties.getWidth())) {
			if ((properties.getDefaultHeight() != entity.height) || (properties.getDefaultWidth() != entity.width)) {
				properties.setDefaultWidth(width);
				properties.setDefaultHeight(height);
			}
		}

		final float TLHeight = (float) (properties.getDefaultHeight() * (properties.getTarget() * 0.01));
		final float TLWidth = (float) (properties.getDefaultWidth() * (properties.getTarget() * 0.01));

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
		} else {
			width = TLWidth;
			height = TLHeight;
		}
		float clamp = 0.252F;
		if (entity instanceof EntityPlayer) {
			clamp = 0.252f;
		} else {
			clamp = 0.45f;
		}
		width = MathHelper.clamp(width, clamp, 1.8F);
		height = MathHelper.clamp(height, 0.252F, 5.4F); // for some reason 5.000000099999F any higher then this and it breaks

		entity.width = width;
		entity.height = height;
		if (properties.getHeight() != height) {
			properties.setHeight(height);
		}
		if (properties.getWidth() != width) {
			properties.setWidth(width);
		}

		// System.out.println(5 / 1.8);
		final double d0 = entity.width / 2.0D;
		final AxisAlignedBB bb = entity.getEntityBoundingBox();
		//		entity.setEntityBoundingBox(new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.minX + width, bb.minY + (height), bb.minZ + width));
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY + (height), entity.posZ + d0));
	}
}
