package xzeroair.trinkets.client.races;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRenderRaceHandler<T> {

    @SideOnly(Side.CLIENT)
    default void whileTransforming(EntityLivingBase entity) {
    }

    @SideOnly(Side.CLIENT)
    default void whileTransformed(EntityLivingBase entity) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderLayer(EntityLivingBase entity, RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderLayer(RenderLivingBase renderer, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderPlayerPost(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default <T extends EntityLivingBase> void doRenderLivingSpecialsPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default <T extends EntityLivingBase> void doRenderLivingSpecialsPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default <T extends EntityLivingBase> void doRenderLivingPre(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default <T extends EntityLivingBase> void doRenderLivingPost(EntityLivingBase entity, double x, double y, double z, RenderLivingBase<T> renderer, float partialTick) {
    }

    @SideOnly(Side.CLIENT)
    default void doRenderHand(EnumHand hand, ItemStack itemStack, float swingProgress, float interpolatedPitch, float equipProgress, float partialTicks) {
    }

    @SideOnly(Side.CLIENT)
    default void onClientTick() {
    }

}
