package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.ForgeRegistry;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.TrinketsConfig;

@SideOnly(Side.CLIENT)
public class GuiRaceSelectionScreen extends GuiScreen {

	public EntityPlayer player;
	public EntityProperties properties;
	public int buttonPressed;

	protected boolean flip = false;

	public GuiRaceSelectionScreen(EntityPlayer player) {
		this.player = player;
		properties = Capabilities.getEntityProperties(player);
	}

	public static ResourceLocation background = null;
	private float oldMouseX;
	private float oldMouseY;

	//	private GuiAttributesScrollingList abilitySelectionList;

	//	private GuiScreen mainMenu;
	private GuiScrollingList raceSelection;//abilityDescriptions, raceAttributes, raceBonuses;
	private int selected = -1;
	//	private IAbilityInterface selectedMod;
	private int listWidth;

	private int buttonMargin = 1;

	public void selectAbilityIndex(int index) {
		if (index == selected) {
			return;
		}
		selected = index;
		//		selectedMod = ((index >= 0) && (index <= abilities.size())) ? abilities.get(selected) : null;

		this.updateCache();
	}

	public boolean abilityIndexSelected(int index) {
		return index == selected;
	}

	Map<Integer, EntityRace> buttonMap = new HashMap<>();

	@Override
	public void initGui() {
		buttonList.clear();
		buttonMap.clear();
		super.initGui();

		//		addButton(new GuiButton(buttonId, x, y, widthIn, heightIn, buttonText))
		//		this.addButton(new GuiPropertiesButton(1, 2, 2, 50, 20, "<--"));
		this.addButton(new GuiPropertiesButton(2, width - 16, 2, 14, 20, TextFormatting.RED + "X"));

		//		abilities = Lists.newArrayList(properties.getAbilityHandler().getAbilitiesList());
		final int slotHeight = 20;

		final ForgeRegistry<EntityRace> registryList = EntityRace.Registry;
		int ID = 3;
		int xPos = 40;
		int yPos = 40;
		for (final EntityRace r : registryList) {
			if (r.getUUID().compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0) {
				continue;
			}
			boolean forbiddon = false;
			for (final String blkList : TrinketsConfig.SERVER.races.selectionBlacklist) {
				if (r.getName().equalsIgnoreCase(blkList)) {
					forbiddon = true;
					break;
				}
			}
			if (forbiddon) {
				continue;
			}
			final int txtLength = this.getFontRenderer().getStringWidth(r.getName()) + 10;
			this.addButton(new GuiButton(ID, xPos, yPos, txtLength, 20, r.getName()));
			buttonMap.putIfAbsent(ID, r);
			ID++;
			if ((xPos + txtLength) >= width) {
				xPos = 10;
				yPos += 24;
			} else {
				xPos += txtLength;
			}
		}
		//		if ((abilities == null) || abilities.isEmpty()) {
		//			return;
		//		}
		//		for (final IAbilityInterface ability : abilities) {
		//			if (ability != null) {
		//				listWidth = Math.max(listWidth, this.getFontRenderer().getStringWidth(ability.getDisplayName()) + 10);
		//			}
		//		}
		//		listWidth = Math.min(listWidth, 150);
		//        this.modList = new GuiSlotModList(this, mods, listWidth, slotHeight);
		//		abilitySelectionList = new GuiAttributesScrollingList(0, this, abilities, listWidth, slotHeight);
		//		this.updateCache();
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		buttonPressed = 0;
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
		super.mouseClicked(x, y, button);
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the
	 * equivalent of KeyListener.keyTyped(KeyEvent e). Args : character (character
	 * on the key), keyCode (lwjgl Keyboard key code)
	 */
	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
		super.keyTyped(c, keyCode);
		if (keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
			player.closeScreen();
			this.displayNormalInventory();
		}
		//		search.textboxKeyTyped(c, keyCode);
	}

