package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.capabilities.sizeCap.ISizeCap;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, ISizeCap nbt){

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
		width = MathHelper.clamp(width, 0.252F, 1.2F);
		height = MathHelper.clamp(height, 0.252F, 3.6F);

		entity.width = width;
		entity.height = height;
		if(nbt.getHeight() != height) {
			nbt.setHeight(height);
		}
		if(nbt.getWidth() != width) {
			nbt.setWidth(width);
		}

		final double d0 = entity.width/2.0D;

		final AxisAlignedBB bb = entity.getEntityBoundingBox();
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY+ height, entity.posZ + d0));
	}
}
