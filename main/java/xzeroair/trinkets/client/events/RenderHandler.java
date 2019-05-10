package xzeroair.trinkets.client.events;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.util.helpers.RenderHandlerHelper;

public class RenderHandler {

	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		//		if(!(Loader.isModLoaded("artemislib"))) {
		RenderHandlerHelper.renderPlayerPre(event);
		//		} else {
		//		}
	}

	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		//		if(!(Loader.isModLoaded("artemislib"))) {
		RenderHandlerHelper.renderPlayerPost(event);
		//		}else {
		//		}
	}

	@SubscribeEvent
	public void onRenderSpecialPre(RenderLivingEvent.Specials.Pre event) {
		//		if(!(Loader.isModLoaded("artemislib"))) {
		RenderHandlerHelper.renderSpecialPre(event);
		//		} else {
		//		}
	}

	@SubscribeEvent
	public void onRenderSpecialPost(RenderLivingEvent.Specials.Post event) {
		//		if(!(Loader.isModLoaded("artemislib"))) {
		RenderHandlerHelper.renderSpecialPost(event);
		//		} else {
		//		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		RenderHandlerHelper.renderWorld();
	}

}
