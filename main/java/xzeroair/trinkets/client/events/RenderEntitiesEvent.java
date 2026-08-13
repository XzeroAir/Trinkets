package xzeroair.trinkets.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;

import javax.annotation.Nonnull;

public class RenderEntitiesEvent {

    @SubscribeEvent
    public void renderPlayerPre(@Nonnull RenderPlayerEvent.Pre event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, cap -> {
            GlStateManager.pushMatrix();
            cap.getRaceHandler().getRaceRenderer().doRenderPlayerPre(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });

    }

    @SubscribeEvent
    public void renderPlayerPost(@Nonnull RenderPlayerEvent.Post event) {
        final EntityPlayer player = event.getEntityPlayer();
        Capabilities.getEntityProperties(player, cap -> {
            cap.getRaceHandler().getRaceRenderer().doRenderPlayerPost(player, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            GlStateManager.popMatrix();
        });
    }

    @SubscribeEvent
    public void onRenderSpecialPre(@Nonnull RenderLivingEvent.Specials.Pre event) {
        final EntityLivingBase entity = event.getEntity();
        Capabilities.getEntityProperties(entity, cap -> {
            cap.getRaceHandler().getRaceRenderer().doRenderLivingSpecialsPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });
    }

    @SubscribeEvent
    public void onRenderSpecialPost(@Nonnull RenderLivingEvent.Specials.Post event) {
        final EntityLivingBase entity = event.getEntity();
        Capabilities.getEntityProperties(entity, cap -> {
            cap.getRaceHandler().getRaceRenderer().doRenderLivingSpecialsPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
        });
    }

    @SubscribeEvent
    public void onRenderLivingPre(@Nonnull RenderLivingEvent.Pre event) {
        final EntityLivingBase entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            Capabilities.getEntityProperties(entity, cap -> {
                cap.getRaceHandler().getRaceRenderer().doRenderLivingPre(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            });
        }
    }

    @SubscribeEvent
    public void onRenderLivingPost(@Nonnull RenderLivingEvent.Post event) {
        final EntityLivingBase entity = event.getEntity();
        if (!(entity instanceof EntityPlayer)) {
            Capabilities.getEntityProperties(entity, cap -> {
                cap.getRaceHandler().getRaceRenderer().doRenderLivingPost(entity, event.getX(), event.getY(), event.getZ(), event.getRenderer(), event.getPartialRenderTick());
            });
        }
    }

    @SubscribeEvent
    public void onRenderSpecificHand(RenderSpecificHandEvent event) {
        final EntityPlayerSP player = Minecraft.getMinecraft().player;
        Capabilities.getEntityProperties(player, cap -> {
            cap.getRaceHandler().getRaceRenderer().doRenderHand(event.getHand(), event.getItemStack(), event.getSwingProgress(), event.getInterpolatedPitch(), event.getEquipProgress(), event.getPartialTicks());
        });
    }

}
