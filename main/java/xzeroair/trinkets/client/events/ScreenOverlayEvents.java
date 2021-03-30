package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

	private int costResetCounter = 0;
	private int updateCounter = 0;

	public void SyncMana(float mana, int bonusMana, float maxMana) {
		this.setMana(mana);
		this.setBonusMana(bonusMana);
		this.setMaxMana(maxMana);
	}

	public void SyncCost(float cost) {
		this.setCost(cost);
	}

	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent event) {

	}

	@SubscribeEvent
	public void renderGameOverlayTextEvent(RenderGameOverlayEvent.Text event) {

	}

	@SubscribeEvent
	public void renderGameOverlayPreEvent(RenderGameOverlayEvent.Pre event) {

	}

	@SubscribeEvent
	public void renderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
		if (event.isCancelable() || !((event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) || (event.getType() == RenderGameOverlayEvent.ElementType.JUMPBAR)) || !TrinketsConfig.CLIENT.MPBar.shown || !TrinketsConfig.SERVER.mana.mana_enabled) {
			return;
		}
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
		final int l = (event.getResolution().getScaledWidth() / 2) - 91;
		final int i1 = (event.getResolution().getScaledWidth() / 2);
		final int j1 = event.getResolution().getScaledHeight() / 2;
		if (this.getCost() > 0) {
			++costResetCounter;
			if (costResetCounter >= 60) {
				this.setCost(0);
				costResetCounter = 0;
			}
		}
		// Y = a(1 - r)x
		double x = (TrinketsConfig.CLIENT.MPBar.X);
		double y = (TrinketsConfig.CLIENT.MPBar.Y);
		int scaleF = event.getResolution().getScaleFactor();
		int xPos = (int) (w * x);
		//		System.out.println(h * scaleF);
		//		int nH = (h * scaleF);
		//		double HeightPer = (nH * 100) / nH;
		//		System.out.println(HeightPer);
		//		float perY = ((y * 100) / nH) * 0.01F;
		//		float perY2 = nH * (perY * 0.01F);
		//		(this.getDefaultHeight() * (this.getSize() * 0.01F));
		//		 ((y * 100) / (h * scaleF))
		//		System.out.println(((yPtest * 100) / yPtest));
		//		System.out.println(nH * perY);
		int yPos = (int) (h * y);//(int) (h * perY);
		//		System.out.println(((y * 100) / yPtest));
		//		int m = (int) (((int) (mana * barWidth) / maxMana) * 1);
		//		System.out.println(yPos + " " + nH + " " + perY);
		GlStateManager.pushMatrix();
		manaGui.renderManaGui(MathHelper.clamp(xPos, 0, w), MathHelper.clamp(yPos, 0, h), updateCounter, this.getMana(), this.getMaxMana(), this.getCost());
		GlStateManager.popMatrix();
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
