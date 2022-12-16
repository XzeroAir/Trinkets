package xzeroair.trinkets.client.gui;

import java.io.IOException;

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
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class TrinketGui extends TrinketInventoryEffectRenderer {

	public static ResourceLocation background = null;

	private float oldMouseX;
	private float oldMouseY;

	public TrinketGui(EntityPlayer player) {
		super(new TrinketInventoryContainer(player.inventory, !player.getEntityWorld().isRemote, player));
		allowUserInput = true;
		//		color = new ColorHelper().setColor(TrinketsConfig.CLIENT.GUI.GuiColor);
	}

	private void resetGuiLeft() {
		guiLeft = (width - xSize) / 2;
	}

	protected void renderPotionIcons() {
		boolean hasVisibleEffect = false;
		for (final PotionEffect potioneffect : mc.player.getActivePotionEffects()) {
			final Potion potion = potioneffect.getPotion();
			if (potion.shouldRender(potioneffect)) {
				hasVisibleEffect = true;
				break;
			}
		}
		if (mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
			hasActivePotionEffects = false;
		} else {
			hasActivePotionEffects = TrinketsConfig.CLIENT.GUI.PotionIcons;
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
		buttonList.clear();
		super.initGui();
		this.resetGuiLeft();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the
	 * items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
		fontRenderer.drawString(I18n.format("container.crafting", new Object[0]), 97, 8, 4210752);
		if (TrinketsConfig.CLIENT.debug.showID) {
			int ID = 0;
			for (int i1 = 0; i1 < inventorySlots.inventorySlots.size(); ++i1) {
				final Slot slot = inventorySlots.inventorySlots.get(i1);
				if (slot instanceof TrinketSlot) {
					if (!slot.getHasStack()) {
						GlStateManager.pushMatrix();
						GlStateManager.color(1, 0, 0, 1);
						final int tXpos = (slot.xPos);
						final int tYpos = (slot.yPos);
						fontRenderer.drawString(I18n.format(TextFormatting.DARK_RED + String.valueOf(ID)), tXpos + 4, tYpos + 4, 0);
						GlStateManager.popMatrix();
					}
					ID++;
				} else {
					GlStateManager.pushMatrix();
					GlStateManager.color(1, 0, 0, 1);
					final int tXpos = (slot.xPos);
					final int tYpos = (slot.yPos);
					fontRenderer.drawString(I18n.format(TextFormatting.DARK_RED + String.valueOf(i1)), tXpos + 4, tYpos + 4, 0);
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
		oldMouseX = mouseX;
		oldMouseY = mouseY;
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
		GlStateManager.pushMatrix();
		background = new ResourceLocation(TrinketsConfig.CLIENT.GUI.GuiTex);
		//		background = new ResourceLocation(Reference.MODID + ":textures/gui/gui_inventory.png");

		final int k = guiLeft;
		final int l = guiTop;
		final int X = TrinketsConfig.CLIENT.GUI.X;
		final int Y = TrinketsConfig.CLIENT.GUI.Y;

		final int GuiSize = TrinketsConfig.CLIENT.GUI.guiTexSize;

		final int x = (k + X);
		final int y = (l + Y);

		final float[] rgb = ColorHelper.getRGBColor(TrinketsConfig.CLIENT.GUI.GuiColor);
		if (TrinketsConfig.CLIENT.GUI.Z == 0) {
			GlStateManager.color(
					rgb[0],
					rgb[1],
					rgb[2]
			);
			mc.getTextureManager().bindTexture(background);
			if (background.toString().contentEquals(Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png")) {
				this.renderTrinketInventory(x, y);
			} else {
				this.drawTexturedModalRect(x, y, 0, 0, GuiSize, GuiSize);
			}
		}

		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

		if (TrinketsConfig.CLIENT.GUI.Z == 1) {
			GlStateManager.color(
					rgb[0],
					rgb[1],
					rgb[2]
			);
			mc.getTextureManager().bindTexture(background);

			if (background.toString().contentEquals(Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png")) {
				this.renderTrinketInventory(x, y);
			} else {
				this.drawTexturedModalRect(x, y, 0, 0, GuiSize, GuiSize);
			}
		}
		GlStateManager.color(1, 1, 1, 1);
		DrawingHelper.drawEntityOnScreen(k + 51, l + 75, 30, false, -180, (k + 51) - oldMouseX, (l + 75) - 50 - oldMouseY, mc.player);
		GlStateManager.popMatrix();
	}

	@Override
	protected void actionPerformed(GuiButton button) {

	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		if (par2 == ModKeyBindings.TRINKET_GUI.getKeyCode()) {
			mc.player.closeScreen();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	public void displayNormalInventory() {
		final GuiInventory gui = new GuiInventory(mc.player);
		mc.displayGuiScreen(gui);
	}

	private void renderTrinketInventory(int x, int y) {
		final int slots = TrinketsConfig.SERVER.GUI.guiSlotsRows;
		final int columnLength = Math.min(8, slots);
		final int maxColumns = MathHelper.ceil(slots / 8.0);
		int X = x;
		int Y = y;
		int texX = 64;
		int texY = 32;
		int c = 0;
		int l = 0;
		for (int i = 0; i < slots; i++) {
			final boolean isFirstColumn = c == 0;
			final boolean isLastColumn = c == (maxColumns - 1);
			final boolean isFirstRow = (i == 0) || (l == 0);
			final boolean isLastRow = (i == (slots - 1)) || (l == (columnLength - 1));
			if (isFirstColumn && isLastColumn) {
				if ((l == 4) || (l == 7)) {
					this.drawTexturedModalRect((X - 5) - (c * 18), ((Y - 0) + (l * 18)), texX + 96, 8, 23, 8);
					Y += 4;
				}
				if (isFirstRow && isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 0, 32, 32);
				} else if (!isFirstRow && !isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 0, 32, 32);
				} else if (isFirstRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 64, 32, 32);
				} else if (isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 32, texY + 32, 32, 32);
				} else {

				}
			} else if (!isFirstColumn && !isLastColumn) {
				if ((l == 4) || (l == 7)) {
					this.drawTexturedModalRect((X - 5) - (c * 18), ((Y - 0) + (l * 18)), texX + 96, 8, 23, 8);
					Y += 4;
				}
				if (isFirstRow && isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 0, texY + 32, 32, 32);
				} else if (!isFirstRow && !isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 0, 32, 32);
				} else if (isFirstRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 64, texY + 64, 32, 32);
				} else if (isLastRow) {
					if (l == 7) {
						this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 32, 32, 32);
					} else {
						this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 32, texY + 32, 32, 32);
					}
				} else {

				}
			} else if (isFirstColumn) {
				if ((l == 4) || (l == 7)) {
					this.drawTexturedModalRect((X - 5) - (c * 18), ((Y - 0) + (l * 18)), texX + 96, 8, 23, 8);
					Y += 4;
				}
				if (isFirstRow && isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 0, 32, 32);
				} else if (!isFirstRow && !isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 0, 32, 32);
				} else if (isFirstRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 32, texY + 64, 32, 32);
				} else if (isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 32, texY + 32, 32, 32);
				} else {

				}
			} else if (isLastColumn) {
				if ((l == 4) || (l == 7)) {
					this.drawTexturedModalRect((X - 5) - (c * 18), ((Y - 0) + (l * 18)), texX + 96, 8, 23, 8);
					Y += 4;
				}
				if (isFirstRow && isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 0, texY + 32, 32, 32);
				} else if (!isFirstRow && !isLastRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 0, 32, 32);
				} else if (isFirstRow) {
					this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 4) + (l * 18)), texX + 64, texY + 64, 32, 32);
				} else if (isLastRow) {
					if (l == 7) {
						this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 64, texY + 32, 32, 32);
					} else {
						this.drawTexturedModalRect(((X - (32 - 18)) - 0) - (c * 18), ((Y - 0) + (l * 18)), texX + 32, texY + 32, 32, 32);
					}
				} else {

				}
			} else {

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
