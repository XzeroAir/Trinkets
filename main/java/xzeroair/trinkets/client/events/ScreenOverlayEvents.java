package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
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

	private final int costResetCounter = 0;
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
		final EntityPlayer player = Minecraft.getMinecraft().player;
		final MagicStats stats = Capabilities.getMagicStats(player);
		if (stats != null) {
			this.setMana(stats.getMana());
			this.setBonusMana(stats.getBonusMana());
			this.setMaxMana(stats.getMaxMana());
		}
		//		if ((event.getType() == ElementType.AIR) && TrinketsConfig.CLIENT.MPBar.shown && TrinketsConfig.SERVER.mana.mana_enabled) {
		//			return;
		if (!this.needMana() && (this.getCost() <= 0)) {
			if (updateCounter < 60) {
				++updateCounter;
			}
			if (updateCounter >= 60) {
				if (!(Minecraft.getMinecraft().currentScreen instanceof ManaHud)) {
					if (!TrinketsConfig.CLIENT.MPBar.always_shown) {
						return;
					}
				}
			}
		} else {
			updateCounter = 0;
		}
		final int h = event.getResolution().getScaledHeight();
		final int w = event.getResolution().getScaledWidth();
		//		final int l = (event.getResolution().getScaledWidth() / 2) - 91;
		//		final int i1 = (event.getResolution().getScaledWidth() / 2);
		//		final int j1 = event.getResolution().getScaledHeight() / 2;
		final double x = (TrinketsConfig.CLIENT.MPBar.translatedX);
		final double y = (TrinketsConfig.CLIENT.MPBar.translatedY);
		//			int scaleF = event.getResolution().getScaleFactor();
		final int xPos = (int) Math.round(w * x);
		final int yPos = (int) Math.round(h * y);
		//			int left = (w / 2) + 92; //Same x offset as the hunger bar
		//			int top = h - GuiIngameForge.right_height;
		GlStateManager.pushMatrix();
		//		GlStateManager.color(1, 1, 1, 1);
		//			manaGui.renderManaGui(left, top, updateCounter, this.getMana(), this.getMaxMana(), this.getCost());
		manaGui.renderManaGui(event, MathHelper.clamp(xPos, 0, w), MathHelper.clamp(yPos, 0, h), updateCounter, this.getMana(), this.getMaxMana(), this.getCost());
		//		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
		//		}
	}

	private void setMana(float mana) {
		this.mana = mana;
	}

	private void setBonusMana(int bonusMana) {
		this.bonusMana = bonusMana;
	}

	private void setMaxMana(float maxMana) {
		this.maxMana = maxMana;
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
