package xzeroair.trinkets.client.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.client.ConstantsTextureResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ParticleLightningOrb extends Particle {

    List<Vec3d> points = new ArrayList<>();
    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
    protected double prevPosX2;
    protected double prevPosY2;
    protected double prevPosZ2;
    protected double posX2;
    protected double posY2;
    protected double posZ2;
    private boolean depth = false;

    public ParticleLightningOrb(World world, double x, double y, double z, int color, float a, boolean bool, float scale) {
        super(world, x, y, z);
        final int r = (color & 16711680) >> 16;
        final int g = (color & 65280) >> 8;
        final int b = (color & 255) >> 0;
        this.particleRed = r / 255.0F;
        this.particleGreen = g / 255.0F;
        this.particleBlue = b / 255.0F;
        this.particleAlpha = a;
        this.depth = bool;
        this.particleMaxAge = 16;
        this.particleAge = 0;
        this.particleScale = scale;
    }

    public ParticleLightningOrb(World world, double x, double y, double z, double x2, double y2, double z2, int color, float a, boolean bool, float scale) {
        super(world, x, y, z);
        this.posX2 = x2;
        this.posY2 = y2;
        this.posZ2 = z2;
        this.prevPosX2 = x2;
        this.prevPosY2 = y2;
        this.prevPosZ2 = z2;
        final int r = (color & 16711680) >> 16;
        final int g = (color & 65280) >> 8;
        final int b = (color & 255) >> 0;
        this.particleRed = r / 255.0F;
        this.particleGreen = g / 255.0F;
        this.particleBlue = b / 255.0F;
        this.particleAlpha = a;
        this.depth = bool;
        this.particleMaxAge = 16;
        this.particleAge = 0;
        this.particleScale = scale;
    }

    public ParticleLightningOrb(World world, double x, double y, double z, double x2, double y2, double z2, double xSpeed, double ySpeed, double zSpeed, int color, float a, boolean bool, float scale) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.posX2 = x2;
        this.posY2 = y2;
        this.posZ2 = z2;
        this.prevPosX2 = x2;
        this.prevPosY2 = y2;
        this.prevPosZ2 = z2;
        final int r = (color & 16711680) >> 16;
        final int g = (color & 65280) >> 8;
        final int b = (color & 255) >> 0;
        this.particleRed = r / 255.0F;
        this.particleGreen = g / 255.0F;
        this.particleBlue = b / 255.0F;
        this.particleAlpha = a;
        this.depth = bool;
        this.particleMaxAge = 16;
        this.particleAge = 0;
        this.particleScale = scale;
    }

    @Override
    public void renderParticle(@Nonnull BufferBuilder buffer, @Nonnull Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

        final float x = (float) ((this.prevPosX + ((this.posX - this.prevPosX) * partialTicks)) - interpPosX);
        final float y = (float) ((this.prevPosY + ((this.posY - this.prevPosY) * partialTicks)) - interpPosY);
        final float z = (float) ((this.prevPosZ + ((this.posZ - this.prevPosZ) * partialTicks)) - interpPosZ);

        final Vec3d origin = new Vec3d(x + 0.0, y + 0.2, z + 0.0);

        final int i = (int) (((this.particleAge + partialTicks) * 15.0F) / this.particleMaxAge);
        if (i <= 15) {
            final int lmv = 240;
            final int lmv2 = 240;
            for (int p = 0; p < this.particleMaxAge; p++) {
                if (p < 1) {
                    this.points.add(origin);
                } else {
                    Vec3d lP = this.points.get(p - 1);
                    Vec3d next = new LightningVertex(lP).getVec();
                    this.points.add(next);
                }
            }
            float r = this.particleRed;
            float g = this.particleGreen;
            float b = this.particleBlue;
            float a = this.particleAlpha;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
            GlStateManager.glLineWidth(2f);

            for (int p = 0; p < (this.points.size() - 1); p++) {
                int clamped = MathHelper.clamp(p, 0, (this.points.size() - 1));
                Vec3d lp = this.points.get(clamped);
                Vec3d cp = lp;
                buffer.pos(cp.x, cp.y, cp.z).tex(0, 0).color(r, g, b, a).endVertex();
            }
            Tessellator.getInstance().draw();
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
            GlStateManager.glLineWidth(8f);

            for (int p = 0; p < (this.points.size() - 1); p++) {
                int clamped = MathHelper.clamp(p, 0, (this.points.size() - 1));
                Vec3d lp = this.points.get(clamped);
                Vec3d cp = lp;
                buffer.pos(cp.x, cp.y, cp.z).tex(0, 0).color(r, g, b, a * 0.5F).endVertex();
            }
            Tessellator.getInstance().draw();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.glLineWidth(2F);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(ConstantsTextureResourceLocation.PARTICLE_GREED);
            final float f = (i % 16) / 16.0F;
            final float f1 = f + 0.0625f;
            final float f2 = i / 16 / 16.0F;
            final float f3 = f2 + 0.0625f;
            //float f4 effects size/scale
            final float f4 = 0.5f;//2.0F * this.size;
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

            buffer.pos(x - (rotationX * f4) - (rotationXY * f4), y - (rotationZ * f4), z - (rotationYZ * f4) - (rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lmv, lmv2).endVertex();
            buffer.pos((x - (rotationX * f4)) + (rotationXY * f4), y + (rotationZ * f4), (z - (rotationYZ * f4)) + (rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lmv, lmv2).endVertex();
            buffer.pos(x + (rotationX * f4) + (rotationXY * f4), y + (rotationZ * f4), z + (rotationYZ * f4) + (rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lmv, lmv2).endVertex();
            buffer.pos((x + (rotationX * f4)) - (rotationXY * f4), y - (rotationZ * f4), (z + (rotationYZ * f4)) - (rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(lmv, lmv2).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.glLineWidth(2F);
            GlStateManager.popMatrix();
            this.points.clear();
        }
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

}