package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.entityRenders.RenderSummonWrapper;
import xzeroair.trinkets.entity.EntitySummonWrapper;

import javax.annotation.Nonnull;

public class RenderEntitiesEvent {

    public static final RenderEntitiesEvent instance = new RenderEntitiesEvent();

    @SubscribeEvent
    public void renderPlayerPre(@Nonnull RenderPlayerEvent.Pre event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, cap -> {
            GlStateManager.pushMatrix();
            cap.getRaceHandler()
               .getRaceRenderer()
               .doRenderPlayerPre(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });

    }

    @SubscribeEvent
    public void renderPlayerPost(@Nonnull RenderPlayerEvent.Post event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, cap -> {
            cap.getRaceHandler()
               .getRaceRenderer()
               .doRenderPlayerPost(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            GlStateManager.popMatrix();
        });
        this.renderShoulderCompanions(event, player);
    }

    @SuppressWarnings("rawtypes")
    private void renderShoulderCompanions(RenderPlayerEvent.Post event, EntityPlayer player) {
        final float partialTicks = event.getPartialRenderTick();
        final float bodyYaw = player.prevRenderYawOffset + MathHelper.wrapDegrees(player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        final float yaw = bodyYaw * 0.017453292F;
        for (final EntitySummonWrapper wrapper : player.world.getEntitiesWithinAABB(EntitySummonWrapper.class, player.getEntityBoundingBox()
                                                                                                                     .grow(1.0D))) {
            if (!wrapper.isPerchedOn(player)) {
                continue;
            }
            final Render renderer = event.getRenderer().getRenderManager().getEntityRenderObject(wrapper);
            if (!(renderer instanceof RenderSummonWrapper)) {
                continue;
            }
            final double shoulderOffset = player.width * 0.6D * (wrapper.isPerchedOnLeftShoulder() ? 1D : -1D);
            final double x = event.getX() + MathHelper.cos(yaw) * shoulderOffset;
            final double y = event.getY() + player.height * 0.75D;
            final double z = event.getZ() + MathHelper.sin(yaw) * shoulderOffset;
            ((RenderSummonWrapper) renderer).renderOnShoulder(wrapper, x, y, z, bodyYaw, partialTicks);
        }
    }

    @SubscribeEvent
    public void onRenderSpecialPre(@Nonnull RenderLivingEvent.Specials.Pre event) {
        final EntityLivingBase entity = event.getEntity();
        Capabilities.getEntityProperties(entity, cap -> {
            cap.getRaceHandler()
               .getRaceRenderer()
               .doRenderLivingSpecialsPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });
    }

    @SubscribeEvent
    public void onRenderSpecialPost(@Nonnull RenderLivingEvent.Specials.Post event) {
        final EntityLivingBase entity = event.getEntity();
        Capabilities.getEntityProperties(entity, cap -> {
            cap.getRaceHandler()
               .getRaceRenderer()
               .doRenderLivingSpecialsPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });
    }

    @SubscribeEvent
    public void onRenderLivingPre(@Nonnull RenderLivingEvent.Pre event) {
        final EntityLivingBase entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            Capabilities.getEntityProperties(entity, cap -> {
                cap.getRaceHandler()
                   .getRaceRenderer()
                   .doRenderLivingPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            });
        }
    }

    @SubscribeEvent
    public void onRenderLivingPost(@Nonnull RenderLivingEvent.Post event) {
        final EntityLivingBase entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            Capabilities.getEntityProperties(entity, cap -> {
                cap.getRaceHandler()
                   .getRaceRenderer()
                   .doRenderLivingPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            });
        }
    }

    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        Capabilities.getEntityProperties(player, cap -> {
            cap.getRaceHandler()
               .getRaceRenderer()
               .doRenderHand(event.getHand(), event.getItemStack(), event.getSwingProgress(), event.getInterpolatedPitch(), event.getEquipProgress(), event.getPartialTicks());
        });
    }

}
