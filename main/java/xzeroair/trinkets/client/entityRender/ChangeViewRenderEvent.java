package xzeroair.trinkets.client.entityRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;

public class ChangeViewRenderEvent {

	private final Minecraft mc = Minecraft.getMinecraft();
	private EntityRenderer renderer, prevRenderer;

	@SubscribeEvent
	public void RenderTick(RenderTickEvent event) {
		EntityPlayer player = this.mc.player;
		if (player != null) {
			if (TrinketHelper.AccessoryCheck(player, TrinketHelper.SizeTrinkets) || !EntityRaceHelper.getRace(player).contentEquals("none")) {
				if (this.renderer == null) {
					this.renderer = new TrinketsViewRenderEntity(this.mc, this.mc.getResourceManager(), 4F);
				}
				if (this.mc.entityRenderer != this.renderer) {
					// be sure to store the previous renderer
					this.prevRenderer = this.mc.entityRenderer;
					this.mc.entityRenderer = this.renderer;
				}
			} else if ((this.prevRenderer != null) && (this.mc.entityRenderer != this.prevRenderer)) {
				// reset the renderer
				this.mc.entityRenderer = this.prevRenderer;
				this.renderer = null;
				this.prevRenderer = null;
			}
		}
	}

}
