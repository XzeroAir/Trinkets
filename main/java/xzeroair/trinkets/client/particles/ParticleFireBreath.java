package xzeroair.trinkets.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.util.Reference;

@SideOnly(Side.CLIENT)
public class ParticleFireBreath extends Particle {

	public ParticleFireBreath(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float scale) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = 1F;
		particleMaxAge = 15;
		particleScale = scale;
	}

	@Override
	public void renderParticle(BufferBuilder worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/particle/dragon_breath.png"));
		float f = particleTextureIndexX / 16.0F;
		float f1 = f + 0.0624375F;
		float f2 = particleTextureIndexY / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.15F * particleScale;

		if (particleTexture != null) {
			f = particleTexture.getMinU();
			f1 = particleTexture.getMaxU();
			f2 = particleTexture.getMinV();
			f3 = particleTexture.getMaxV();
		}

		int b = this.getBrightnessForRender(partialTicks);
		int j = (b >> 16) & 65535;
		int k = b & 65535;
		final float f5 = (float) ((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
		final float f6 = (float) ((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
		final float f7 = (float) ((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((-rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (-rotationYZ * f4) - (rotationXZ * f4)), new Vec3d((-rotationX * f4) + (rotationXY * f4), rotationZ * f4, (-rotationYZ * f4) + (rotationXZ * f4)), new Vec3d((rotationX * f4) + (rotationXY * f4), rotationZ * f4, (rotationYZ * f4) + (rotationXZ * f4)), new Vec3d((rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (rotationYZ * f4) - (rotationXZ * f4)) };

		if (particleAngle != 0.0F) {
			float f8 = particleAngle + ((particleAngle - prevParticleAngle) * partialTicks);
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d(f10, f11, f12);

			for (int l = 0; l < 4; ++l) {
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
			}
		}
		worldRendererIn.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		worldRendererIn.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		worldRendererIn.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		worldRendererIn.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		//			worldRendererIn.pos(f5 - (rotationX * f4) - (rotationXY * f4), f6 - (rotationZ * f4), f7 - (rotationYZ * f4) - (rotationXZ * f4)).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		//			worldRendererIn.pos((f5 - (rotationX * f4)) + (rotationXY * f4), f6 + (rotationZ * f4), (f7 - (rotationYZ * f4)) + (rotationXZ * f4)).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		//			worldRendererIn.pos(f5 + (rotationX * f4) + (rotationXY * f4), f6 + (rotationZ * f4), f7 + (rotationYZ * f4) + (rotationXZ * f4)).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		//			worldRendererIn.pos((f5 + (rotationX * f4)) - (rotationXY * f4), f6 - (rotationZ * f4), (f7 + (rotationYZ * f4)) - (rotationXZ * f4)).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		GlStateManager.popMatrix();
	}

	@Override
	public void onUpdate() {
		//		super.onUpdate();
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge) {
			this.setExpired();
		}
		//
		//		motionY -= 0.04D * particleGravity;
		//		this.move(motionX, motionY, motionZ);
		//		motionX *= 0.9800000190734863D;
		//		motionY *= 0.9800000190734863D;
		//		motionZ *= 0.9800000190734863D;
		//
		//		if (onGround) {
		//			motionX *= 0.699999988079071D;
		//			motionZ *= 0.699999988079071D;
		//		}
	}

	//	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
}
