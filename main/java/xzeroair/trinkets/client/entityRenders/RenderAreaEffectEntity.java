package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.entity.AreaEffectEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderAreaEffectEntity extends Render<AreaEffectEntity> {

    public RenderAreaEffectEntity(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(@Nonnull AreaEffectEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.renderAreaEffect(entity, x, y, z, partialTicks);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void renderAreaEffect(AreaEffectEntity entity, double x, double y, double z, float partialTicks) {
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
            buffer.pos(Math.cos(angle) * radius, 0D, Math.sin(angle) * radius).color(red, green, blue, alpha).endVertex();
        }

        tessellator.draw();
        GlStateManager.glLineWidth(1.0F);
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
    protected ResourceLocation getEntityTexture(@Nonnull AreaEffectEntity entity) {
        return null;
    }

    public static final RenderAreaEffectEntity.Factory FACTORY = new RenderAreaEffectEntity.Factory();

    public static class Factory implements IRenderFactory<AreaEffectEntity> {

        @Override
        public Render<? super AreaEffectEntity> createRenderFor(RenderManager manager) {
            return new RenderAreaEffectEntity(manager);
        }

    }
}