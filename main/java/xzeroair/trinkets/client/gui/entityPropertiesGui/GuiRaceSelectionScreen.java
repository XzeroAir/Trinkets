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
import net.minecraft.nbt.NBTTagCompound;
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
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.client.ConstantsTextureResourceLocation;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
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
public class GuiRaceSelectionScreen extends GuiScreen implements ITrinketGuiInterface {

    protected final EntityPlayer player;
    public EntityRace selectedRace;
    public Element selectedPrimaryElement;
    public Element selectedSecondaryElement;
    public int selectedGender;
    protected final EntityProperties properties;
    public int buttonPressed;
    public int BACK = 1;
    public int EXIT = 2;
    public int CONFIRM = 3;
    protected ArrayList<RaceCache> raceCaches;
    protected ArrayList<Element> elementCache;

    public GuiRaceSelectionScreen(EntityPlayer player) {
        this.player = player;
        this.properties = Capabilities.getEntityProperties(player);
    }

    public static ResourceLocation background = null;
    private float oldMouseX;
    private float oldMouseY;

    private GuiScrollingList raceSelection, elementSelection, secondaryElementSelection, raceDescriptions, elementDescriptions;
    private int raceSelected, primaryElementSelected, secondaryElementSelected = -1;
    private int raceListWidth;
    private int elementListWidth;

    private final int buttonMargin = 1;

    public void selectIndex(int index) {
        if (index == this.raceSelected) {
            return;
        }
        this.raceSelected = index;
        this.selectedRace = ((index >= 0) && (index <= this.raceCaches.size())) ? this.raceCaches.get(this.raceSelected).getRace() : null;
        this.updateCache();
    }

    public boolean indexSelected(int index) {
        return index == this.raceSelected;
    }

    public void selectPrimaryElement(int index) {
        if (index == this.primaryElementSelected) {
            return;
        }
        this.primaryElementSelected = index;
        this.selectedPrimaryElement = ((index >= 0) && (index <= this.elementCache.size())) ? this.elementCache.get(this.primaryElementSelected) : null;
        this.updateCache();
    }

    public boolean primaryElementSelected(int index) {
        return index == this.primaryElementSelected;
    }

    public void selectSecondaryElement(int index) {
        if (index == this.secondaryElementSelected) {
            return;
        }
        this.secondaryElementSelected = index;
        this.selectedSecondaryElement = ((index >= 0) && (index <= this.elementCache.size())) ? this.elementCache.get(this.secondaryElementSelected) : null;
        this.updateCache();
    }