	public void displayNormalInventory() {
		final GuiInventory gui = new GuiInventory(mc.player);
		mc.displayGuiScreen(gui);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for
	 * buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		buttonPressed = button.id;
		super.actionPerformed(button);
		if (button.id == 1) {
			mc.player.openGui(Trinkets.instance, 2, mc.player.world, 0, 0, 0);
		}
		if (button.id == 2) {
			mc.player.closeScreen();
		}
		if (button.id > 2) {
			final EntityRace race = buttonMap.get(button.id);
			if (race != null) {
				//				properties.setOriginalRace(race);
				properties.setImbuedRace(race);
				properties.sendInformationToServer();
				mc.player.closeScreen();
			}
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		//		properties.sendInformationToServer();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return super.doesGuiPauseGame();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//TODO Rendering Might still be broken?
		oldMouseX = mouseX;
		oldMouseY = mouseY;
		//		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (properties == null) {
			return;
		}

		if (raceSelection != null) {
			raceSelection.drawScreen(mouseX, mouseY, partialTicks);
		}
	}

	/**
	 * Handles mouse input.
	 */
	@Override
	public void handleMouseInput() throws IOException {
		final int mouseX = (Mouse.getEventX() * width) / mc.displayWidth;
		final int mouseY = height - ((Mouse.getEventY() * height) / mc.displayHeight) - 1;

		super.handleMouseInput();
		if (raceSelection != null) {
			raceSelection.handleMouseInput(mouseX, mouseY);
		}
	}

	public int drawLine(String line, int offset, int shifty) {
		fontRenderer.drawString(line, offset, shifty, 0xd7edea);
		return shifty + 10;
	}

	public Minecraft getMinecraftInstance() {
		return mc;
	}

	public FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	private void updateCache() {
		final ResourceLocation logoPath2 = null;
		final Dimension logoDims2 = new Dimension(0, 0);
		//		final List<String> lines2 = new ArrayList<>();
		//		TranslationHelper.addAttributeTooltips(properties.getCurrentRace().getRaceAttributes(), lines2);
		//		raceAttributes = new Info(
		//				listWidth, lines2, logoPath2, logoDims2, 32,
		//				(GuiRaceSelectionScreen.this.height - 18) + 4,
		//				(width - listWidth - 30)
		//		);
		//		if (selectedMod == null) {
		//			return;
		//		}

		final ResourceLocation logoPath = null;
		final Dimension logoDims = new Dimension(0, 0);
		final List<String> lines = new ArrayList<>();

		//		String logoFile = selectedMod.getDisplayName();//getMetadata().logoFile;
		//		if (!logoFile.isEmpty()) {
		//			TextureManager tm = mc.getTextureManager();
		//			IResourcePack pack = FMLClientHandler.instance().getResourcePackFor(selectedMod.getModId());
		//			try {
		//				BufferedImage logo = null;
		//				if (pack != null) {
		//					logo = pack.getPackImage();
		//				} else {
		//					InputStream logoResource = this.getClass().getResourceAsStream(logoFile);
		//					if (logoResource != null) {
		//						logo = TextureUtil.readBufferedImage(logoResource);
		//					}
		//				}
		//				if (logo != null) {
		//					logoPath = tm.getDynamicTextureLocation("modlogo", new DynamicTexture(logo));
		//					logoDims = new Dimension(logo.getWidth(), logo.getHeight());
		//				}
		//			} catch (IOException e) {
		//			}
		//		}
		//		String source = "?";
		//		try {
		//			source = properties.getAbilityHandler().getAbilitySource(selectedMod);
		//		} catch (final Exception e) {
		//		}
		//		lines.add(selectedMod.getDisplayName() + " - " + source);
		//			lines.add(String.format("Version: %s (%s)", selectedMod.getDisplayVersion(), selectedMod.getVersion()));
		//			lines.add(String.format("Mod ID: '%s' Mod State: %s", selectedMod.getModId(), Loader.instance().getModState(selectedMod)));

		lines.add(null);
		for (int i = 0; i < 40; i++) {
			lines.add("This is a Test Line");
		}
		//		lines.addAll(selectedMod.getDescription());

		//		raceSelection = new Info((width - listWidth - 30) - listWidth - 30, lines, logoPath, logoDims);
	}

	private class Info extends GuiScrollingList {
		@Nullable
		private ResourceLocation logoPath;
		private Dimension logoDims;
		private List<ITextComponent> lines = null;

