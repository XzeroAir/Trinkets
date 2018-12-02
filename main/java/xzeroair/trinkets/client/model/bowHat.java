package xzeroair.trinkets.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class bowHat extends ModelBase {


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

		this.Base = new ModelRenderer(this, 0, 64);
		this.Base.setRotationPoint(0.0F, -9.0F, 0.0F);
		this.Base.addBox(-6.0F, 0.0F, -3.0F, 12, 1, 5, 0.0F);

		this.Base3 = new ModelRenderer(this, 47, 65);
		this.Base3.setRotationPoint(0.0F, -11.0F, 0.0F);
		this.Base3.addBox(-1.0F, 0.0F, -3.0F, 2, 1, 5, 0.0F);

		this.BowLeft3 = new ModelRenderer(this, 32, 70);
		this.BowLeft3.setRotationPoint(3.0F, -12.0F, 0.0F);
		this.BowLeft3.addBox(0.0F, 0.0F, -3.0F, 4, 1, 5, 0.0F);
		this.setRotateAngle(BowLeft3, 0.0F, 0.0F, -0.3490658503988659F);

		this.BowRight4 = new ModelRenderer(this, 45, 77);
		this.BowRight4.setRotationPoint(-1.0F, -10.0F, 0.0F);
		this.BowRight4.addBox(-3.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowRight4, 0.0F, 0.0F, 0.7853981633974483F);

		this.BowRight3 = new ModelRenderer(this, 32, 76);
		this.BowRight3.setRotationPoint(-3.0F, -12.0F, 0.0F);
		this.BowRight3.addBox(-4.0F, 0.0F, -3.0F, 4, 1, 5, 0.0F);
		this.setRotateAngle(BowRight3, 0.0F, 0.0F, 0.3490658503988659F);

		this.BowLeft1 = new ModelRenderer(this, 0, 70);
		this.BowLeft1.setRotationPoint(8.0F, -10.0F, 0.0F);
		this.BowLeft1.addBox(0.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowLeft1, 0.0F, 0.0F, 2.356194490192345F);

		this.BowLeft4 = new ModelRenderer(this, 45, 71);
		this.BowLeft4.setRotationPoint(1.0F, -10.0F, 0.0F);
		this.BowLeft4.addBox(0.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowLeft4, 0.0F, 0.0F, -0.7853981633974483F);

		this.BowLeft2 = new ModelRenderer(this, 16, 70);
		this.BowLeft2.setRotationPoint(8.0F, -10.0F, 0.0F);
		this.BowLeft2.addBox(0.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowLeft2, 0.0F, 0.0F, -2.356194490192345F);

		this.BowRight2 = new ModelRenderer(this, 16, 76);
		this.BowRight2.setRotationPoint(-8.0F, -10.0F, 0.0F);
		this.BowRight2.addBox(-3.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowRight2, 0.0F, 0.0F, 2.356194490192345F);

		this.Base2 = new ModelRenderer(this, 34, 64);
		this.Base2.setRotationPoint(0.0F, -10.0F, 0.0F);
		this.Base2.addBox(-2.0F, 0.0F, -3.0F, 4, 1, 5, 0.0F);

		this.BowRight1 = new ModelRenderer(this, 0, 76);
		this.BowRight1.setRotationPoint(-8.0F, -10.0F, 0.0F);
		this.BowRight1.addBox(-3.0F, 0.0F, -3.0F, 3, 1, 5, 0.0F);
		this.setRotateAngle(BowRight1, 0.0F, 0.0F, -2.356194490192345F);


//		this.bipedHead.addChild(this.Base);

		this.Base.addChild(this.Base2);
		this.Base.addChild(this.Base3);

		this.Base.addChild(this.BowLeft1);
		this.Base.addChild(this.BowLeft2);
		this.Base.addChild(this.BowLeft3);
		this.Base.addChild(this.BowLeft4);

		this.Base.addChild(this.BowRight1);
		this.Base.addChild(this.BowRight2);
		this.Base.addChild(this.BowRight3);
		this.Base.addChild(this.BowRight4);

	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
//		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(scale, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, entity);
		this.Base.render(scale);

	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
