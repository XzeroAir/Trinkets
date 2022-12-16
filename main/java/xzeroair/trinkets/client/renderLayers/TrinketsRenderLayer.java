<<<<<<< Updated upstream
package xzeroair.trinkets.client.renderLayers;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketsRenderLayer implements LayerRenderer<EntityPlayer> {

	private boolean isSlim;
	private RenderPlayer renderer;

	public TrinketsRenderLayer(boolean slim, RenderPlayer render) {
		isSlim = slim;
		renderer = render;
	}

	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (player.isInvisible() || (player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) || !TrinketsConfig.CLIENT.rendering)
			return;
		Minecraft.getMinecraft().profiler.startSection("Trinkets Render Layer");
		Minecraft.getMinecraft().profiler.startSection("Race Render Layer");
		Capabilities.getEntityProperties(player, prop -> {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			prop.onRender(renderer, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
		});
		Minecraft.getMinecraft().profiler.endSection();

		Minecraft.getMinecraft().profiler.startSection("Item Render Layer");
		TrinketHelper.applyToAccessories(player, stack -> {
			if (stack.getItem() instanceof IAccessoryInterface) {
				final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
				GlStateManager.pushMatrix();
				GlStateManager.color(1F, 1F, 1F, 1F);
				trinket.playerRenderLayer(stack, player, renderer, isSlim, partialTicks, scale);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.popMatrix();
			}
		});
		Minecraft.getMinecraft().profiler.endSection();
		Minecraft.getMinecraft().profiler.endSection();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
=======
package xzeroair.trinkets.client.renderLayers;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketsRenderLayer implements LayerRenderer<EntityPlayer> {

	private boolean isSlim;
	private RenderPlayer renderer;

	public TrinketsRenderLayer(boolean slim, RenderPlayer render) {
		isSlim = slim;
		renderer = render;
	}

	@Override
	public void doRenderLayer(@Nonnull EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (player.isInvisible() || (player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) || !TrinketsConfig.CLIENT.rendering)
			return;
		Minecraft.getMinecraft().profiler.startSection("Trinkets Render Layer");
		Minecraft.getMinecraft().profiler.startSection("Race Render Layer");
		Capabilities.getEntityProperties(player, prop -> {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			prop.onRender(renderer, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
		});
		Minecraft.getMinecraft().profiler.endSection();

		Minecraft.getMinecraft().profiler.startSection("Item Render Layer");
		TrinketHelper.applyToAccessories(player, stack -> {
			if (stack.getItem() instanceof IAccessoryInterface) {
				final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
				GlStateManager.pushMatrix();
				GlStateManager.color(1F, 1F, 1F, 1F);
				trinket.playerRenderLayer(stack, player, renderer, isSlim, partialTicks, scale);
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.popMatrix();
			}
		});
		Minecraft.getMinecraft().profiler.endSection();
		Minecraft.getMinecraft().profiler.endSection();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
>>>>>>> Stashed changes
}