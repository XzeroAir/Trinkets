package xzeroair.trinkets.client.entityRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.EntityRaceHelper;

public class ChangeViewRenderEvent {

	private final Minecraft mc = Minecraft.getMinecraft();
	private EntityRenderer renderer, prevRenderer;

	@SubscribeEvent
	public void RenderTick(RenderTickEvent event) {
		if (TrinketsConfig.CLIENT.entityRenderer) {
			EntityPlayer player = mc.player;
			if (player != null) {
				if (TrinketHelper.AccessoryCheck(player, TrinketHelper.SizeTrinkets) || !EntityRaceHelper.getRace(player).contentEquals("none")) {
					if (renderer == null) {
						renderer = new TrinketsViewRenderEntity(mc, mc.getResourceManager());
					}
					if (mc.entityRenderer != renderer) {
						// be sure to store the previous renderer
						prevRenderer = mc.entityRenderer;
						mc.entityRenderer = renderer;
					}
				} else if ((prevRenderer != null) && (mc.entityRenderer != prevRenderer)) {
					// reset the renderer
					mc.entityRenderer = prevRenderer;
					renderer = null;
					prevRenderer = null;
				}
			}
		}
	}

}
