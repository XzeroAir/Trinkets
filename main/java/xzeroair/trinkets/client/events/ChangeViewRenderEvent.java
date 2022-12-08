package xzeroair.trinkets.client.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ChangeViewRenderEvent {

	//	private final Minecraft mc = Minecraft.getMinecraft();
	//	private EntityRenderer renderer, prevRenderer;

	@SubscribeEvent
	public void RenderTick(RenderTickEvent event) {
		//		if (TrinketsConfig.CLIENT.entityRenderer) {
		//			EntityPlayer player = mc.player;
		//			if (player != null) {
		//				EntityProperties prop = Capabilities.getEntityRace(player);
		//				boolean flag = (prop != null) && !prop.getCurrentRace().equals(EntityRaces.none) && !prop.isNormalHeight();
		//				if (flag) {
		//					if (renderer == null) {
		//						renderer = new PlayerRaceCameraRenderer(mc, mc.getResourceManager());
		//						//						renderer = new PlayerRaceCameraRendererOptifine(mc, mc.getResourceManager());
		//					}
		//					if (mc.entityRenderer != renderer) {
		//						// be sure to store the previous renderer
		//						prevRenderer = mc.entityRenderer;
		//						mc.entityRenderer = renderer;
		//					}
		//				} else if ((prevRenderer != null) && (mc.entityRenderer != prevRenderer)) {
		//					// reset the renderer
		//					mc.entityRenderer = prevRenderer;
		//					renderer = null;
		//					prevRenderer = null;
		//				}
		//			}
		//		}
	}

}
