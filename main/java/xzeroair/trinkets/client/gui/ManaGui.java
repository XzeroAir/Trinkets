package xzeroair.trinkets.client.gui;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.manaCap.ManaStats;
import xzeroair.trinkets.util.Reference;

public class ManaGui extends Gui {

	public static ResourceLocation background = null;

	private final ResourceLocation waterTex = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana.png");

	private final Minecraft mc;
	private final Random rand = new Random();
	private float oldMouseX;
	private float oldMouseY;

	public ManaGui(Minecraft mc)
	{
		this.mc = mc;
	}

	public void renderManaGui(int x, int y, int tick, float mana) {
		background = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana.png");
		final EntityPlayer player = mc.player;
		final ManaStats thirst = Capabilities.getPlayerMana(player);
		if(thirst != null) {
			GlStateManager.color(1, 1, 1, 1);
			//			mc.getTextureManager().bindTexture(background);
			for (int l5 = 0; l5 < 10; ++l5)
			{
				final int j6 = y;
				int l6 = 0;//16;
				int j7 = 0;

				if (mc.player.isPotionActive(MobEffects.HUNGER))
				{
					l6 += 36;
					j7 = 13;
				}
				//				if ((saturation <= 0.0F) && ((tick % ((water * 3) + 1)) == 0))
				//				{
				//					j6 = y + (rand.nextInt(3) - 1);
				//				}
				final int l7 = x - (l5 * 8) - 9;
				//				drawTexturedModalRect(l7, j6, 0 + (j7 * 9), 0, 9, 10);

				if (((l5 * 2) + 1) < mana)
				{
					//					drawTexturedModalRect(l7, j6, l6 + 9, 0, 9, 10);
				}

				//				if (((l5 * 2) + 1) == water)
				//				{
				//					drawTexturedModalRect(l7, j6, l6 + 18, 0, 9, 10);
				//				}
				//				if (((l5 * 2) + 1) < saturation)
				//				{
				//					drawTexturedModalRect(l7, j6, l6 + 27, 0, 9, 10);
				//				}
				//
				//				if (((l5 * 2) + 1) == saturation)
				//				{
				//					drawTexturedModalRect(l7, j6, l6 + 36, 0, 9, 10);
				//				}
			}
		}
		//		this.drawTexturedModalRect(x, y, 0, 0, 9, 9);
	}

	private void TrinketDrawGui(int x, int y, int texX, int texY, int width, int height, float scale, float r, float g, float b, float a) {
		GlStateManager.pushMatrix();
		GlStateManager.color(r, g, b, a);
		final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//Bottom Left
		buffer.pos(x + 0, y + height, zLevel).tex((texX + 0) * scale, (texY + height) * scale).endVertex();
		//bottom right
		buffer.pos(x + width, y + height, zLevel).tex((texX + width) * scale, (texY + height) * scale).endVertex();
		//top right
		buffer.pos(x + width, y + 0, zLevel).tex((texX + width) * scale, (texY + 0) * scale).endVertex();
		//top left
		buffer.pos(x + 0, y + 0, zLevel).tex((texX + 0) * scale, (texY + 0) * scale).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();
	}

}
