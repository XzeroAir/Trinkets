package xzeroair.trinkets.client.Overlays;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.client.gui.ManaGui;

public class OverlayRenderer {

	public static OverlayRenderer instance = new OverlayRenderer();

	private final Minecraft mc = Minecraft.getMinecraft();
	private final ManaGui manaGui = new ManaGui(mc);

	private float mana = 0;
	private float playerMana = 0;

	private int updateCounter;

	public void setMana(float mana, float playerMana) {
		setMana(mana);
		setPlayerMana(playerMana);
	}

	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
		if(event.isCancelable() || (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)) {
			return;
		}
		++updateCounter;
		//		if(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() != ModItems.crafting.itemWand) {
		//			return;
		//		}

		GlStateManager.disableLighting();

		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		final int x = 200;
		final int y = 10;
		//
		//		x = fontRenderer.drawString("Mana ", x, y, 0xffffffff);
		//		x = fontRenderer.drawString("" + getMana(), x, y, 0xffff0000);
		//		x = fontRenderer.drawString(" Influence ", x, y, 0xffffffff);
		//		x = fontRenderer.drawString("" + getManaInfluence(), x, y, 0xffff0000);
		//		y += 10;
		//		x = 200;
		//		x = fontRenderer.drawString("Player ", x, y, 0xffffffff);
		//		x = fontRenderer.drawString("" + getPlayerMana(), x, y, 0xffff0000);
		//		y += 10;
		final EntityPlayerSP player = Minecraft.getMinecraft().player;
		//				final ThirstStats water = PlayerProperties.getPlayerThirst(player);
		//				if(water != null) {
		//		GlStateManager.pushMatrix();
		//		x = fontRenderer.drawString("Player ", x, y, 0xffffffff);
		//		x = fontRenderer.drawString("" + getWater(), x, y, 0xffffffff);
		//		y += 10;
		//		x = fontRenderer.drawString("" + getSaturation(), x, y, 0xffffffff);
		//		GlStateManager.resetColor();
		//		GlStateManager.popMatrix();
		//		Minecraft.getMinecraft().renderEngine.bindTexture(waterTex);
		//		final int k = water;
		final int h = event.getResolution().getScaledHeight();
		final int w = event.getResolution().getScaledWidth();
		final int l = (event.getResolution().getScaledWidth() / 2) - 91;
		final int i1 = (event.getResolution().getScaledWidth() / 2) + 91;
		final int j1 = event.getResolution().getScaledHeight() - 59;

		manaGui.renderManaGui(i1, j1, updateCounter, getMana());
	}

	private void DrawGui(int x, int y, int texX, int texY, int width, int height, float scale, float r, float g, float b, float a) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		//		GlStateManager.disableTexture2D();
		//		GlStateManager.color(r, g, b, a);
		final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//Bottom Left
		buffer.pos(x + 0, y + height, 0).tex((texX + 0) * scale, (texY + height) * scale).endVertex();
		//bottom right
		buffer.pos(x + width, y + height, 0).tex((texX + width) * scale, (texY + height) * scale).endVertex();
		//top right
		buffer.pos(x + width, y + 0, 0).tex((texX + width) * scale, (texY + 0) * scale).endVertex();
		//top leftrrr
		buffer.pos(x + 0, y + 0, 0).tex((texX + 0) * scale, (texY + 0) * scale).endVertex();
		Tessellator.getInstance().draw();
		//		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	private void setMana(float mana) {
		this.mana = mana;
	}

	private void setPlayerMana(float playerMana) {
		this.playerMana = playerMana;
	}

	private float getMana() {
		return mana;
	}

	private float getPlayerMana() {
		return playerMana;
	}

}
