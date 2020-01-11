package xzeroair.trinkets.client.gui;

import java.util.Collection;

import com.google.common.collect.Ordering;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.util.TrinketsConfig;

public class TrinketInventoryEffectRenderer extends TrinketGuiContainer {

	protected boolean hasActivePotionEffects;

	public TrinketInventoryEffectRenderer(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}
	/** True if there is some potion effect to display */

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		updateActivePotionEffects();
	}

	protected void updateActivePotionEffects()
	{
		boolean hasVisibleEffect = false;
		for(final PotionEffect potioneffect : mc.player.getActivePotionEffects()) {
			final Potion potion = potioneffect.getPotion();
			if(potion.shouldRender(potioneffect)) { hasVisibleEffect = true; break; }
		}
		if (mc.player.getActivePotionEffects().isEmpty() || !hasVisibleEffect)
		{
			guiLeft = (width - xSize) / 2;
			hasActivePotionEffects = false;
		}
		else
		{
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent(this))) {
				guiLeft = (width - xSize) / 2;
			} else {
				guiLeft = 160 + ((width - xSize - 200) / 2);
			}
			hasActivePotionEffects = true;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (hasActivePotionEffects)
		{
			drawActivePotionEffects();
		}
	}

	/**
	 * Display the potion effects list
	 */
	private void drawActivePotionEffects()
	{
		final int xPos = TrinketsConfig.CLIENT.GUI.X;//MathHelper.clamp(38, 38, 39);
		final int yPos = TrinketsConfig.CLIENT.GUI.Y;//MathHelper.clamp(3, 3, 61);

		final int i = (guiLeft - (118 + (16 * TrinketsConfig.SERVER.GUI.guiSlotsRows))) + xPos;//TrinketsConfig.CLIENT.button.Xoffset;
		int j = guiTop;// + yPos;
		final int k = 166;
		final Collection<PotionEffect> collection = mc.player.getActivePotionEffects();

		if (!collection.isEmpty())
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableLighting();
			int l = 33;

			if (collection.size() > 5)
			{
				l = 132 / (collection.size() - 1);
			}

			for (final PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
			{
				final Potion potion = potioneffect.getPotion();
				if(!potion.shouldRender(potioneffect)) {
					continue;
				}
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
				this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

				if (potion.hasStatusIcon())
				{
					final int i1 = potion.getStatusIconIndex();
					this.drawTexturedModalRect(i + 6, j + 7, 0 + ((i1 % 8) * 18), 198 + ((i1 / 8) * 18), 18, 18);
				}

				potion.renderInventoryEffect(potioneffect, this, i, j, zLevel);
				if (!potion.shouldRenderInvText(potioneffect)) { j += l; continue; }
				String s1 = I18n.format(potion.getName());

				if (potioneffect.getAmplifier() == 1)
				{
					s1 = s1 + " " + I18n.format("enchantment.level.2");
				}
				else if (potioneffect.getAmplifier() == 2)
				{
					s1 = s1 + " " + I18n.format("enchantment.level.3");
				}
				else if (potioneffect.getAmplifier() == 3)
				{
					s1 = s1 + " " + I18n.format("enchantment.level.4");
				}

				fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6, 16777215);
				final String s = Potion.getPotionDurationString(potioneffect, 1.0F);
				fontRenderer.drawStringWithShadow(s, i + 10 + 18, j + 6 + 10, 8355711);
				j += l;
			}
		}
	}
}
