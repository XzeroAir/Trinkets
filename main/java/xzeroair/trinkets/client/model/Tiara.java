package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
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

		textureWidth = 32;
		textureHeight = 32;

		this.Tiara = new ModelRenderer(this, 0, 0);
		this.Tiara.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Tiara.addBox(-1.0F, -0.0F, -4.05F, 2, 2, 1, 0.0F);

		this.Tiara1Right = new ModelRenderer(this, 0, 3);
		this.Tiara1Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara1Right.addBox(-0.9F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara1Right, 0.0F, 0.5061454830783556F, 0.0F);

		this.Tiara1Left = new ModelRenderer(this, 0, 3);
		this.Tiara1Left.mirror = true;
		this.Tiara1Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara1Left.addBox(-1.1F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara1Left, 0.0F, -0.5061454830783556F, 0.0F);

		this.Tiara2Right = new ModelRenderer(this, 0, 3);
		this.Tiara2Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara2Right.addBox(-0.8F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara2Right, 0.0F, 1.0122909661567112F, 0.0F);

		this.Tiara2Left = new ModelRenderer(this, 0, 3);
		this.Tiara2Left.mirror = true;
		this.Tiara2Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara2Left.addBox(-1.2F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara2Left, 0.0F, -1.0122909661567112F, 0.0F);

		this.Tiara3Left = new ModelRenderer(this, 0, 3);
		this.Tiara3Left.mirror = true;
		this.Tiara3Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara3Left.addBox(-1.4F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara3Left, 0.0F, -1.5707963267948966F, 0.0F);

		this.Tiara3Right = new ModelRenderer(this, 0, 3);
		this.Tiara3Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Tiara3Right.addBox(-0.6F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		this.setRotateAngle(Tiara3Right, 0.0F, 1.5707963267948966F, 0.0F);

		this.Tiara4Right = new ModelRenderer(this, 0, 5);
		this.Tiara4Right.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Tiara4Right.addBox(-0.9F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		this.setRotateAngle(Tiara4Right, 0.0F, 0.5061454830783556F, 0.0F);

		this.Tiara4Left = new ModelRenderer(this, 0, 5);
		this.Tiara4Left.mirror = true;
		this.Tiara4Left.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Tiara4Left.addBox(-0.1F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		this.setRotateAngle(Tiara4Left, 0.0F, -0.5061454830783556F, 0.0F);

		this.Tiara.addChild(this.Tiara1Right);
		this.Tiara.addChild(this.Tiara1Left);
		this.Tiara.addChild(this.Tiara2Right);
		this.Tiara.addChild(this.Tiara2Left);
		this.Tiara.addChild(this.Tiara3Right);
		this.Tiara.addChild(this.Tiara3Left);
		this.Tiara.addChild(this.Tiara4Right);
		this.Tiara.addChild(this.Tiara4Left);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		Tiara.render(scaleFactor);
	}


	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		this.Tiara.rotateAngleX = 0F;
		this.Tiara.rotateAngleY = 4.7F;
		this.Tiara.rotateAngleZ = 0F;
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
