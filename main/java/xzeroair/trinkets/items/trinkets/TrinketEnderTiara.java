package xzeroair.trinkets.items.trinkets;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.model.BipedJsonModel;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityEnderQueen;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigEnderCrown;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketEnderTiara extends AccessoryBase {

	public static final ConfigEnderCrown serverConfig = TrinketsConfig.SERVER.Items.ENDER_CROWN;
	public static final ClientConfigEnderCrown clientConfig = TrinketsConfig.CLIENT.items.ENDER_CROWN;

	public TrinketEnderTiara(String name) {
		super(name);
		this.setUUID("a45dbc1c-17e9-40b4-b6a3-09dea74355b7");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "damageignored", serverConfig.dmgChance);
		final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "endermenchance", serverConfig.spawnChance);
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "endermenfollow", serverConfig.Follow);
		final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "waterhurts", serverConfig.water_hurts);
		final KeyEntry key4 = new OptionEntry("chance", serverConfig.dmgChance || serverConfig.spawnChance, MathHelper.clamp((1F / serverConfig.chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		final KeyEntry TAN = new LangEntry(this.getTranslationKey(stack), "coldimmune", tan && serverConfig.compat.tan.immuneToCold);
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4, TAN);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityEnderQueen());
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		if (tan && serverConfig.compat.tan.immuneToCold) {
			abilities.add(new AbilityColdImmunity());
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EntityEquipmentSlot.HEAD;//super.getEquipmentSlot(stack);
	}

	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
		boolean alreadyEquipped = (entity instanceof EntityLivingBase) && TrinketHelper.AccessoryCheck((EntityLivingBase) entity, this);
		return !alreadyEquipped && super.isValidArmor(stack, armorType, entity);
	}

	@Override
	public boolean canEquipAccessory(ItemStack stack, EntityLivingBase player) {
		boolean alreadyEquipped = false;
		try {
			alreadyEquipped = TrinketHelper.getSlotInfoForItemFromEquipment(player, s -> (s != null) && !s.isEmpty() && s.isItemEqual(stack)) == null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alreadyEquipped && super.canEquipAccessory(stack, player);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		if (!itemStack.isEmpty()) {
			BipedJsonModel model = tiara;
			model.tranformType = ItemCameraTransforms.TransformType.HEAD;
			model.equippedAsAccessory = false;
			model.setModelAttributes(_default);
			model.isSneak = _default.isSneak;
			model.isRiding = _default.isRiding;
			model.isChild = _default.isChild;
			model.rightArmPose = _default.rightArmPose;
			model.leftArmPose = _default.leftArmPose;
			return model;
		}
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}

	private BipedJsonModel tiara;

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
		if (!clientConfig.doRender) {
			return;
		}
		if (tiara == null) {
			tiara = new BipedJsonModel(new ResourceLocation(this.getRegistryName().toString() + "_model"));
		}
		GlStateManager.pushMatrix();
		BipedJsonModel model = tiara;
		model.equippedAsAccessory = true;
		model.tranformType = ItemCameraTransforms.TransformType.HEAD;
		double sneakOffset = player.isSneaking() ? 0.2 : 0;
		boolean hasHelmet = player.hasItemInSlot(EntityEquipmentSlot.HEAD);
		float hScale = hasHelmet ? 1.2F : 1F;
		double helmetOffsetY = hasHelmet ? 0.12 : 0;
		double helmetOffsetZ = hasHelmet ? -0.04 : 0;
		GlStateManager.translate(0, sneakOffset, 0);
		renderer.getMainModel().bipedHead.postRender(scale);
		GlStateManager.translate(0.0, 0.19, -0.04);
		GlStateManager.scale(scale * 10, scale * 10, scale * 10);
		GlStateManager.translate(0.0F, helmetOffsetY, helmetOffsetZ);
		GlStateManager.scale(hScale, hScale, hScale);
		model.render(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, player.rotationYaw, player.rotationPitch, scale);
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
		final ModelResourceLocation model = new ModelResourceLocation(this.getRegistryName().toString() + "_model", "inventory");
		ModelBakery.registerItemVariants(this, normal, model);
		ModelLoader.setCustomMeshDefinition(this, stack -> Capabilities.getTrinketProperties(stack, normal, (prop, tex) -> {
			if (prop.getSlotInfo().getHandlerType().equals(ItemHandlerType.HEAD)) {
				return model;
			} else {
				return tex;
			}
		}));
	}
}
