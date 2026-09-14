package xzeroair.trinkets.client.entityRenders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import xzeroair.trinkets.client.entityRenders.wrapper.EntityWrapperPresentation;
import xzeroair.trinkets.client.entityRenders.wrapper.EntityWrapperPresentations;
import xzeroair.trinkets.entity.EntitySummonWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderSummonWrapper extends Render<EntitySummonWrapper> {

    public RenderSummonWrapper(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(@Nonnull EntitySummonWrapper wrapper, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!wrapper.isPerchedOnSummoner()) {
            this.renderWrapper(wrapper, x, y, z, entityYaw, partialTicks);
        }
    }

    public void renderOnShoulder(@Nonnull EntitySummonWrapper wrapper, double x, double y, double z, float entityYaw, float partialTicks) {
        this.renderWrapper(wrapper, x, y, z, entityYaw, partialTicks);
    }

    private void renderWrapper(@Nonnull EntitySummonWrapper wrapper, double x, double y, double z, float entityYaw, float partialTicks) {
        final EntityWrapperPresentation presentation = EntityWrapperPresentations.get(wrapper.getBehaviorTemplateId());
        if (presentation != null) {
            presentation.render(wrapper, this, x, y, z, entityYaw, partialTicks);
            return;
        }
        this.renderSource(wrapper, x, y, z, entityYaw, partialTicks);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void renderSource(@Nonnull EntitySummonWrapper wrapper, double x, double y, double z, float entityYaw, float partialTicks) {
        final EntityLivingBase source = wrapper.getSourceEntity();
        if (source == null) {
            return;
        }

        final Render sourceRenderer = this.renderManager.getEntityRenderObject(source);
        if (sourceRenderer == this) {
            return;
        }

        this.synchronizeRenderState(wrapper, source);
        sourceRenderer.doRender(source, x, y, z, entityYaw, partialTicks);
    }

    public void synchronizeRenderState(@Nonnull EntitySummonWrapper wrapper, @Nonnull EntityLivingBase source) {
        source.prevPosX = wrapper.prevPosX;
        source.prevPosY = wrapper.prevPosY;
        source.prevPosZ = wrapper.prevPosZ;
        source.lastTickPosX = wrapper.lastTickPosX;
        source.lastTickPosY = wrapper.lastTickPosY;
        source.lastTickPosZ = wrapper.lastTickPosZ;
        source.setPosition(wrapper.posX, wrapper.posY, wrapper.posZ);
        source.prevRotationYaw = wrapper.prevRotationYaw;
        source.prevRotationPitch = wrapper.prevRotationPitch;
        source.rotationYaw = wrapper.rotationYaw;
        source.rotationPitch = wrapper.rotationPitch;
        source.prevRenderYawOffset = wrapper.prevRenderYawOffset;
        source.renderYawOffset = wrapper.renderYawOffset;
        source.prevRotationYawHead = wrapper.prevRotationYawHead;
        source.rotationYawHead = wrapper.rotationYawHead;
        source.prevLimbSwingAmount = wrapper.prevLimbSwingAmount;
        source.limbSwingAmount = wrapper.limbSwingAmount;
        source.limbSwing = wrapper.limbSwing;
        source.hurtTime = wrapper.hurtTime;
        source.maxHurtTime = wrapper.maxHurtTime;
        source.deathTime = wrapper.deathTime;
        source.ticksExisted = wrapper.ticksExisted;
        source.onGround = wrapper.onGround;
        source.setSneaking(wrapper.isSneaking());
        source.setSprinting(wrapper.isSprinting());
        source.setInvisible(wrapper.isInvisible());
        source.setGlowing(wrapper.isGlowing());
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntitySummonWrapper entity) {
        return TextureMap.LOCATION_MISSING_TEXTURE;
    }

    public static final Factory FACTORY = new Factory();

    public static class Factory implements IRenderFactory<EntitySummonWrapper> {

        @Override
        public Render<? super EntitySummonWrapper> createRenderFor(RenderManager manager) {
            return new RenderSummonWrapper(manager);
        }
    }
}
