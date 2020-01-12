package xzeroair.trinkets.util.compat.enhancedvisuals;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.addon.toughasnails.ToughAsNailsAddon;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualTypeSplat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.TrinketsConfig;

public class EnhancedVisualsRenderEvent {

	Minecraft mc = Minecraft.getMinecraft();

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if((mc.player != null) && (mc.player.world != null)) {
			if(TrinketsConfig.compat.enhancedvisuals) {
				if(TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketDragonsEye)) {
					if(TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
						if((VisualManager.getPersitentVisual(ToughAsNailsAddon.heat) != null) && VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).type.isEnabled()) {
							final float intensity = VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).getIntensity(event.renderTickTime);
							if((intensity > 0f)) {
								VisualManager.getPersitentVisual(ToughAsNailsAddon.heat).reset();
							}
						}
					}
					if((VisualManager.visuals.getValues(VisualCategory.splat) != null) && !(VisualManager.visuals.getValues(VisualCategory.splat).isEmpty())) {
						for(final Visual v:VisualManager.visuals.getValues(VisualCategory.splat)) {
							if(v.type == VisualTypeSplat.fire) {
								System.out.println("This Running?");
								v.properties.height = 0;
								v.properties.width = 0;
							}
						}
					}
				}
				if(TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketEnderTiara)) {
					if((VisualManager.getPersitentVisual(VisualType.slender) != null) && VisualManager.getPersitentVisual(VisualType.slender).type.isEnabled()) {
						final float intensity = VisualManager.getPersitentVisual(VisualType.slender).getIntensity(event.renderTickTime);
						if((intensity > 0f)) {
							VisualManager.getPersitentVisual(VisualType.slender).reset();
						}
					}
				}
				if(TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketSea) && mc.player.isInWater()) {
					if((VisualManager.getPersitentVisual(VisualType.blur) != null) && VisualManager.getPersitentVisual(VisualType.blur).type.isEnabled()) {
						final float intensity = VisualManager.getPersitentVisual(VisualType.blur).getIntensity(event.renderTickTime);
						if((intensity > 0f) && (intensity < 20f)) {
							VisualManager.getPersitentVisual(VisualType.blur).reset();
						}
					}
				}
				if(TrinketHelper.AccessoryCheck(mc.player, ModItems.trinkets.TrinketDamageShield)) {
					if((VisualManager.getPersitentVisual(VisualType.blur) != null) && VisualManager.getPersitentVisual(VisualType.blur).type.isEnabled()) {
						final float intensity = VisualManager.getPersitentVisual(VisualType.blur).getIntensity(event.renderTickTime);
						if((intensity > 0f) && (intensity <= (100*TrinketsConfig.SERVER.DAMAGE_SHIELD.explosion_amount))) {
							VisualManager.getPersitentVisual(VisualType.blur).reset();
						}
					}
				}
			}
		}
	}
}
