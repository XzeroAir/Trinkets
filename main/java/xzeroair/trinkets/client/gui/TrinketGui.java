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

	private ColorHelper color;

	public TrinketGui(EntityPlayer player) {
		super(new TrinketInventoryContainer(player.inventory, !player.getEntityWorld().isRemote, player));
		allowUserInput = true;
		color = new ColorHelper().setColor(TrinketsConfig.CLIENT.GUI.GuiColor);
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
		if (TrinketsConfig.CLIENT.GUI.SLOT.showID) {
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

		final float gr = color.getRed();//gColor[0];
		final float gg = color.getGreen();//gColor[1];
		final float gb = color.getBlue();//gColor[2];
		final float ga = color.getAlpha();//gColor[3];

		final int k = guiLeft;
		final int l = guiTop;
		final int X = TrinketsConfig.CLIENT.GUI.X;//MathHelper.clamp(38, 38, 39);
		final int Y = TrinketsConfig.CLIENT.GUI.Y;//MathHelper.clamp(3, 3, 61);

		final int GuiSize = TrinketsConfig.CLIENT.GUI.guiTexSize;

		final int x = (k + X);
		final int y = (l + Y);

		if (TrinketsConfig.CLIENT.GUI.Z == 0) {
			GlStateManager.color(gr, gg, gb, ga);
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
			GlStateManager.color(gr, gg, gb, ga);
			mc.getTextureManager().bindTexture(background);

			if (background.toString().contentEquals(Reference.RESOURCE_PREFIX + "textures/gui/gui_inventory.png")) {
				this.renderTrinketInventory(x, y);
			} else {
				this.drawTexturedModalRect(x, y, 0, 0, GuiSize, GuiSize);
			}
		}
		GlStateManager.color(1, 1, 1, 1);
		DrawingHelper.drawEntityOnScreen(k + 51, l + 75, 30, (k + 51) - oldMouseX, (l + 75) - 50 - oldMouseY, mc.player);
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
		final int SX = 18;
		final int SY = 18;
		final int SZ = 8;
		int tx = 83;
		int ty = 3;
		int tsX = 18;
		int tsY = 18;
		int offsetX = 0;
		int offsetY = 0;
		final int slotRowAmount = TrinketsConfig.SERVER.GUI.guiSlotsRows;
		for (int r = 0; r < slotRowAmount; r++) {
			final int slotRowLength = r == 0 ? 8 : TrinketsConfig.SERVER.GUI.guiSlotsRowLength;
			for (int i = 0; i < slotRowLength; i++) {
				if (i == 0) {
					if (r == 0) {
						offsetX = 0;
						offsetY = 4;
						tx = 80;
						ty = 0;
						tsX = 3;
						tsY = 20;
						this.drawTexturedModalRect(((x - (r * SX)) - (offsetX + 3)), ((y + (i * SY)) - (offsetY - 2)), tx, ty, tsX, tsY);
						tx = 144;
						ty = 32;
						tsX = 18;
						tsY = 22;
					} else {
						if ((r == 1) && (i == (slotRowLength - 1))) {
							offsetX = 0;
							offsetY = 0;
							tx = 80;
							ty = 0;
							tsX = 21;
							tsY = 24;
							this.drawTexturedModalRect(((x - (r * SX)) - (offsetX + 3)), ((y + (i * SY)) - (offsetY + 3)), tx, ty, tsX, tsY);
							tx = 144;
							ty = 96;
							tsX = 18;
							tsY = 22;
						} else {
							if ((i == (slotRowLength - 1))) {
								offsetX = 3;
								offsetY = 3;
								tx = 80;
								ty = 0;
								tsX = 21;
								tsY = 24;
							} else {
								offsetX = 3;
								offsetY = 3;
								tx = 80;
								ty = 0;
								tsX = 21;
								tsY = 21;
							}
						}
					}
				}
				if ((i > 0) && (i < slotRowLength)) {
					tx = 80;
					ty = 3;
					tsX = 21;
					tsY = 18;
					offsetX = 3;
					offsetY = 0;
					if ((i > 3)) {
						offsetY = -4;
					} else {
						offsetY = 0;
					}
					if (i == 4) {
						this.drawTexturedModalRect(((x - (r * SX) - (offsetX - 1))), ((y + (i * SY)) - (offsetY + 4)), 160, 0, 22, 4);
					}
					if ((i < 7) && (i == (slotRowLength - 1))) {
						if (r == 1) {
							offsetX = 0;
							tx = 80;
							ty = 3;
							tsX = 3;
							tsY = 22;
							this.drawTexturedModalRect(((x - (r * SX)) - (offsetX + 3)), ((y + (i * SY)) - (offsetY)), tx, ty, tsX, tsY);
							tx = 144;
							ty = 96;
							tsX = 18;
							tsY = 22;
						} else {
							offsetX = 3;
							tx = 80;
							ty = 3;
							tsX = 21;
							tsY = 21;
						}
					}
					if (i == 7) {
						offsetX = 3;
						offsetY = -8;
						this.drawTexturedModalRect(((x - (r * SX) - (offsetX - 1))), ((y + (i * SY)) - (offsetY + 4)), 160, 0, 20, 4);
						if (r == 0) {
							offsetX = 0;
							offsetY = -8;
							tx = 80;
							ty = 3;
							tsX = 3;
							tsY = 22;
							this.drawTexturedModalRect(((x - (r * SX)) - (offsetX + 3)), ((y + (i * SY)) - (offsetY - 0)), tx, ty, tsX, tsY);
							tx = 144;
							ty = 96;
							tsX = 18;
							tsY = 22;
						} else {
							tx = 80;
							ty = 3;
							tsX = 21;
							tsY = 21;
						}
					}
				}
				this.drawTexturedModalRect(x - (r * SX) - offsetX, (y + (i * SY)) - offsetY, tx, ty, tsX, tsY);
			}
		}
	}

}
