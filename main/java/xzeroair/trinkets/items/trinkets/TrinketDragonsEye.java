package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.*;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
import xzeroair.trinkets.util.config.ServerConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketDragonsEye extends AccessoryBase {

	public static final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
	public static final ClientConfigDragonsEye clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

	public TrinketDragonsEye(String name) {
		super(name);
		this.setUUID("6a345136-49b7-4b71-88dc-87301e329ac1");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == this.getCreativeTab()) {
			final ItemStack fire = new ItemStack(this, 1, 0);
			items.add(fire);
			if (serverConfig.compat.iaf.ICE_VARIANT) {
				final ItemStack ice = new ItemStack(this, 1, 0);
				Capabilities.getTrinketProperties(ice, prop -> {
					prop.setVariant(1);
					prop.getElementAttributes().setPrimaryElement(Elements.ICE);
				});
				items.add(ice);
			}
			if (serverConfig.compat.iaf.LIGHTNING_VARIANT) {
				final ItemStack lightning = new ItemStack(this, 1, 0);
				Capabilities.getTrinketProperties(lightning, prop -> {
					prop.setVariant(2);
					prop.getElementAttributes().setPrimaryElement(Elements.LIGHTNING);
				});
				items.add(lightning);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "treasurefinder", serverConfig.oreFinder);
		String oreTarget = "NONE";
		try {
			final EntityPlayer player = Minecraft.getMinecraft().player;
			final IAbilityInterface ability = Capabilities.getEntityProperties(
					player,
					null,
					(prop, a) -> prop.getAbilityHandler().getAbility("xat:" + Abilities.blockDetection)
			);
			if (ability instanceof AbilityBlockFinder) {
				final AbilityBlockFinder finder = (AbilityBlockFinder) ability;
				final String target = finder.parseTargetName(finder.getTreasure());
				if (!target.isEmpty()) {
					oreTarget = target;
				}
			}
		} catch (Exception e) {
			oreTarget = "ERROR";
		}
		final KeyEntry key1 = new OptionEntry("target", serverConfig.oreFinder, oreTarget.trim());
		final KeyEntry keybind1 = new KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
		final KeyEntry keybind2 = new KeyBindEntry("deofkb", ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName());
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		final Element element = this.getPrimaryElement(stack);
		final boolean isIceVariant = element == Elements.ICE;
		final boolean isLightningVariant = element == Elements.LIGHTNING;
		final KeyEntry key2 = new OptionEntry("variantresist", new TextComponentTranslation(isIceVariant ? "xat.effect.ice_resistance" : isLightningVariant ? "xat.effect.lightning_resistance" : "effect.fireResistance").getFormattedText());
		final KeyEntry TANHot = new LangEntry(this.getTranslationKey(stack), "heatimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry TANCold = new LangEntry(this.getTranslationKey(stack), "coldimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry IAFParalysis = new LangEntry(this.getTranslationKey(stack), "paralysisimmune", isLightningVariant && serverConfig.compat.iaf.LIGHTNING_VARIANT && serverConfig.compat.iaf.PARALYSIS_IMMUNITY);
		final KeyEntry key3 = new OptionEntry("typeimmune", new TextComponentTranslation(isIceVariant ? TANCold.option() : isLightningVariant ? IAFParalysis.option() : TANHot.option()).getFormattedText());
		final KeyEntry IAFFrostWalker = new LangEntry(this.getTranslationKey(stack) + ".compat.iaf.ice", "frostwalker", isIceVariant && serverConfig.compat.iaf.ICE_VARIANT && serverConfig.compat.iaf.FROST_WALKER);

		return helper.formatAddVariables(translation, key, key1, keybind1, keybind2, key2, TANHot, TANCold, key3, IAFFrostWalker, IAFParalysis).replace("#underline:", "");
	}

	@Override
	public String[] getAttributeConfig() {
		return serverConfig.attributes;
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityNightVision().toggleAbility(true));
		final Element element = this.getPrimaryElement(stack);
		final boolean isIceVariant = element == Elements.ICE;
		final boolean isLightningVariant = element == Elements.LIGHTNING;
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		if (serverConfig.compat.iaf.ICE_VARIANT && isIceVariant) {
			abilities.add(new AbilityIceImmunity());
			if (serverConfig.compat.iaf.FROST_WALKER) {
				abilities.add(new AbilityFrostWalker());
			}
			if (tan && serverConfig.compat.tan.immuneToCold) {
				abilities.add(new AbilityColdImmunity());
			}
		} else if (serverConfig.compat.iaf.LIGHTNING_VARIANT && isLightningVariant) {
			abilities.add(new AbilityLightningImmunity());
		}else {
			abilities.add(new AbilityFireImmunity());
			if (tan && serverConfig.compat.tan.immuneToHeat) {
				abilities.add(new AbilityHeatImmunity());
			}
		}
		if (serverConfig.oreFinder) {
			abilities.add(new AbilityBlockFinder());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		final Element element = this.getPrimaryElement(stack);
		final String displayName;
		if (element == Elements.ICE) {
			displayName = new TextComponentTranslation(this.getTranslationKey(stack) + ".ice.name").getFormattedText();
		} else if (element == Elements.LIGHTNING) {
			displayName = new TextComponentTranslation(this.getTranslationKey(stack) + ".lightning.name").getFormattedText();
		} else if (element == Elements.FIRE) {
			displayName = new TextComponentTranslation(this.getTranslationKey(stack) + ".fire.name").getFormattedText();
		} else {
			displayName = super.getItemStackDisplayName(stack);
		}
		return displayName.trim();
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		//		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
		final ModelResourceLocation lightningVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_lightning", "inventory");
		final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_fire", "inventory");
		ModelBakery.registerItemVariants(this, normal, fireVariant, iceVariant, lightningVariant);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			final Element element = this.getPrimaryElement(stack);
			if (element == Elements.ICE) {
				return iceVariant;
			} else if (element == Elements.LIGHTNING) {
				return lightningVariant;
			} else if (element == Elements.FIRE) {
				return fireVariant;
			} else {
				return normal;
			}
		});
	}
}