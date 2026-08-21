package xzeroair.trinkets.mixin;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.util.compat.SwimmingSizeCompat;

import javax.annotation.Nullable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin {
    @Inject(method = "updateSize", at = @At("HEAD"))
    private void trinkets_updateRaceSizeProgression(CallbackInfo ci) {
        final EntityProperties properties = Capabilities.getEntityProperties((EntityPlayer) (Object) this);
        if (properties != null) {
            properties.getRaceHandler().updateSize();
        }
    }

    @ModifyVariable(method = "updateSize", at = @At("STORE"), index = 1)
    private float trinkets_useAdjustedRaceWidth(float vanillaWidth) {
        final EntityRacePropertiesHandler raceHandler = this.trinkets_getActiveRaceHandler();
        return raceHandler == null ? vanillaWidth : raceHandler.getAdjustedWidth();
    }

    @ModifyVariable(method = "updateSize", at = @At("STORE"), index = 2)
    private float trinkets_useAdjustedRaceHeight(float vanillaHeight) {
        final EntityRacePropertiesHandler raceHandler = this.trinkets_getActiveRaceHandler();
        return raceHandler == null ? vanillaHeight : raceHandler.getAdjustedHeight();
    }

    @Unique
    @Nullable
    private EntityRacePropertiesHandler trinkets_getActiveRaceHandler() {
        final EntityProperties properties = Capabilities.getEntityProperties((EntityPlayer) (Object) this);
        if (properties == null) {
            return null;
        }
        final EntityRacePropertiesHandler raceHandler = properties.getRaceHandler();
        return (raceHandler.isTransforming() || raceHandler.isTransformed()) && !SwimmingSizeCompat.isSwimming(properties.getEntity()) ? raceHandler : null;
    }
}
