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
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
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
import xzeroair.trinkets.client.ConstantsTextureResourceLocation;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
import xzeroair.trinkets.enums.EnumRenderLocation;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.ConstantsConfigLang;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.helpers.PotionHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiAttributesScreen extends GuiScreen implements ITrinketGuiInterface {


    protected final EntityPlayer player;
    @Nullable
    protected final EntityProperties properties;
    public int buttonPressed;

    public GuiAttributesScreen(EntityPlayer player) {
        this.player = player;
        this.properties = Capabilities.getEntityProperties(player);
    }

    private float oldMouseX;
    private float oldMouseY;

    private GuiAttributesScrollingList abilitySelectionList;

    private GuiScrollingList overviewInfo, abilityDescriptions, raceAttributes, raceProperties, skillTreeInfo, loreInfo;
    @Nullable
    private ResourceLocation overviewIcon;
    private String overviewTitle = "";
    private int selected = -1;
    @Nullable
    private AbilityHolder selectedAbility;
    private int listWidth;
    private ArrayList<AbilityHolder> abilities;

    private static final int BACK = 1;
    private static final int EXIT = 2;
    private static final int OVERVIEW = 3;
    private static final int STATS = 4;
    private static final int ABILITIES = 5;
    private static final int SKILLS = 6;
    private static final int LORE = 7;
    private int selectedTab = OVERVIEW;

    public void selectAbilityIndex(int index) {
        if (index == this.selected) {
            return;
        }
        this.selected = index;
        this.selectedAbility = ((index >= 0) && (index < this.abilities.size())) ? this.abilities.get(this.selected) : null;

        this.updateCache();
    }

    public boolean abilityIndexSelected(int index) {
        return index == this.selected;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        this.listWidth = 0;
        this.selected = -1;
        this.selectedAbility = null;

        this.addButton(new GuiPropertiesButton(BACK, 2, 2, 50, 20, "<--"));
        this.addButton(new GuiPropertiesButton(EXIT, this.width - 16, 2, 14, 20, TextFormatting.RED + "X"));
        this.addButton(new GuiPropertiesButton(OVERVIEW, 10, 32, 54, 20, I18n.format("gui.xat.race.selection.overview")));
        this.addButton(new GuiPropertiesButton(STATS, 66, 32, 42, 20, I18n.format("gui.xat.race.selection.stats")));
        this.addButton(new GuiPropertiesButton(ABILITIES, 110, 32, 54, 20, I18n.format(ConstantsConfigLang.CONFIG_ABILITIES)));
        this.addButton(new GuiPropertiesButton(SKILLS, 166, 32, 42, 20, I18n.format("gui.xat.profile.skills")));
        this.addButton(new GuiPropertiesButton(LORE, 210, 32, 42, 20, I18n.format("gui.xat.profile.lore")));

        this.abilities = Lists.newArrayList(this.properties.getAbilityHandler().getActiveAbilities().values());
        final int slotHeight = 20;
        for (final AbilityHolder ability : this.abilities) {
            String name = ability.getAbility().getDisplayName();
            this.listWidth = Math.max(this.listWidth, this.getFontRenderer().getStringWidth(name) + 10);
        }
        this.listWidth = Math.min(this.listWidth, 150);
        this.abilitySelectionList = new GuiAttributesScrollingList(0, this, this.abilities, this.listWidth, slotHeight);
        if (!this.abilities.isEmpty()) {
            this.selected = 0;
            this.selectedAbility = this.abilities.get(0);
        }
        this.updateTabButtons();
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
        if (button.id == BACK) {
            this.mc.player.openGui(Trinkets.instance, Reference.GUI_ENTITY, this.mc.player.world, 0, 0, 0);
        }
        if (button.id == EXIT) {
            this.displayNormalInventory();
        }
        if ((button.id == OVERVIEW) || (button.id == STATS) || (button.id == ABILITIES) || (button.id == SKILLS) || (button.id == LORE)) {
            this.selectedTab = button.id;
            this.updateTabButtons();
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

        if ((this.selectedTab == OVERVIEW) && (this.overviewInfo != null)) {
            this.overviewInfo.drawScreen(mouseX, mouseY, partialTicks);
            this.drawOverviewIcon();
        }
        if ((this.selectedTab == ABILITIES) && (this.abilitySelectionList != null)) {
            this.abilitySelectionList.drawScreen(mouseX, mouseY, partialTicks);
        }
        if ((this.selectedTab == ABILITIES) && (this.abilityDescriptions != null)) {
            this.abilityDescriptions.drawScreen(mouseX, mouseY, partialTicks);
        }
        if ((this.selectedTab == STATS) && (this.raceAttributes != null)) {
            this.raceAttributes.drawScreen(mouseX, mouseY, partialTicks);
        }
        if ((this.selectedTab == STATS) && (this.raceProperties != null)) {
            this.raceProperties.drawScreen(mouseX, mouseY, partialTicks);
        }
        if ((this.selectedTab == SKILLS) && (this.skillTreeInfo != null)) {
            this.skillTreeInfo.drawScreen(mouseX, mouseY, partialTicks);
        }
        if ((this.selectedTab == LORE) && (this.loreInfo != null)) {
            this.loreInfo.drawScreen(mouseX, mouseY, partialTicks);
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
        if ((this.selectedTab == ABILITIES) && (this.abilityDescriptions != null)) {
            this.abilityDescriptions.handleMouseInput(mouseX, mouseY);
        }
        if ((this.selectedTab == ABILITIES) && (this.abilitySelectionList != null)) {
            this.abilitySelectionList.handleMouseInput(mouseX, mouseY);
        }
        if ((this.selectedTab == STATS) && (this.raceAttributes != null)) {
            this.raceAttributes.handleMouseInput(mouseX, mouseY);
        }
        if ((this.selectedTab == STATS) && (this.raceProperties != null)) {
            this.raceProperties.handleMouseInput(mouseX, mouseY);
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
        this.overviewInfo = null;
        this.abilityDescriptions = null;
        this.raceAttributes = null;
        this.raceProperties = null;
        this.skillTreeInfo = null;
        this.loreInfo = null;
        this.overviewIcon = null;
        this.overviewTitle = "";
        final ResourceLocation logoPath2 = null;
        final Dimension logoDims2 = new Dimension(0, 0);
        final EntityRacePropertiesHandler raceHandler = this.properties.getRaceHandler();
        final List<String> overviewLines = new ArrayList<>();
        this.overviewTitle = TextFormatting.GOLD + raceHandler.getDisplayName();
        this.overviewIcon = ConstantsTextureResourceLocation.getPotionIconForRace(raceHandler.getRace(), raceHandler.getRaceCache()
                .getPrimaryElement());
        overviewLines.add(this.overviewTitle);
        overviewLines.add(null);
        if (!raceHandler.getRaceCache().comparePrimaryElement(Elements.NEUTRAL)) {
            overviewLines.add(I18n.format("gui.xat.profile.element") + ": " + raceHandler.getRaceCache()
                    .getPrimaryElement()
                    .getDisplayName());
        }
        overviewLines.add(I18n.format("gui.xat.race.gender") + ": " + this.getGenderName(raceHandler.getGender()));
        overviewLines.add(null);
        raceHandler.getDescription(overviewLines, EnumRenderLocation.ALWAYS.getId(), EnumRenderLocation.GUI.getId());
        this.overviewInfo = new Info(this.width - 20, overviewLines, logoPath2, logoDims2, 56, this.height - 60, 10);
        final List<String> lines2 = new ArrayList<>();
        lines2.add(TextFormatting.GOLD + I18n.format(ConstantsConfigLang.CONFIG_ATTRIBUTES));
        lines2.add(null);
        try {
            final String[] attributeConfig = raceHandler.getAttributes();
            if (attributeConfig != null) {
                for (final String entry : attributeConfig) {
                    final AttributeEntry attribute = ConfigHelper.getAttributeEntry(entry);
                    if (attribute != null) {
                        lines2.add(this.formatAttribute(attribute));
                    }
                }
            }
        } catch (Exception ignored) {
        }
        final List<String> propertyLines = new ArrayList<>();
        this.addDamageRules(propertyLines, raceHandler.getActiveDamageTypesToIgnore());
        this.addPotionEffects(propertyLines, ConstantsConfigLang.CONFIG_EFFECTS, raceHandler.getActiveEffectsToAdd());
        this.addPotionEffects(propertyLines, ConstantsConfigLang.CONFIG_RESISTANCES, raceHandler.getActiveEffectsToRemove());
        if (propertyLines.isEmpty()) {
            propertyLines.add(TextFormatting.GRAY + I18n.format("gui.xat.profile.stats.empty"));
        }
        final int statsColumnGap = 6;
        final int statsColumnWidth = (this.width - 20 - statsColumnGap) / 2;
        this.raceAttributes = new Info(statsColumnWidth, lines2, logoPath2, logoDims2, 56, this.height - 60, 10);
        this.raceProperties = new Info(statsColumnWidth, propertyLines, logoPath2, logoDims2, 56, this.height - 60, 10 + statsColumnWidth + statsColumnGap);
        final List<String> skillLines = new ArrayList<>();
        skillLines.add(TextFormatting.GOLD + I18n.format("gui.xat.profile.skills"));
        skillLines.add(null);
        skillLines.add(TextFormatting.GRAY + I18n.format("gui.xat.profile.skills.unavailable"));
        this.skillTreeInfo = new Info(this.width - 20, skillLines, null, new Dimension(0, 0), 56, this.height - 60, 10);
        final List<String> loreLines = new ArrayList<>();
        loreLines.add(TextFormatting.GOLD + I18n.format("gui.xat.profile.lore"));
        loreLines.add(null);
        loreLines.add(TextFormatting.GRAY + I18n.format("gui.xat.profile.lore.unavailable"));
        this.loreInfo = new Info(this.width - 20, loreLines, null, new Dimension(0, 0), 56, this.height - 60, 10);
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

        this.abilityDescriptions = new Info(this.width - this.listWidth - 30, lines, logoPath, logoDims, 56, this.height - 60, this.listWidth + 20);
    }

    private String formatAttribute(AttributeEntry attribute) {
        final String translationKey = "attribute.name." + attribute.getAttribute();
        String name = I18n.hasKey(translationKey) ? I18n.format(translationKey) : attribute.getAttribute();
        if (!I18n.hasKey(translationKey)) {
            final int namespace = name.lastIndexOf('.');
            if (namespace >= 0) {
                name = name.substring(namespace + 1);
            }
            name = name.replaceAll("([a-z])([A-Z])", "$1 $2");
            if (!name.isEmpty()) {
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            }
        }
        final TextFormatting amountColor = attribute.getAmount() >= 0 ? TextFormatting.GREEN : TextFormatting.RED;
        return TextFormatting.GRAY + name + TextFormatting.WHITE + ": " + amountColor + this.formatAttributeAmount(attribute.getAmount(), attribute.getOperation()) + TextFormatting.DARK_GRAY + " (OP:" + attribute.getOperation() + ")";
    }

    private String formatAttributeAmount(double amount, int operation) {
        final BigDecimal value = BigDecimal.valueOf(amount);
        final BigDecimal displayValue = operation == 0 ? value : value.movePointRight(2);
        final String sign = displayValue.signum() >= 0 ? "+" : "";
        return sign + displayValue.stripTrailingZeros().toPlainString() + (operation == 0 ? "" : "%");
    }

    private void addDamageRules(List<String> lines, String[] rules) {
        if ((rules == null) || (rules.length == 0)) {
            return;
        }
        lines.add(null);
        lines.add(TextFormatting.GOLD + I18n.format(ConstantsConfigLang.CONFIG_DAMAGE_TYPES));
        for (String rule : rules) {
            final String formattedRule = this.formatDamageRule(rule);
            if (!formattedRule.isEmpty()) {
                lines.add(formattedRule);
            }
        }
    }

    private void addPotionEffects(List<String> lines, String headingKey, String[] effects) {
        if ((effects == null) || (effects.length == 0)) {
            return;
        }
        lines.add(null);
        lines.add(TextFormatting.GOLD + I18n.format(headingKey));
        for (String effect : effects) {
            final String formattedEffect = this.formatPotionEffect(effect);
            if (!formattedEffect.isEmpty()) {
                lines.add(TextFormatting.GRAY + formattedEffect);
            }
        }
    }

    private String formatPotionEffect(String effectConfig) {
        final Potion potion = PotionHelper.getPotionFromConfig(effectConfig);
        if (potion == null) {
            return ConfigHelper.cleanConfigEntry(effectConfig);
        }
        final String translationKey = potion.getName();
        return I18n.hasKey(translationKey) ? I18n.format(translationKey) : potion.getRegistryName().toString();
    }

    private String formatDamageRule(String ruleConfig) {
        final String rule = ConfigHelper.cleanConfigEntry(ruleConfig);
        if (rule.isEmpty()) {
            return "";
        }
        final String[] parts = rule.replaceFirst("^on(?:Attacked|Hurt|Damaged):", "").split(";");
        if (parts.length < 2) {
            return TextFormatting.GRAY + rule;
        }
        try {
            final float multiplier = Float.parseFloat(parts[parts.length - 1]);
            final int difference = Math.round(Math.abs(1F - multiplier) * 100F);
            final String condition = this.formatDamageCondition(parts);
            if (multiplier <= 1F) {
                return TextFormatting.GRAY + I18n.format("gui.xat.profile.stats.damage.reduced", condition, difference);
            }
            return TextFormatting.GRAY + I18n.format("gui.xat.profile.stats.damage.increased", condition, difference);
        } catch (NumberFormatException ignored) {
            return TextFormatting.GRAY + rule;
        }
    }

    private String formatDamageCondition(String[] parts) {
        if (parts.length < 2) {
            return I18n.format("gui.xat.profile.stats.damage.all");
        }
        final String condition = parts[1];
        if ("isMagic".equals(condition)) {
            return I18n.format("xat.tooltip.magic");
        }
        if ("isFire".equals(condition)) {
            return Elements.FIRE.getDisplayName();
        }
        if ("isIce".equals(condition)) {
            return Elements.ICE.getDisplayName();
        }
        if ("isLightning".equals(condition)) {
            return Elements.LIGHTNING.getDisplayName();
        }
        if ("isLight".equals(condition)) {
            return Elements.LIGHT.getDisplayName();
        }
        if ("isDark".equals(condition)) {
            return Elements.DARK.getDisplayName();
        }
        if ("isWater".equals(condition)) {
            return Elements.WATER.getDisplayName();
        }
        if ("isEarth".equals(condition)) {
            return Elements.EARTH.getDisplayName();
        }
        if ("isAir".equals(condition)) {
            return Elements.AIR.getDisplayName();
        }
        if ("isPoison".equals(condition)) {
            return Elements.POISON.getDisplayName();
        }
        if ("isVoid".equals(condition)) {
            return Elements.VOID.getDisplayName();
        }
        return "*".equals(parts[0]) ? I18n.format("gui.xat.profile.stats.damage.all") : parts[0];
    }

    private String getGenderName(int gender) {
        switch (gender) {
            case 1:
                return I18n.format("gui.xat.race.gender.male");
            case 2:
                return I18n.format("gui.xat.race.gender.female");
            default:
                return I18n.format("gui.xat.race.gender.normal");
        }
    }

    private void drawOverviewIcon() {
        if (this.overviewIcon == null) {
            return;
        }
        final int x = 14 + this.fontRenderer.getStringWidth(this.overviewTitle) + 4;
        final int y = 56;
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(this.overviewIcon);
        this.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
        GlStateManager.disableBlend();
    }

    private void updateTabButtons() {
        for (final GuiButton button : this.buttonList) {
            if ((button.id == OVERVIEW) || (button.id == STATS) || (button.id == ABILITIES) || (button.id == SKILLS) || (button.id == LORE)) {
                button.enabled = button.id != this.selectedTab;
            }
        }
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
                wr.pos(offset + this.logoDims.width, top + this.logoDims.height, GuiAttributesScreen.this.zLevel)
                        .tex(1, 1)
                        .endVertex();
                wr.pos(offset + this.logoDims.width, top, GuiAttributesScreen.this.zLevel).tex(1, 0).endVertex();
                wr.pos(offset, top, GuiAttributesScreen.this.zLevel).tex(0, 0).endVertex();
                tess.draw();
                GlStateManager.disableBlend();
                top += this.logoDims.height + 10;
            }

            for (final ITextComponent line : this.lines) {
                if (line != null) {
                    GlStateManager.enableBlend();
                    GuiAttributesScreen.this.getFontRenderer()
                            .drawStringWithShadow(line.getFormattedText(), this.left + 4, top, 0xFFFFFF);
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
            super(parent.mc, listWidth, parent.height, 56, parent.height - 60, 10, slotHeight, parent.width, parent.height);
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
