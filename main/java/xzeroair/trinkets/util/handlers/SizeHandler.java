package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
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
		if ((entity.width > width) && !entity.world.isRemote) {
			entity.move(MoverType.SELF, entity.width - width, 0.0D, entity.width - width);
		}
	}
	//   protected void setSize(float width, float height)
	//   {
	//       if (width != this.width || height != this.height)
	//       {
	//           float f = this.width;
	//           this.width = width;
	//           this.height = height;
	//
	//           if (this.width < f)
	//           {
	//               double d0 = (double)width / 2.0D;
	//               this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double)this.height, this.posZ + d0));
	//               return;
	//           }
	//
	//           AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
	//           this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.width, axisalignedbb.minY + (double)this.height, axisalignedbb.minZ + (double)this.width));
	//
	//           if (this.width > f && !this.firstUpdate && !this.world.isRemote)
	//           {
	//               this.move(MoverType.SELF, (double)(f - this.width), 0.0D, (double)(f - this.width));
	//           }
	//       }
	//   }
}
