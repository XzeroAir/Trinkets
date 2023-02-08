package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityResistance;
import xzeroair.trinkets.traits.abilities.compat.firstaid.AbilityIgnoreHeadshot;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketDamageShield extends AccessoryBase {

	public static final ConfigDamageShield serverConfig = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
	public static final ClientConfigDamageShield clientConfig = TrinketsConfig.CLIENT.items.DAMAGE_SHIELD;

	public TrinketDamageShield(String name) {
		super(name);
		this.setUUID("c0885371-20dd-4c56-86eb-78f24d9fe777");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "explosionresist", serverConfig.explosion_resist);
		final KeyEntry key1 = new OptionEntry("explosionresistamount", serverConfig.explosion_resist, ((100F - (serverConfig.explosion_amount * 100F)) + "%"));
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "damageignored", serverConfig.damage_ignore);
		String hits = "0";
		try {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			final IAbilityInterface ability = Capabilities.getEntityProperties(
					player,
					null,
					(prop, a) -> prop.getAbilityHandler().getAbility("xat:" + Abilities.safeGuard)
			);
			if (ability instanceof AbilityResistance) {
				final AbilityResistance safeGuard = (AbilityResistance) ability;
				hits = safeGuard.getHitCount() + "";
			}
		} catch (Exception e) {
			hits = "ERROR";
		}
		final KeyEntry key3 = new OptionEntry("damageignoredhitcount", serverConfig.damage_ignore, hits);
		final KeyEntry key4 = new OptionEntry("cfghitcount", serverConfig.damage_ignore, serverConfig.hits);
		String effect = "ERROR";
		try {
			final Potion peffect = Potion.getPotionFromResourceLocation(serverConfig.potionEffect);
			if (peffect != null) {
				effect = new TextComponentTranslation(peffect.getName()).getUnformattedText();
			}
		} catch (Exception e) {
		}
		final KeyEntry key5 = new OptionEntry("soheffect", true, effect);
		final KeyEntry FirstAid = new LangEntry(this.getTranslationKey(stack), "headshots", serverConfig.compat.firstaid.chance_ignore);
		final KeyEntry key6 = new OptionEntry("headshotchance", serverConfig.compat.firstaid.chance_ignore, MathHelper.clamp((1F / serverConfig.compat.firstaid.chance_headshots) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%");
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4, FirstAid, key5, key6);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityResistance());
		if (Trinkets.FirstAid && serverConfig.compat.firstaid.chance_ignore) {
			abilities.add(new AbilityIgnoreHeadshot());
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		Capabilities.getTrinketProperties(stack, prop -> prop.setVariant(0));
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
		if (TrinketsConfig.SERVER.misc.retrieveVIP) {
			Capabilities.getVipStatus(player, status -> {
				Capabilities.getTrinketProperties(stack, prop -> prop.setVariant(status.getStatus()));
			});
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation bro = new ModelResourceLocation(this.getRegistryName().toString() + "_bro", "inventory");
		final ModelResourceLocation panda = new ModelResourceLocation(this.getRegistryName().toString() + "_panda", "inventory");
		final ModelResourceLocation vip = new ModelResourceLocation(this.getRegistryName().toString() + "_vip", "inventory");
		final ModelResourceLocation artsy = new ModelResourceLocation(this.getRegistryName().toString() + "_artsy", "inventory");
		final ModelResourceLocation twilight = new ModelResourceLocation(this.getRegistryName().toString() + "_twilight", "inventory");
		ModelBakery.registerItemVariants(this, normal, bro, panda, vip, artsy, twilight);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			int variant = Capabilities.getTrinketProperties(stack, 0, (prop, var) -> prop.getVariant());
			switch (variant) {
			case 0:
				return normal;
			case 1:
				return vip;
			case 2:
				return bro;
			case 3:
				return panda;
			case 4:
				return artsy;
			case 5:
				return twilight;
			default:
				return vip;
			}
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
		if (!clientConfig.doRender) {
			return;
		}
		final float offsetX = 0.17F;
		final float offsetY = 0.22F;
		final float offsetZ = 0.16F;
		GlStateManager.pushMatrix();
		if (player.isSneaking()) {
			GlStateManager.translate(0F, 0.2F, 0F);
		}
		renderer.getMainModel().bipedBody.postRender(scale);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.translate(offsetX, -offsetY, offsetZ);
		if (player.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
			GlStateManager.translate(offsetX - 0.14F, 0, -(offsetZ - 0.2F));
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
}