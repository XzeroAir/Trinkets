package xzeroair.trinkets.mixin;

import net.minecraft.client.renderer.entity.Render;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Mixin;
import xzeroair.trinkets.client.renderLayers.IRenderShadowSize;

@Mixin(Render.class)
public interface PlayerShadowMixin extends IRenderShadowSize {

    @Accessor("shadowSize")
    @Override
    void trinkets_setShadowSize(float shadowSize);
}
