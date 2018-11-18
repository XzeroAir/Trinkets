package xzeroair.trinkets.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Wings extends ModelBiped {
	public ModelRenderer LeftWing;
	public ModelRenderer RightWing;

	// create an animation cycle
	// for movement based animations you need to measure distance moved
	// and perform number of cycles per block distance moved.
	protected double distanceMovedTotal = 0.0D;

	// don't make this too large or animations will be skipped


	protected static final double CYCLES_PER_BLOCK = 3.0D;
	protected int cycleIndex = 0;
	protected float[][] undulationCycle = new float[][]
			{
		{ 1F, -1F, -1F, 0F, 1F, 1F, 0F, -1F },
		{ 0F, 1F, -1F, -1F, 0F, 1F, 1F, 0F },
		{ -0F, 0F, 0F, -0F, -0F, 0F, 0F, 0F },
		{ -1F, -0.5F, 0F, 0.5F, 1F, -1F, 0F, 1F },
		{ 0F, -1F, 1F, 1F, 0F, -1F, -1F, 0F },
		{ 1F, -1F, 0F, 1F, 1F, 0F, -1F, -1F },
			};

			public Wings(float scale) {
				super(scale, 0, 32, 32);

				RightWing = new ModelRenderer(this, 0, 0);
				RightWing.setRotationPoint(-2.0F, 1.0F, 2.0F);
				RightWing.addBox(0.0F, -2.0F, 0.0F, 1, 16, 8, 0.0F);
				LeftWing = new ModelRenderer(this, 0, 0);
				LeftWing.mirror = true;
				LeftWing.setRotationPoint(1.0F, 1.0F, 2.0F);
				LeftWing.addBox(0.0F, -2.0F, 0.0F, 1, 16, 8, 0.0F);

				//				bipedBody.addChild(LeftWing);
				//				bipedBody.addChild(RightWing);
			}

			@Override
			public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
				//		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
				RightWing.render(scale);
				LeftWing.render(scale);
			}

			public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
				modelRenderer.rotateAngleX = x;
				modelRenderer.rotateAngleY = y;
				modelRenderer.rotateAngleZ = z;
			}

			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
				float v = 0.08f;
				float f = 1.0F;
				float flap = (MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI) * 1.4F * limbSwingAmount * 1.0F) / f;

				if (((entityIn.motionX >= v) || (entityIn.motionZ >= v)) || (((entityIn.motionX <= -v) || (entityIn.motionZ <= -v))) || !entityIn.onGround) {

					if (!entityIn.onGround && !entityIn.isRiding()) {
						double time = entityIn.world.getWorldTime() + ageInTicks;
						//						double time = ageInTicks;
						float f1 = 16F;
						float f2 = 32F;
						float f3 = 48F;
						float f4 = 64F;
						float f5 = 80F;
						float f6 = 96F;

						float[][] Cycle = new float[][]
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
								cycleIndex = (int) ((time*CYCLES_PER_BLOCK) %Cycle.length);

								LeftWing.rotateAngleY = degToRad(Cycle[cycleIndex][0]);
								RightWing.rotateAngleY = -degToRad(Cycle[cycleIndex][0]);
					}
					if (entityIn.onGround) {
						RightWing.rotateAngleY = -flap;
						LeftWing.rotateAngleY = flap;
					}
				} else {
					RightWing.rotateAngleY = -0.5f;
					LeftWing.rotateAngleY = 0.5f;

				}
			}
			protected void updateDistanceMovedTotal(Entity parEntity)
			{
				distanceMovedTotal += parEntity.getDistance(parEntity.prevPosX, parEntity.prevPosY,

						parEntity.prevPosZ);
			}

			protected double getDistanceMovedTotal(Entity parEntity)
			{
				return (distanceMovedTotal);
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
