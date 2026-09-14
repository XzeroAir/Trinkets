package xzeroair.trinkets.client.entityRenders.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.entityRenders.RenderSummonWrapper;
import xzeroair.trinkets.entity.EntitySummonWrapper;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;

@SideOnly(Side.CLIENT)
public final class SlimeSummonPresentation implements EntityWrapperPresentation {

    private ColoredSlimeRenderer slimeRenderer;

    @Override
    public void render(EntitySummonWrapper wrapper, RenderSummonWrapper renderer, double x, double y, double z, float entityYaw, float partialTicks) {
        final EntityLivingBase source = wrapper.getSourceEntity();
        if (!(source instanceof EntitySlime)) {
            renderer.renderSource(wrapper, x, y, z, entityYaw, partialTicks);
            return;
        }

        if (this.slimeRenderer == null) {
            this.slimeRenderer = new ColoredSlimeRenderer(renderer.getRenderManager());
        }
        renderer.synchronizeRenderState(wrapper, source);
        this.slimeRenderer.setPresentationColor(wrapper.getPresentationColor());
        final float visualSize = this.getVisualSize(wrapper.getHealth());
        this.slimeRenderer.setBodyScale(visualSize / ((EntitySlime) source).getSlimeSize(), visualSize / ((EntitySlime) source).getSlimeSize());
        this.slimeRenderer.setShoulderScale(wrapper.isPerchedOnSummoner() ? this.getShoulderScale(((EntitySlime) source).getSlimeSize()) : 1.0F);
        this.slimeRenderer.doRender((EntitySlime) source, x, y, z, entityYaw, partialTicks);
    }

    private float getVisualSize(float health) {
        final float configuredMaximumHealth = Math.max(2.0F, TrinketsConfig.SERVER.RACES.SLIME.ABILITIES.SUMMON_SLIME.MAXIMUM_SUMMON_HEALTH);
        final float mediumHealth = configuredMaximumHealth * 0.5F;
        final float progress = MathHelper.clamp((health - mediumHealth) / (configuredMaximumHealth - mediumHealth), 0.0F, 1.0F);
        return 1.0F + progress;
    }

    private float getShoulderScale(int slimeSize) {
        return slimeSize >= 3 ? 0.5F : slimeSize == 2 ? 0.7F : 1.0F;
    }

    private static final class ColoredSlimeRenderer extends RenderLiving<EntitySlime> {

        private static final ResourceLocation SLIME_TEXTURE = new ResourceLocation("textures/entity/slime/slime.png");
        private final ColoredSlimeGel colorLayer;
        private float shoulderScale = 1.0F;
        private float bodyScaleXZ = 1.0F;
        private float bodyScaleY = 1.0F;

        private ColoredSlimeRenderer(RenderManager renderManager) {
            super(renderManager, new ModelSlime(16), 0.25F);
            this.colorLayer = new ColoredSlimeGel(this);
            this.addLayer(this.colorLayer);
        }

        private void setPresentationColor(int color) {
            this.colorLayer.setColor(color);
        }

        private void setShoulderScale(float shoulderScale) {
            this.shoulderScale = shoulderScale;
        }

        private void setBodyScale(float bodyScaleXZ, float bodyScaleY) {
            this.bodyScaleXZ = bodyScaleXZ;
            this.bodyScaleY = bodyScaleY;
        }

        @Override
        public void doRender(EntitySlime slime, double x, double y, double z, float entityYaw, float partialTicks) {
            this.shadowSize = 0.25F * slime.getSlimeSize();
            super.doRender(slime, x, y, z, entityYaw, partialTicks);
        }

        @Override
        protected void preRenderCallback(EntitySlime slime, float partialTicks) {
            GlStateManager.scale(0.999F, 0.999F, 0.999F);
            final float slimeSize = slime.getSlimeSize();
            final float squish = (slime.prevSquishFactor + ((slime.squishFactor - slime.prevSquishFactor) * partialTicks)) / ((slimeSize * 0.5F) + 1.0F);
            final float scale = 1.0F / (squish + 1.0F);
            GlStateManager.scale(this.shoulderScale * this.bodyScaleXZ * scale * slimeSize, this.shoulderScale * this.bodyScaleY * (1.0F / scale) * slimeSize, this.shoulderScale * this.bodyScaleXZ * scale * slimeSize);
        }

        @Override
        protected ResourceLocation getEntityTexture(EntitySlime slime) {
            return SLIME_TEXTURE;
        }
    }

    private static final class ColoredSlimeGel implements LayerRenderer<EntitySlime> {

        private static final ResourceLocation MASK_TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/wrapper/slime_mask.png");
        private final RenderLiving<EntitySlime> slimeRenderer;
        private final ModelBase slimeModel = new ModelSlime(0);
        private int color = -1;

        private ColoredSlimeGel(RenderLiving<EntitySlime> slimeRenderer) {
            this.slimeRenderer = slimeRenderer;
        }

        private void setColor(int color) {
            this.color = color;
        }

        @Override
        public void doRenderLayer(EntitySlime slime, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (slime.isInvisible() || this.color < 0) {
                return;
            }

            final float[] rgb = ColorHelper.getRGBColor(this.color);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.depthMask(false);
            GlStateManager.color(rgb[0], rgb[1], rgb[2], 0.35F);
            Minecraft.getMinecraft().renderEngine.bindTexture(MASK_TEXTURE);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeModel.render(slime, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
