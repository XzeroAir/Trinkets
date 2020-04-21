package xzeroair.trinkets.util.compat.enhancedvisuals;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class EnhancedVisualsRenderEvent {

	public static EnhancedVisualsRenderEvent instance = new EnhancedVisualsRenderEvent();

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void FireParticlesEvent(team.creative.enhancedvisuals.api.event.FireParticlesEvent event) {

	}

	@SubscribeEvent
	public void EndermenEvent(team.creative.enhancedvisuals.api.event.SelectEndermanEvent event) {
		if (TrinketsConfig.compat.enhancedvisuals && (mc.player != null) && !event.isCanceled()) {
			if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketEnderTiara)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void SplashEvent(team.creative.enhancedvisuals.api.event.SplashEvent event) {

	}

	@SubscribeEvent
	public void VisualExplosionEvent(team.creative.enhancedvisuals.api.event.VisualExplosionEvent event) {
		if (TrinketsConfig.compat.enhancedvisuals && (mc.player != null) && !event.isCanceled()) {
			if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketDamageShield)) {
				event.setCanceled(true);
			}
		}
	}
}
