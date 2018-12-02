package xzeroair.trinkets.util.handlers;

import java.lang.reflect.Method;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class SizeHandler {

	public static final Method setSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);

	public static void setSize(EntityLivingBase entity, ICap cap){

		float width = entity.width;
		float height = entity.height;

		if((entity.height != cap.getHeight()) || (entity.width != cap.getWidth())) {
			if((cap.getDefaultHeight() != entity.height) || (cap.getDefaultWidth() != entity.width)) {
				cap.setDefaultWidth(width);
				cap.setDefaultHeight(height);
			}
		}

		if (entity.isSneaking())
		{
			width = (cap.getDefaultWidth()*cap.getTarget())/100;
			height = ((cap.getDefaultHeight()*cap.getTarget())/100)*0.9F;
		}
		else if (entity.isElytraFlying())
		{
			width = (cap.getDefaultWidth()*cap.getTarget())/100;
			height = ((cap.getDefaultHeight()*cap.getTarget())/100)*0.33F;
		}
		else if (entity.isPlayerSleeping())
		{
			width = 0.2F;
			height = 0.2F;
		}
		else if (entity.isRiding())
		{
			if(((cap.getDefaultHeight()*cap.getTarget())/100) > (cap.getDefaultHeight()/2)) {
				width = (cap.getDefaultWidth()*cap.getTarget())/100;
				height = ((cap.getDefaultHeight()*cap.getTarget())/100)*0.66F;
			} else {
				width = (cap.getDefaultWidth()*cap.getTarget())/100;
				height = ((cap.getDefaultHeight()*cap.getTarget())/100)*2.5F;
			}
		}
		else
		{
			width = (cap.getDefaultWidth()*cap.getTarget())/100;
			height = ((cap.getDefaultHeight()*cap.getTarget())/100)*1.0F;
		}
		width = MathHelper.clamp(width, 0.25F, 1.2F);
		height = MathHelper.clamp(height, 0.25F, 3.6F);
		cap.setHeight(height);
		cap.setWidth(width);
		try
		{
			setSize.invoke(entity, width, height);
		}
		catch (ReflectiveOperationException e)
		{
			e.printStackTrace();
		}

		double d0 = (double)entity.width/2.0D;

		AxisAlignedBB bb = entity.getEntityBoundingBox();
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY+ (double)height, entity.posZ + d0));
	}

	public static void mobSize(EntityLivingBase entity, ICap cap) {

//		float width = entity.width;
//		float height = entity.height;
//		
//		if(cap.getTrans() == false) {
//			if(cap.getWidth() != cap.getDefaultHeight() || cap.getHeight() != cap.getDefaultHeight()) {
//				cap.setWidth(width);
//				cap.setHeight(height);
//				cap.setDefaultWidth(width);
//				cap.setDefaultHeight(height);
//				entity.width = cap.getDefaultHeight();
//				entity.height = cap.getDefaultHeight();
//				try
//				{
//					setSize.invoke(entity, cap.getDefaultWidth(), cap.getDefaultHeight());
//				}
//				catch (ReflectiveOperationException e)
//				{
//					e.printStackTrace();
//				}
//			}
//			if(cap.getDefaultHeight() != entity.height && cap.getDefaultWidth() != entity.width) {
//				System.out.println("Trigger");
//				entity.width = cap.getDefaultWidth();
//				entity.height = cap.getDefaultHeight();
//				try
//				{
//					setSize.invoke(entity, cap.getDefaultWidth(), cap.getDefaultHeight());
//				}
//				catch (ReflectiveOperationException e)
//				{
//					e.printStackTrace();
//				}
//
//			}
//		} else if(cap.getTrans() == true) {
//			if (entity.isSneaking())
//			{
//				width = (cap.getDefaultWidth()*cap.getTarget())/100;
//				height = ((cap.getDefaultHeight()*cap.getTarget())/100)*0.92F;
//			} else {
//				width = (cap.getDefaultWidth()*cap.getTarget())/100;
//				height = ((cap.getDefaultHeight()*cap.getTarget())/100)*1.0F;
//			}
//			width = MathHelper.clamp(width, 0.2F, 1.2F);
//			height = MathHelper.clamp(height, 0.2F, 3.6F);
//
//			if ((width != entity.width) || (height != entity.height))
//			{
//				entity.width = width;
//				entity.height = height;
//				try
//				{
//					setSize.invoke(entity, width, height);
//				}
//				catch (ReflectiveOperationException e)
//				{
//					e.printStackTrace();
//				}
//			}
//
//			double d0 = (double)entity.width/2.0D;
//
//			AxisAlignedBB bb = entity.getEntityBoundingBox();
//			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, bb.minY, entity.posZ - d0, entity.posX + d0, bb.minY+ (double)height, entity.posZ + d0));
//		}
	}

}
