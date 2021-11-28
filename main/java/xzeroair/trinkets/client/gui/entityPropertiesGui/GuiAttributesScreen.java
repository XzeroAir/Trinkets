package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

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
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

@SideOnly(Side.CLIENT)
public class GuiAttributesScreen extends GuiScreen {

	private enum SortType implements Comparator<IAbilityInterface> {
		NORMAL(24),
		A_TO_Z(25) {
			@Override
			protected int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		},
		Z_TO_A(26) {
			@Override
			protected int compare(String name1, String name2) {
				return name2.compareTo(name1);
			}
		};

		private int buttonID;

		private SortType(int buttonID) {
			this.buttonID = buttonID;
		}

		@Nullable
		public static SortType getTypeForButton(GuiButton button) {
			for (final SortType t : values()) {
				if (t.buttonID == button.id) {
					return t;
				}
			}
			return null;
		}

		protected int compare(String name1, String name2) {
			return 0;
		}

		@Override
		public int compare(IAbilityInterface o1, IAbilityInterface o2) {
			final String name1 = StringUtils.stripControlCodes(o1.getDisplayName()).toLowerCase();
			final String name2 = StringUtils.stripControlCodes(o2.getDisplayName()).toLowerCase();
			return this.compare(name1, name2);
		}
	}

	public EntityPlayer player;
	public EntityProperties properties;
	protected GuiPropertiesSlider r, g, b, a;
	public ColorHelper color;
	public int buttonPressed;

	public int redSlider = 5;
	public int greenSlider = 6;
	public int blueSlider = 7;
	public int alphaSlider = 8;

	protected boolean flip = false;

	public GuiAttributesScreen(EntityPlayer player) {
		this.player = player;
		color = new ColorHelper();
		properties = Capabilities.getEntityRace(player);
	}

	public static ResourceLocation background = null;
	private float oldMouseX;
	private float oldMouseY;

	private GuiAttributesScrollingList abilitySelectionList;

	//	private GuiScreen mainMenu;
	private GuiScrollingList abilityDescriptions, raceAttributes, raceBonuses;
	private int selected = -1;
	private IAbilityInterface selectedMod;
	private int listWidth;
	private ArrayList<IAbilityInterface> abilities;

	private int buttonMargin = 1;
	private int numButtons = SortType.values().length;

	private String lastFilterText = "";

	private boolean sorted = false;

	private SortType sortType = SortType.NORMAL;

	public void selectAbilityIndex(int index) {
		if (index == selected) {
			return;
		}
		selected = index;
		selectedMod = ((index >= 0) && (index <= abilities.size())) ? abilities.get(selected) : null;

		this.updateCache();
	}

	public boolean abilityIndexSelected(int index) {
		return index == selected;
	}

	@Override
	public void initGui() {
		buttonList.clear();
		super.initGui();

		this.addButton(new GuiPropertiesButton(1, 2, 2, 50, 20, "<--"));
		this.addButton(new GuiPropertiesButton(2, width - 16, 2, 14, 20, TextFormatting.RED + "X"));

		abilities = Lists.newArrayList(properties.getAbilityHandler().getAbilitiesList());
		final int slotHeight = 20;
		if ((abilities == null) || abilities.isEmpty()) {
			return;
		}
		for (final IAbilityInterface ability : abilities) {
			if (ability != null) {
				listWidth = Math.max(listWidth, this.getFontRenderer().getStringWidth(ability.getDisplayName()) + 10);
			}
		}
		listWidth = Math.min(listWidth, 150);
		//        this.modList = new GuiSlotModList(this, mods, listWidth, slotHeight);
		abilitySelectionList = new GuiAttributesScrollingList(0, this, abilities, listWidth, slotHeight);
		this.updateCache();
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
			this.displayNormalInventory();
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;//super.doesGuiPauseGame();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//TODO Rendering Might still be broken?
		oldMouseX = mouseX;
		oldMouseY = mouseY;
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (properties == null) {
			return;
		}

		if (abilitySelectionList != null) {
			abilitySelectionList.drawScreen(mouseX, mouseY, partialTicks);
		}
		if (abilityDescriptions != null) {
			abilityDescriptions.drawScreen(mouseX, mouseY, partialTicks);
		}
		if (raceAttributes != null) {
			raceAttributes.drawScreen(mouseX, mouseY, partialTicks);
		}
		if (raceBonuses != null) {
			raceBonuses.drawScreen(mouseX, mouseY, partialTicks);
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
		if (abilityDescriptions != null) {
			abilityDescriptions.handleMouseInput(mouseX, mouseY);
		}
		if (abilitySelectionList != null) {
			abilitySelectionList.handleMouseInput(mouseX, mouseY);
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
		abilityDescriptions = null;
		raceAttributes = null;
		final ResourceLocation logoPath2 = null;
		final Dimension logoDims2 = new Dimension(0, 0);
		final List<String> lines2 = new ArrayList<>();
		TranslationHelper.addAttributeTooltips(properties.getCurrentRace().getRaceAttributes(), lines2);
		raceAttributes = new Info(
				listWidth, lines2, logoPath2, logoDims2, 32,
				(GuiAttributesScreen.this.height - 18) + 4,
				(width - listWidth - 30)
		);
		if (selectedMod == null) {
			return;
		}

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
		String source = "?";
		try {
			source = properties.getAbilityHandler().getAbilitySource(selectedMod);
		} catch (final Exception e) {
		}
		lines.add(selectedMod.getDisplayName() + " - " + source);
		//			lines.add(String.format("Version: %s (%s)", selectedMod.getDisplayVersion(), selectedMod.getVersion()));
		//			lines.add(String.format("Mod ID: '%s' Mod State: %s", selectedMod.getModId(), Loader.instance().getModState(selectedMod)));

		lines.add(null);
		lines.addAll(selectedMod.getDescription());

		abilityDescriptions = new Info((width - listWidth - 30) - listWidth - 30, lines, logoPath, logoDims);
	}

	private class Info extends GuiScrollingList {
		@Nullable
		private ResourceLocation logoPath;
		private Dimension logoDims;
		private List<ITextComponent> lines = null;

		public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims, int top, int bottom, int left) {
			super(
					GuiAttributesScreen.this.getMinecraftInstance(),
					width,
					GuiAttributesScreen.this.height,
					top, bottom,
					left, 60,
					GuiAttributesScreen.this.width,
					GuiAttributesScreen.this.height
			);
			this.lines = this.resizeContent(lines);
			this.logoPath = logoPath;
			this.logoDims = logoDims;

			this.setHeaderInfo(true, this.getHeaderHeight());
		}

		public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims) {
			super(
					GuiAttributesScreen.this.getMinecraftInstance(),
					width,
					GuiAttributesScreen.this.height,
					32, (GuiAttributesScreen.this.height - 18) + 4,
					GuiAttributesScreen.this.listWidth + 20, 60,
					GuiAttributesScreen.this.width,
					GuiAttributesScreen.this.height
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
					ret.addAll(GuiUtilRenderComponents.splitText(chat, maxTextLength, GuiAttributesScreen.this.getFontRenderer(), false, true));
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
				GuiAttributesScreen.this.getMinecraftInstance().renderEngine.bindTexture(logoPath);
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
					GuiAttributesScreen.this.getFontRenderer().drawStringWithShadow(line.getFormattedText(), left + 4, top, 0xFFFFFF);
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
						GuiAttributesScreen.this.handleComponentClick(part);
						break;
					}
				}
			}
		}
	}

}