    public boolean secondaryElementSelected(int index) {
        return index == this.secondaryElementSelected;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
//        raceCaches.clear();
//        elementCache.clear();
        super.initGui();

        this.raceSelected = -1;
        this.primaryElementSelected = -1;
        this.secondaryElementSelected = -1;
        this.raceListWidth = 0;
        this.elementListWidth = 0;
        String[] altSelectionConfig = TrinketsConfig.getClientStore().RACE_SELECTION_BLACKLIST;

        //		addButton(new GuiButton(buttonId, x, y, widthIn, heightIn, buttonText))
        if (!this.properties.isFirstLogin()) {
            this.addButton(new GuiPropertiesButton(this.BACK, 2, 2, 50, 20, "<--"));
        }
        this.addButton(new GuiPropertiesButton(this.EXIT, this.width - (14 + 40), 2, 40, 20, TextFormatting.RED + "X"));
        this.addButton(new GuiPropertiesButton(this.CONFIRM, this.width - (60 + 20), this.height - 32, 60, 20, TextFormatting.GREEN + "CONFIRM"));

        final ForgeRegistry<EntityRace> registryList = EntityRace.Registry;
        this.raceCaches = new ArrayList<>();
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
                    this.raceCaches.add(new RaceCache(entry));
                    if (this.raceListWidth <= txtLength) {
                        this.raceListWidth = txtLength;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        int fromTop = 32;
        int fromBottom = 60;

        this.raceListWidth = Math.min(this.raceListWidth + 12, 150);
        this.raceSelection = new RaceSelectionList(4, this, this.raceCaches, this.raceListWidth, this.height, fromTop, this.height - fromBottom, 10, 18);
//        this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight);
        final ForgeRegistry<Element> elements = Element.Registry;
        this.elementCache = new ArrayList<>();
        for (Element ele : elements.getValuesCollection()) {
            int txtLength = this.getFontRenderer().getStringWidth(ele.getDisplayName().trim());
            this.elementCache.add(ele);
            if (this.elementListWidth <= txtLength) {
                this.elementListWidth = txtLength;
            }
        }
        this.elementListWidth = Math.min(this.elementListWidth + 12, 150);
        this.elementSelection = new ElementSelectionList(5, this, this.elementCache, this.elementListWidth, this.height, fromTop, this.height - fromBottom, this.width - (160), 18, true);
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
        if (button.id == this.CONFIRM) {
            if (this.selectedRace != null) {
                RaceCache race = new RaceCache(this.selectedRace, this.selectedPrimaryElement == null ? Elements.NEUTRAL : this.selectedPrimaryElement);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("OriginalRace", race.saveToNBT(new NBTTagCompound()));
                this.properties.setOriginalRaceCache(race);
                this.properties.sendInformationToServer(tag);
                this.mc.player.closeScreen();
            }
        } else {
            RaceCache blank = new RaceCache();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("OriginalRace", blank.saveToNBT(new NBTTagCompound()));
            this.properties.setOriginalRaceCache(blank);
            this.properties.sendInformationToServer(tag);
            if (button.id == this.BACK) {
                this.mc.player.openGui(Trinkets.instance, Reference.GUI_ENTITY, this.mc.player.world, 0, 0, 0);
            }
            if (button.id == this.EXIT) {
                this.mc.player.closeScreen();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
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
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.properties == null) {
            return;
        }

        if (this.raceSelection != null) {
            this.raceSelection.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.elementSelection != null) {
            this.elementSelection.drawScreen(mouseX, mouseY, partialTicks);
        }

        if (this.raceDescriptions != null) {
            this.raceDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }
        if (this.elementDescriptions != null) {
            this.elementDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }

        int x = this.width - 60;//(width / 2);
        int y = this.height - 30;//(height / 2);
        if (this.selectedRace != null) {
            int pX = x - 16;
            int pY = y - 20;
            String name = this.selectedRace.getDisplayName();
            int txtLength = this.fontRenderer.getStringWidth(name);
            int distToAdd = txtLength % 2 == 0 ? 0 : 1;
            DrawingHelper.Draw(pX, pY, -100, 0, 0, 0, 0, (txtLength + distToAdd) + 10, 14, 0, 0, 0, 0, 0, 0.5F);
//            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, selectedRace.getPrimaryColor());
            this.fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, 0xFFAA00);
        }
        if (this.selectedPrimaryElement != null) {
            int pX = x - 16;
            int pY = y - 34;
            String name = this.selectedPrimaryElement.getDisplayName();
            int txtLength = this.fontRenderer.getStringWidth(name);
            int distToAdd = txtLength % 2 == 0 ? 0 : 1;
            DrawingHelper.Draw(pX, pY, -100, 0, 0, 0, 0, (txtLength + distToAdd) + 10, 14, 0, 0, 0, 0, 0, 0.5F);
//            fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, selectedPrimaryElement.getPrimaryColor());
            this.fontRenderer.drawStringWithShadow(name, pX + 6, pY + 3, 0xFFAA00);
            // font.drawString(font.trimStringToWidth(name, listWidth - 10), left + 3, top + 2, 0xFFFFFF);
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
        if (this.raceSelection != null) {
            this.raceSelection.handleMouseInput(mouseX, mouseY);
        }
        if (this.elementSelection != null) {
            this.elementSelection.handleMouseInput(mouseX, mouseY);
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

    private void updateCache() {
        this.raceDescriptions = null;
        this.elementDescriptions = null;
        if (this.selectedRace != null) {
            final ResourceLocation logoPath = ConstantsTextureResourceLocation.getPotionIconForRace(this.selectedRace, this.selectedPrimaryElement == null ? Elements.NEUTRAL : this.selectedPrimaryElement);
            final Dimension logoDims = new Dimension(18 * 2, 18 * 2);
            final List<String> lines = new ArrayList<>();
            this.selectedRace.getRaceHandler(this.player, this.properties, new RaceCache(this.selectedRace, this.selectedPrimaryElement)).getDescription(lines, EnumRenderLocation.ALWAYS.getId(), EnumRenderLocation.GUI.getId());
            this.raceDescriptions = new Info(this.width - (160 + (this.raceListWidth + 14)), lines, logoPath, logoDims, 32, (this.height - 60), this.raceListWidth + 12);
        }
    }

    private class Info extends GuiScrollingList {
        @Nullable
        private final ResourceLocation logoPath;
        private final Dimension logoDims;
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
                final int maxTextLength = this.listWidth - 8;
                if (maxTextLength >= 0) {
                    ret.addAll(GuiUtilRenderComponents.splitText(chat, maxTextLength, GuiRaceSelectionScreen.this.getFontRenderer(), false, true));
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
                GuiRaceSelectionScreen.this.getMinecraftInstance().renderEngine.bindTexture(this.logoPath);
                final BufferBuilder wr = tess.getBuffer();
                final int offset = (this.left + (this.listWidth / 2)) - (this.logoDims.width / 2);
                wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                wr.pos(offset, top + this.logoDims.height, GuiRaceSelectionScreen.this.zLevel).tex(0, 1).endVertex();
                wr.pos(offset + this.logoDims.width, top + this.logoDims.height, GuiRaceSelectionScreen.this.zLevel).tex(1, 1).endVertex();
                wr.pos(offset + this.logoDims.width, top, GuiRaceSelectionScreen.this.zLevel).tex(1, 0).endVertex();
                wr.pos(offset, top, GuiRaceSelectionScreen.this.zLevel).tex(0, 0).endVertex();
                tess.draw();
                GlStateManager.disableBlend();
                top += this.logoDims.height + 10;
            }

            for (final ITextComponent line : this.lines) {
                if (line != null) {
                    GlStateManager.enableBlend();
                    GuiRaceSelectionScreen.this.getFontRenderer().drawStringWithShadow(line.getFormattedText(), this.left + 4, top, 0xFFFFFF);
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
                    k += GuiRaceSelectionScreen.this.fontRenderer.getStringWidth(((TextComponentString) part).getText());
                    if (k >= x) {
                        GuiRaceSelectionScreen.this.handleComponentClick(part);
                        break;
                    }
                }
            }
        }
    }


    public class RaceSelectionList extends GuiScrollingList {

        private final GuiRaceSelectionScreen parent;
        private final ArrayList<RaceCache> cache;

        private final int id;

        public RaceSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<RaceCache> cache, int width, int height, int top, int bottom, int left, int slotHeight) {
            super(parent.mc, width, height, top, bottom, left, slotHeight, parent.width, parent.height);
            this.id = ID;
            this.parent = parent;
            this.cache = cache;
        }

        public RaceSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<RaceCache> cache, int listWidth, int slotHeight) {
            this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight);
        }

        @Override
        protected int getSize() {
            return this.cache.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            this.parent.selectIndex(index);
        }

        @Override
        protected boolean isSelected(int index) {
            return this.parent.indexSelected(index);
        }

        @Override
        protected void drawBackground() {
            //		parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return ((this.getSize()) * this.slotHeight) + 1;
        }

        ArrayList<RaceCache> getRaces() {
            return this.cache;
        }

        @Override
        protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
            if (idx >= this.cache.size()) return;
            final RaceCache race = this.cache.get(idx);
            final String name = race == null ? "ERROR" : TextFormatting.GOLD + race.getRace().getDisplayName();
            final FontRenderer font = this.parent.getFontRenderer();
            font.drawString(font.trimStringToWidth(name, this.listWidth - 10), this.left + 3, top + 2, 0xFFFFFF);
        }
    }

    public class ElementSelectionList extends GuiScrollingList {

        private final GuiRaceSelectionScreen parent;
        private final ArrayList<Element> cache;

        private final int id;
        private final boolean primary;

        public ElementSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<Element> cache, int width, int height, int top, int bottom, int left, int slotHeight, boolean primary) {
            super(parent.mc, width, height, top, bottom, left, slotHeight, parent.width, parent.height);
            this.id = ID;
            this.parent = parent;
            this.cache = cache;
            this.primary = primary;
        }

        public ElementSelectionList(int ID, GuiRaceSelectionScreen parent, ArrayList<Element> cache, int listWidth, int slotHeight, boolean primary) {
            this(ID, parent, cache, listWidth, parent.height, 32, (parent.height - 88) + 4, 10, slotHeight, primary);
        }

        @Override
        protected int getSize() {
            return this.cache.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            if (this.primary) {
                this.parent.selectPrimaryElement(index);
            } else {
                this.parent.selectSecondaryElement(index);
            }
        }

        @Override
        protected boolean isSelected(int index) {
            if (this.primary) {
                return this.parent.primaryElementSelected(index);
            } else {
                return this.parent.secondaryElementSelected(index);
            }
        }

        @Override
        protected void drawBackground() {
            //		parent.drawDefaultBackground();
        }

        @Override
        protected int getContentHeight() {
            return ((this.getSize()) * this.slotHeight) + 1;
        }

        ArrayList<Element> getElements() {
            return this.cache;
        }

        @Override
        protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
            if (idx >= this.cache.size()) return;
            final Element ele = this.cache.get(idx);
            final String name = ele == null ? "ERROR" : TextFormatting.GOLD + ele.getDisplayName();
            final FontRenderer font = this.parent.getFontRenderer();
            font.drawString(font.trimStringToWidth(name, this.listWidth - 10), this.left + 3, top + 2, 0xFFFFFF);
        }

    }

}
