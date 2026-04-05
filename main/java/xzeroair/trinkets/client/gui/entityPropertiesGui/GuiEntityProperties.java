package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.gui.ITrinketGuiInterface;
import xzeroair.trinkets.client.gui.helpers.ColorSlider;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.ConstantsTextTranslations;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiEntityProperties extends GuiScreen implements ITrinketGuiInterface {

    public enum ColorOption {
        //@formatter:off
        Normal(0),
        Inverted(1),
        Solid(2);
        //@formatter:on

        private final int id;

        ColorOption(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static int getMaxLength() {
            return values().length;
        }

        public static ColorOption color(int value) {
            if ((value < 0) || (value >= values().length)) {
                value = 0;
            }
            return values()[value];
        }
    }


    public EntityPlayer player;
    public EntityRacePropertiesHandler properties;

    private final ResourceLocation background = null;
    private float oldMouseX;
    private float oldMouseY;

    public ColorSlider color1;
    public ColorSlider color2;
    public ColorSlider color3;

    protected boolean flip;
    protected int colorOption;

    public GuiEntityProperties(EntityPlayer player) {
        this.player = player;
        this.properties = Capabilities.getEntityProperties(player, null, (prop, rtn) -> prop.getRaceHandler());
        this.flip = false;
        this.colorOption = 0;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.color1.getColorField().updateCursorCounter();
        this.color2.getColorField().updateCursorCounter();
        if (this.color3 != null) {
            this.color3.getColorField().updateCursorCounter();
        }
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        if (this.properties == null) {
            return;
        }

        int buttonID = 0;
        /// Close Button
        this.addButton(new GuiPropertiesButton(buttonID++, this.width - 24, 2, 22, 20, TextFormatting.RED + "<--", (button, pressed) -> {
            this.displayNormalInventory();
        }));

        this.addButton(new GuiPropertiesButton(buttonID++, this.width - (60 + 20), this.height - 32, 60, 20, TextFormatting.GREEN + "CONFIRM", (button, pressed) -> {
            this.player.closeScreen();
        }));

        /// Mana Bar config gui
        this.addButton(new GuiPropertiesButton(buttonID++, 2, this.height - 22, 60, 20, ConstantsTextTranslations.GUI_MANA_BAR.getFormattedText(), (button, pressed) -> {
//            this.player.closeScreen();
            this.player.openGui(Trinkets.instance, 1, this.player.world, 0, 0, 0);
        }));

        /// Change the mana bar direction from horizontal to vertical.
        this.addButton(new GuiPropertiesButton(buttonID++, 64, this.height - 22, 14, 20, (button, pressed) -> {
            TrinketsConfig.CLIENT.MANA_BAR_HUD.mana_horizontal = !TrinketsConfig.CLIENT.MANA_BAR_HUD.mana_horizontal;
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }, (button, renderPre) -> {
            button.displayString = TrinketsConfig.CLIENT.MANA_BAR_HUD.mana_horizontal ? "H" : "V";
        }));

        /// Always show the mana bar, or only when it's full.
        this.addButton(new GuiPropertiesButton(buttonID++, 80, this.height - 22, 14, 20, (button, pressed) -> {
            TrinketsConfig.CLIENT.MANA_BAR_HUD.always_shown = !TrinketsConfig.CLIENT.MANA_BAR_HUD.always_shown;
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }, (button, renderPre) -> {
            button.displayString = TrinketsConfig.CLIENT.MANA_BAR_HUD.always_shown ? "A" : "^";
        }));

        /// Stat and information screen
        this.addButton(new GuiPropertiesButton(buttonID++, 2, 2, 50, 20, ConstantsTextTranslations.GUI_RACE_INFO.getFormattedText(), (button, pressed) -> {
//            this.player.closeScreen();
            this.player.openGui(Trinkets.instance, 3, this.player.world, 0, 0, 0);
        }));

        /// Whether to show the players racial trait, Wings, ears, etc.
        this.addButton(new GuiPropertiesButton(buttonID++, 2, 40, 60, 20, (button, pressed) -> {
            this.properties.setShowTraits(!this.properties.showTraits());
        }, (button, renderPre) -> {
            button.displayString = "" + this.properties.showTraits();
        }));

        /// Flip the player renderer, so it's possible to see the players back.
        this.addButton(new GuiPropertiesButton(buttonID++, (this.width / 2) - 30, 0, 60, 20, ConstantsTextTranslations.GUI_FLIP_PLAYER.getFormattedText(), (button, pressed) -> {
            this.flip = !this.flip;
        }));

//        int bX = (this.width - (this.width / 4));
//        int bY = (this.height - (this.height / 2));
//        bX -= 30;
//        bY -= (this.height / 4) + 34;

        int sliderWidth = 50;
        int sliderHeight = 20;
        int bX = this.width - (sliderWidth + 50);
        int bY = 34;
        bX = 70;
        bY = 40;
        this.addButton(new GuiPropertiesButton(buttonID++, bX, bY, sliderWidth + 22, sliderHeight, ColorOption.color(this.properties.getColorOption()).name(), (button, pressed) -> {
            this.colorOption++;
            if (this.colorOption >= ColorOption.getMaxLength()) {
                this.colorOption = 0;
            }
            button.displayString = ColorOption.color(this.colorOption).name();
            this.properties.setColorOption(this.colorOption);
        }));

        bY += 20;
        /**
         * Trait Variant slider.
         * Decides the players rendered trait.
         */
        int rVMax = this.properties.getRace().getRaceInformation().getPrimaryTraitMaxVariants() - 1;
        int rV = this.properties.getTraitVariant();
        float startingValue = rVMax <= 0 ? 0F : (rV + 0F) / (rVMax - 0F);
        this.addButton(new GuiPropertiesSlider(buttonID++, bX, bY, sliderWidth + 22, sliderHeight, ConstantsTextTranslations.GUI_MAIN_TRAIT_VARIANT.getFormattedText() + ": " + ((int) (startingValue * rVMax)), startingValue, rVMax > 0 ? 1F : 0F, 0F, (slider, wrapper) -> {
            int result = (int) ((slider.sliderValue * slider.sliderMaxValue) * (rVMax));
            this.properties.setTraitVariant(result);
            slider.displayString = ConstantsTextTranslations.GUI_MAIN_TRAIT_VARIANT.getFormattedText() + ": " + (result);
        }));
        final int traitPrimaryColor = this.properties.getPrimaryTraitColor();
        final int traitSecondaryColor = this.properties.getSecondaryTraitColor();

        bY += sliderHeight + 1;
        this.color1 = new ColorSlider(this, buttonID++, bX, bY, sliderWidth, sliderHeight, traitPrimaryColor, this.properties.getRaceCache().getPrimaryColor(), this.fontRenderer, (color) -> this.properties.setPrimaryTraitColor(color));
        this.color1.init(this.buttonList);
        this.color2 = new ColorSlider(this, this.color1.getNextId(), this.color1.getX(), (this.color1.getNextY() + 2), sliderWidth, sliderHeight, traitSecondaryColor, this.properties.getRaceCache().getSecondaryColor(), this.fontRenderer, (color) -> this.properties.setSecondaryTraitColor(color));
        this.color2.init(this.buttonList);
//        bX = this.color2.getNextX();
//        bY = this.color2.getNextY();
//        bX = 70;
//        bY = 40;
        bX = this.width - (sliderWidth + 50);
        bY = 60;

        int rVMax2 = this.properties.getRace().getRaceInformation().getSecondaryTraitMaxVariants();
        if (rVMax2 > 0) {
            int rV2 = this.properties.getTraitAuxVariant();
            float startingValue2 = rVMax2 <= 0 ? 0F : (rV2 + 0F) / (rVMax2 - 0F);
            this.addButton(new GuiPropertiesSlider(this.color2.getNextId(), bX, bY, sliderWidth + 22, sliderHeight, ConstantsTextTranslations.GUI_MAIN_TRAIT_VARIANT.getFormattedText() + ": " + ((int) (startingValue2 * rVMax2)), startingValue2, 1F, 0F, (slider, wrapper) -> {
                int result = (int) ((slider.sliderValue * slider.sliderMaxValue) * (rVMax2));
                this.properties.setTraitAuxVariant(result);
                slider.displayString = ConstantsTextTranslations.GUI_MAIN_TRAIT_VARIANT.getFormattedText() + ": " + (result);
            }));
            bY += sliderHeight + 1;
            final int traitAuxColor = this.properties.getTraitAuxColor();
            this.color3 = new ColorSlider(this, this.color2.getNextId() + 1, bX, bY, sliderWidth, sliderHeight, traitAuxColor, this.properties.getRaceCache().getRace().getRaceInformation().getOptionalColor(), this.fontRenderer, (color) -> this.properties.setTraitAuxColor(color));
            this.color3.init(this.buttonList);
        }

    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        super.keyTyped(par1, par2);
        if (this.color1.getColorField().isFocused()) {
            this.color1.keyTyped(par1, par2);
            this.properties.setPrimaryTraitColor(this.color1.getColor());
        } else if (this.color2.getColorField().isFocused()) {
            this.color2.keyTyped(par1, par2);
            this.properties.setSecondaryTraitColor(this.color2.getColor());
        } else if (this.color3 != null && this.color3.getColorField().isFocused()) {
            this.color3.keyTyped(par1, par2);
            this.properties.setTraitAuxColor(this.color3.getColor());
        } else {
            if (par2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
                this.player.closeScreen();
                this.displayNormalInventory();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.color1.mouseClicked(mouseX, mouseY, mouseButton);
        this.color2.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.color3 != null) {
            this.color3.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        this.properties.getProperties().sendInformationToServer(this.properties.savedNBTData(new NBTTagCompound()));
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.oldMouseX = mouseX;
        this.oldMouseY = mouseY;
        GlStateManager.translate(0, 0, -500);
        this.drawDefaultBackground();
        GlStateManager.translate(0, 0, 500);

        super.drawScreen(mouseX, mouseY, partialTicks);

        // Render Trait Button
        int backHeight = 30;
        if (this.properties.getRace().canFly()) {
            backHeight = 42;
            this.fontRenderer.drawStringWithShadow("Flying", 17, 62, this.properties.canFly() ? 51200 : 13107200);
        }
        DrawingHelper.Draw(2, 30, -100, 0, 0, 0, 0, 60, backHeight, 0, 0, 0, 0, 0, 0.5F);
        this.fontRenderer.drawStringWithShadow("Show Trait", 6, 32, 16777215);

        if (!this.properties.getRaceCache().compareRace(EntityRaces.none)) {
            int rX = 60;
            int rY = 4;
            String race = this.properties.getRace().getDisplayName();
            int rTxtLength = this.fontRenderer.getStringWidth(race);
            int distanceToAdd = rTxtLength % 2 == 0 ? rTxtLength + 10 : rTxtLength + 9;
            DrawingHelper.Draw(rX, rY, -100, 0, 0, 0, 0, distanceToAdd, 14, 0, 0, 0, 0, 0, 0.5F);
            this.fontRenderer.drawStringWithShadow(race, rX + 6, rY + 2, 16777215);

            if (!this.properties.getRaceCache().comparePrimaryElement(Elements.NEUTRAL)) {
                Element ele = this.properties.getRaceCache().getPrimaryElement();
                int pX = rX + distanceToAdd + 4;
                int pY = rY;
                String eleString = ele.getDisplayName();
                int pTxtLength = this.fontRenderer.getStringWidth(eleString);
                DrawingHelper.Draw(pX, pY, -100, 0, 0, 0, 0, pTxtLength % 2 == 0 ? pTxtLength + 10 : pTxtLength + 9, 14, 0, 0, 0, 0, 0, 0.5F);
                this.fontRenderer.drawStringWithShadow(eleString, pX + 6, pY + 2, ele.getPrimaryColor());
            }
        }

        this.color1.render(mouseX, mouseY, partialTicks);
        this.color2.render(mouseX, mouseY, partialTicks);
        if (this.color3 != null) {
            this.color3.render(mouseX, mouseY, partialTicks);
        }
        GlStateManager.translate(0, 0, -200);
        final int h = this.properties.getProperties().getHeightValue();
        final double scale = ((300D / (h * 1D)) * 30D);
        DrawingHelper.Draw((this.width / 2) - 50, (this.height / 2) - 75, -100, 0, 0, 0, 0, 100, 180, 0, 0, 0, 0, 0, 0.5F);
        DrawingHelper.drawEntityOnScreen(this.width / 2, (this.height / 2) + 100, (int) scale, this.flip, 180, ((this.width / 2)) - this.oldMouseX, ((this.height / 2)) - 50 - this.oldMouseY, this.player);

//        this.drawHoveringText("", mouseX, mouseY);
//        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;//super.doesGuiPauseGame();
    }

    public void displayNormalInventory() {
        final GuiInventory gui = new GuiInventory(this.mc.player);
        this.mc.displayGuiScreen(gui);
    }
}
