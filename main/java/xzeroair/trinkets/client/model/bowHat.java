package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

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
		textureWidth = 64;
		textureHeight = 128;

		Base = new ModelRenderer(this);
		Base.setRotationPoint(0.0F, -0.54F, 0.0F);
		this.setRotateAngle(Base, 0.0F, 0.0F, 0.0F);
		Base.setTextureOffset(0, 64).addBox(-6.0F, 0.0F, -3.0F, 12, 1, 5, 0.0F);

		Base2 = new ModelRenderer(this);
		Base2.setRotationPoint(0.0F, -10.0F, 0.0F);
		Base.addChild(Base2);
		Base2.setTextureOffset(34, 64).addBox(-2.0F, 9.0F, -3.0F, 4, 1, 5, 0.0F);

		Base3 = new ModelRenderer(this);
		Base3.setRotationPoint(0.0F, -11.0F, 0.0F);
		Base.addChild(Base3);
		Base3.setTextureOffset(47, 65).addBox(-1.0F, 9.0F, -3.0F, 2, 1, 5, 0.0F);

		BowLeft1 = new ModelRenderer(this);
		BowLeft1.setRotationPoint(8.0F, -10.0F, 0.0F);
		Base.addChild(BowLeft1);
		this.setRotateAngle(BowLeft1, 0.0F, 0.0F, 2.3562F);
		BowLeft1.setTextureOffset(0, 70).addBox(6.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

		BowLeft2 = new ModelRenderer(this);
		BowLeft2.setRotationPoint(8.0F, -10.0F, 0.0F);
		Base.addChild(BowLeft2);
		this.setRotateAngle(BowLeft2, 0.0F, 0.0F, -2.3562F);
		BowLeft2.setTextureOffset(16, 70).addBox(-6.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

		BowLeft3 = new ModelRenderer(this);
		BowLeft3.setRotationPoint(3.0F, -12.0F, 0.0F);
		Base.addChild(BowLeft3);
		this.setRotateAngle(BowLeft3, 0.0F, 0.0F, -0.3491F);
		BowLeft3.setTextureOffset(32, 70).addBox(-3.0782F, 8.4572F, -3.0F, 4, 1, 5, 0.0F);

		BowLeft4 = new ModelRenderer(this);
		BowLeft4.setRotationPoint(1.0F, -10.0F, 0.0F);
		Base.addChild(BowLeft4);
		this.setRotateAngle(BowLeft4, 0.0F, 0.0F, -0.7854F);
		BowLeft4.setTextureOffset(45, 71).addBox(-6.364F, 6.364F, -3.0F, 3, 1, 5, 0.0F);

		BowRight1 = new ModelRenderer(this);
		BowRight1.setRotationPoint(-8.0F, -10.0F, 0.0F);
		Base.addChild(BowRight1);
		this.setRotateAngle(BowRight1, 0.0F, 0.0F, -2.3562F);
		BowRight1.setTextureOffset(0, 76).addBox(-9.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

		BowRight2 = new ModelRenderer(this);
		BowRight2.setRotationPoint(-8.0F, -10.0F, 0.0F);
		Base.addChild(BowRight2);
		this.setRotateAngle(BowRight2, 0.0F, 0.0F, 2.3562F);
		BowRight2.setTextureOffset(16, 76).addBox(3.364F, -6.364F, -3.0F, 3, 1, 5, 0.0F);

		BowRight3 = new ModelRenderer(this);
		BowRight3.setRotationPoint(-3.0F, -12.0F, 0.0F);
		Base.addChild(BowRight3);
		this.setRotateAngle(BowRight3, 0.0F, 0.0F, 0.3491F);
		BowRight3.setTextureOffset(32, 76).addBox(-0.9218F, 8.4572F, -3.0F, 4, 1, 5, 0.0F);

		BowRight4 = new ModelRenderer(this);
		BowRight4.setRotationPoint(-1.0F, -10.0F, 0.0F);
		Base.addChild(BowRight4);
		this.setRotateAngle(BowRight4, 0.0F, 0.0F, 0.7854F);
		BowRight4.setTextureOffset(45, 77).addBox(3.364F, 6.364F, -3.0F, 3, 1, 5, 0.0F);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(Base.offsetX, Base.offsetY, Base.offsetZ);
		GlStateManager.translate(Base.rotationPointX * scale, Base.rotationPointY * scale, Base.rotationPointZ * scale);
		float fscale = 0.0475F;
		GlStateManager.scale(fscale, fscale, fscale);
		GlStateManager.translate(-Base.offsetX, -Base.offsetY, -Base.offsetZ);
		GlStateManager.translate(-Base.rotationPointX * scale, -Base.rotationPointY * scale, -Base.rotationPointZ * scale);
		Base.render(scale);
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
