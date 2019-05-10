package xzeroair.trinkets.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.Reference;

public class Wings extends ModelBase {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png");

	public ModelRenderer LeftWing;
	public ModelRenderer RightWing;

	protected double distanceMovedTotal = 0.0D;

	protected static final double CYCLES_PER_BLOCK = 3.0D;
	protected int cycleIndex = 0;

	public Wings() {
		this.textureWidth = 32;
		this.textureHeight = 32;

		this.RightWing = new ModelRenderer(this, 0, 0);
		this.RightWing.setRotationPoint(-2.0F, 1.0F, 2.0F);
		this.RightWing.addBox(0.0F, -2.0F, 0.0F, 0, 16, 8, 0.0F);
		this.LeftWing = new ModelRenderer(this, 0, 0);
		this.LeftWing.mirror = true;
		this.LeftWing.setRotationPoint(1.0F, 1.0F, 2.0F);
		this.LeftWing.addBox(0.0F, -2.0F, 0.0F, 0, 16, 8, 0.0F);

	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		this.RightWing.render(scale);
		this.LeftWing.render(scale);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		final float v = 0.08f;
		final float f = 1.0F;
		final float flap = (MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI) * 1.4F * limbSwingAmount * 1.0F) / f;

		if (((entityIn.motionX >= v) || (entityIn.motionZ >= v)) || (((entityIn.motionX <= -v) || (entityIn.motionZ <= -v))) || !entityIn.onGround) {

			if (!entityIn.onGround && !entityIn.isRiding()) {
				final double time = entityIn.world.getWorldTime() + ageInTicks;

				final float f1 = 16F;
				final float f2 = 32F;
				final float f3 = 48F;
				final float f4 = 64F;
				//				final float f5 = 80F;
				//				final float f6 = 96F;

				final float[][] Cycle = new float[][]
						{
					{ f4, 0f, 0f},
					{ f3, 0F, 0f},
					{ f2, 0F, 0F},
					{ f1, 0F, 0F},
					{ f, 0F, 0F},
					{ f, 0F, 0F},
					{ f1, 0F, 0F},
					{ f2, 0F, 0F},
					{ f3, 0F, 0F},
					{ f4, 0F, 0F},
						};
						this.cycleIndex = (int) ((time*CYCLES_PER_BLOCK) %Cycle.length);

						this.LeftWing.rotateAngleY = degToRad(Cycle[this.cycleIndex][0]);
						this.RightWing.rotateAngleY = -degToRad(Cycle[this.cycleIndex][0]);
			}
			if (entityIn.onGround) {
				this.RightWing.rotateAngleY = -flap;
				this.LeftWing.rotateAngleY = flap;
			}
		} else {
			this.RightWing.rotateAngleY = -0.5f;
			this.LeftWing.rotateAngleY = 0.5f;

		}
	}
	protected void updateDistanceMovedTotal(Entity parEntity)
	{
		this.distanceMovedTotal += parEntity.getDistance(parEntity.prevPosX, parEntity.prevPosY,

				parEntity.prevPosZ);
	}

	protected double getDistanceMovedTotal()
	{
		return (this.distanceMovedTotal);
	}

	protected float degToRad(float degrees)
	{
		return (degrees * (float)Math.PI) / 180 ;
	}

	protected void setRotation(ModelRenderer model, float rotX, float rotY, float rotZ)
	{
		model.rotateAngleX = degToRad(rotX);
		model.rotateAngleY = degToRad(rotY);
		model.rotateAngleZ = degToRad(rotZ);
	}

	// spin methods are good for testing and debug rotation points and offsets in the model
	protected void spinX(ModelRenderer model)
	{
		model.rotateAngleX += degToRad(0.5F);
	}

	protected void spinY(ModelRenderer model)
	{
		model.rotateAngleY += degToRad(0.5F);
	}

	protected void spinZ(ModelRenderer model)
	{
		model.rotateAngleZ += degToRad(0.5F);
	}
}
