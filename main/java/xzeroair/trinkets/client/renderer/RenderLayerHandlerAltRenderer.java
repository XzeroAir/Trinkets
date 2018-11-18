package xzeroair.trinkets.client.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Main;
import xzeroair.trinkets.client.model.Wings;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.helpers.TrinketHelper;
import xzeroair.trinkets.util.helpers.TrinketHelper.TrinketType;

@SideOnly(Side.CLIENT)
public class RenderLayerHandlerAltRenderer implements LayerRenderer<EntityPlayer> {

	private static PlayerRenderAlt render;

	public RenderLayerHandlerAltRenderer(PlayerRenderAlt renderIn) {

		render = renderIn;

	}

	@Override
	public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(TrinketHelper.getBaubleType(player, TrinketType.rings) == ModItems.small_ring) {
			GlStateManager.pushMatrix();
			if (player.isSneaking()) {
				GlStateManager.translate(0F, 0.2F, 0F);
				GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			}
			ModelBiped baseModel = render.getMainModel();
			ModelBiped wings = Main.proxy.getModel("wings");
			//			baseModel.bipedBody.render(scale);
			render.bindTexture(new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png"));
			//			wings.copyModelAngles(baseModel.bipedBody, wings.bipedBody);
			ModelBiped wing = new Wings(0.0F);
			//			wing.copyModelAngles(baseModel.bipedBody, wings.bipedBody);
			wing.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();
		}
		if(TrinketHelper.getBaubleType(player, TrinketType.head) == ModItems.ender_tiara) {
			GlStateManager.pushMatrix();
			if (player.isSneaking()) {
				GlStateManager.translate(0F, 0.2F, 0F);
			}
			ModelBiped tiara = Main.proxy.getModel("tiara");
			ModelBiped baseModel = render.getMainModel();
			render.bindTexture(new ResourceLocation(Reference.MODID + ":textures/ender_tiara_model.png"));
			tiara.copyModelAngles(baseModel.bipedHead, tiara.bipedHead);
			//			tiara.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			tiara.bipedHead.render(scale);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}