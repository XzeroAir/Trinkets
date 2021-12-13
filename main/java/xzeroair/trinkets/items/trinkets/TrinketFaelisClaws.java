package xzeroair.trinkets.items.trinkets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityViciousStrike;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Claw;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.helpers.DrawingHelper;

public class TrinketFaelisClaws extends AccessoryBase {

	public static final Claw clientConfig = TrinketsConfig.CLIENT.items.FAELIS_CLAW;
	public static final ConfigFaelisClaw serverConfig = TrinketsConfig.SERVER.Items.FAELIS_CLAW;

	public TrinketFaelisClaws(String name) {
		super(name);
		this.setUUID("4959ec73-142d-4b82-bd0d-cd6cd7431611");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void initAbilities(EntityLivingBase entity) {
		if (serverConfig.bleed) {
			this.addAbility(entity, Abilities.viciousStrike, new AbilityViciousStrike());
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/claws.png");

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRender(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale, boolean isTrinket) {
		if (!clientConfig.doRender) {
			return;
		}

		final float offsetX = isSlim ? -12.4F : -18.6F;
		final float offsetY = 61F;
		final float offsetZ = -21F;
		final float bS = 0.16f;
		GlStateManager.pushMatrix();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableCull();
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedLeftArm.postRender(scale);
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		GlStateManager.translate(-offsetX, offsetY, offsetZ);
		GlStateManager.rotate(-90F, 0F, 1F, 0F);
		DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedRightArm.postRender(scale);
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		GlStateManager.translate(offsetX, offsetY, offsetZ);
		GlStateManager.rotate(-90F, 0F, 1F, 0F);
		DrawingHelper.Draw(0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32);
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

}
