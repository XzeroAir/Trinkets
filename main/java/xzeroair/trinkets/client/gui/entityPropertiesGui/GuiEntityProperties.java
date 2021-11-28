package xzeroair.trinkets.client.gui.entityPropertiesGui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
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
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.ColorHelper;
import xzeroair.trinkets.util.helpers.DrawingHelper;

@SideOnly(Side.CLIENT)
public class GuiEntityProperties extends GuiScreen {

	public EntityPlayer player;
	public EntityProperties properties;
	public GuiTextField colorField;
	protected GuiPropertiesSlider r, g, b, a;
	public ColorHelper color;
	public int buttonPressed;

	public int manaBar = 1;
	public int abilityMenu = 2;
	public int manaOrientation = 3;
	public int toggleTrait = 4;
	public int colorFieldID = 5;
	public int flipPlayer = 6;
	public int resetColor = 7;
	public int redSlider = 8;
	public int greenSlider = 9;
	public int blueSlider = 10;
	//		public int alphaSlider = 11;
	public int closeGui = 11;

	protected boolean flip = false;

	public GuiEntityProperties(EntityPlayer player) {
		this.player = player;
		color = new ColorHelper();
		properties = Capabilities.getEntityRace(player);
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

		this.addButton(new GuiPropertiesButton(manaBar, 2, height - 22, 60, 20, "Mana Bar")); // Open Mana bar gui
		this.addButton(new GuiPropertiesButton(abilityMenu, 2, 2, 50, 20, "Stats"));
		this.addButton(new GuiPropertiesButton(manaOrientation, 64, height - 22, 14, 20, "")); // Change Mana Bar Direction
		this.addButton(new GuiPropertiesButton(toggleTrait, 2, 40, 60, 20, "")); // Toggle Trait Shown

		int bX = (width - (width / 4));
		int bY = (height - (height / 2));
		bX -= 30;
		bY -= (height / 4);
		colorField = new GuiTextField(colorFieldID, fontRenderer, bX, bY, 100, 20);
		colorField.setMaxStringLength(8);
		colorField.setText(properties.getTraitColor());
		color.setColor(properties.getTraitColor());
		this.addButton(new GuiPropertiesButton(flipPlayer, (width / 2) - 30, 0, 60, 20, "Flip")); // Flip Player
		// WAS ID 3
		this.addButton(new GuiPropertiesButton(resetColor, bX + 102, bY - 1, 20, 20, "R")); // Color Reset
		bY += 24;
		r = new GuiPropertiesSlider(this, redSlider, bX, bY, 100, 20, "Red", color.getRed(), 1F, 0F);
		bY += 20;
		g = new GuiPropertiesSlider(this, greenSlider, bX, bY, 100, 20, "Green", color.getGreen(), 1F, 0F);
		bY += 20;
		b = new GuiPropertiesSlider(this, blueSlider, bX, bY, 100, 20, "Blue", color.getBlue(), 1F, 0F);
		bY += 20;
		this.addButton(r);
		this.addButton(g);
		this.addButton(b);

		this.addButton(new GuiPropertiesButton(closeGui, width - 16, 2, 14, 20, TextFormatting.RED + "X")); // Change Mana Bar Direction
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
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		buttonPressed = button.id;
		if (button.id == manaBar) {
			//			mc.player.closeScreen();
			mc.player.openGui(Trinkets.instance, 1, mc.player.world, 0, 0, 0);
		}
		if (button.id == abilityMenu) {
			//			mc.player.closeScreen();
			mc.player.openGui(Trinkets.instance, 3, mc.player.world, 0, 0, 0);
		}
		if (button.id == manaOrientation) {
			TrinketsConfig.CLIENT.MPBar.mana_horizontal = !TrinketsConfig.CLIENT.MPBar.mana_horizontal;
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
		if (button.id == toggleTrait) {
			properties.setTraitsShown(!properties.showTraits());
		}
		if (button.id == flipPlayer) {
			flip = !flip;
		}
		if (button.id == resetColor) {
			properties.setTraitColor("#ffffff");
			color.setColor("#ffffff");
			colorField.setText(properties.getTraitColor());
			colorField.setTextColor(16777215);
			r.sliderValue = 1F;
			g.sliderValue = 1F;
			b.sliderValue = 1F;
		}
		if (button.id == closeGui) {
			this.displayNormalInventory();
		}

	}

	@Override
	public void onGuiClosed() {
		properties.sendInformationToServer();
		//		super.onGuiClosed();
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
		//		TrinketsConfig.CLIENT.Hud.X = ((mouseX * 100) / width) * 0.01D;
		//		TrinketsConfig.CLIENT.Hud.Y = ((mouseY * 100) / height) * 0.01D;
		//		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		//		if (!properties.isFake()) {
		int backHeight = 30;
		if (properties.getCurrentRace().equals(EntityRaces.dragon) || properties.getCurrentRace().equals(EntityRaces.fairy)) {
			backHeight = 42;
			int flying = properties.showTraits() ? 51200 : 13107200;
			fontRenderer.drawStringWithShadow("Flying", 17, 62, flying);
		}
		DrawingHelper.Draw(2, 30, -100, 0, 0, 0, 0, 60, backHeight, 0, 0, 0, 0, 0, 0.5F);
		fontRenderer.drawStringWithShadow("Show Trait", 6, 32, 16777215);
		//		}

		colorField.drawTextBox();
		GlStateManager.pushMatrix();
		int exampleX = width - (width / 4);
		int exampleY = (height - (height / 2));
		exampleX -= 30;
		exampleY -= (height / 4);
		DrawingHelper.Draw(exampleX + 102, exampleY, 0, 0, 0, 0, 0, 18, 18, 0, 0, color.getRed(), color.getGreen(), color.getBlue(), 1F);
		//		GlStateManager.resetColor();
		GlStateManager.color(1, 1, 1);

		//		float t = 30 / (((30 * (properties.getSize() * 0.001F)) * 100) / 30);//(int) (30 * properties.getSize() * 0.01F);
		float t = ((30 * 10) / properties.getSize());
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		DrawingHelper.Draw((width / 2) - 50, (height / 2) - 75, -100, 0, 0, 0, 0, 100, 180, 0, 0, 0, 0, 0, 0.5F);
		this.drawEntityOnScreen(width / 2, (height / 2) + 100, (int) (30 * t), ((width / 2)) - oldMouseX, ((height / 2)) - 50 - oldMouseY, mc.player);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
		//		drawHoveringText(text, mouseX, y);
		//		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		//		ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		super.keyTyped(par1, par2);
		colorField.textboxKeyTyped(par1, par2);
		properties.setTraitColor(colorField.getText());
		//		if (par2 == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
		//			player.closeScreen();
		//			this.displayNormalInventory();
		//		}
	}

	public void displayNormalInventory() {
		final GuiInventory gui = new GuiInventory(mc.player);
		mc.displayGuiScreen(gui);
	}

	public void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, 50.0F);
		GlStateManager.scale((-scale), scale, scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-((float) Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		float YawOffset = (float) Math.atan(mouseX / 40.0F) * 20.0F;
		float yaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
		ent.renderYawOffset = flip ? -YawOffset : YawOffset;
		ent.rotationYaw = flip ? -yaw : yaw;
		ent.rotationPitch = -((float) Math.atan(mouseY / 40.0F)) * 20.0F;
		ent.rotationYawHead = ent.rotationYaw;
		ent.prevRotationYawHead = ent.rotationYaw;
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		if (flip) {
			GlStateManager.rotate(-180, 0.0F, 1.0F, 0.0F);
		}
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
