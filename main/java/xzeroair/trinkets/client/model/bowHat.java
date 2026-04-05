package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;

public class bowHat extends ModelBase {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/models/armor/ribbon_bow_layer_1.png");

    public ModelRenderer Base;
    public ModelRenderer Base2;
    public ModelRenderer Base3;
    public ModelRenderer BowLeft1;
    public ModelRenderer BowLeft2;
    public ModelRenderer BowLeft3;
    public ModelRenderer BowLeft4;
    public ModelRenderer BowRight1;
    public ModelRenderer BowRight2;
    public ModelRenderer BowRight3;
    public ModelRenderer BowRight4;

    public bowHat() {
        //		super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;

        this.Base = new ModelRenderer(this);
        this.Base.setRotationPoint(0.0F, -0.54F, 0.0F);
        this.setRotateAngle(this.Base, 0.0F, 0.0F, 0.0F);
        this.Base.setTextureOffset(0, 64).addBox(-6.0F, 0.0F, -3.0F, 12, 1, 5, 0.0F);

        this.Base2 = new ModelRenderer(this);
        this.Base2.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.Base.addChild(this.Base2);
        this.Base2.setTextureOffset(34, 64).addBox(-2.0F, 9.0F, -3.0F, 4, 1, 5, 0.0F);

        this.Base3 = new ModelRenderer(this);
        this.Base3.setRotationPoint(0.0F, -11.0F, 0.0F);
        this.Base.addChild(this.Base3);
        this.Base3.setTextureOffset(47, 65).addBox(-1.0F, 9.0F, -3.0F, 2, 1, 5, 0.0F);

        this.BowLeft1 = new ModelRenderer(this);
        this.BowLeft1.setRotationPoint(8.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowLeft1);
        this.setRotateAngle(this.BowLeft1, 0.0F, 0.0F, 2.3562F);
        this.BowLeft1.setTextureOffset(0, 70).addBox(6.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

        this.BowLeft2 = new ModelRenderer(this);
        this.BowLeft2.setRotationPoint(8.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowLeft2);
        this.setRotateAngle(this.BowLeft2, 0.0F, 0.0F, -2.3562F);
        this.BowLeft2.setTextureOffset(16, 70).addBox(-6.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

        this.BowLeft3 = new ModelRenderer(this);
        this.BowLeft3.setRotationPoint(3.0F, -12.0F, 0.0F);
        this.Base.addChild(this.BowLeft3);
        this.setRotateAngle(this.BowLeft3, 0.0F, 0.0F, -0.3491F);
        this.BowLeft3.setTextureOffset(32, 70).addBox(-3.0782F, 8.4572F, -3.0F, 4, 1, 5, 0.0F);

        this.BowLeft4 = new ModelRenderer(this);
        this.BowLeft4.setRotationPoint(1.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowLeft4);
        this.setRotateAngle(this.BowLeft4, 0.0F, 0.0F, -0.7854F);
        this.BowLeft4.setTextureOffset(45, 71).addBox(-6.364F, 6.364F, -3.0F, 3, 1, 5, 0.0F);

        this.BowRight1 = new ModelRenderer(this);
        this.BowRight1.setRotationPoint(-8.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowRight1);
        this.setRotateAngle(this.BowRight1, 0.0F, 0.0F, -2.3562F);
        this.BowRight1.setTextureOffset(0, 76).addBox(-9.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

        this.BowRight2 = new ModelRenderer(this);
        this.BowRight2.setRotationPoint(-8.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowRight2);
        this.setRotateAngle(this.BowRight2, 0.0F, 0.0F, 2.3562F);
        this.BowRight2.setTextureOffset(16, 76).addBox(3.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

        this.BowRight3 = new ModelRenderer(this);
        this.BowRight3.setRotationPoint(-3.0F, -12.0F, 0.0F);
        this.Base.addChild(this.BowRight3);
        this.setRotateAngle(this.BowRight3, 0.0F, 0.0F, 0.3491F);
        this.BowRight3.setTextureOffset(32, 76).addBox(-0.9218F, 8.4572F, -3.0F, 4, 1, 5, 0.0F);

        this.BowRight4 = new ModelRenderer(this);
        this.BowRight4.setRotationPoint(-1.0F, -10.0F, 0.0F);
        this.Base.addChild(this.BowRight4);
        this.setRotateAngle(this.BowRight4, 0.0F, 0.0F, 0.7854F);
        this.BowRight4.setTextureOffset(45, 77).addBox(3.364F, 6.364F, -3.0F, 3, 1, 5, 0.0F);
    }

    @Override
    public void render(@Nonnull Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.Base.offsetX, this.Base.offsetY, this.Base.offsetZ);
        GlStateManager.translate(this.Base.rotationPointX * scale, this.Base.rotationPointY * scale, this.Base.rotationPointZ * scale);
        float fscale = 0.0475F;
        GlStateManager.scale(fscale, fscale, fscale);
        GlStateManager.translate(-this.Base.offsetX, -this.Base.offsetY, -this.Base.offsetZ);
        GlStateManager.translate(-this.Base.rotationPointX * scale, -this.Base.rotationPointY * scale, -this.Base.rotationPointZ * scale);
        this.Base.render(scale);
        GlStateManager.popMatrix();
        //		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        //		Base.render(scale);

    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
