package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityViciousStrike;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.helpers.DrawingHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketFaelisClaws extends AccessoryBase {

	public static final ClientConfigFaelisClaw clientConfig = TrinketsConfig.CLIENT.items.FAELIS_CLAW;
	public static final ConfigFaelisClaw serverConfig = TrinketsConfig.SERVER.Items.FAELIS_CLAW;

	public TrinketFaelisClaws(String name) {
		super(name);
		this.setUUID("4959ec73-142d-4b82-bd0d-cd6cd7431611");
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new OptionEntry("clawbleed", serverConfig.bleed, serverConfig.bleedDuration);
		return helper.formatAddVariables(translation, key);
	}

	@Override
	public String[] getAttributeConfig() {
		return serverConfig.attributes;
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		if (serverConfig.bleed) {
			abilities.add(new AbilityViciousStrike());
		}
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
	public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
		if (!clientConfig.doRender) {
			return;
		}

		int count = TrinketHelper.countAccessories(player, s -> !s.isEmpty() && (s.getItem().getRegistryName().compareTo(stack.getItem().getRegistryName()) == 0));
		final float offsetX = isSlim ? -12.4F : -18.6F;
		final float offsetY = 61F;
		final float offsetZ = -21F;
		final float bS = 0.16f;
		boolean flag = (serverConfig.compat.baubles.equip_multiple);
		boolean flag1 = flag ? count > 0 : true;
		boolean flag2 = flag ? count > 1 : true;
		if (flag1) {
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
		}
		if (flag2) {
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

}
