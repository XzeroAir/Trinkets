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

	//	@SubscribeEvent
	//	public void onRenderTick(RenderTickEvent event) {
	//		if ((mc.player != null) && (mc.player.world != null)) {
	//			if (TrinketsConfig.compat.enhancedvisuals) {
	//				if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketDragonsEye)) {
	//					if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
	//						//						if((VisualManager.getPersitentVisual(ToughAsNailsAddon.heat) != null) && VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).type.isEnabled()) {
	//						//							final float intensity = VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).getIntensity(event.renderTickTime);
	//						//							if((intensity > 0f)) {
	//						//								VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).reset();
	//						//							}
	//						//						}
	//						//					}
	//						if ((VisualManager.visuals.getValues(VisualCategory.splat) != null) && !(VisualManager.visuals.getValues(VisualCategory.splat).isEmpty())) {
	//							for (final Visual v : VisualManager.visuals.getValues(VisualCategory.splat)) {
	//								if (v.type == VisualTypeSplat.fire) {
	//									v.properties.height = 0;
	//									v.properties.width = 0;
	//								}
	//							}
	//						}
	//					}
	//					if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketEnderTiara)) {
	//						if ((VisualManager.getPersitentVisual(VisualType.slender) != null) && VisualManager.getPersitentVisual(VisualType.slender).type.isEnabled()) {
	//							final float intensity = VisualManager.getPersitentVisual(VisualType.slender).getIntensity(event.renderTickTime);
	//							if ((intensity > 0f)) {
	//								VisualManager.getPersitentVisual(VisualType.slender).reset();
	//							}
	//						}
	//					}
	//					if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketSea) && mc.player.isInWater()) {
	//						if ((VisualManager.getPersitentVisual(VisualType.blur) != null) && VisualManager.getPersitentVisual(VisualType.blur).type.isEnabled()) {
	//							final float intensity = VisualManager.getPersitentVisual(VisualType.blur).getIntensity(event.renderTickTime);
	//							if ((intensity > 0f) && (intensity < 20f)) {
	//								VisualManager.getPersitentVisual(VisualType.blur).reset();
	//							}
	//						}
	//					}
	//					if (TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketDamageShield)) {
	//						if ((VisualManager.getPersitentVisual(VisualType.blur) != null) && VisualManager.getPersitentVisual(VisualType.blur).type.isEnabled()) {
	//							final float intensity = VisualManager.getPersitentVisual(VisualType.blur).getIntensity(event.renderTickTime);
	//							if ((intensity > 0f) && (intensity <= (100 * TrinketsConfig.SERVER.DAMAGE_SHIELD.explosion_amount))) {
	//								VisualManager.getPersitentVisual(VisualType.blur).reset();
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//	}
}
