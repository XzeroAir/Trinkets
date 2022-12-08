package xzeroair.trinkets.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleFlame;
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
public class ParticleFireBreath extends ParticleFlame {

	//	private float flameScale;

	public ParticleFireBreath(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int color, float scale) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		final int r = (color & 16711680) >> 16;
		final int g = (color & 65280) >> 8;
		final int b = (color & 255) >> 0;
		particleRed = r / 255.0F;
		particleGreen = g / 255.0F;
		particleBlue = b / 255.0F;
		particleAlpha = 1F;
		particleAge = 0;
		particleMaxAge = 31;//(int) (8.0D / ((Math.random() * 0.8D) + 0.2D)) + 4;
		particleScale = scale;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		final float fT = (particleAge + partialTicks) / particleMaxAge;
		final float flameScale = particleScale;
		final int i = (int) (((particleAge + partialTicks) * 30.0F) / particleMaxAge);
		particleScale = flameScale * (1.0F - (fT * fT * 0.5F));
		//		particleScale = flameScale * (1.0F - (i * i * 0.5F));
		if (i <= (particleMaxAge - 1)) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.MODID, "textures/particle/dragon_breath.png"));
			//			float f = particleTextureIndexX % 16.0F;
			//			float f1 = f + 0.0624375F;
			//			float f2 = particleTextureIndexY / 16.0F;
			//			float f3 = f2 + 0.0624375F;
			final boolean flipU = false;
			final boolean flipUV = false;

			final int width = 16;
			final int height = 16;
			final float tileWidth = 1F / 256;
			final float tileHeight = 1F / 256;
			final int x = i < 16 ? i * 16 : (i + 1) * 16;
			final int y = i < 16 ? 16 : 0;

			final float uF = flipU ? x + width : x;
			final float uF2 = flipU ? x : x + width;
			final float vF = flipUV ? y + height : y;
			final float vF2 = flipUV ? y : y + height;

			//			final float f = (i % 16) / 16.0F;//uF;//(y) * tileHeight;//(i % 16) / 16.0F; // Determines the <- -> left right position
			//			final float f1 = f + 0.0625F;//uF2;//(y + height) * tileHeight;
			//			final float f2 = i / 16 / 16.0F;//vF;//(x) * tileWidth;//i / 32 / 32.0F;
			//			final float f3 = f2 + 0.0625F;//vF2;//(x + height) * tileHeight;//0.0625F;//f2 + 0.0625f;
			final int offset = particleMaxAge;//i <= 8 ? 8 : 16;
			final float f4 = particleScale;
			//					(0.0625F) * (offset - i);
			//			System.out.println(f4);
			//			final float f4 = particleScale;

			//			System.out.println((i % 16) == 0);
			//			System.out.println(f + " | " + f1 + " | " + f2 + " | " + f3);
			//			if (particleTexture != null) {
			//				f = particleTexture.getMinU();
			//				f1 = particleTexture.getMaxU();
			//				f2 = particleTexture.getMinV();
			//				f3 = particleTexture.getMaxV();
			//			}

			final int b = this.getBrightnessForRender(partialTicks);
			final int j = (b >> 16) & 65535;
			final int k = b & 65535;
			final float f5 = (float) ((prevPosX + ((posX - prevPosX) * partialTicks)) - interpPosX);
			final float f6 = (float) ((prevPosY + ((posY - prevPosY) * partialTicks)) - interpPosY);
			final float f7 = (float) ((prevPosZ + ((posZ - prevPosZ) * partialTicks)) - interpPosZ);
			final Vec3d[] avec3d = new Vec3d[] {
					new Vec3d((-rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (-rotationYZ * f4) - (rotationXZ * f4)),
					new Vec3d((-rotationX * f4) + (rotationXY * f4), rotationZ * f4, (-rotationYZ * f4) + (rotationXZ * f4)),
					new Vec3d((rotationX * f4) + (rotationXY * f4), rotationZ * f4, (rotationYZ * f4) + (rotationXZ * f4)),
					new Vec3d((rotationX * f4) - (rotationXY * f4), -rotationZ * f4, (rotationYZ * f4) - (rotationXZ * f4))
			};

			if (particleAngle != 0.0F) {
				final float f8 = particleAngle + ((particleAngle - prevParticleAngle) * partialTicks);
				final float f9 = MathHelper.cos(f8 * 0.5F);
				final float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
				final float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
				final float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
				final Vec3d vec3d = new Vec3d(f10, f11, f12);

				for (int l = 0; l < 4; ++l) {
					avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0F * f9));
				}
			}
			buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(uF * tileWidth, vF2 * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(uF2 * tileWidth, vF2 * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(uF2 * tileWidth, vF * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(uF * tileWidth, vF * tileHeight).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 + avec3d[0].x, f6 + avec3d[0].y, f7 + avec3d[0].z).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 + avec3d[1].x, f6 + avec3d[1].y, f7 + avec3d[1].z).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 + avec3d[2].x, f6 + avec3d[2].y, f7 + avec3d[2].z).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 + avec3d[3].x, f6 + avec3d[3].y, f7 + avec3d[3].z).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 - (rotationX * f4) - (rotationXY * f4), f6 - (rotationZ * f4), f7 - (rotationYZ * f4) - (rotationXZ * f4)).tex(f1, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos((f5 - (rotationX * f4)) + (rotationXY * f4), f6 + (rotationZ * f4), (f7 - (rotationYZ * f4)) + (rotationXZ * f4)).tex(f1, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos(f5 + (rotationX * f4) + (rotationXY * f4), f6 + (rotationZ * f4), f7 + (rotationYZ * f4) + (rotationXZ * f4)).tex(f, f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			//			buffer.pos((f5 + (rotationX * f4)) - (rotationXY * f4), f6 - (rotationZ * f4), (f7 + (rotationYZ * f4)) - (rotationXZ * f4)).tex(f, f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		//		prevPosX = posX;
		//		prevPosY = posY;
		//		prevPosZ = posZ;
		//
		//		if (particleAge++ >= particleMaxAge) {
		//			this.setExpired();
		//		}
		//		//
		//		this.move(motionX, motionY, motionZ);
		//		motionX *= 0.9599999785423279D;
		//		motionY *= 0.9599999785423279D;
		//		motionZ *= 0.9599999785423279D;
		//
		//		if (onGround) {
		//			motionX *= 0.699999988079071D;
		//			motionZ *= 0.699999988079071D;
		//		}
		//				prevPosX = posX;
		//				prevPosY = posY;
		//				prevPosZ = posZ;
		//
		//				if (particleAge++ >= particleMaxAge) {
		//					this.setExpired();
		//				}
		//
		//				motionY -= 0.04D * particleGravity;
		//				this.move(motionX, motionY, motionZ);
		//				motionX *= 0.9800000190734863D;
		//				motionY *= 0.9800000190734863D;
		//				motionZ *= 0.9800000190734863D;
		//
		//				if (onGround) {
		//					motionX *= 0.699999988079071D;
		//					motionZ *= 0.699999988079071D;
		//				}
	}

	//	private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
}
