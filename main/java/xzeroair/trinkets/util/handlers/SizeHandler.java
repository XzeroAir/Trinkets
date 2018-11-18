package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, ICap nbt){

		float width = entity.width;
		float height = entity.height;

		if((entity.height != nbt.getHeight()) || (entity.width != nbt.getWidth())) {
			if((nbt.getDefaultHeight() != entity.height) || (nbt.getDefaultWidth() != entity.width)) {
				nbt.setDefaultWidth(width);
				nbt.setDefaultHeight(height);
			}
		}

		if (entity.isSneaking())
		{
			width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
			height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*0.9F;
		}
		else if (entity.isElytraFlying())
		{
			width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
			height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*0.33F;
		}
		else if (entity.isPlayerSleeping())
		{
			width = 0.2F;
			height = 0.2F;
		}
		else if (entity.isRiding())
		{
			if(((nbt.getDefaultHeight()*nbt.getTarget())/100) > (nbt.getDefaultHeight()/2)) {
				width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
				height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*0.66F;
			} else {
				width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
				height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*2.5F;
			}
		}
		else
		{
			width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
			height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*1.0F;
		}
		width = MathHelper.clamp(width, 0.25F, 1.2F);
		height = MathHelper.clamp(height, 0.25F, 3.0F);

		if ((width != entity.width) || (height != entity.height))
		{

			entity.width = width;
			entity.height = height;
			nbt.setHeight(height);
			nbt.setWidth(width);

			double d0 = (double)entity.width/2.0D;

			AxisAlignedBB bb = entity.getEntityBoundingBox();
			if(Loader.isModLoaded("metamorph")) {
				entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY+ (double)height, entity.posZ + d0));
			} else {
				entity.setEntityBoundingBox(new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.minX + entity.width, bb.minY + entity.height, bb.minZ + entity.width));
			}
		}
	}

	public static void mobSize(EntityLivingBase entity, ICap nbt) {

		float width = entity.width;
		float height = entity.height;

		if((entity.height != nbt.getHeight()) || (entity.width != nbt.getWidth())) {
			if((nbt.getDefaultHeight() != entity.height) || (nbt.getDefaultWidth() != entity.width)) {
				nbt.setDefaultWidth(width);
				nbt.setDefaultHeight(height);
			}
		}

		if (entity.isSneaking())
		{
			width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
			height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*0.92F;
		} else {
			width = (nbt.getDefaultWidth()*nbt.getTarget())/100;
			height = ((nbt.getDefaultHeight()*nbt.getTarget())/100)*1.0F;
		}
		width = MathHelper.clamp(width, 0.05F, 1.2F);
		height = MathHelper.clamp(height, 0.1F, 3.0F);

		if ((width != entity.width) || (height != entity.height))
		{

			if(!entity.isChild()) {
				entity.width = width;
				nbt.setWidth(width);
			}
			entity.height = height;
			nbt.setHeight(height);

			double d0 = (double)entity.width/2.0D;

			AxisAlignedBB bb = entity.getEntityBoundingBox();
			//			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + (double)height, entity.posZ + d0));

			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY + entity.height, entity.posZ + d0));
		}
	}

}
