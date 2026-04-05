package xzeroair.trinkets.client.gui.hud.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;

public class ManaGui extends Gui {

    private final ResourceLocation manaBar = new ResourceLocation(Reference.RESOURCE_PREFIX + "textures/gui/mana_bar.png");

    private final Minecraft mc;

    public ManaGui(Minecraft mc) {
        this.mc = mc;
    }

    public void renderManaGui(RenderGameOverlayEvent event, int x, int y, int tick, float mana, float maxMana, float cost) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        // Actual Size of the Bar
        final int barWidth = TrinketsConfig.CLIENT.MANA_BAR_HUD.width;
        final int barHeight = TrinketsConfig.CLIENT.MANA_BAR_HUD.height;

        // Texture Section Size
        final int texWidth = 0;
        final int texHeight = barHeight + (TrinketsConfig.CLIENT.MANA_BAR_HUD.rendTexID * barHeight);

        // UV Wrapped Tex Size
        final int texUVWidth = barWidth;
        final int texUVHeight = barHeight * 8;

        if (mana > maxMana) {
            mana = maxMana;
        }
        if (cost > mana) {
            cost = mana;
        }
        final int z = -90;
        final float test = ((((mana - cost) * 100) / maxMana) * 0.01F);
        final int currentManaWithCost = (int) (barWidth * test);//(int) ((cost * 100) / maxMana);

        mc.getTextureManager().bindTexture(manaBar);
        GlStateManager.translate(x, y, z);
        try {
            final int hs = mc.displayHeight;
            final int ws = mc.displayWidth;
            final int hsf = event.getResolution().getScaledHeight();
            final int wsf = event.getResolution().getScaledWidth();
            final int sF = event.getResolution().getScaleFactor();
            final float scaleH = ((hsf * 1F) / hs);
            final float scaleW = ((wsf * 1F) / ws);
            GlStateManager.scale(scaleW * sF, scaleH * sF, 0);
            //						GlStateManager.scale(0.5, 0.5, 0.5);
        } catch (final Exception ignored) {
        }
        if (!TrinketsConfig.CLIENT.MANA_BAR_HUD.mana_horizontal) {
            GlStateManager.rotate(-90, 0, 0, 1);
        }
        GlStateManager.translate(-x, -y, z);

        // Render Bar
        GlStateManager.color(1, 1, 1, 1);
//        boolean blending = GL11.glGetBoolean(GL11.GL_BLEND);
//        System.out.println(blending + "");
        if (TrinketsConfig.CLIENT.MANA_BAR_HUD.rendLocPre || Trinkets.MOD_COMPAT.InventoryHUD) {
            GlStateManager.enableBlend();
        }
        // Bar Background
        drawModalRectWithCustomSizedTexture(x, y, 0, 0, barWidth, barHeight, texUVWidth, texUVHeight);
        if (mana != 0) {
            // Mana Bar
            drawModalRectWithCustomSizedTexture(x, y, texWidth, texHeight, currentManaWithCost, barHeight, texUVWidth, texUVHeight);
        }
        if (TrinketsConfig.CLIENT.MANA_BAR_HUD.rendLocPre || Trinkets.MOD_COMPAT.InventoryHUD) {
            GlStateManager.disableBlend();
        }
        GlStateManager.color(1, 1, 1, 1);
        // Render Bar End

        // Render Text on Bar
        if (TrinketsConfig.CLIENT.MANA_BAR_HUD.SHOW_TEXT) {
            GlStateManager.pushMatrix();
            // TEXT is 7 high, 5 wide in pixels
            mana = (float) Math.round(mana * 100) / 100;
            maxMana = (float) Math.round(MathHelper.clamp(maxMana, 0, maxMana) * 100) / 100;
            final String txt = (int) mana + "/" + (int) maxMana;
            x += (barWidth / 2) - ((txt.length() * 6) / 2);
            x += 1;
            y += (barHeight / 2) - 4;
            x = fontRenderer.drawStringWithShadow(txt, x, y, 0xffffffff);
            GlStateManager.popMatrix();
        }
    }

}
