package xzeroair.trinkets.client.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.util.helpers.RayTraceHelper.Beam;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ParticleSmell extends Particle {

    private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);

    private Vec3d start;
    private Vec3d end;
    private boolean depth = false;
    private Beam beam;
    private List<Vec3d> points = new ArrayList<>();
    private Vec3d firstSegment, secondSegment, thirdSegment;

    private final boolean first = false;

    public ParticleSmell(World world, double x, double y, double z, float r, float g, float b, float a) {
        super(world, x, y, z);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleAlpha = a;
        this.depth = false;
        this.particleMaxAge = 16;
        this.particleAge = 0;
    }

    public ParticleSmell(World world, BlockPos pos, float r, float g, float b, float a) {
        super(world, pos.getX(), pos.getY(), pos.getZ());
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleAlpha = a;
        this.depth = false;
        this.particleMaxAge = 16;
        this.particleAge = 0;
    }

    public ParticleSmell(World world, Vec3d start, Vec3d end, int color, float a, boolean bool) {
        super(world, start.x, start.y, start.z);
        final int r = (color & 16711680) >> 16;
        final int g = (color & 65280) >> 8;
        final int b = (color & 255) >> 0;
        this.particleRed = r / 255.0F;
        this.particleGreen = g / 255.0F;
        this.particleBlue = b / 255.0F;
        this.particleAlpha = a;
        this.depth = bool;
        this.particleMaxAge = 1;
        this.particleAge = 0;
        this.start = start;
        this.end = end;
    }

    public ParticleSmell(World world, Vec3d start, Vec3d end, Beam beam, int color, float a, boolean bool) {
        super(world, start.x, start.y, start.z);
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
        this.start = start;
        this.end = end;
        this.beam = beam;
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

        //		float r = 1f;//particleRed;
        //		float g = 0f;//particleGreen;
        //		float b = 0f;//particleBlue;
        //		float a = 1F;//particleAlpha;
        float r = this.particleRed;
        float g = this.particleGreen;
        float b = this.particleBlue;
        float a = this.particleAlpha;//MathHelper.clamp(particleAlpha, 0.11F, Float.MAX_VALUE);

        if (this.depth) {
            //			GlStateManager.enableBlend();
        }
        //		if (!depth) {
        //		}

        final int i = (int) (((this.particleAge + partialTicks) * 15.0F) / this.particleMaxAge);

        if (i <= 15) {
            GlStateManager.pushMatrix();
            final float f = (i % 16) / 16.0F;
            final float f1 = f + 0.0625f;
            final float f2 = i / 16 / 16.0F;
            final float f3 = f2 + 0.0625f;
            //float f4 effects size/scale
            final float f4 = 0.25f;//2.0F * this.size;
            final float f5 = (float) ((this.prevPosX + ((this.posX - this.prevPosX) * 0.5)) - interpPosX);
            final float f6 = (float) ((this.prevPosY + ((this.posY - this.prevPosY) * 0.5)) - interpPosY);
            final float f7 = (float) ((this.prevPosZ + ((this.posZ - this.prevPosZ) * 0.5)) - interpPosZ);
            final float f8 = (float) ((this.end.x + ((0.0) * 0.5)) - interpPosX);
            final float f9 = (float) ((this.end.y + ((0.0) * 0.5)) - interpPosY);
            final float f10 = (float) ((this.end.z + ((0.0) * 0.5)) - interpPosZ);
            final int lmv = 0;
            final int lmv2 = 240;
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            if (!this.depth) {
                GlStateManager.disableDepth();
            }
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            GlStateManager.glLineWidth(6f);
            buffer.pos(f5, f6, f7).tex(0, 0).lightmap(lmv, lmv2).color(r, g, b, a).endVertex();
            buffer.pos(f8, f9, f10).tex(0, 0).lightmap(lmv, lmv2).color(r, g, b, a).endVertex();
            Tessellator.getInstance().draw();
            if (!this.depth) {
                GlStateManager.enableDepth();
            }
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void onUpdate() {
        if ((this.posX != this.prevPosX) || (this.posY != this.prevPosY) || (this.posZ != this.prevPosZ)) {
            this.setExpired();
        }
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