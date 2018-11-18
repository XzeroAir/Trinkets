package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import xzeroair.trinkets.Main;

public class ModelPlayerAlt extends ModelPlayer
{

	public ModelPlayerAlt(float modelSize, boolean isSlim)
	{
		super(modelSize, isSlim);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		//		if(entityIn.isRiding()) {
		//			boolean t = true;
		//			boolean f = false;
		//			float arm = -40F;
		//			float leg = 0.0F;
		//
		//			//arms
		//			this.bipedLeftArm.rotateAngleX = arm;
		//
		//			//			this.bipedLeftArmwear.rotateAngleX = arm;
		//
		//			this.bipedRightArm.rotateAngleX = arm;
		//
		//			//			this.bipedRightArmwear.rotateAngleX = arm;
		//			//legs
		//
		//			this.bipedLeftLeg.rotateAngleX = leg;
		//
		//			//			this.bipedLeftLegwear.rotateAngleX = leg;
		//
		//			this.bipedRightLeg.rotateAngleX = leg;
		//
		//			//			this.bipedRightLegwear.rotateAngleX = leg;
		//		}
		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
	}

	@Override
	public void postRenderArm(float scale, EnumHandSide side)
	{
		super.postRenderArm(scale, side);

	}
}