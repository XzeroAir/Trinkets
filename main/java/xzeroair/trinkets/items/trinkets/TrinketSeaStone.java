package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWaterAffinity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityParasitesImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityThirstImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigSeaStone;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketSeaStone extends AccessoryBase {

	public static final ConfigSeaStone serverConfig = TrinketsConfig.SERVER.Items.SEA_STONE;
	public static final ClientConfigSeaStone clientConfig = TrinketsConfig.CLIENT.items.SEA_STONE;

	public TrinketSeaStone(String name) {
		super(name);
		this.setUUID("6029aecd-318e-4b45-8c36-2ddd7f481e36");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "bubbles", serverConfig.underwater_breathing);
		final KeyEntry key1 = new OptionEntry("bubbles", true, serverConfig.underwater_breathing ? 10 + "" : 1 + "");
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "betterswimming", serverConfig.Swim_Tweaks);
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "tanthirst", tan && serverConfig.compat.tan.prevent_thirst);
		return helper.formatAddVariables(translation, key, key1, key2, key3);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityWaterAffinity());
		final boolean simplediff = (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || simplediff;
		if (tan && serverConfig.compat.tan.prevent_thirst) {
			abilities.add(new AbilityThirstImmunity());
			if (simplediff) {
				abilities.add(new AbilityParasitesImmunity());
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
		if (!clientConfig.doRender) {
			return;
		}
		final float offsetY = 0.16F;
		final float offsetZ = 0.14F;
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedBody.postRender(scale);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(0F, -offsetY, offsetZ);
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(0F, 0, -(offsetZ - 0.2F));
		}
		final float bS = 3f;
		GlStateManager.scale(scale * bS, scale * bS, scale * bS);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation worn = new ModelResourceLocation(this.getRegistryName().toString() + "_worn", "inventory");
		ModelBakery.registerItemVariants(this, normal, worn);
		ModelLoader.setCustomMeshDefinition(this, stack -> worn);
	}

}
