package xzeroair.trinkets.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.container.TrinketInventoryContainer;
import xzeroair.trinkets.container.TrinketSlot;
import xzeroair.trinkets.util.ConstantsResourceLocations;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nonnull;
import java.io.IOException;

public class TrinketGui extends TrinketInventoryEffectRenderer {

    public static ResourceLocation background;

    private float oldMouseX;
    private float oldMouseY;
    protected final int slots;

    public TrinketGui(EntityPlayer player) {
        super(new TrinketInventoryContainer(player.inventory, !player.getEntityWorld().isRemote, player));
        this.allowUserInput = true;
        this.slots = TrinketsConfig.SERVER.GUI.SLOTS;
        background = new ResourceLocation(ConstantsResourceLocations.TrinketsGui);
    }

    private void resetGuiLeft() {
        this.guiLeft = (this.width - this.xSize) / 2;
    }

    protected void renderPotionIcons() {
        boolean hasVisibleEffect = false;
        for (final PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
            final Potion potion = potioneffect.getPotion();
            if (potion.shouldRender(potioneffect)) {
                hasVisibleEffect = true;
                break;
            }
        }
        if (this.mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
            this.hasActivePotionEffects = false;
        } else {
            this.hasActivePotionEffects = TrinketsConfig.CLIENT.GUI.ICONS_POTIONS;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        //		((TrinketInventoryContainer)inventorySlots).trinket.setEventBlock(false);
        this.renderPotionIcons();
        this.resetGuiLeft();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.resetGuiLeft();
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the
     * items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        this.fontRenderer.drawString(I18n.format("container.crafting"), 97, 8, 4210752);
        if (TrinketsConfig.CLIENT.debug.showID) {
            int ID = 0;
            for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
                final Slot slot = this.inventorySlots.inventorySlots.get(i1);
                if (slot instanceof TrinketSlot) {
                    if (!slot.getHasStack()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.color(1, 0, 0, 1);
                        final int tXpos = (slot.xPos);
                        final int tYpos = (slot.yPos);
                        this.fontRenderer.drawString(I18n.format(TextFormatting.DARK_RED + String.valueOf(ID)), tXpos + 4, tYpos + 4, 0);
                        GlStateManager.popMatrix();
                    }
                    ID++;
                } else {
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1, 0, 0, 1);
                    final int tXpos = (slot.xPos);
                    final int tYpos = (slot.yPos);
                    this.fontRenderer.drawString(I18n.format(TextFormatting.DARK_RED + String.valueOf(i1)), tXpos + 4, tYpos + 4, 0);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GlStateManager.pushMatrix();

        final int k = this.guiLeft;
        final int l = this.guiTop;
        final int X = TrinketsConfig.CLIENT.GUI.X;
        final int Y = TrinketsConfig.CLIENT.GUI.Y;

        final int GuiSize = TrinketsConfig.CLIENT.GUI.ATLAS_SIZE;

        final int x = (k + X);
        final int y = (l + Y);
        GlStateManager.color(1, 1, 1, 1);
//        final float[] rgb = ColorHelper.getRGBColor(TrinketsConfig.CLIENT.GUI.GUI_COLOR);
        if (TrinketsConfig.CLIENT.GUI.Z == 0) {
//            GlStateManager.color(rgb[0], rgb[1], rgb[2]);
            this.mc.getTextureManager().bindTexture(background);
            this.renderTrinketInventory(x, y);
        }

        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (TrinketsConfig.CLIENT.GUI.Z == 1) {
//            GlStateManager.color(rgb[0], rgb[1], rgb[2]);
            this.mc.getTextureManager().bindTexture(background);
            this.renderTrinketInventory(x, y);
        }
        GlStateManager.color(1, 1, 1, 1);
        DrawingHelper.drawEntityOnScreen(k + 51, l + 75, 30, false, -180, (k + 51) - this.oldMouseX, (l + 75) - 50 - this.oldMouseY, this.mc.player);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {

    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        if (par2 == ModKeyBindings.TRINKET_GUI.getKeyCode()) {
            this.mc.player.closeScreen();
        } else {
            super.keyTyped(par1, par2);
        }
    }

    public void displayNormalInventory() {
        final GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
    }

    private void renderTrinketInventory(int x, int y) {
        final int columnLength = Math.min(8, this.slots);
        final int maxColumns = MathHelper.ceil(this.slots / 8.0);
        int X = x;
        int Y = y;
        int texX = 64;
        int texY = 32;
        int c = 0;
        int l = 0;
        for (int i = 0; i < this.slots; i++) {
            final boolean isFirstColumn = c == 0;
            final boolean isLastColumn = c == (maxColumns - 1);
            final boolean isFirstRow = (i == 0) || (l == 0);
            final boolean isLastRow = (i == (this.slots - 1)) || (l == (columnLength - 1));
            if (isFirstColumn && isLastColumn) {
                if ((l == 4) || (l == 7)) {
                    this.drawTexturedModalRect((X - 5) - (c * 18), ((Y) + (l * 18)), texX + 96, 8, 23, 8);
                    Y += 4;
                }
                if (isFirstRow && isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY, 32, 32);
                } else if (!isFirstRow && !isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY, 32, 32);
                } else if (isFirstRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 64, 32, 32);
                } else if (isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 32, texY + 32, 32, 32);
                }
            } else if (!isFirstColumn && !isLastColumn) {
                if ((l == 4) || (l == 7)) {
                    this.drawTexturedModalRect((X - 5) - (c * 18), ((Y) + (l * 18)), texX + 96, 8, 23, 8);
                    Y += 4;
                }
                if (isFirstRow && isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX, texY + 32, 32, 32);
                } else if (!isFirstRow && !isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY, 32, 32);
                } else if (isFirstRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 64, texY + 64, 32, 32);
                } else if (isLastRow) {
                    if (l == 7) {
                        this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY + 32, 32, 32);
                    } else {
                        this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 32, texY + 32, 32, 32);
                    }
                }
            } else if (isFirstColumn) {
                if ((l == 4) || (l == 7)) {
                    this.drawTexturedModalRect((X - 5) - (c * 18), ((Y) + (l * 18)), texX + 96, 8, 23, 8);
                    Y += 4;
                }
                if (isFirstRow && isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY, 32, 32);
                } else if (!isFirstRow && !isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY, 32, 32);
                } else if (isFirstRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 64, 32, 32);
                } else if (isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 32, texY + 32, 32, 32);
                }
            } else if (isLastColumn) {
                if ((l == 4) || (l == 7)) {
                    this.drawTexturedModalRect((X - 5) - (c * 18), ((Y) + (l * 18)), texX + 96, 8, 23, 8);
                    Y += 4;
                }
                if (isFirstRow && isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX, texY + 32, 32, 32);
                } else if (!isFirstRow && !isLastRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY, 32, 32);
                } else if (isFirstRow) {
                    this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y - 4) + (l * 18)), texX + 64, texY + 64, 32, 32);
                } else if (isLastRow) {
                    if (l == 7) {
                        this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 64, texY + 32, 32, 32);
                    } else {
                        this.drawTexturedModalRect(((X - (32 - 18))) - (c * 18), ((Y) + (l * 18)), texX + 32, texY + 32, 32, 32);
                    }
                }
            }
            l++;
            if ((l % 8) == 0) {
                l = 0;
                c += 1;
                Y = y;
            }
        }
    }
}
