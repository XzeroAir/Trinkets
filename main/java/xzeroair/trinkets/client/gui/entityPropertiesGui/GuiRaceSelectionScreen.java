package xzeroair.trinkets.client.gui.entityPropertiesGui;

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
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties.RaceCache;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class GuiRaceSelectionScreen extends GuiScreen {

    public EntityPlayer player;
    public EntityRace selectedRace;
    public Element selectedPrimaryElement;
    public Element selectedSecondaryElement;
    public int selectedGender;
    public EntityProperties properties;
    public int buttonPressed;
    public int BACK = 1;
    public int EXIT = 2;
    public int CONFIRM = 3;
    protected ArrayList<RaceCache> raceCaches;
    protected ArrayList<Element> elementCache;

    public GuiRaceSelectionScreen(EntityPlayer player) {
        this.player = player;
        properties = Capabilities.getEntityProperties(player);
    }

    public static ResourceLocation background = null;
    private float oldMouseX;
    private float oldMouseY;

    private GuiScrollingList raceSelection, elementSelection, secondaryElementSelection, raceDescriptions, elementDescriptions;
    private int raceSelected, primaryElementSelected, secondaryElementSelected = -1;
    private int raceListWidth;
    private int elementListWidth;

    private int buttonMargin = 1;

    public void selectIndex(int index) {
        if (index == raceSelected) {
            return;
        }
        raceSelected = index;
        selectedRace = ((index >= 0) && (index <= raceCaches.size())) ? raceCaches.get(raceSelected).getRace() : null;
        this.updateCache();
    }

    public boolean indexSelected(int index) {
        return index == raceSelected;
    }

    public void selectPrimaryElement(int index) {
        if (index == primaryElementSelected) {
            return;
        }
        primaryElementSelected = index;
        selectedPrimaryElement = ((index >= 0) && (index <= elementCache.size())) ? elementCache.get(primaryElementSelected) : null;
        this.updateCache();
    }

    public boolean primaryElementSelected(int index) {
        return index == primaryElementSelected;
    }

    public void selectSecondaryElement(int index) {
        if (index == secondaryElementSelected) {
            return;
        }
        secondaryElementSelected = index;
        selectedSecondaryElement = ((index >= 0) && (index <= elementCache.size())) ? elementCache.get(secondaryElementSelected) : null;
        this.updateCache();
    }

    public boolean secondaryElementSelected(int index) {
        return index == secondaryElementSelected;
    }

    @Override
    public void initGui() {
        buttonList.clear();
//        raceCaches.clear();
//        elementCache.clear();
        super.initGui();

        raceSelected = -1;
        primaryElementSelected = -1;
        secondaryElementSelected = -1;
        raceListWidth = 0;
        elementListWidth = 0;
        String[] altSelectionConfig = TrinketsConfig.getClientStore().RACE_SELECTION_BLACKLIST;

        //		addButton(new GuiButton(buttonId, x, y, widthIn, heightIn, buttonText))
        this.addButton(new GuiPropertiesButton(BACK, 2, 2, 50, 20, "<--"));
        this.addButton(new GuiPropertiesButton(EXIT, width - (14 + 40), 2, 40, 20, TextFormatting.RED + "X"));
        this.addButton(new GuiPropertiesButton(CONFIRM, width - (60 + 20), height - 32, 60, 20, TextFormatting.GREEN + "CONFIRM"));

        final ForgeRegistry<EntityRace> registryList = EntityRace.Registry;
        raceCaches = new ArrayList<>();
        try {
            for (EntityRace entry : registryList.getValuesCollection()) {
                if (entry.getUUID().compareTo(UUID.fromString("00000000-0000-0000-0000-000000000000")) == 0) {
                    continue;
                }
                boolean forbid = false;
                for (final String blkList : altSelectionConfig) {
                    if ((entry.getName().equalsIgnoreCase(blkList)) || (entry.getRegistryName().toString().equalsIgnoreCase(blkList))) {
                        forbid = true;
                        break;
                    }
                }
                if (!forbid) {
                    int txtLength = this.getFontRenderer().getStringWidth(entry.getDisplayName().trim());
                    raceCaches.add(new RaceCache(entry));
                    if (raceListWidth <= txtLength) {
                        raceListWidth = txtLength;
                    }
                }
            }
        } catch (Exception e) {
        }
        int fromTop = 32;
        int fromBottom = 60;

        raceListWidth = Math.min(raceListWidth + 12, 150);
        raceSelection = new RaceSelectionList(4, this, raceCaches, raceListWidth, height, fromTop, height - fromBottom, 10, 18);
//        this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight);
        final ForgeRegistry<Element> elements = Element.Registry;
        elementCache = new ArrayList<>();
        for (Element ele : elements.getValuesCollection()) {
            int txtLength = this.getFontRenderer().getStringWidth(ele.getDisplayName().trim());
            elementCache.add(ele);
            if (elementListWidth <= txtLength) {
                elementListWidth = txtLength;
            }
        }
        elementListWidth = Math.min(elementListWidth + 12, 150);
        elementSelection = new ElementSelectionList(5, this, elementCache, elementListWidth, height, fromTop, height - fromBottom, width - (160), 18, true);
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
        if (button.id == BACK) {
            mc.player.openGui(Trinkets.instance, Reference.GUI_ENTITY, mc.player.world, 0, 0, 0);
        }
        if (button.id == EXIT) {
            mc.player.closeScreen();
        }
        if (button.id == CONFIRM) {
            if (selectedRace != null) {
                properties.setOriginalRace(new RaceCache(selectedRace, selectedPrimaryElement == null ? Elements.NEUTRAL : selectedPrimaryElement));
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
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (properties == null) {
            return;
        }

        if (raceSelection != null) {
            raceSelection.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (elementSelection != null) {
            elementSelection.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (raceDescriptions != null) {
            raceDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (elementDescriptions != null) {
            elementDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }

        int x = width - 60;//(width / 2);
        int y = height - 30;//(height / 2);
        if (selectedRace != null) {
            int pX = x - 16;
            int pY = y - 20;
            String name = selectedRace.getDisplayName();
            int txtLength = this.fontRenderer.getStringWidth(name);
            int distToAdd = txtLength % 2 == 0 ? 0 : 1;
            DrawingHelper.Draw(pX, pY, -100, 0, 0, 0, 0, (txtLength + distToAdd) + 10, 14, 0, 0, 0, 0, 0, 0.5F);
//            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, selectedRace.getPrimaryColor());
            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, 0xFFAA00);
        }
        if (selectedPrimaryElement != null) {
            int pX = x - 16;
            int pY = y - 34;
            String name = selectedPrimaryElement.getDisplayName();
            int txtLength = this.fontRenderer.getStringWidth(name);
            int distToAdd = txtLength % 2 == 0 ? 0 : 1;
            DrawingHelper.Draw(pX, pY, -100, 0, 0, 0, 0, (txtLength + distToAdd) + 10, 14, 0, 0, 0, 0, 0, 0.5F);
//            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, selectedPrimaryElement.getPrimaryColor());
            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, 0xFFAA00);
            // font.drawString(font.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF);
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
        if (elementSelection != null) {
            elementSelection.handleMouseInput(mouseX, mouseY);
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
        raceDescriptions = null;
        elementDescriptions = null;
        if (selectedRace != null) {
            final ResourceLocation logoPath = new ResourceLocation(Reference.MODID, "textures/potions/" + selectedRace.getName().toLowerCase() + ".png");
            final Dimension logoDims = new Dimension(18 * 2, 18 * 2);
            final List<String> lines = new ArrayList<>();
            selectedRace.getRaceHandler(player, selectedPrimaryElement == null ? Elements.NEUTRAL : selectedPrimaryElement).getDescription(lines, EnumRenderLocation.ALWAYS.getId(), EnumRenderLocation.GUI.getId());
            raceDescriptions = new Info(width - (160 + 56), lines, logoPath, logoDims, 32, (this.height - 60), raceListWidth + 12);
        }
    }

    private class Info extends GuiScrollingList {
        @Nullable
        private ResourceLocation logoPath;
        private Dimension logoDims;
        private List<ITextComponent> lines = null;

        public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims, int top, int bottom, int left) {
            super(GuiRaceSelectionScreen.this.getMinecraftInstance(), width, GuiRaceSelectionScreen.this.height, top, bottom, left, 60, GuiRaceSelectionScreen.this.width, GuiRaceSelectionScreen.this.height);
            this.lines = this.resizeContent(lines);
            this.logoPath = logoPath;
            this.logoDims = logoDims;

            this.setHeaderInfo(true, this.getHeaderHeight());
        }

        public Info(int width, List<String> lines, @Nullable ResourceLocation logoPath, Dimension logoDims) {
            super(GuiRaceSelectionScreen.this.getMinecraftInstance(), width, GuiRaceSelectionScreen.this.height, 32, (GuiRaceSelectionScreen.this.height - 18) + 4, GuiRaceSelectionScreen.this.raceListWidth + 20, 60, GuiRaceSelectionScreen.this.width, GuiRaceSelectionScreen.this.height);
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


    public class RaceSelectionList extends GuiScrollingList {

        private GuiRaceSelectionScreen parent;
        private ArrayList<RaceCache> cache;

        private int id;

        public RaceSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<RaceCache> cache, int width, int height, int top, int bottom, int left, int slotHeight) {
            super(parent.mc, width, height, top, bottom, left, slotHeight, parent.width, parent.height);
            id = ID;
            this.parent = parent;
            this.cache = cache;
        }

        public RaceSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<RaceCache> cache, int listWidth, int slotHeight) {
            this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight);
        }

        @Override
        protected int getSize() {
            return cache.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            parent.selectIndex(index);
        }

        @Override
        protected boolean isSelected(int index) {
            return parent.indexSelected(index);
        }

        @Override
        protected void drawBackground() {
            //		parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return ((this.getSize()) * slotHeight) + 1;
        }

        ArrayList<RaceCache> getRaces() {
            return cache;
        }

        @Override
        protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
            if (idx >= cache.size()) return;
            final RaceCache race = cache.get(idx);
            final String name = race == null ? "ERROR" : TextFormatting.GOLD + race.getRace().getDisplayName();
            final FontRenderer font = parent.getFontRenderer();
            font.drawString(font.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF);
        }
    }

    public class ElementSelectionList extends GuiScrollingList {

        private GuiRaceSelectionScreen parent;
        private ArrayList<Element> cache;

        private int id;
        private boolean primary;

        public ElementSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<Element> cache, int width, int height, int top, int bottom, int left, int slotHeight, boolean primary) {
            super(parent.mc, width, height, top, bottom, left, slotHeight, parent.width, parent.height);
            id = ID;
            this.parent = parent;
            this.cache = cache;
            this.primary = primary;
        }

        public ElementSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<Element> cache, int listWidth, int slotHeight, boolean primary) {
            this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight, primary);
        }

        @Override
        protected int getSize() {
            return cache.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            if (primary) {
                parent.selectPrimaryElement(index);
            } else {
                parent.selectSecondaryElement(index);
            }
        }

        @Override
        protected boolean isSelected(int index) {
            if (primary) {
                return parent.primaryElementSelected(index);
            } else {
                return parent.secondaryElementSelected(index);
            }
        }

        @Override
        protected void drawBackground() {
            //		parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return ((this.getSize()) * slotHeight) + 1;
        }

        ArrayList<Element> getElements() {
            return cache;
        }

        @Override
        protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
            if (idx >= cache.size()) return;
            final Element ele = cache.get(idx);
            final String name = ele == null ? "ERROR" : TextFormatting.GOLD + ele.getDisplayName();
            final FontRenderer font = parent.getFontRenderer();
            font.drawString(font.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF);
        }

    }

}
