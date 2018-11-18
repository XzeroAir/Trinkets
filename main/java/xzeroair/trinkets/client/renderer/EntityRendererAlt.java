package xzeroair.trinkets.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityRendererAlt extends EntityRenderer
{
	private final Minecraft mc;
	private final IResourceManager resourceManager;

	private float offsetY = 3.0F/4;
	private EntityRenderer entityRenderer;

	public EntityRendererAlt(Minecraft mcIn, IResourceManager resourceManagerIn)
	{
		super(mcIn, resourceManagerIn);
		mc = mcIn;
		resourceManager = resourceManagerIn;
	}
	@Override
	public void updateRenderer()
	{
		super.updateRenderer();
	}
	@Override
	public void getMouseOver(float partialTicks)
	{
		if ((mc.player == null) || mc.player.isPlayerSleeping()){
			super.getMouseOver(partialTicks);
			return;
		}
		mc.player.posY -= offsetY;
		mc.player.prevPosY -= offsetY;
		mc.player.lastTickPosY -= offsetY;
		super.getMouseOver(partialTicks);
		mc.player.posY += offsetY;
		mc.player.prevPosY += offsetY;
		mc.player.lastTickPosY += offsetY;
	}
	@Override
	public void updateCameraAndRender(float partialTicks, long nanoTime)
	{
		if ((mc.player == null) || mc.player.isPlayerSleeping()){
			super.updateCameraAndRender(partialTicks, nanoTime);
			return;
		}

		mc.player.renderOffsetY -= offsetY;
		super.updateCameraAndRender(partialTicks, nanoTime);
		mc.player.renderOffsetY = 1.62F/4;
	}

	@Override
	public void renderWorld(float partialTicks, long finishTimeNano)
	{
		super.renderWorld(partialTicks, finishTimeNano);
		//				this.updateLightmap(partialTicks);
		//
		//				if (this.mc.getRenderViewEntity() == null)
		//				{
		//					this.mc.setRenderViewEntity(this.mc.player);
		//				}
		//
		//				this.getMouseOver(partialTicks);
		//				GlStateManager.enableDepth();
		//				GlStateManager.enableAlpha();
		//				GlStateManager.alphaFunc(516, 0.5F);
		//				this.mc.mcProfiler.startSection("center");
		//
		//				if (this.mc.gameSettings.anaglyph)
		//				{
		//					anaglyphField = 0;
		//					GlStateManager.colorMask(false, true, true, false);
		//					this.renderWorldPass(0, partialTicks, finishTimeNano);
		//					anaglyphField = 1;
		//					GlStateManager.colorMask(true, false, false, false);
		//					this.renderWorldPass(1, partialTicks, finishTimeNano);
		//					GlStateManager.colorMask(true, true, true, false);
		//				}
		//				else
		//				{
		//					this.renderWorldPass(2, partialTicks, finishTimeNano);
		//				}
		//
		//				this.mc.mcProfiler.endSection();
	}

	/**
	 * Setup orthogonal projection for rendering GUI screen overlays
	 */
	@Override
	public void setupOverlayRendering()
	{
		super.setupOverlayRendering();
		//		ScaledResolution scaledresolution = new ScaledResolution(this.mc);
		//		GlStateManager.clear(256);
		//		GlStateManager.matrixMode(5889);
		//		GlStateManager.loadIdentity();
		//		GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		//		GlStateManager.matrixMode(5888);
		//		GlStateManager.loadIdentity();
		//		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}
	@Override
	public void resetData()
	{
		super.resetData();
		//		this.itemActivationItem = null;
		//		this.mapItemRenderer.clearLoadedMaps();
	}

	public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		if (!isSneaking)
		{
			GlStateManager.disableDepth();
		}

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		int i = fontRendererIn.getStringWidth(str) / 2;
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();

		if (!isSneaking)
		{
			fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
			GlStateManager.enableDepth();
		}

		GlStateManager.depthMask(true);
		fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}