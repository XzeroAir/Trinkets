package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BipedJsonModel extends ModelPlayer {

    public boolean hasEffect;
    public ModelResourceLocation modelResourceLocation;

    public BipedJsonModel(ResourceLocation modelLocation) {
        this(modelLocation, "inventory", 16, 16, false);
    }

    public BipedJsonModel(ResourceLocation modelLocation, String variant, int texWidth, int texHeight, boolean hasEffect) {
        super(0F, true);
        this.textureWidth = texWidth;
        this.textureHeight = texHeight;
        this.hasEffect = hasEffect;
        this.modelResourceLocation = new ModelResourceLocation(modelLocation, variant);
    }

    protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;

        for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
        }

        while (f >= 180.0F) {
            f -= 360.0F;
        }

        return prevYawOffset + (partialTicks * f);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale) {
        this.render(entity, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, -1);
    }

    @SideOnly(Side.CLIENT)
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale, int color) {
        this.render(entity, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, color, -1, -1, false);
    }

    @SideOnly(Side.CLIENT)
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale, int color, int altColor) {
        this.render(entity, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, color, altColor, -1, false);
    }

    @SideOnly(Side.CLIENT)
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale, int color, int altColor, int auxColor, boolean override) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
        TextureManager texManager = Minecraft.getMinecraft().getTextureManager();
        GlStateManager.pushMatrix();
        try {
            IBakedModel model = renderer.getItemModelMesher().getModelManager().getModel(this.modelResourceLocation);
            if (model != null && !model.isBuiltInRenderer()) {
                texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                texManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.pushMatrix();
                this.renderModel(renderer, model, color, altColor, auxColor, override);
                if (this.hasEffect) {
                    this.renderEffect(renderer, model);
                }
                GlStateManager.cullFace(GlStateManager.CullFace.BACK);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                texManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.pushMatrix();
            }
            GlStateManager.popMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlStateManager.popMatrix();
    }

    private void renderModel(RenderItem renderer, IBakedModel model, int color, int altColor, int auxColor, boolean override) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        for (EnumFacing enumfacing : EnumFacing.values()) {
            this.renderQuads(bufferbuilder, model.getQuads(null, enumfacing, 0L), color, altColor, auxColor, override);
        }
        this.renderQuads(bufferbuilder, model.getQuads(null, null, 0L), color, altColor, auxColor, override);
        tessellator.draw();
    }

    public void renderQuads(BufferBuilder buffer, List<BakedQuad> quads, int color, int altColor, int auxColor, boolean override) {
        boolean flag = (color == -1);
        int i = 0;
        for (int j = quads.size(); i < j; ++i) {
            BakedQuad bakedquad = quads.get(i);
            int k = -1;
//            if ((renderWithOverride && !flag) || (!bakedquad.hasTintIndex() && !flag)) {
//            if ((renderWithOverride && !flag)) {
            if (override) {
                k = color;
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                k = k | -16777216;
            } else if (bakedquad.hasTintIndex()) {
                if (bakedquad.getTintIndex() == 2) {
                    k = auxColor;
                } else if (bakedquad.getTintIndex() == 1) {
                    k = altColor;
                } else {
                    k = color;
                }
//                System.out.println(bakedquad.getTintIndex() + "|" + "|" + k);
//                if (flag) {
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                k = k | -16777216;
//                } else {
//                    k = -1;
//                }
            }
            this.renderQuadColor(buffer, bakedquad, k);
        }
    }

    public void renderQuadColor(BufferBuilder buffer, BakedQuad quad, int auxColor) {
        if (quad.getFormat().equals(buffer.getVertexFormat())) {
            buffer.addVertexData(quad.getVertexData());
//            if (buffer.getVertexFormat().hasColor())
//            {
            ForgeHooksClient.putQuadColor(buffer, quad, auxColor);
//            }
        } else {
            LightUtil.renderQuadColorSlow(buffer, quad, auxColor);
        }
    }

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private void renderEffect(RenderItem renderer, IBakedModel model) {
        TextureManager texManager = Minecraft.getMinecraft().getTextureManager();
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        texManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(renderer, model, -8372020, -8372020, -8372020, false);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        this.renderModel(renderer, model, -8372020, -8372020, -8372020, false);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
}
