package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.entity.EntityAreaEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderAreaEffect extends Render<EntityAreaEffect> {

    public RenderAreaEffect(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(@Nonnull EntityAreaEffect entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.renderAreaEffect(entity, x, y, z, partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void renderAreaEffect(EntityAreaEffect entity, double x, double y, double z, float partialTicks) {
        if (entity.isBreathImpact() && (entity.getParticle() == EnumParticleTypes.CRIT_MAGIC)) {
            this.renderLightningField(entity, x, y, z, partialTicks);
        }
        if (!entity.shouldRenderCircle()) {
            return;
        }

        final int color = entity.getColor();
        final float red = (color >> 16 & 255) / 255.0F;
        final float green = (color >> 8 & 255) / 255.0F;
        final float blue = (color & 255) / 255.0F;
        final float time = entity.ticksExisted + partialTicks;
        final float radius = Math.max(0.1F, entity.getRadius() + ((float) Math.sin(time * 0.18F) * 0.08F));
        final float alpha = 0.45F + (0.2F * (float) Math.sin(time * 0.12F));

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.03D, z);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.glLineWidth(4.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

        final int segments = 64;
        for (int i = 0; i < segments; i++) {
            double angle = 2.0D * Math.PI * (double) i / (double) segments;
            buffer.pos(Math.cos(angle) * radius, 0D, Math.sin(angle) * radius)
                  .color(red, green, blue, alpha)
                  .endVertex();
        }

        tessellator.draw();
        GlStateManager.glLineWidth(1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void renderLightningField(EntityAreaEffect entity, double x, double y, double z, float partialTicks) {
        final int color = entity.getColor();
        final float red = (color >> 16 & 255) / 255.0F;
        final float green = (color >> 8 & 255) / 255.0F;
        final float blue = (color & 255) / 255.0F;
        final float time = entity.ticksExisted + partialTicks;
        final float radius = Math.max(0.5F, entity.getRadius());

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.35D, z);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.glLineWidth(2.0F);

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        for (int arc = 0; arc < 5; arc++) {
            final double angle = (arc * (Math.PI * 2.0D / 5.0D)) + (time * 0.17D);
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int segment = 0; segment <= 5; segment++) {
                final double progress = segment / 5.0D;
                final double distance = (0.16D + (radius * 0.7D * progress));
                final double sideways = Math.sin((time * 0.75D) + (arc * 2.17D) + (segment * 4.31D)) * 0.11D;
                final double arcX = (Math.cos(angle) * distance) - (Math.sin(angle) * sideways);
                final double arcY = Math.sin((time * 0.31D) + (arc * 1.41D) + (segment * 1.93D)) * 0.16D;
                final double arcZ = (Math.sin(angle) * distance) + (Math.cos(angle) * sideways);
                buffer.pos(arcX, arcY, arcZ).color(red, green, blue, 0.85F).endVertex();
            }
            tessellator.draw();
        }

        GlStateManager.glLineWidth(1.0F);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    @Override
    public void doRenderShadowAndFire(@Nonnull Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityAreaEffect entity) {
        return null;
    }

    public static final RenderAreaEffect.Factory FACTORY = new RenderAreaEffect.Factory();

    public static class Factory implements IRenderFactory<EntityAreaEffect> {

        @Override
        public Render<? super EntityAreaEffect> createRenderFor(RenderManager manager) {
            return new RenderAreaEffect(manager);
        }

    }
}