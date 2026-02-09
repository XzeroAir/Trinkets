package xzeroair.trinkets.client.gui.entityPropertiesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiEntityProperties extends GuiScreen {

    public EntityPlayer player;
    public EntityProperties properties;
    public GuiTextField colorField, colorField2;
    protected GuiPropertiesSlider r, g, b, a, r2, g2, b2, a2, variant;
    //	public ColorHelper colorHelper;
    public int[] color;
    public int buttonPressed;

    public int manaBar = 1;
    public int abilityMenu = 2;
    public int manaOrientation = 3;
    public int manaBarAlways = 19;
    public int toggleTrait = 4;
    public int colorFieldID = 5;
    public int flipPlayer = 6;
    public int resetColor = 7;
    public int redSlider = 8;
    public int greenSlider = 9;
    public int blueSlider = 10;
    //		public int alphaSlider = 11;
    public int closeGui = 11;
    public int traitX = 12;
    public int traitY = 13;
    public int traitZ = 14;
    public int traitWidth = 15;
    public int redSlider2 = 16;
    public int greenSlider2 = 17;
    public int blueSlider2 = 18;
    public int colorFieldID2 = 19;
    public int resetColor2 = 20;
    public int variantSlider = 21;

    protected boolean flip = false;

    public GuiEntityProperties(EntityPlayer player) {
        this.player = player;
        properties = Capabilities.getEntityProperties(player);
    }

    public static ResourceLocation background = null;
    private float oldMouseX;
    private float oldMouseY;

    @Override
    public void updateScreen() {
        super.updateScreen();
        colorField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();

        /**
         * Open the player Mana Bar movement gui.
         */
        this.addButton(new GuiPropertiesButton(manaBar, 2, height - 22, 60, 20, "Mana Bar", (button, pressed) -> {
            mc.player.openGui(Trinkets.instance, 1, mc.player.world, 0, 0, 0);
        })); // Open Mana bar gui

        /**
         * Open the player's stat gui.
         */
        this.addButton(new GuiPropertiesButton(abilityMenu, 2, 2, 50, 20, "Stats", (button, pressed) -> {
            mc.player.openGui(Trinkets.instance, 3, mc.player.world, 0, 0, 0);
        }));

        /**
         * Change the mana bar direction from horizontal to vertical.
         */
        this.addButton(new GuiPropertiesButton(manaOrientation, 64, height - 22, 14, 20, (button, pressed) -> {
            TrinketsConfig.CLIENT.MPBar.mana_horizontal = !TrinketsConfig.CLIENT.MPBar.mana_horizontal;
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }, (button, renderPre) -> {
            button.displayString = TrinketsConfig.CLIENT.MPBar.mana_horizontal ? "H" : "V";
        })); // Change Mana Bar Direction

        /**
         * Always show the mana bar, or only when it's full.
         */
        this.addButton(new GuiPropertiesButton(manaBarAlways, 80, height - 22, 14, 20, (button, pressed) -> {
            TrinketsConfig.CLIENT.MPBar.always_shown = !TrinketsConfig.CLIENT.MPBar.always_shown;
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
        }, (button, renderPre) -> {
            button.displayString = TrinketsConfig.CLIENT.MPBar.always_shown ? "A" : "^";
        })); // Show Mana Bar Always

        /**
         * Whether to show the players racial trait, Wings, ears, etc.
         * This is a cosmetic change locally.
         * Other players can still see the trait.
         */
        this.addButton(new GuiPropertiesButton(toggleTrait, 2, 40, 60, 20, (button, pressed) -> {
            properties.getRaceHandler().setShowTraits(!properties.getRaceHandler().showTraits());
        }, (button, renderPre) -> {
            button.displayString = "" + properties.getRaceHandler().showTraits();
        })); // Toggle Trait Shown

        /**
         * Flip the player renderer, so it's possible to see the players back.
         */
        this.addButton(new GuiPropertiesButton(flipPlayer, (width / 2) - 30, 0, 60, 20, "Flip", (button, pressed) -> {
            flip = !flip;
        }));// Flip Player

        int bX = (width - (width / 4));
        int bY = (height - (height / 2));
        bX -= 30;
        bY -= (height / 4) + 34;

        /**
         * Trait Variant slider.
         * Decides the players rendered trait.
         */
        int rV = properties.getRaceHandler().getTraitVariant();
        int rVMax = properties.getRaceHandler().getMaxTraitVariant() - 1;
        float startingValue = rVMax <= 0 ? 0F : (rV + 0F) / (rVMax - 0F);
        variant = new GuiPropertiesSlider(variantSlider, bX, bY, 100, 20, "Variant: " + ((int) (startingValue * rVMax)), startingValue, 1F, 0F, (slider, wrapper) -> {
            int result = (int) ((slider.sliderValue * slider.sliderMaxValue) * (rVMax));
            properties.getRaceHandler().setTraitVariant(result);
            slider.displayString = "Variant: " + (result);
        });
        this.addButton(variant);

        bY += 23;

        /**
         * Primary Color Text Field.
         */
        colorField = new GuiTextField(colorFieldID, fontRenderer, bX, bY, 100, 20);
        colorField.setMaxStringLength(8);
        final String traitColor = properties == null ? "#ffffff" : properties.getRaceHandler().getTraitColor();
        final float[] defaultRGB = ColorHelper.getRGBColor(traitColor);
        colorField.setText(traitColor);

        /**
         * Primary Color reset button.
         */
        this.addButton(new GuiPropertiesButton(resetColor, bX + 102, bY - 1, 20, 20, "R", (button, pressed) -> {
            final String defaultHex = ColorHelper.convertDecimalColorToHexadecimal(properties.getCurrentRace().getRace().getPrimaryColor());
            properties.getRaceHandler().setTraitColor(defaultHex);
            colorField.setText(defaultHex);
//            colorField.setTextColor(properties.getCurrentRace().getPrimaryColor());
            final float[] rgb = ColorHelper.getRGBColor(defaultHex);
            r.sliderValue = rgb[0];
            r.displayString = "Red: " + ((int) (rgb[0] * 255));
            g.sliderValue = rgb[1];
            g.displayString = "Green: " + ((int) (rgb[1] * 255));
            b.sliderValue = rgb[2];
            b.displayString = "Blue: " + ((int) (rgb[2] * 255));
        })); // Color Reset

        bY += 21;

        /**
         * Primary Trait Slider for the Red Channel
         */
        r = new GuiPropertiesSlider(redSlider, bX, bY, 100, 20, "Red: " + (int) ((defaultRGB[0] * 1F) * 255), defaultRGB[0], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(slider.getSliderValue(), rgb[1], rgb[2]);
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setTraitColor(hex);
            slider.displayString = "Red" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField.setText(hex);
//            colorField.setTextColor(decimal);
        });
        this.addButton(r);

        bY += 20;

        /**
         * Primary Trait Slider for the Green Channel
         */
        g = new GuiPropertiesSlider(greenSlider, bX, bY, 100, 20, "Green: " + (int) ((defaultRGB[1] * 1F) * 255), defaultRGB[1], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(rgb[0], slider.getSliderValue(), rgb[2]);
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setTraitColor(hex);
            slider.displayString = "Green" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField.setText(hex);
//            colorField.setTextColor(decimal);
        });
        this.addButton(g);

        bY += 20;

        /**
         * Primary Trait Slider for the Blue Channel
         */
        b = new GuiPropertiesSlider(blueSlider, bX, bY, 100, 20, "Blue: " + (int) ((defaultRGB[2] * 1F) * 255), defaultRGB[2], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(rgb[0], rgb[1], slider.getSliderValue());
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setTraitColor(hex);
            slider.displayString = "Blue" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField.setText(hex);
//            colorField.setTextColor(decimal);
        });
        this.addButton(b);

        bY += 20;
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        bY += 3;
        colorField2 = new GuiTextField(colorFieldID2, fontRenderer, bX, bY, 100, 20);
        colorField2.setMaxStringLength(8);
        final String traitColor2 = properties == null ? "#ffffff" : properties.getRaceHandler().getAltTraitColor();
        final float[] defaultRGB2 = ColorHelper.getRGBColor(traitColor2);
        colorField2.setText(traitColor2);

        this.addButton(new GuiPropertiesButton(resetColor2, bX + 102, bY - 1, 20, 20, "R", (button, pressed) -> {
            final String defaultHex = ColorHelper.convertDecimalColorToHexadecimal(properties.getCurrentRace().getRace().getSecondaryColor());
            properties.getRaceHandler().setAltTraitColor(defaultHex);
            colorField2.setText(defaultHex);
//            colorField2.setTextColor(properties.getCurrentRace().getSecondaryColor());
            final float[] rgb = ColorHelper.getRGBColor(defaultHex);
            r2.sliderValue = rgb[0];
            r2.displayString = "Red: " + ((int) (rgb[0] * 255));
            g2.sliderValue = rgb[1];
            g2.displayString = "Green: " + ((int) (rgb[1] * 255));
            b2.sliderValue = rgb[2];
            b2.displayString = "Blue: " + ((int) (rgb[2] * 255));
        })); // Color Reset

        bY += 21;

        r2 = new GuiPropertiesSlider(redSlider2, bX, bY, 100, 20, "Red: " + (int) ((defaultRGB2[0] * 1F) * 255), defaultRGB2[0], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getAltTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(slider.getSliderValue(), rgb[1], rgb[2]);
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setAltTraitColor(hex);
            slider.displayString = "Red" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField2.setText(hex);
//            colorField2.setTextColor(decimal);
        });
        this.addButton(r2);

        bY += 20;

        g2 = new GuiPropertiesSlider(greenSlider2, bX, bY, 100, 20, "Green: " + (int) ((defaultRGB2[1] * 1F) * 255), defaultRGB2[1], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getAltTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(rgb[0], slider.getSliderValue(), rgb[2]);
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setAltTraitColor(hex);
            slider.displayString = "Green" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField2.setText(hex);
//            colorField2.setTextColor(decimal);
        });
        this.addButton(g2);

        bY += 20;

        b2 = new GuiPropertiesSlider(blueSlider2, bX, bY, 100, 20, "Blue: " + (int) ((defaultRGB2[2] * 1F) * 255), defaultRGB2[2], 1F, 0F, (slider, wrapper) -> {
            final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getAltTraitColor());
            final String hex = ColorHelper.getHexColorFromRGB(rgb[0], rgb[1], slider.getSliderValue());
//            final int decimal = ColorHelper.convertHexadecimalToDecimal(hex);
            properties.getRaceHandler().setAltTraitColor(hex);
            slider.displayString = "Blue" + ": " + (int) ((slider.sliderValue * slider.sliderMaxValue) * 255);
            colorField2.setText(hex);
//            colorField2.setTextColor(decimal);
        });
        this.addButton(b2);

        bY += 20;

        this.addButton(new GuiPropertiesButton(closeGui, width - 16, 2, 14, 20, TextFormatting.RED + "X", (button, pressed) -> {
            this.displayNormalInventory();
        })); // Change Mana Bar Direction
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        buttonPressed = 0;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        colorField.mouseClicked(mouseX, mouseY, mouseButton);
        colorField2.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        properties.sendInformationToServer();
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
        oldMouseX = mouseX;
        oldMouseY = mouseY;
        //		TrinketsConfig.CLIENT.Hud.X = ((mouseX * 100) / width) * 0.01D;
        //		TrinketsConfig.CLIENT.Hud.Y = ((mouseY * 100) / height) * 0.01D;
        //		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        // Render Trait Button
        int backHeight = 30;
        if (properties.getCurrentRace().getRace().canFly()) {
            backHeight = 42;
            final int flying = properties.getRaceHandler().canFly() ? 51200 : 13107200;
            fontRenderer.drawStringWithShadow("Flying", 17, 62, flying);
        }
        DrawingHelper.Draw(2, 30, -100, 0, 0, 0, 0, 60, backHeight, 0, 0, 0, 0, 0, 0.5F);
        fontRenderer.drawStringWithShadow("Show Trait", 6, 32, 16777215);


        // Render Color Box
        colorField.drawTextBox();
        int exampleX = width - (width / 4);
        int exampleY = (height - (height / 2));
        exampleX -= 30;
        exampleY -= (height / 4);
        exampleY -= 11;
        final float[] rgb = ColorHelper.getRGBColor(properties.getRaceHandler().getTraitColor());
        final float rV = rgb[0];
        final float gV = rgb[1];
        final float bV = rgb[2];
        DrawingHelper.Draw(exampleX + 103, exampleY, 0, 0, 0, 0, 0, 18, 18, 0, 0, rV, gV, bV, 1F);

        colorField2.drawTextBox();
        final float[] rgb2 = ColorHelper.getRGBColor(properties.getRaceHandler().getAltTraitColor());
        final float rV2 = rgb2[0];
        final float gV2 = rgb2[1];
        final float bV2 = rgb2[2];
        DrawingHelper.Draw(exampleX + 103, exampleY + 84, 0, 0, 0, 0, 0, 18, 18, 0, 0, rV2, gV2, bV2, 1F);

        final int h = properties.getHeightValue();
        final double scale = ((300D / (h * 1D)) * 30D);
        DrawingHelper.Draw((width / 2) - 50, (height / 2) - 75, -100, 0, 0, 0, 0, 100, 180, 0, 0, 0, 0, 0, 0.5F);
        DrawingHelper.drawEntityOnScreen(width / 2, (height / 2) + 100, (int) scale, flip, 180, ((width / 2)) - oldMouseX, ((height / 2)) - 50 - oldMouseY, mc.player);

        //		drawHoveringText(text, mouseX, y);
        //		this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        super.keyTyped(par1, par2);
        if (colorField.isFocused()) {
            colorField.textboxKeyTyped(par1, par2);
            final String text = colorField.getText().toLowerCase().replaceAll("[^#0-9a-f]", "");
            colorField.setText(text);
            final float[] rgb = ColorHelper.getRGBColor(text);
            properties.getRaceHandler().setTraitColor(text);
            r.sliderValue = rgb[0];
            r.displayString = "Red: " + ((int) (rgb[0] * 255));
            g.sliderValue = rgb[1];
            g.displayString = "Green: " + ((int) (rgb[1] * 255));
            b.sliderValue = rgb[2];
            b.displayString = "Blue: " + ((int) (rgb[2] * 255));
//            colorField.setTextColor(ColorHelper.getDecimalFromRGB(rgb[0], rgb[1], rgb[2]));
        } else if (colorField2.isFocused()) {
            colorField2.textboxKeyTyped(par1, par2);
            final String text = colorField2.getText().toLowerCase().replaceAll("[^#0-9a-f]", "");
            colorField2.setText(text);
            final float[] rgb = ColorHelper.getRGBColor(text);
            properties.getRaceHandler().setAltTraitColor(text);
            r2.sliderValue = rgb[0];
            r2.displayString = "Red: " + ((int) (rgb[0] * 255));
            g2.sliderValue = rgb[1];
            g2.displayString = "Green: " + ((int) (rgb[1] * 255));
            b2.sliderValue = rgb[2];
            b2.displayString = "Blue: " + ((int) (rgb[2] * 255));
//            colorField2.setTextColor(ColorHelper.getDecimalFromRGB(rgb[0], rgb[1], rgb[2]));
        } else {

        }
    }

    public void displayNormalInventory() {
        final GuiInventory gui = new GuiInventory(mc.player);
        mc.displayGuiScreen(gui);
    }
}
