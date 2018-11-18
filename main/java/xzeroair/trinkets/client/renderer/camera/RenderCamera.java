package xzeroair.trinkets.client.renderer.camera;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.entity.camera.EntityCamera;

public class RenderCamera extends Render<EntityCamera> {

	public RenderCamera(RenderManager renderManagerIn) {
		super(renderManagerIn);
		shadowSize = 0.0F;
	}

	@Override
	public void doRender(EntityCamera entity, double x, double y, double z, float entityYaw, float partialTicks){}


	@Override
	protected ResourceLocation getEntityTexture(EntityCamera entity) {
		// TODO Auto-generated method stub
		return null;
	}

}