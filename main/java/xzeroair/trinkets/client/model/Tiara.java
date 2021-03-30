package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.util.Reference;

public class Tiara extends ModelBase {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/ender_tiara_model.png");

	public ModelRenderer Tiara;
	public ModelRenderer Tiara1Right;
	public ModelRenderer Tiara1Left;
	public ModelRenderer Tiara2Right;
	public ModelRenderer Tiara2Left;
	public ModelRenderer Tiara3Right;
	public ModelRenderer Tiara3Left;
	public ModelRenderer Tiara4Right;
	public ModelRenderer Tiara4Left;

	public Tiara() {

		textureWidth = 16;
		textureHeight = 16;

		Tiara = new ModelRenderer(this, 0, 0);
		Tiara.setRotationPoint(0.0F, -0.635F, 0.06F);
		Tiara.addBox(-1.0F, -0.0F, -4.05F, 2, 2, 1, 0.0F);

		Tiara1Right = new ModelRenderer(this, 0, 3);
		Tiara1Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara1Right.addBox(-0.9F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara1Right, 0.0F, 0.5061454830783556F, 0.0F);

		Tiara1Left = new ModelRenderer(this, 0, 3);
		Tiara1Left.mirror = true;
		Tiara1Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara1Left.addBox(-1.1F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara1Left, 0.0F, -0.5061454830783556F, 0.0F);

		Tiara2Right = new ModelRenderer(this, 0, 3);
		Tiara2Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara2Right.addBox(-0.8F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara2Right, 0.0F, 1.0122909661567112F, 0.0F);

		Tiara2Left = new ModelRenderer(this, 0, 3);
		Tiara2Left.mirror = true;
		Tiara2Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara2Left.addBox(-1.2F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara2Left, 0.0F, -1.0122909661567112F, 0.0F);

		Tiara3Left = new ModelRenderer(this, 0, 3);
		Tiara3Left.mirror = true;
		Tiara3Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara3Left.addBox(-1.4F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara3Left, 0.0F, -1.5707963267948966F, 0.0F);

		Tiara3Right = new ModelRenderer(this, 0, 3);
		Tiara3Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		Tiara3Right.addBox(-0.6F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara3Right, 0.0F, 1.5707963267948966F, 0.0F);

		Tiara4Right = new ModelRenderer(this, 0, 5);
		Tiara4Right.setRotationPoint(0.0F, 0.0F, 0.0F);
		Tiara4Right.addBox(-0.9F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		this.setRotateAngle(Tiara4Right, 0.0F, 0.5061454830783556F, 0.0F);

		Tiara4Left = new ModelRenderer(this, 0, 5);
		Tiara4Left.mirror = true;
		Tiara4Left.setRotationPoint(0.0F, 0.0F, 0.0F);
		Tiara4Left.addBox(-0.1F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		this.setRotateAngle(Tiara4Left, 0.0F, -0.5061454830783556F, 0.0F);

		Tiara.addChild(Tiara1Right);
		Tiara.addChild(Tiara1Left);
		Tiara.addChild(Tiara2Right);
		Tiara.addChild(Tiara2Left);
		Tiara.addChild(Tiara3Right);
		Tiara.addChild(Tiara3Left);
		Tiara.addChild(Tiara4Right);
		Tiara.addChild(Tiara4Left);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(Tiara.offsetX, Tiara.offsetY, Tiara.offsetZ);
		GlStateManager.translate(Tiara.rotationPointX * scaleFactor, Tiara.rotationPointY * scaleFactor, Tiara.rotationPointZ * scaleFactor);
		float fscale = 0.0725F;
		GlStateManager.scale(fscale, fscale, fscale);
		GlStateManager.translate(-Tiara.offsetX, -Tiara.offsetY, -Tiara.offsetZ);
		GlStateManager.translate(-Tiara.rotationPointX * scaleFactor, -Tiara.rotationPointY * scaleFactor, -Tiara.rotationPointZ * scaleFactor);
		Tiara.render(scaleFactor);
		GlStateManager.popMatrix();
		//		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		//		Tiara.render(scaleFactor);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		Tiara.rotateAngleX = 0F;
		Tiara.rotateAngleY = 4.7F;
		Tiara.rotateAngleZ = 0F;
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
