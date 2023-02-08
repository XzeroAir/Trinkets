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
		boolean flying = (entity instanceof EntityPlayer) && ((EntityPlayer) entity).capabilities.isFlying;
		float width = entity.width;
		float height = entity.height;

		if (entity.isSneaking()) {
			width = TLWidth;
			height = !flying ? TLHeight * 0.92F : TLHeight;
		} else if (entity.isElytraFlying()) {
			width = TLWidth;
			height = TLHeight * 0.2F;
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
		float Wclamp = 0.252F;
		float Hclamp = 0.45F;
		if (entity instanceof EntityPlayer) {
			Wclamp = 0.3f;
			Hclamp = 0.45f;
		} else {
			Wclamp = 0.3f;
			Hclamp = 0.45f;
		}
		width = (float) MathHelper.clamp(StringUtils.getAccurateDouble(width), Wclamp, 5.4F);
		height = (float) MathHelper.clamp(StringUtils.getAccurateDouble(height), Hclamp, 5.4F);

		if ((width != entity.width) || (height != entity.height)) {
			entity.width = width;
			entity.height = height;
			try {
				TrinketReflectionHelper.ENTITY_SETSIZE.invoke(entity, width, height);
			} catch (Exception e) {
			}
		}
		final double d0 = entity.width / 2.0D;
		double x1 = entity.posX - d0;
		double z1 = entity.posZ - d0;
		double x2 = entity.posX + d0;
		double z2 = entity.posZ + d0;
		double y = entity.posY + entity.height;
		entity.setEntityBoundingBox(new AxisAlignedBB(x1, entity.posY, z1, x2, y, z2));
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
