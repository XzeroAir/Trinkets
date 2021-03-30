package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.TrinketsConfig;

public class SizeHandler {

	public static void setSize(EntityLivingBase entity, float TLHeight, float TLWidth) {
		if ((Loader.isModLoaded("artemislib")) && TrinketsConfig.compat.artemislib) {
			return;
		}

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
		} else {
			width = TLWidth;
			height = TLHeight;
		}
		float Wclamp = 0.252F;
		float Hclamp = 0.45F;
		if (entity instanceof EntityPlayer) {
			Wclamp = 0.252f;
			Hclamp = 0.45f;
		} else {
			Wclamp = 0.3f;
			Hclamp = 0.45f;
		}
		width = MathHelper.clamp(width, Wclamp, 5.4F);
		height = MathHelper.clamp(height, Hclamp, 5.4F); // for some reason 5.000000099999F any higher then this and it breaks
		entity.width = width;
		entity.height = height;

		double d0 = entity.width / 2.0D;
		entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + entity.height, entity.posZ + d0));
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

		setSize(entity, TLHeight, TLWidth);
	}
}