		public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims, int top, int bottom, int left) {
			super(
					GuiRaceSelectionScreen.this.getMinecraftInstance(),
					width,
					GuiRaceSelectionScreen.this.height,
					top, bottom,
					left, 60,
					GuiRaceSelectionScreen.this.width,
					GuiRaceSelectionScreen.this.height
			);
			this.lines = this.resizeContent(lines);
			this.logoPath = logoPath;
			this.logoDims = logoDims;

			this.setHeaderInfo(true, this.getHeaderHeight());
		}

		public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims) {
			super(
					GuiRaceSelectionScreen.this.getMinecraftInstance(),
					width,
					GuiRaceSelectionScreen.this.height,
					32, (GuiRaceSelectionScreen.this.height - 18) + 4,
					GuiRaceSelectionScreen.this.listWidth + 20, 60,
					GuiRaceSelectionScreen.this.width,
					GuiRaceSelectionScreen.this.height
			);
			this.lines = this.resizeContent(lines);
			this.logoPath = logoPath;
			this.logoDims = logoDims;

			this.setHeaderInfo(true, this.getHeaderHeight());
		}

		@Override
		protected int getSize() {
			return 0;
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
		}

		@Override
		protected boolean isSelected(int index) {
			return false;
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks) {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}

		private List<ITextComponent> resizeContent(List<String> lines) {
			final List<ITextComponent> ret = new ArrayList<>();
			for (final String line : lines) {
				if (line == null) {
					ret.add(null);
					continue;
				}

				final ITextComponent chat = ForgeHooks.newChatWithLinks(line, false);
				final int maxTextLength = listWidth - 8;
				if (maxTextLength >= 0) {
					ret.addAll(GuiUtilRenderComponents.splitText(chat, maxTextLength, GuiRaceSelectionScreen.this.getFontRenderer(), false, true));
				}
			}
			return ret;
		}

		private int getHeaderHeight() {
			int height = 0;
			if (logoPath != null) {
				final double scaleX = logoDims.width / 200.0;
				final double scaleY = logoDims.height / 65.0;
				double scale = 1.0;
				if ((scaleX > 1) || (scaleY > 1)) {
					scale = 1.0 / Math.max(scaleX, scaleY);
				}
				logoDims.width *= scale;
				logoDims.height *= scale;

				height += logoDims.height;
				height += 10;
			}
			height += (lines.size() * 10);
			if (height < (bottom - top - 8)) {
				height = bottom - top - 8;
			}
			return height;
		}

		@Override
		protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
			int top = relativeY;

			if (logoPath != null) {
				GlStateManager.enableBlend();
				GuiRaceSelectionScreen.this.getMinecraftInstance().renderEngine.bindTexture(logoPath);
				final BufferBuilder wr = tess.getBuffer();
				final int offset = (left + (listWidth / 2)) - (logoDims.width / 2);
				wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				wr.pos(offset, top + logoDims.height, zLevel).tex(0, 1).endVertex();
				wr.pos(offset + logoDims.width, top + logoDims.height, zLevel).tex(1, 1).endVertex();
				wr.pos(offset + logoDims.width, top, zLevel).tex(1, 0).endVertex();
				wr.pos(offset, top, zLevel).tex(0, 0).endVertex();
				tess.draw();
				GlStateManager.disableBlend();
				top += logoDims.height + 10;
			}

			for (final ITextComponent line : lines) {
				if (line != null) {
					GlStateManager.enableBlend();
					GuiRaceSelectionScreen.this.getFontRenderer().drawStringWithShadow(line.getFormattedText(), left + 4, top, 0xFFFFFF);
					GlStateManager.disableAlpha();
					GlStateManager.disableBlend();
				}
				top += 10;
			}
		}

		@Override
		protected void clickHeader(int x, int y) {
			int offset = y;
			if (logoPath != null) {
				offset -= logoDims.height + 10;
			}
			if (offset <= 0) {
				return;
			}

			final int lineIdx = offset / 10;
			if (lineIdx >= lines.size()) {
				return;
			}

			final ITextComponent line = lines.get(lineIdx);
			if (line != null) {
				int k = -4;
				for (final ITextComponent part : line) {
					if (!(part instanceof TextComponentString)) {
						continue;
					}
					k += fontRenderer.getStringWidth(((TextComponentString) part).getText());
					if (k >= x) {
						GuiRaceSelectionScreen.this.handleComponentClick(part);
						break;
					}
				}
			}
		}
	}

}
