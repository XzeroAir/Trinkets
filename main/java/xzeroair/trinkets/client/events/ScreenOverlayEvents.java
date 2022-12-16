package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.client.gui.hud.mana.ManaGui;
import xzeroair.trinkets.client.gui.hud.mana.ManaHud;
import xzeroair.trinkets.util.TrinketsConfig;

public class ScreenOverlayEvents {

	public static ScreenOverlayEvents instance = new ScreenOverlayEvents();

	private final Minecraft mc = Minecraft.getMinecraft();
	private final ManaGui manaGui = new ManaGui(mc);

	private float mana = 0;
	private float maxMana = 0;
	private int bonusMana = 0;

	private float manaCost = 0;

	private int updateCounter = 0;

	public void SyncMana(float mana, int bonusMana, float maxMana) {
		this.setMana(mana);
		this.setBonusMana(bonusMana);
		this.setMaxMana(maxMana);
	}

	public void SyncCost(float cost) {
		this.setCost(cost);
	}

	//	@SubscribeEvent
	//	public void renderGameOverlayEvent(RenderGameOverlayEvent event) {
	//
	//	}
	//
	//	@SubscribeEvent
	//	public void renderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) {
	//
	//	}
	//
	//	@SubscribeEvent
	//	public void renderGameOverlayPreEvent(RenderGameOverlayEvent.Pre event) {
	//
	//	}

	@SubscribeEvent
	public void renderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
		//TODO Make sure this works properly
		if (event.isCanceled() || !(event.getType() == RenderGameOverlayEvent.ElementType.ALL) || !TrinketsConfig.CLIENT.MPBar.shown || !TrinketsConfig.SERVER.mana.mana_enabled) {
			return;
		}
		final MagicStats stats = Capabilities.getMagicStats(Minecraft.getMinecraft().player);
		if (stats != null) {
			this.setMana(stats.getMana());
			this.setBonusMana(stats.getBonusMana());
			this.setMaxMana(stats.getMaxMana());
		}
		if (!this.needMana() && (this.getCost() <= 0)) {
			if (this.updateCounter()) {
				if (!(Minecraft.getMinecraft().currentScreen instanceof ManaHud)) {
					if (!TrinketsConfig.CLIENT.MPBar.always_shown) {
						return;
					}
				}
			}
		} else {
			this.resetCounter();
		}
		final int h = event.getResolution().getScaledHeight();
		final int w = event.getResolution().getScaledWidth();
		final double x = (TrinketsConfig.CLIENT.MPBar.translatedX);
		final double y = (TrinketsConfig.CLIENT.MPBar.translatedY);
		final int xPos = (int) Math.round(w * x);
		final int yPos = (int) Math.round(h * y);
		GlStateManager.pushMatrix();
		manaGui.renderManaGui(event, MathHelper.clamp(xPos, 0, w), MathHelper.clamp(yPos, 0, h), updateCounter, this.getMana(), this.getMaxMana(), this.getCost());
		GlStateManager.popMatrix();
	}

	private boolean updateCounter() {
		if (updateCounter < 80) {
			++updateCounter;
		}
		if (updateCounter >= 80) {
			return true;
		}
		return false;
	}

	public int getUpdateCounter() {
		return updateCounter;
	}

	private void resetCounter() {
		updateCounter = 0;
	}

	private void setMana(float mana) {
		if (this.mana != mana) {
			this.mana = mana;
			this.resetCounter();
		}
	}

	private void setBonusMana(int bonusMana) {
		if (this.bonusMana != bonusMana) {
			this.bonusMana = bonusMana;
			this.resetCounter();
		}
	}

	private void setMaxMana(float maxMana) {
		if (this.maxMana != maxMana) {
			this.maxMana = maxMana;
			this.resetCounter();
		}
	}

	private boolean needMana() {
		return this.getMana() != this.getMaxMana();
	}

	private float getMana() {
		return mana;
	}

	private int getBonusMana() {
		return bonusMana;
	}

	private float getMaxMana() {
		return maxMana;
	}

	private float getCost() {
		return manaCost;
	}

	private void setCost(float cost) {
		manaCost = cost;
	}
}
