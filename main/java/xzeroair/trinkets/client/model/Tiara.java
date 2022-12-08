package xzeroair.trinkets.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import xzeroair.trinkets.util.Reference;

public class Tiara extends ModelBiped {

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
		Tiara.addBox(-1.0F, -0.0F, -4.05F, 2, 2, 1, 0.0F);
		Tiara.setRotationPoint(0.0F, 0.0F, 0.0F);

		Tiara1Right = new ModelRenderer(this, 0, 3);
		Tiara1Right.addBox(-0.9F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		Tiara1Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara1Right, 0.0F, 0.5061454830783556F, 0.0F, 1F);

		Tiara1Left = new ModelRenderer(this, 0, 3);
		Tiara1Left.mirror = true;
		Tiara1Left.addBox(-1.1F, -0.0F, -4.0F, 2, 1, 1, 0.0F);
		Tiara1Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara1Left, 0.0F, -0.5061454830783556F, 0.0F, 1F);

		Tiara2Right = new ModelRenderer(this, 0, 3);
		Tiara2Right.addBox(-0.8F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		Tiara2Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara2Right, 0.0F, 1.0122909661567112F, 0.0F, 1F);

		Tiara2Left = new ModelRenderer(this, 0, 3);
		Tiara2Left.mirror = true;
		Tiara2Left.addBox(-1.2F, -0.0F, -3.9F, 2, 1, 1, 0.0F);
		Tiara2Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara2Left, 0.0F, -1.0122909661567112F, 0.0F, 1F);

		Tiara3Left = new ModelRenderer(this, 0, 3);
		Tiara3Left.mirror = true;
		Tiara3Left.addBox(-1.4F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		Tiara3Left.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara3Left, 0.0F, -1.5707963267948966F, 0.0F, 1F);

		Tiara3Right = new ModelRenderer(this, 0, 3);
		Tiara3Right.addBox(-0.6F, -0.0F, -3.7F, 2, 1, 1, 0.0F);
		Tiara3Right.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.setRotateAngle(Tiara3Right, 0.0F, 1.5707963267948966F, 0.0F, 1F);

		Tiara4Right = new ModelRenderer(this, 0, 5);
		Tiara4Right.addBox(-0.9F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		Tiara4Right.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.setRotateAngle(Tiara4Right, 0.0F, 0.5061454830783556F, 0.0F, 1F);

		Tiara4Left = new ModelRenderer(this, 0, 5);
		Tiara4Left.mirror = true;
		Tiara4Left.addBox(-0.1F, -0.0F, -4.0F, 1, 1, 1, 0.0F);
		Tiara4Left.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.setRotateAngle(Tiara4Left, 0.0F, -0.5061454830783556F, 0.0F, 1F);

		//		bipedHead.addChild(Tiara);
		//		bipedHead.isHidden = true;
		Tiara.addChild(Tiara1Right);
		Tiara.addChild(Tiara1Left);
		Tiara.addChild(Tiara2Right);
		Tiara.addChild(Tiara2Left);
		Tiara.addChild(Tiara3Right);
		Tiara.addChild(Tiara3Left);
		Tiara.addChild(Tiara4Right);
		Tiara.addChild(Tiara4Left);
	}

	public boolean equippedAsAccessory = false;

	protected float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f;

		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return prevYawOffset + (partialTicks * f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale, float partialTicks) {
		this.setRotateAngle(Tiara, 0, 0, 0, scale);
		float sneak = (isSneak ? 0.2F : 0F);
		Tiara.offsetX = 0;
		Tiara.offsetY = 0;//-0.04F;//-0.62F;
		Tiara.offsetZ = 0;//0.01F;//0.01F;
		Tiara.rotationPointX = 0F;
		Tiara.rotationPointY = 0F;
		Tiara.rotationPointZ = 0F;
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, entity);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, sneak, 0);
		if (equippedAsAccessory) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
			Tiara.render(scale);
			Tiara.postRender(scale);
		} else {
			bipedHead.postRender(scale);
			try {
				GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
				GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
				RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
				TextureManager texManager = Minecraft.getMinecraft().getTextureManager();
				IBakedModel model = renderer.getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(Reference.MODID, "moon_rose"), "inventory"));
				if (model != null) {
					model = model.getOverrides().handleItemState(model, ItemStack.EMPTY, (World) null, (EntityLivingBase) null);
					model.handlePerspective(ItemCameraTransforms.TransformType.HEAD);
					texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					texManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					GlStateManager.enableRescaleNormal();
					GlStateManager.alphaFunc(516, 0.1F);
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					GlStateManager.pushMatrix();
					// TODO: check if negative scale is a thing
					model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.HEAD, false);
					GlStateManager.translate(-0.5F, -0.0F, -0.5F);

					if (model.isBuiltInRenderer()) {
						//		                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						//		                GlStateManager.enableRescaleNormal();
						//		                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
					} else {
						Tessellator tessellator = Tessellator.getInstance();
						BufferBuilder bufferbuilder = tessellator.getBuffer();
						bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

						for (EnumFacing enumfacing : EnumFacing.values()) {
							renderer.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), -1, ItemStack.EMPTY);
						}

						renderer.renderQuads(bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), -1, ItemStack.EMPTY);
						tessellator.draw();
						//		                this.renderModel(model, stack);
						//
						//		                if (stack.hasEffect())
						//		                {
						//		                    this.renderEffect(model);
						//		                }
					}
					GlStateManager.cullFace(GlStateManager.CullFace.BACK);
					GlStateManager.popMatrix();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableBlend();
					texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					texManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
					GlStateManager.pushMatrix();
				}
				GlStateManager.popMatrix();
			} catch (Exception e) {
			}
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale) {
		this.render(entity, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, 1F);
	}

	//	private void renderEffect(IBakedModel model) {
	//		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
	//		TextureManager texManager = Minecraft.getMinecraft().getTextureManager();
	//		GlStateManager.depthMask(false);
	//		GlStateManager.depthFunc(514);
	//		GlStateManager.disableLighting();
	//		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
	//		texManager.bindTexture(RES_ITEM_GLINT);
	//		GlStateManager.matrixMode(5890);
	//		GlStateManager.pushMatrix();
	//		GlStateManager.scale(8.0F, 8.0F, 8.0F);
	//		float f = (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
	//		GlStateManager.translate(f, 0.0F, 0.0F);
	//		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
	//		this.renderModel(model, -8372020);
	//		GlStateManager.popMatrix();
	//		GlStateManager.pushMatrix();
	//		GlStateManager.scale(8.0F, 8.0F, 8.0F);
	//		float f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
	//		GlStateManager.translate(-f1, 0.0F, 0.0F);
	//		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
	//		this.renderModel(model, -8372020);
	//		GlStateManager.popMatrix();
	//		GlStateManager.matrixMode(5888);
	//		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	//		GlStateManager.enableLighting();
	//		GlStateManager.depthFunc(515);
	//		GlStateManager.depthMask(true);
	//		texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	//	}

	//    private void renderModel(RenderItem renderer, IBakedModel model, int color, ItemStack stack)
	//    {
	//        if (net.minecraftforge.common.ForgeModContainer.allowEmissiveItems)
	//        {
	//            net.minecraftforge.client.ForgeHooksClient.renderLitItem(renderer, model, color, stack);
	//            return;
	//        }
	//        Tessellator tessellator = Tessellator.getInstance();
	//        BufferBuilder bufferbuilder = tessellator.getBuffer();
	//        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
	//
	//        for (EnumFacing enumfacing : EnumFacing.values())
	//        {
	//        	renderer.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), color, stack);
	//        }
	//
	//        renderer.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
	//        tessellator.draw();
	//    }

	public void copyFromModel(ModelRenderer source, ModelRenderer dest) {
		//		dest.offsetX = source.offsetX;
		//		dest.offsetY = source.offsetY;
		//		dest.offsetZ = source.offsetZ;
		//		dest.rotationPointX = source.rotationPointX;
		//		dest.rotationPointY = source.rotationPointY;
		//		dest.rotationPointZ = source.rotationPointZ;
		dest.rotateAngleX = source.rotateAngleX;
		dest.rotateAngleY = source.rotateAngleY;
		dest.rotateAngleZ = source.rotateAngleZ;
	}

	//	private void matrixRotateNode(Node n, double roll, double pitch, double yaw) {
	//		double A11 = Math.cos(roll) * Math.cos(yaw);
	//		double A12 = (Math.cos(pitch) * Math.sin(roll)) + (Math.cos(roll) * Math.sin(pitch) * Math.sin(yaw));
	//		double A13 = (Math.sin(roll) * Math.sin(pitch)) - (Math.cos(roll) * Math.cos(pitch) * Math.sin(yaw));
	//		double A21 = -Math.cos(yaw) * Math.sin(roll);
	//		double A22 = (Math.cos(roll) * Math.cos(pitch)) - (Math.sin(roll) * Math.sin(pitch) * Math.sin(yaw));
	//		double A23 = (Math.cos(roll) * Math.sin(pitch)) + (Math.cos(pitch) * Math.sin(roll) * Math.sin(yaw));
	//		double A31 = Math.sin(yaw);
	//		double A32 = -Math.cos(yaw) * Math.sin(pitch);
	//		double A33 = Math.cos(pitch) * Math.cos(yaw);
	//
	//		double d = Math.acos(((A11 + A22 + A33) - 1d) / 2d);
	//		if (d != 0d) {
	//			double den = 2d * Math.sin(d);
	//			Point3D p = new Point3D((A32 - A23) / den, (A13 - A31) / den, (A21 - A12) / den);
	//			n.setRotationAxis(p);
	//			n.setRotate(Math.toDegrees(d));
	//		}
	//	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z, float scale) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
		if ((modelRenderer.rotateAngleX == 0.0F) && (modelRenderer.rotateAngleY == 0.0F) && (modelRenderer.rotateAngleZ == 0.0F)) {
			if ((modelRenderer.rotationPointX != 0.0F) || (modelRenderer.rotationPointY != 0.0F) || (modelRenderer.rotationPointZ != 0.0F)) {
				GlStateManager.translate(modelRenderer.rotationPointX * scale, modelRenderer.rotationPointY * scale, modelRenderer.rotationPointZ * scale);
			}
		} else {
			GlStateManager.translate(modelRenderer.rotationPointX * scale, modelRenderer.rotationPointY * scale, modelRenderer.rotationPointZ * scale);

			if (modelRenderer.rotateAngleZ != 0.0F) {
				GlStateManager.rotate(modelRenderer.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
			}

			if (modelRenderer.rotateAngleY != 0.0F) {
				GlStateManager.rotate(modelRenderer.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
			}

			if (modelRenderer.rotateAngleX != 0.0F) {
				GlStateManager.rotate(modelRenderer.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
			}
		}
	}
}
