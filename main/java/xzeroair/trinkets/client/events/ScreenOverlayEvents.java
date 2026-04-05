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
    private final ManaGui manaGui = new ManaGui(this.mc);

    private float mana = 0;
    private float maxMana = 0;

    private float manaCost = 0;

    private int updateCounter = 0;

    public void SyncMana(float mana, int bonusMana, float maxMana) {
        this.setMana(mana);
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

    @SubscribeEvent
    public void renderGameOverlayPreEvent(RenderGameOverlayEvent.Pre event) {
        if (TrinketsConfig.CLIENT.MANA_BAR_HUD.rendLocPre) {
            this.renderManaBar(event);
        }
    }

    @SubscribeEvent
    public void renderGameOverlayPostEvent(RenderGameOverlayEvent.Post event) {
        if (!TrinketsConfig.CLIENT.MANA_BAR_HUD.rendLocPre) {
            this.renderManaBar(event);
        }
    }

    private void renderManaBar(RenderGameOverlayEvent event) {
        //TODO Make sure this works properly
        if (event.isCanceled() || !(event.getType() == RenderGameOverlayEvent.ElementType.ALL) || !TrinketsConfig.CLIENT.MANA_BAR_HUD.shown || !TrinketsConfig.SERVER.MAGIC.mana_enabled) {
            return;
        }
        final MagicStats stats = Capabilities.getMagicStats(Minecraft.getMinecraft().player);
        if (stats != null) {
            this.setMana(stats.getMana());
            this.setMaxMana(stats.getMaxMana());
        }
        if (!this.needMana() && (this.getCost() <= 0)) {
            if (this.updateCounter()) {
                if (!(Minecraft.getMinecraft().currentScreen instanceof ManaHud)) {
                    if ((this.getMaxMana() <= 0) || !TrinketsConfig.CLIENT.MANA_BAR_HUD.always_shown) {
                        return;
                    }
                }
            }
        } else {
            this.resetCounter();
        }
        final int h = event.getResolution().getScaledHeight();
        final int w = event.getResolution().getScaledWidth();
        final double x = (TrinketsConfig.CLIENT.MANA_BAR_HUD.translatedX);
        final double y = (TrinketsConfig.CLIENT.MANA_BAR_HUD.translatedY);
        final int xPos = (int) Math.round(w * x);
        final int yPos = (int) Math.round(h * y);
        GlStateManager.pushMatrix();
        this.manaGui.renderManaGui(event, MathHelper.clamp(xPos, 0, w), MathHelper.clamp(yPos, 0, h), this.updateCounter, this.getMana(), this.getMaxMana(), this.getCost());
        GlStateManager.popMatrix();
    }

    private boolean updateCounter() {
        if (this.updateCounter < 80) {
            ++this.updateCounter;
        }
        return this.updateCounter >= 80;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    private void resetCounter() {
        this.updateCounter = 0;
    }

    private void setMana(float mana) {
        if (this.mana != mana) {
            this.mana = mana;
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
        return this.mana;
    }

    private float getMaxMana() {
        return this.maxMana;
    }

    private float getCost() {
        return this.manaCost;
    }

    private void setCost(float cost) {
        this.manaCost = cost;
    }
}
