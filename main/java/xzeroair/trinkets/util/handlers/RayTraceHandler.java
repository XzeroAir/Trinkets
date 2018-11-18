package xzeroair.trinkets.util.handlers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceHandler {

	public static RayTraceResult rayTraceResult(EntityLivingBase entity, double distance){
		RayTraceResult resultPos = entity.rayTrace(distance, 1.0f);
		return resultPos;
	}

	public static BlockPos blockResult(EntityLivingBase entity, double distance){
		Vec3d EyePos = entity.getPositionEyes(1.0F);
		BlockPos inFront = entity.getPosition().add(EyePos.x, EyePos.y, EyePos.z);
		return inFront;
	}

}
