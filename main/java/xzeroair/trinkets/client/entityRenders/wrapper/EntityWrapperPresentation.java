package xzeroair.trinkets.client.entityRenders.wrapper;

import xzeroair.trinkets.client.entityRenders.RenderSummonWrapper;
import xzeroair.trinkets.entity.EntitySummonWrapper;

public interface EntityWrapperPresentation {

    void render(EntitySummonWrapper wrapper, RenderSummonWrapper renderer, double x, double y, double z, float entityYaw, float partialTicks);
}
