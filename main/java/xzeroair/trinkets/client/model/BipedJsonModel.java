package xzeroair.trinkets.client.model;

import java.util.List;

import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BipedJsonModel extends ModelBiped {

	public boolean hasEffect;
	public int colorDecimal;
	public ItemCameraTransforms.TransformType tranformType;
	public ModelResourceLocation modelResourceLocation;
	private IItemColor itemColorer;

	public boolean equippedAsAccessory = false;

	public BipedJsonModel(ResourceLocation modelLocation) {
		this(modelLocation, TransformType.HEAD);
	}

	public BipedJsonModel(ResourceLocation modelLocation, TransformType type) {
		this(modelLocation, type, -1);
	}

	public BipedJsonModel(ResourceLocation modelLocation, TransformType type, int color) {
		this(modelLocation, "inventory", type, color, false);
	}

	public BipedJsonModel(ResourceLocation modelLocation, String variant, TransformType type, int color, boolean hasEffect) {
		textureWidth = 16;
		textureHeight = 16;
		this.hasEffect = hasEffect;
		colorDecimal = color;
		tranformType = type;
		modelResourceLocation = new ModelResourceLocation(modelLocation, variant);
		// TODO This Causes a Crash on the Server, Fix it
		itemColorer = new Colorer(colorDecimal);
		//		modelResourceLocation = new ModelResourceLocation(new ResourceLocation(Reference.MODID, "moon_rose"), "inventory");
	}

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

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float yaw, float pitch, float scale) {
		float sneak = (isSneak ? 0.2F : 0F);
		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
		TextureManager texManager = Minecraft.getMinecraft().getTextureManager();
		GlStateManager.pushMatrix();
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale, entity);
		GlStateManager.translate(0, sneak, 0);
		if (!equippedAsAccessory) {
			if (tranformType == TransformType.HEAD) {
				bipedHead.postRender(scale);
			} else {
				bipedBody.postRender(scale);
			}
		}
		try {
			GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			IBakedModel model = renderer.getItemModelMesher().getModelManager().getModel(modelResourceLocation);
			//			IBakedModel model = renderer.getItemModelMesher().getModelManager().getModel(
			//					new ModelResourceLocation(
			//							new ResourceLocation(Reference.MODID, "trait_tuarian"),
			//							"inventory"
			//					)
			//			);
			//			System.out.println(model == null);
			if (model != null) {
				model = model.getOverrides().handleItemState(model, ItemStack.EMPTY, (World) null, (EntityLivingBase) null);
				model.handlePerspective(tranformType);
				texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				texManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				GlStateManager.alphaFunc(516, 0.1F);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.pushMatrix();
				model = ForgeHooksClient.handleCameraTransforms(model, tranformType, false);
				GlStateManager.translate(-0.5F, -0.0F, -0.5F);

				if (model.isBuiltInRenderer()) {
				} else {
					this.renderModel(renderer, model, colorDecimal, ItemStack.EMPTY);
					if (hasEffect) {
						this.renderEffect(renderer, model);
					}
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
			e.printStackTrace();
		}
		GlStateManager.popMatrix();
	}

	private void renderModel(RenderItem renderer, IBakedModel model, int color) {
		this.renderModel(renderer, model, color, ItemStack.EMPTY);
	}

	private void renderModel(RenderItem renderer, IBakedModel model, int color, ItemStack stack) {
		//		if (net.minecraftforge.common.ForgeModContainer.allowEmissiveItems) {
		//			net.minecraftforge.client.ForgeHooksClient.renderLitItem(renderer, model, color, stack);
		//			return;
		//		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			this.renderQuads(renderer, bufferbuilder, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
		}

		this.renderQuads(renderer, bufferbuilder, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
		tessellator.draw();
	}

	public void renderQuads(RenderItem renderer, BufferBuilder buffer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = (color == -1);
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = itemColorer.colorMultiplier(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			} else {
				//				k = -1;
			}

			net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, bakedquad, k);
		}
	}

	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	private void renderEffect(RenderItem renderer, IBakedModel model) {
		//		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
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
		this.renderModel(renderer, model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = (Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		this.renderModel(renderer, model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		texManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	@SideOnly(Side.CLIENT)
	private static class Colorer implements IItemColor {
		int color;

		public Colorer(int color) {
			this.color = color;
		}

		@Override
		public int colorMultiplier(@Nonnull ItemStack stack, int tintIndex) {
			if (tintIndex == 1)
				return 0xFFFFFFFF;
			return color;
			//			FluidStack fluidStack = FluidUtil.getFluidContained(stack);
			//			if (fluidStack == null)
			//				return 0xFFFFFFFF;
			//			return fluidStack.getFluid().getColor(fluidStack);
		}
	}
}
