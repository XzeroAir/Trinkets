<<<<<<< Updated upstream
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
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityBlockFinder;
import xzeroair.trinkets.traits.abilities.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.AbilityFrostWalker;
import xzeroair.trinkets.traits.abilities.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
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
		this.setAttributeConfig(serverConfig.attributes);
		//		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == this.getCreativeTab()) {
			final ItemStack fire = new ItemStack(this, 1, 0);
			items.add(fire);
			if (serverConfig.compat.iaf.ICE_VARIANT) {
				final ItemStack ice = new ItemStack(this, 1, 0);
				Capabilities.getTrinketProperties(ice, prop -> prop.setVariant(1));
				items.add(ice);
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
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final KeyEntry key2 = new OptionEntry("variantresist", variant == 1 ? "Ice Resistance" : "Fire Resistance");
		final KeyEntry TANHot = new LangEntry(this.getTranslationKey(stack), "heatimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry TANCold = new LangEntry(this.getTranslationKey(stack), "coldimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry key3 = new OptionEntry("typeimmune", new TextComponentTranslation(variant == 1 ? TANCold.option() : TANHot.option()).getFormattedText());
		final KeyEntry IAFFrostWalker = new LangEntry(this.getTranslationKey(stack) + ".compat.iaf.ice", "frostwalker", (variant == 1) && serverConfig.compat.iaf.ICE_VARIANT && serverConfig.compat.iaf.FROST_WALKER);

		return helper.formatAddVariables(translation, key, key1, keybind1, keybind2, key2, TANHot, TANCold, key3, IAFFrostWalker).replace("#underline:", "");
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityNightVision().toggleAbility(true));
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		if (variant == 0) {
			//		I&F damage Type = dragon_fire
			abilities.add(new AbilityFireImmunity());
			if (tan && serverConfig.compat.tan.immuneToHeat) {
				abilities.add(new AbilityHeatImmunity());
			}
		}
		if (serverConfig.compat.iaf.ICE_VARIANT && (variant == 1)) {
			abilities.add(new AbilityIceImmunity());
			if (serverConfig.compat.iaf.FROST_WALKER) {
				abilities.add(new AbilityFrostWalker());
			}
			if (tan && serverConfig.compat.tan.immuneToCold) {
				abilities.add(new AbilityColdImmunity());
			}
		}
		if (serverConfig.oreFinder) {
			abilities.add(new AbilityBlockFinder());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final String displayName = variant == 1 ? new TextComponentTranslation(this.getTranslationKey(stack) + ".ice.name").getFormattedText() : super.getItemStackDisplayName(stack);
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
		final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
		ModelBakery.registerItemVariants(this, fireVariant, iceVariant);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			final int type = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
			if (type == 1) {
				return iceVariant;
			} else {
				return fireVariant;
			}
		});
	}
=======
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
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityBlockFinder;
import xzeroair.trinkets.traits.abilities.AbilityFireImmunity;
import xzeroair.trinkets.traits.abilities.AbilityFrostWalker;
import xzeroair.trinkets.traits.abilities.AbilityIceImmunity;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityColdImmunity;
import xzeroair.trinkets.traits.abilities.compat.survival.AbilityHeatImmunity;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
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
		this.setAttributeConfig(serverConfig.attributes);
		//		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == this.getCreativeTab()) {
			final ItemStack fire = new ItemStack(this, 1, 0);
			items.add(fire);
			if (serverConfig.compat.iaf.ICE_VARIANT) {
				final ItemStack ice = new ItemStack(this, 1, 0);
				Capabilities.getTrinketProperties(ice, prop -> prop.setVariant(1));
				items.add(ice);
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
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final KeyEntry key2 = new OptionEntry("variantresist", variant == 1 ? "Ice Resistance" : "Fire Resistance");
		final KeyEntry TANHot = new LangEntry(this.getTranslationKey(stack), "heatimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry TANCold = new LangEntry(this.getTranslationKey(stack), "coldimmune", tan && serverConfig.compat.tan.immuneToHeat);
		final KeyEntry key3 = new OptionEntry("typeimmune", new TextComponentTranslation(variant == 1 ? TANCold.option() : TANHot.option()).getFormattedText());
		final KeyEntry IAFFrostWalker = new LangEntry(this.getTranslationKey(stack) + ".compat.iaf.ice", "frostwalker", (variant == 1) && serverConfig.compat.iaf.ICE_VARIANT && serverConfig.compat.iaf.FROST_WALKER);

		return helper.formatAddVariables(translation, key, key1, keybind1, keybind2, key2, TANHot, TANCold, key3, IAFFrostWalker).replace("#underline:", "");
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityNightVision().toggleAbility(true));
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final boolean tan = (Trinkets.ToughAsNails && TrinketsConfig.compat.toughasnails) || (Trinkets.SimpleDifficulty && TrinketsConfig.compat.simpledifficulty);
		if (variant == 0) {
			//		I&F damage Type = dragon_fire
			abilities.add(new AbilityFireImmunity());
			if (tan && serverConfig.compat.tan.immuneToHeat) {
				abilities.add(new AbilityHeatImmunity());
			}
		}
		if (serverConfig.compat.iaf.ICE_VARIANT && (variant == 1)) {
			abilities.add(new AbilityIceImmunity());
			if (serverConfig.compat.iaf.FROST_WALKER) {
				abilities.add(new AbilityFrostWalker());
			}
			if (tan && serverConfig.compat.tan.immuneToCold) {
				abilities.add(new AbilityColdImmunity());
			}
		}
		if (serverConfig.oreFinder) {
			abilities.add(new AbilityBlockFinder());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		final int variant = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
		final String displayName = variant == 1 ? new TextComponentTranslation(this.getTranslationKey(stack) + ".ice.name").getFormattedText() : super.getItemStackDisplayName(stack);
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
		final ModelResourceLocation fireVariant = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation iceVariant = new ModelResourceLocation(this.getRegistryName().toString() + "_ice", "inventory");
		ModelBakery.registerItemVariants(this, fireVariant, iceVariant);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			final int type = Capabilities.getTrinketProperties(stack, 0, (prop, ret) -> prop.getVariant());
			if (type == 1) {
				return iceVariant;
			} else {
				return fireVariant;
			}
		});
	}
>>>>>>> Stashed changes
}