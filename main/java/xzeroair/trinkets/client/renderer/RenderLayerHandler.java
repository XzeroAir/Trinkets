package xzeroair.trinkets.client.renderer;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
public class RenderLayerHandler implements LayerRenderer<EntityPlayer> {

	private static RenderPlayer render;

	public RenderLayerHandler(RenderPlayer renderIn) {

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
			render.bindTexture(new ResourceLocation(Reference.MODID + ":" + "textures/fairy_wings.png"));
			ModelBiped wing = new Wings(0.0F);
			//			wing.copyModelAngles(baseModel.bipedBody, wings.bipedBody);
			wings.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.popMatrix();
		}
		if(TrinketHelper.getBaubleType(player, TrinketType.head) == ModItems.ender_tiara) {
			GlStateManager.pushMatrix();
			if (player.isSneaking()) {
				GlStateManager.translate(0F, 0.2F, 0F);
			}
			//			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			//			renderItem.getItemModelMesher().register(ModItems.ender_tiara, 0, new ModelResourceLocation(Reference.MODID + ":" + "tiara3test", "inventory"));
			ModelBiped tiara = Main.proxy.getModel("tiara");
			ModelBiped baseModel = render.getMainModel();
			render.bindTexture(new ResourceLocation(Reference.MODID + ":textures/ender_tiara_model.png"));
			//			tiara.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			tiara.copyModelAngles(baseModel.bipedHead, tiara.bipedHead);
			tiara.bipedHead.render(scale);
			GlStateManager.popMatrix();
		}
		if(TrinketHelper.getBaubleType(player, TrinketType.amulet) == ModItems.fish_stone) {
			GlStateManager.pushMatrix();
			float n = 0.75F; //Neck
			GlStateManager.scale(0.25F, 0.25F, 0.25F);
			GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
			if (player.isSneaking()) {
				GlStateManager.translate(0F, -n, 0F);
				GlStateManager.rotate(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
			}
			if(player.inventory.armorItemInSlot(2).isEmpty()) {
				GlStateManager.translate(0F, -n, 0.6F);
			} else {
				GlStateManager.translate(0F, -n, 0.8F);
			}
			Minecraft.getMinecraft().getRenderItem().renderItem(TrinketHelper.getBaubleTypeStack(player, TrinketType.amulet), ItemCameraTransforms.TransformType.NONE);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	public static void initRender() {
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		//				RenderPlayer render;
		render = skinMap.get("default");
		render.addLayer(new RenderLayerHandler(render));

		render = skinMap.get("slim");
		render.addLayer(new RenderLayerHandler(render));

		//		for (RenderPlayer playerRender :
		//			Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
		//
		//			playerRender.addLayer(new RenderLayerHandler(render));
		//		}

		//		Render<Entity> renderer =
		//				Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(EntityPlayer.class);
		//
		//		if (renderer instanceof RenderLivingBase) {
		//
		//			((RenderLivingBase<?>) renderer).addLayer(new RenderLayerHandler(render));
		//		}
	}
}
