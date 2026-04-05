package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xzeroair.trinkets.entity.AreaEffectEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderAreaEffectEntity extends Render<AreaEffectEntity> {

    public RenderAreaEffectEntity(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(@Nonnull AreaEffectEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public void doRenderShadowAndFire(@Nonnull Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        super.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull AreaEffectEntity entity) {
        return null;
    }

    public static final RenderAreaEffectEntity.Factory FACTORY = new RenderAreaEffectEntity.Factory();

    public static class Factory implements IRenderFactory<AreaEffectEntity> {

        @Override
        public Render<? super AreaEffectEntity> createRenderFor(RenderManager manager) {
            return new RenderAreaEffectEntity(manager);
        }

    }
}