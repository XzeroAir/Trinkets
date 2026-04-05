package xzeroair.trinkets.client.gui;

import com.google.common.collect.Ordering;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import xzeroair.trinkets.util.TrinketsConfig;

import java.util.Collection;

public class TrinketInventoryEffectRenderer extends TrinketGuiContainer {

    protected boolean hasActivePotionEffects;
    protected final int slots;

    public TrinketInventoryEffectRenderer(Container container) {
        super(container);
        this.slots = TrinketsConfig.SERVER.GUI.SLOTS;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when
     * the GUI is displayed and when the window resizes, the buttonList is cleared
     * beforehand.
     */
    @Override
    public void initGui() {
        super.initGui();
        this.updateActivePotionEffects();
    }

    protected void updateActivePotionEffects() {
        boolean hasVisibleEffect = false;
        for (final PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
            final Potion potion = potioneffect.getPotion();
            if (potion.shouldRender(potioneffect)) {
                hasVisibleEffect = true;
                break;
            }
        }
        if (this.mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
            this.guiLeft = (this.width - this.xSize) / 2;
            this.hasActivePotionEffects = false;
        } else {
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent(this))) {
                this.guiLeft = (this.width - this.xSize) / 2;
            } else {
                this.guiLeft = 160 + ((this.width - this.xSize - 200) / 2);
            }
            this.hasActivePotionEffects = true;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.hasActivePotionEffects) {
            this.drawActivePotionEffects();
        }
    }

    /**
     * Display the potion effects list
     */
    private void drawActivePotionEffects() {
        final int xPos = TrinketsConfig.CLIENT.GUI.X;//MathHelper.clamp(38, 38, 39);
        final int yPos = TrinketsConfig.CLIENT.GUI.Y;//MathHelper.clamp(3, 3, 61);
        final int multi = MathHelper.ceil(this.slots / 8.0);
        final int offset = (18 * (multi));
        final int i = (((this.guiLeft - xPos) + 5) - 140) - (offset);//(guiLeft - (118 + (16 * TrinketsConfig.SERVER.GUI.guiSlotsRows))) + xPos;//TrinketsConfig.CLIENT.button.Xoffset;
        int j = this.guiTop;// + yPos;
        final int k = 166;
        final Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();

        if (!collection.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }

            for (final PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
                final Potion potion = potioneffect.getPotion();
                if (!potion.shouldRender(potioneffect)) {
                    continue;
                }
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
                this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

                if (potion.hasStatusIcon()) {
                    final int i1 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(i + 6, j + 7, ((i1 % 8) * 18), 198 + ((i1 / 8) * 18), 18, 18);
                }

                potion.renderInventoryEffect(potioneffect, this, i, j, this.zLevel);
                if (!potion.shouldRenderInvText(potioneffect)) {
                    j += l;
                    continue;
                }
                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1) {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                } else if (potioneffect.getAmplifier() == 2) {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                } else if (potioneffect.getAmplifier() == 3) {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                this.fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
                final String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                this.fontRenderer.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
                j += l;
            }
        }
    }
}
