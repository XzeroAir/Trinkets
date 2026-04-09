package xzeroair.trinkets.client.gui.entityPropertiesGui;

import com.google.common.collect.Lists;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiAttributesScreen extends GuiScreen implements ITrinketGuiInterface {

    private enum SortType implements Comparator<IAbilityInterface> {
        NORMAL(24), A_TO_Z(25) {
            @Override
            protected int compare(String name1, String name2) {
                return name1.compareTo(name2);
            }
        }, Z_TO_A(26) {
            @Override
            protected int compare(String name1, String name2) {
                return name2.compareTo(name1);
            }
        };

        private final int buttonID;

        SortType(int buttonID) {
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

    protected final EntityPlayer player;
    @Nullable
    protected final EntityProperties properties;
    protected GuiPropertiesSlider r, g, b, a;
    public int buttonPressed;

    public int redSlider = 5;
    public int greenSlider = 6;
    public int blueSlider = 7;
    public int alphaSlider = 8;

    protected boolean flip = false;

    public GuiAttributesScreen(EntityPlayer player) {
        this.player = player;
        this.properties = Capabilities.getEntityProperties(player);
    }

    public static ResourceLocation background = null;
    private float oldMouseX;
    private float oldMouseY;

    private GuiAttributesScrollingList abilitySelectionList;

    //	private GuiScreen mainMenu;
    private GuiScrollingList abilityDescriptions, raceAttributes, raceBonuses;
    private int selected = -1;
    @Nullable
    private AbilityHolder selectedAbility;
    private int listWidth;
    private ArrayList<AbilityHolder> abilities;

    private final int buttonMargin = 1;
    private final int numButtons = SortType.values().length;

    private final String lastFilterText = "";

    private final boolean sorted = false;

    private final SortType sortType = SortType.NORMAL;

    public void selectAbilityIndex(int index) {
        if (index == this.selected) {
            return;
        }
        this.selected = index;
        this.selectedAbility = ((index >= 0) && (index <= this.abilities.size())) ? this.abilities.get(this.selected) : null;

        this.updateCache();
    }

    public boolean abilityIndexSelected(int index) {
        return index == this.selected;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        this.addButton(new GuiPropertiesButton(1, 2, 2, 50, 20, "<--"));
        this.addButton(new GuiPropertiesButton(2, this.width - 16, 2, 14, 20, TextFormatting.RED + "X"));

        this.abilities = Lists.newArrayList(this.properties.getAbilityHandler().getActiveAbilities().values());
        final int slotHeight = 20;
        if ((this.abilities == null) || this.abilities.isEmpty()) {
            return;
        }
        for (final AbilityHolder ability : this.abilities) {
            String name = ability.getAbility().getDisplayName();
            this.listWidth = Math.max(this.listWidth, this.getFontRenderer().getStringWidth(name) + 10);
        }
        this.listWidth = Math.min(this.listWidth, 150);
        //        this.modList = new GuiSlotModList(this, mods, listWidth, slotHeight);
        this.abilitySelectionList = new GuiAttributesScrollingList(0, this, this.abilities, this.listWidth, slotHeight);
        this.updateCache();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.buttonPressed = 0;
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
            this.player.closeScreen();
            this.displayNormalInventory();
        }
        //		search.textboxKeyTyped(c, keyCode);
    }

    public void displayNormalInventory() {
        final GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
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
        this.buttonPressed = button.id;
        super.actionPerformed(button);
        if (button.id == 1) {
            this.mc.player.openGui(Trinkets.instance, Reference.GUI_ENTITY, this.mc.player.world, 0, 0, 0);
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
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.properties == null) {
            return;
        }

        if (this.abilitySelectionList != null) {
            this.abilitySelectionList.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.abilityDescriptions != null) {
            this.abilityDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.raceAttributes != null) {
            this.raceAttributes.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.raceBonuses != null) {
            this.raceBonuses.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput() throws IOException {
        final int mouseX = (Mouse.getEventX() * this.width) / this.mc.displayWidth;
        final int mouseY = this.height - ((Mouse.getEventY() * this.height) / this.mc.displayHeight) - 1;

        super.handleMouseInput();
        if (this.abilityDescriptions != null) {
            this.abilityDescriptions.handleMouseInput(mouseX, mouseY);
        }
        if (this.abilitySelectionList != null) {
            this.abilitySelectionList.handleMouseInput(mouseX, mouseY);
        }
    }

    public int drawLine(String line, int offset, int shifty) {
        this.fontRenderer.drawString(line, offset, shifty, 0xd7edea);
        return shifty + 10;
    }

    public Minecraft getMinecraftInstance() {
        return this.mc;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");

    private void updateCache() {
        this.abilityDescriptions = null;
        this.raceAttributes = null;
        final ResourceLocation logoPath2 = null;
        final Dimension logoDims2 = new Dimension(0, 0);
        final List<String> lines2 = new ArrayList<>();
        try {
            String[] attributeConfig = this.properties.getCurrentRaceCache().getRace().getRaceInformation().getAttributes();
            if (attributeConfig != null) {
                for (String entry : attributeConfig) {
                    AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                    if (attributeShell != null) {
                        final String name = attributeShell.getAttribute();
                        final double amount = attributeShell.getAmount();
                        final int operation = attributeShell.getOperation();
                        double d0 = amount;
                        double d1;
                        boolean flag = false;
                        if ((operation != 1) && (operation != 2)) {
                            d1 = d0;
                        } else {
                            d1 = d0 * 100.0D;
                        }
                        final TextComponentTranslation AttrName = new TextComponentTranslation("attribute.name." + name);
                        if (flag) {
                            final TextComponentTranslation never = new TextComponentTranslation("attribute.modifier.equals." + operation, DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                            lines2.add(" " + never.getFormattedText());
                        } else if (d0 > 0.0D) {
                            final TextComponentTranslation addition = new TextComponentTranslation("attribute.modifier.plus." + operation, DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                            addition.getStyle().setColor(TextFormatting.BLUE);
                            String s = addition.getFormattedText();
                            lines2.add(" " + s);
                        } else if (d0 < 0.0D) {
                            d1 = d1 * -1.0D;
                            final TextComponentTranslation subtraction = new TextComponentTranslation("attribute.modifier.take." + operation, DECIMALFORMAT.format(d1), AttrName.getFormattedText());
                            subtraction.getStyle().setColor(TextFormatting.RED);
                            String s = subtraction.getFormattedText();
                            lines2.add(" " + s);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        this.raceAttributes = new Info(this.listWidth + 40, lines2, logoPath2, logoDims2, 32, (GuiAttributesScreen.this.height - 18) + 4, (this.width - this.listWidth - 50));
        if (this.selectedAbility == null) {
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
        final String source = this.selectedAbility.getSourceID();
        IAbilityInterface abilityInstance = this.selectedAbility.getAbility();
        SlotInformation slotInfo = this.selectedAbility.getInfo();

        lines.add(TextFormatting.GOLD + abilityInstance.getDisplayName());
        lines.add(TextFormatting.AQUA + source);
        //						source = properties.getAbilityHandler().getAbilitySource(selectedMod);
        //		lines.add(selectedAbility. + " - " + source);
        lines.add(null);
        abilityInstance.getDescription(lines, EnumRenderLocation.ALWAYS.getId(), EnumRenderLocation.GUI.getId());
        //			lines.add(String.format("Version: %s (%s)", selectedMod.getDisplayVersion(), selectedMod.getVersion()));
        //			lines.add(String.format("Mod ID: '%s' Mod State: %s", selectedMod.getModId(), Loader.instance().getModState(selectedMod)));

        //		lines.addAll(selectedMod.getDescription());

        this.abilityDescriptions = new Info((this.width - this.listWidth - 50) - this.listWidth - 30, lines, logoPath, logoDims);
    }

    private class Info extends GuiScrollingList {
        @Nullable
        private final ResourceLocation logoPath;
        private final Dimension logoDims;
        private List<ITextComponent> lines = null;

        public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims, int top, int bottom, int left) {
            super(GuiAttributesScreen.this.getMinecraftInstance(), width, GuiAttributesScreen.this.height, top, bottom, left, 60, GuiAttributesScreen.this.width, GuiAttributesScreen.this.height);
            this.lines = this.resizeContent(lines);
            this.logoPath = logoPath;
            this.logoDims = logoDims;

            this.setHeaderInfo(true, this.getHeaderHeight());
        }

        public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims) {
            super(GuiAttributesScreen.this.getMinecraftInstance(), width, GuiAttributesScreen.this.height, 32, (GuiAttributesScreen.this.height - 18) + 4, GuiAttributesScreen.this.listWidth + 20, 60, GuiAttributesScreen.this.width, GuiAttributesScreen.this.height);
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
                final int maxTextLength = this.listWidth - 8;
                if (maxTextLength >= 0) {
                    ret.addAll(GuiUtilRenderComponents.splitText(chat, maxTextLength, GuiAttributesScreen.this.getFontRenderer(), false, true));
                }
            }
            return ret;
        }

        private int getHeaderHeight() {
            int height = 0;
            if (this.logoPath != null) {
                final double scaleX = this.logoDims.width / 200.0;
                final double scaleY = this.logoDims.height / 65.0;
                double scale = 1.0;
                if ((scaleX > 1) || (scaleY > 1)) {
                    scale = 1.0 / Math.max(scaleX, scaleY);
                }
                this.logoDims.width *= scale;
                this.logoDims.height *= scale;

                height += this.logoDims.height;
                height += 10;
            }
            height += (this.lines.size() * 10);
            if (height < (this.bottom - this.top - 8)) {
                height = this.bottom - this.top - 8;
            }
            return height;
        }

        @Override
        protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
            int top = relativeY;

            if (this.logoPath != null) {
                GlStateManager.enableBlend();
                GuiAttributesScreen.this.getMinecraftInstance().renderEngine.bindTexture(this.logoPath);
                final BufferBuilder wr = tess.getBuffer();
                final int offset = (this.left + (this.listWidth / 2)) - (this.logoDims.width / 2);
                wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                wr.pos(offset, top + this.logoDims.height, GuiAttributesScreen.this.zLevel).tex(0, 1).endVertex();
                wr.pos(offset + this.logoDims.width, top + this.logoDims.height, GuiAttributesScreen.this.zLevel).tex(1, 1).endVertex();
                wr.pos(offset + this.logoDims.width, top, GuiAttributesScreen.this.zLevel).tex(1, 0).endVertex();
                wr.pos(offset, top, GuiAttributesScreen.this.zLevel).tex(0, 0).endVertex();
                tess.draw();
                GlStateManager.disableBlend();
                top += this.logoDims.height + 10;
            }

            for (final ITextComponent line : this.lines) {
                if (line != null) {
                    GlStateManager.enableBlend();
                    GuiAttributesScreen.this.getFontRenderer().drawStringWithShadow(line.getFormattedText(), this.left + 4, top, 0xFFFFFF);
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                top += 10;
            }
        }

        @Override
        protected void clickHeader(int x, int y) {
            int offset = y;
            if (this.logoPath != null) {
                offset -= this.logoDims.height + 10;
            }
            if (offset <= 0) {
                return;
            }

            final int lineIdx = offset / 10;
            if (lineIdx >= this.lines.size()) {
                return;
            }

            final ITextComponent line = this.lines.get(lineIdx);
            if (line != null) {
                int k = -4;
                for (final ITextComponent part : line) {
                    if (!(part instanceof TextComponentString)) {
                        continue;
                    }
                    k += GuiAttributesScreen.this.fontRenderer.getStringWidth(((TextComponentString) part).getText());
                    if (k >= x) {
                        GuiAttributesScreen.this.handleComponentClick(part);
                        break;
                    }
                }
            }
        }
    }

    public static class GuiAttributesScrollingList extends GuiScrollingList {

        private final GuiAttributesScreen parent;
        private final ArrayList<AbilityHolder> abilities;

        private final int id;

        public GuiAttributesScrollingList(int ID, GuiAttributesScreen parent, ArrayList<AbilityHolder> abilities, int listWidth, int slotHeight) {
            super(parent.mc, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight, parent.width, parent.height);
            this.id = ID;
            this.parent = parent;
            this.abilities = abilities;
        }

        @Override
        protected int getSize() {
            return this.abilities.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            this.parent.selectAbilityIndex(index);
        }

        @Override
        protected boolean isSelected(int index) {
            return this.parent.abilityIndexSelected(index);
        }

        @Override
        protected void drawBackground() {
            //		parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return ((this.getSize()) * this.slotHeight) + 1;
        }

        ArrayList<AbilityHolder> getAbilities() {
            return this.abilities;
        }

        @Override
        protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
            if (idx >= this.abilities.size()) return;
            final AbilityHolder ability = this.abilities.get(idx);
            final String name = ability == null ? "ERROR" : ability.getAbility().getDisplayName();
            final FontRenderer font = this.parent.getFontRenderer();
            font.drawString(font.trimStringToWidth(name, this.listWidth - 10), this.left + 3, top + 2, 0xFFFFFF);
        }

    }

}
