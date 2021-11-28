package xzeroair.trinkets.client.renderLayers;

import org.lwjgl.opengl.GL11;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.ITrinketContainerHandler;
import xzeroair.trinkets.capabilities.InventoryContainerCapability.TrinketContainerProvider;
import xzeroair.trinkets.capabilities.race.EntityProperties;
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
		if (player.isInvisible() || (player.getActivePotionEffect(MobEffects.INVISIBILITY) != null) || !TrinketsConfig.CLIENT.rendering) {
			return;
		}
		Minecraft.getMinecraft().profiler.startSection("Trinkets Render Layer");

		final EntityProperties cap = Capabilities.getEntityRace(player);
		if (cap != null) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F, 1F);
			cap.onRender(renderer, isSlim, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.popMatrix();
		}

		if (Loader.isModLoaded("baubles")) {
			final IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
			for (int i = 0; i < baubles.getSlots(); i++) {
				final ItemStack stack = baubles.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					GlStateManager.pushMatrix();
					GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
					GlStateManager.color(1F, 1F, 1F, 1F);
					trinket.playerRender(stack, player, renderer, isSlim, partialTicks, scale, false);
					GlStateManager.color(1F, 1F, 1F, 1F);
					GlStateManager.popMatrix();
				}
			}
		}
		final ITrinketContainerHandler Trinket = player.getCapability(TrinketContainerProvider.containerCap, null);
		if (Trinket != null) {
			for (int i = 0; i < Trinket.getSlots(); i++) {
				final ItemStack stack = Trinket.getStackInSlot(i);
				if (stack.getItem() instanceof IAccessoryInterface) {
					final IAccessoryInterface trinket = (IAccessoryInterface) stack.getItem();
					GlStateManager.pushMatrix();
					GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
					GlStateManager.color(1F, 1F, 1F, 1F);
					trinket.playerRender(stack, player, renderer, isSlim, partialTicks, scale, false);
					GlStateManager.color(1F, 1F, 1F, 1F);
					GlStateManager.popMatrix();
				}
			}
		}
		Minecraft.getMinecraft().profiler.endSection();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}