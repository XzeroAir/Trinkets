package xzeroair.trinkets.util.compat.enhancedvisuals;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class EnhancedVisualsRenderEvent {

    public static EnhancedVisualsRenderEvent instance = new EnhancedVisualsRenderEvent();

    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void FireParticlesEvent(team.creative.enhancedvisuals.api.event.FireParticlesEvent event) {

    }

    @SubscribeEvent
    public void EndermenEvent(team.creative.enhancedvisuals.api.event.SelectEndermanEvent event) {
        if (EnhancedVisualsCompat.isModActive() && (mc.player != null) && !event.isCanceled()) {
            if (TrinketHelper.entityHasAbility(mc.player, Reference.MODID + ":" + TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_STATIC)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void SplashEvent(team.creative.enhancedvisuals.api.event.SplashEvent event) {

    }

    @SubscribeEvent
    public void VisualExplosionEvent(team.creative.enhancedvisuals.api.event.VisualExplosionEvent event) {
        if (EnhancedVisualsCompat.isModActive() && (mc.player != null) && !event.isCanceled()) {
            if (Capabilities.getEntityProperties(mc.player, false, (prop, rtn) -> {
                IAbilityInterface ability = prop.getAbilityHandler().getAbility(Reference.MODID + ":" + TrinketsRegistryNames.ModAbilities.ENHANCED_VISUALS_BLUR);
                if (ability != null) {
                    if (ability instanceof IToggleAbility) {
                        return ((IToggleAbility) ability).isAbilityToggled();
                    }
                    return true;
                } else {
                    return rtn;
                }
            })) {
                event.setCanceled(true);
            }
        }
    }
}
