<<<<<<< Updated upstream
package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.AbilityMagnetic;
import xzeroair.trinkets.traits.abilities.AbilityRepel;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketPolarized extends AccessoryBase {

	public static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public TrinketPolarized(String name) {
		super(name);
		this.setUUID("1ed98d9e-3075-45e0-b6f7-fcdff24caed4");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityMagnetic());
		if (serverConfig.repell) {
			abilities.add(new AbilityRepel());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "collect");
		final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "collectxp", serverConfig.collectXP);
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "repel", serverConfig.repell);
		final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
		final boolean main = prop == null ? false : prop.mainAbility();
		final boolean alt = prop == null ? false : prop.altAbility();
		final KeyEntry key3 = new OptionEntry("collecttoggle", serverConfig.collectXP, helper.toggleCheckTranslation(main));
		final KeyEntry key4 = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(alt));
		final KeyEntry key5 = new KeyBindEntry("magnetkb", ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName());
		final KeyEntry key6 = new KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4, key5, key6);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		final ItemStack stack = player.getHeldItem(handIn);
		final boolean client = player.world.isRemote;
		final boolean sneak = player.isSneaking();

		final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
		if ((prop != null)) {
			final EntityProperties entityProps = Capabilities.getEntityProperties(player);
			final TranslationHelper helper = TranslationHelper.INSTANCE;
			if (!sneak) {
				final boolean toggle = prop.mainAbility();
				if (client) {
					final String magnetMode = new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode").getFormattedText();
					final KeyEntry key = new OptionEntry("collecttoggle", serverConfig.collectXP, helper.toggleCheckTranslation(!toggle));
					player.sendStatusMessage(
							new TextComponentString(
									helper.formatAddVariables(magnetMode, key)
							), true
					);
				}
				Capabilities.getEntityProperties(player, props -> {
					if (entityProps != null) {
						final AbilityHolder magnet = entityProps.getAbilityHandler().getAbilityHolder("xat:" + Abilities.magnetic);
						if ((magnet != null) && this.getRegistryName().toString().contentEquals(magnet.getSourceID())) {
							final IAbilityInterface ability = magnet.getAbility();
							if (ability instanceof IToggleAbility) {
								((IToggleAbility) ability).toggleAbility(!toggle);
							}
						}
					}
				});
				prop.toggleMainAbility(!toggle);
			} else {
				final boolean toggle = prop.altAbility();
				if (client) {
					final String repelMode = new TextComponentTranslation(stack.getTranslationKey() + ".repelmode").getFormattedText();
					final KeyEntry key = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(!toggle));
					player.sendStatusMessage(
							new TextComponentString(
									helper.formatAddVariables(repelMode, key)
							), true
					);
				}
				Capabilities.getEntityProperties(player, props -> {
					if (entityProps != null) {
						final AbilityHolder repel = entityProps.getAbilityHandler().getAbilityHolder("xat:" + Abilities.repel);
						if ((repel != null) && this.getRegistryName().toString().contentEquals(repel.getSourceID())) {
							final IAbilityInterface ability = repel.getAbility();
							if (ability instanceof IToggleAbility) {
								((IToggleAbility) ability).toggleAbility(!toggle);
							}
						}
					}
				});
				prop.toggleAltAbility(!toggle);
			}
			//			prop.sendInformationToPlayer(player, player);
		}
		player.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));

	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation magnet = new ModelResourceLocation(this.getRegistryName().toString() + "_magnet", "inventory");
		final ModelResourceLocation repel = new ModelResourceLocation(this.getRegistryName().toString() + "_repell", "inventory");
		final ModelResourceLocation both = new ModelResourceLocation(this.getRegistryName().toString() + "_both", "inventory");
		ModelBakery.registerItemVariants(this, normal, magnet, repel, both);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
			final boolean main = (prop != null) && prop.mainAbility();//this.getTagCompoundSafe(stack).getBoolean("main.ability");
			final boolean alt = (prop != null) && prop.altAbility();//this.getTagCompoundSafe(stack).getBoolean("alt.ability");
			if (main && alt) {
				return both;
			} else if (main) {
				return magnet;
			} else if (alt) {
				return repel;
			} else {
				return normal;
			}
		});
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}
=======
package xzeroair.trinkets.items.trinkets;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.AbilityMagnetic;
import xzeroair.trinkets.traits.abilities.AbilityRepel;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketPolarized extends AccessoryBase {

	public static final ConfigPolarizedStone serverConfig = TrinketsConfig.SERVER.Items.POLARIZED_STONE;

	public TrinketPolarized(String name) {
		super(name);
		this.setUUID("1ed98d9e-3075-45e0-b6f7-fcdff24caed4");
		this.setAttributeConfig(serverConfig.attributes);
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {
		abilities.add(new AbilityMagnetic());
		if (serverConfig.repell) {
			abilities.add(new AbilityRepel());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final KeyEntry key = new LangEntry(this.getTranslationKey(stack), "collect");
		final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "collectxp", serverConfig.collectXP);
		final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "repel", serverConfig.repell);
		final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
		final boolean main = prop == null ? false : prop.mainAbility();
		final boolean alt = prop == null ? false : prop.altAbility();
		final KeyEntry key3 = new OptionEntry("collecttoggle", serverConfig.collectXP, helper.toggleCheckTranslation(main));
		final KeyEntry key4 = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(alt));
		final KeyEntry key5 = new KeyBindEntry("magnetkb", ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName());
		final KeyEntry key6 = new KeyBindEntry("auxkb", ModKeyBindings.AUX_KEY.getDisplayName());
		return helper.formatAddVariables(translation, key, key1, key2, key3, key4, key5, key6);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		final ItemStack stack = player.getHeldItem(handIn);
		final boolean client = player.world.isRemote;
		final boolean sneak = player.isSneaking();

		final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
		if ((prop != null)) {
			final EntityProperties entityProps = Capabilities.getEntityProperties(player);
			final TranslationHelper helper = TranslationHelper.INSTANCE;
			if (!sneak) {
				final boolean toggle = prop.mainAbility();
				if (client) {
					final String magnetMode = new TextComponentTranslation(stack.getTranslationKey() + ".magnetmode").getFormattedText();
					final KeyEntry key = new OptionEntry("collecttoggle", serverConfig.collectXP, helper.toggleCheckTranslation(!toggle));
					player.sendStatusMessage(
							new TextComponentString(
									helper.formatAddVariables(magnetMode, key)
							), true
					);
				}
				Capabilities.getEntityProperties(player, props -> {
					if (entityProps != null) {
						final AbilityHolder magnet = entityProps.getAbilityHandler().getAbilityHolder("xat:" + Abilities.magnetic);
						if ((magnet != null) && this.getRegistryName().toString().contentEquals(magnet.getSourceID())) {
							final IAbilityInterface ability = magnet.getAbility();
							if (ability instanceof IToggleAbility) {
								((IToggleAbility) ability).toggleAbility(!toggle);
							}
						}
					}
				});
				prop.toggleMainAbility(!toggle);
			} else {
				final boolean toggle = prop.altAbility();
				if (client) {
					final String repelMode = new TextComponentTranslation(stack.getTranslationKey() + ".repelmode").getFormattedText();
					final KeyEntry key = new OptionEntry("repeltoggle", serverConfig.repell, helper.toggleCheckTranslation(!toggle));
					player.sendStatusMessage(
							new TextComponentString(
									helper.formatAddVariables(repelMode, key)
							), true
					);
				}
				Capabilities.getEntityProperties(player, props -> {
					if (entityProps != null) {
						final AbilityHolder repel = entityProps.getAbilityHandler().getAbilityHolder("xat:" + Abilities.repel);
						if ((repel != null) && this.getRegistryName().toString().contentEquals(repel.getSourceID())) {
							final IAbilityInterface ability = repel.getAbility();
							if (ability instanceof IToggleAbility) {
								((IToggleAbility) ability).toggleAbility(!toggle);
							}
						}
					}
				});
				prop.toggleAltAbility(!toggle);
			}
			//			prop.sendInformationToPlayer(player, player);
		}
		player.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(handIn));

	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		// Name + Item Damage equals the Lang File Name
		return super.getTranslationKey() + "." + 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation magnet = new ModelResourceLocation(this.getRegistryName().toString() + "_magnet", "inventory");
		final ModelResourceLocation repel = new ModelResourceLocation(this.getRegistryName().toString() + "_repell", "inventory");
		final ModelResourceLocation both = new ModelResourceLocation(this.getRegistryName().toString() + "_both", "inventory");
		ModelBakery.registerItemVariants(this, normal, magnet, repel, both);
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
			final boolean main = (prop != null) && prop.mainAbility();//this.getTagCompoundSafe(stack).getBoolean("main.ability");
			final boolean alt = (prop != null) && prop.altAbility();//this.getTagCompoundSafe(stack).getBoolean("alt.ability");
			if (main && alt) {
				return both;
			} else if (main) {
				return magnet;
			} else if (alt) {
				return repel;
			} else {
				return normal;
			}
		});
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}
>>>>>>> Stashed changes
}