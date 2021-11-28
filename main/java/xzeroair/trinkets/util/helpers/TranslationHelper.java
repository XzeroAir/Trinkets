package xzeroair.trinkets.util.helpers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.items.foods.Mana_Crystal;
import xzeroair.trinkets.items.foods.Mana_Reagent;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;
import xzeroair.trinkets.items.trinkets.TrinketFaelisClaws;
import xzeroair.trinkets.items.trinkets.TrinketGreaterInertia;
import xzeroair.trinkets.items.trinkets.TrinketInertiaNull;
import xzeroair.trinkets.items.trinkets.TrinketPoisonStone;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.items.trinkets.TrinketSeaStone;
import xzeroair.trinkets.items.trinkets.TrinketWitherRing;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xServer.TrinketItems;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigPoisonStone;
import xzeroair.trinkets.util.config.trinkets.ConfigPolarizedStone;
import xzeroair.trinkets.util.config.trinkets.ConfigSeaStone;
import xzeroair.trinkets.util.config.trinkets.ConfigWitherRing;

public class TranslationHelper {

	private static TextFormatting black = TextFormatting.BLACK;
	private static TextFormatting white = TextFormatting.WHITE;
	private static TextFormatting red = TextFormatting.RED;
	private static TextFormatting dRed = TextFormatting.DARK_RED;
	private static TextFormatting blue = TextFormatting.BLUE;
	private static TextFormatting dBlue = TextFormatting.DARK_BLUE;
	private static TextFormatting green = TextFormatting.GREEN;
	private static TextFormatting dGreen = TextFormatting.DARK_GREEN;
	private static TextFormatting lPurple = TextFormatting.LIGHT_PURPLE;
	private static TextFormatting dPurple = TextFormatting.DARK_PURPLE;
	private static TextFormatting yellow = TextFormatting.YELLOW;
	private static TextFormatting aqua = TextFormatting.AQUA;
	private static TextFormatting dAqua = TextFormatting.DARK_AQUA;
	private static TextFormatting gray = TextFormatting.GRAY;
	private static TextFormatting dGray = TextFormatting.DARK_GRAY;
	private static TextFormatting gold = TextFormatting.GOLD;
	private static TextFormatting ST = TextFormatting.STRIKETHROUGH;
	private static TextFormatting UL = TextFormatting.UNDERLINE;
	private static TextFormatting italic = TextFormatting.ITALIC;
	private static TextFormatting bold = TextFormatting.BOLD;
	private static TextFormatting reset = TextFormatting.RESET;
	private static List<KeyEntry> keys;

	public static <T> void initTranslation(T object) {
		if (keys == null) {
			keys = new ArrayList<>();
		}
		if (!keys.isEmpty()) {
			keys.clear();
		}
		//KeyBinds
		keys.add(new KeyEntry(isKeyBind("denvkb"), true, ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName()));
		keys.add(new KeyEntry(isKeyBind("deofkb"), true, ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName()));
		keys.add(new KeyEntry(isKeyBind("arckb"), true, ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName()));
		keys.add(new KeyEntry(isKeyBind("pskb"), true, ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName()));
		keys.add(new KeyEntry(isKeyBind("rakb"), true, ModKeyBindings.RACE_ABILITY.getDisplayName()));
		keys.add(new KeyEntry(isKeyBind("auxkb"), true, ModKeyBindings.AUX_KEY.getDisplayName()));

		//Translation References
		//			keys.add(new KeyEntry(isLang("auxkb"), true, ModKeyBindings.AUX_KEY.getDisplayName()));
		//Variable Replacements
		//			keys.add(new KeyEntry(isOption("auxkb"), true, ModKeyBindings.AUX_KEY.getDisplayName()));
		final boolean isItemStack = object instanceof ItemStack;
		final boolean isItem = object instanceof Item;
		if (isItemStack || isItem) {
			final ItemStack stack = isItemStack ? (ItemStack) object : ItemStack.EMPTY;
			final Item item = isItem ? (Item) object : (stack != null) && !stack.isEmpty() ? stack.getItem() : null;
			final TrinketItems itemCfg = TrinketsConfig.SERVER.Items;
			if (item instanceof TrinketWitherRing) {
				keys.add(new KeyEntry(isOption("witherchance"), itemCfg.WITHER_RING.wither, MathHelper.clamp((1F / itemCfg.WITHER_RING.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%"));
				keys.add(new KeyEntry(isOption("witherduration"), itemCfg.WITHER_RING.wither, itemCfg.WITHER_RING.wither_duration));
				keys.add(new KeyEntry(isOption("leechamount"), itemCfg.WITHER_RING.leech, itemCfg.WITHER_RING.leech_amount));
			}
			if (item instanceof TrinketDragonsEye) {
				//				keys.add(new KeyEntry(isOption("leechamount"), itemCfg.WITHER_RING.leech, itemCfg.WITHER_RING.leech_amount));
			}

			//			if (stack.getItem() instanceof TrinketDragonsEye) {
			//				final ConfigDragonsEye config = TrinketsConfig.SERVER.Items.DRAGON_EYE;
			//
			//				final String Key = "treasurefinder";
			//				final String Key3 = "heatimmune";
			//				final String Key4 = "nightvision";
			//				final String Key5 = "looking";
			//				string = TranslationKeyReplace(stack.getTranslationKey(), config.oreFinder, Key, string);
			//				string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.oreFinder), string);
			//				final boolean looking = !isCapNull && (iCap.Target() >= 0);
			//				if (config.oreFinder) {
			//					final int index = looking ? iCap.Target() : -1;
			//					string = translateDragonEyeTarget(string, index);
			//				}
			//
			//				final boolean heatImmune = config.compat.tan.immuneToHeat && TrinketsConfig.compat.toughasnails;
			//				string = TranslationKeyReplace(stack.getTranslationKey(), heatImmune, Key3, string);
			//				string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(heatImmune), string);
			//
			//				//			string = VariableReplace(config.enabled, Key4, toggleCheckTranslation(!isCapNull && iCap.mainAbility()), string);
			//
			//				string = VariableReplace(config.oreFinder, Key5, toggleCheckTranslation(looking), string);
			//
			//				final String DragonsEyeKeybindNV = ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName();
			//				final String DragonsEyeKeybindOF = ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
			//				string = string
			//						.replace("*denvkb:", DragonsEyeKeybindNV + "")
			//						.replace("*deofkb:", DragonsEyeKeybindOF + "");
			//			}
		}

		//		if (stack.getItem() instanceof TrinketWitherRing) {
		//			ConfigWitherRing config = TrinketsConfig.SERVER.Items.WITHER_RING;
		//			String Key = "wither";
		//			String Key2 = "leech";
		//			string = TranslationKeyReplace(stack.getTranslationKey(), config.wither, Key, string);
		//			string = VariableReplace(config.wither, "witherchance", MathHelper.clamp((1F / config.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);
		//			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.wither), string);
		//
		//			string = TranslationKeyReplace(stack.getTranslationKey(), config.leech, Key2, string);
		//			string = VariableReplace(config.leech, "leechamount", (config.leech_amount * 0.5) + "", string);
		//			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.leech), string);

	}

	private static String isKeyBind(String key) {
		return "*" + key + ":";
	}

	private static String isOption(String key) {
		return "$" + key + ":";
	}

	private static String isLang(String key) {
		return "@" + key + ":";
	}

	public static String formatAddVariables(String string) {
		initTranslation(null);
		if (keys != null) {
			for (final KeyEntry key : keys) {
				if (hasLangReplacement(key.key(), string)) {
					string = langKeyReplace(string, key.enabled(), key.key(), key.option());
				}
				if (hasKey(key.key(), string)) {
					string = optionReplace(string, key.enabled(), key.key(), key.option());
				}
				if (hasKeyBind(key.key(), string)) {
					string = optionReplace(string, key.enabled(), key.key(), key.option());
				}
			}
		}
		return string;
	}

	private static class KeyEntry {

		private String key;
		private String option = "";
		private boolean enabled = false;

		public <T> KeyEntry(String key, T option) {
			this.key = key;
			this.option = option + "";
		}

		public <T> KeyEntry(String key, boolean enabled, T option) {
			this(key, option);
			this.enabled = enabled;
		}

		public String key() {
			return key;
		}

		public String option() {
			return option;
		}

		public boolean enabled() {
			return enabled;
		}

	}

	private static boolean hasKey(String key, String string) {
		if (string.contains(key)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean hasKeyBind(String key, String string) {
		if (string.contains("*" + key.toLowerCase() + ":")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean hasOption(String key, String string) {
		if (string.contains("$" + key.toLowerCase() + ":")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean hasLangReplacement(String key, String string) {
		if (string.contains("@" + key.replace("@", "").replace(":", "").toLowerCase() + ":")) {
			return true;
		} else {
			return false;
		}
	}

	private static String langKeyReplace(String string, boolean enabled, String key, String replacementKey) {
		if (enabled) {
			final String translatedKey = new TextComponentTranslation(replacementKey).getFormattedText();
			return string.replace(key, translatedKey);
		} else {
			return string.replace(key, "");
		}
	}

	private static String optionReplace(String string, boolean enabled, String key, String Variable) {
		if (enabled) {
			return string.replace(key, Variable);
		} else {
			return string.replace(key, "");
		}
	}

	private static boolean isStringEmpty(String string) {
		return string
				.replace("�r", "")
				.replace("�6", "")
				.replace("�", "")
				.replace("§r", "")
				.replaceAll(" ", "")
				.isEmpty();
	}

	public static void addToolTips(String translationKey, List<String> tooltips) {
		for (int i = 1; i < 10; i++) {
			final TextComponentTranslation tooltip = new TextComponentTranslation(translationKey.toLowerCase() + ".tooltip" + i);
			if (!(tooltip.getUnformattedComponentText().contentEquals(tooltip.getKey()) || tooltip.getUnformattedText().isEmpty())) {
				final String string = formatLangKeys(tooltip);
				if ((string != null) && !isStringEmpty(string)) {
					tooltips.add(
							string
					);
				}
			}
		}
		//		addModCompatTooltips(stack, world, tooltip);
	}

	public static String formatLangKeys(TextComponentTranslation string) {
		final String addOptions = formatAddVariables(string.getFormattedText());//TrinketOptionsKeyTranslate(string.getFormattedText());
		final String addColor = addTextColorFromLangKey(addOptions);
		return addColor;
	}

	public static void addTooltips(ItemStack stack, @Nullable World world, List<String> tooltip) {
		if (world == null) { // Try to make this more general and not require an ItemStack or world
			return;
		}
		for (int i = 1; i < 10; i++) {
			final TextComponentTranslation info = new TextComponentTranslation(stack.getTranslationKey() + ".tooltip" + i);
			if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
				final String string = formatLangKeys(stack, info);
				if ((string != null) && !isStringEmpty(string)) {
					tooltip.add(
							string
					);
				}
			}
		}
		addModCompatTooltips(stack, world, tooltip);
	}

	public static void addPotionTooltips(ItemStack stack, @Nullable World world, List<String> tooltip) {
		if (world == null) {
			return;
		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			for (int i = 1; i < 10; i++) {
				final TextComponentTranslation info = new TextComponentTranslation(Reference.MODID + "." + stack.getTranslationKey() + "." + pot.getRegistryName().getPath() + ".tooltip" + i);
				if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
					final String string = formatLangKeys(stack, info);
					if ((string != null) && !isStringEmpty(string)) {
						tooltip.add(
								string
						);
					}
				}
			}
		}
	}

	public static void addModCompatTooltips(ItemStack stack, @Nullable World world, List<String> tooltip) {
		if (world == null) {
			return;
		}
		final TextComponentTranslation ctrl = new TextComponentTranslation(Reference.MODID + ".holdctrl");
		final TextComponentTranslation CompatTan = new TextComponentTranslation(stack.getTranslationKey() + ".compat.tan");
		final TextComponentTranslation CompatFA = new TextComponentTranslation(stack.getTranslationKey() + ".compat.firstaid");
		final TextComponentTranslation CompatEV = new TextComponentTranslation(stack.getTranslationKey() + ".compat.enhancedvisuals");

		final boolean tanEmpty = (CompatTan.getUnformattedComponentText().contentEquals(CompatTan.getKey()) || CompatTan.getUnformattedText().isEmpty());
		final boolean evEmpty = (CompatEV.getUnformattedComponentText().contentEquals(CompatEV.getKey()) || CompatEV.getUnformattedText().isEmpty());
		final boolean faEmpty = (CompatFA.getUnformattedComponentText().contentEquals(CompatFA.getKey()) || CompatFA.getUnformattedText().isEmpty());
		final String TAN = formatLangKeys(stack, CompatTan);
		final String EV = formatLangKeys(stack, CompatEV);
		final String FA = formatLangKeys(stack, CompatFA);
		if (GuiScreen.isCtrlKeyDown()) {
			if (Loader.isModLoaded("toughasnails") && TrinketsConfig.compat.toughasnails) {
				if (!tanEmpty) {
					final String string = formatLangKeys(stack, CompatTan);
					if (!isStringEmpty(string)) {
						tooltip.add(
								string + gold + " (Tough as Nails)"
						);
					}
				}
			}
			if (Loader.isModLoaded("firstaid")) {
				if (!faEmpty) {
					final String string = formatLangKeys(stack, CompatFA);
					if (!isStringEmpty(string)) {
						tooltip.add(
								string + gold + " (First Aid)"
						);
					}
				}
			}
			if (Loader.isModLoaded("enhancedvisuals") && TrinketsConfig.compat.enhancedvisuals) {
				if (!evEmpty) {
					final String string = formatLangKeys(stack, CompatEV);
					if (!isStringEmpty(string)) {
						tooltip.add(
								string + gold + " (Enhanced Visuals)"
						);
					}
				}
			}
		} else {
			if ((!tanEmpty && !isStringEmpty(TAN)) ||
					(!evEmpty && !isStringEmpty(EV)) ||
					(!faEmpty && !isStringEmpty(FA))

			) {
				tooltip.add(TextFormatting.RESET + "" + dGray + addTextColorFromLangKey(ctrl.getFormattedText()));
			}
		}
	}

	public static void addOtherTooltips(AttributeConfigWrapper attributes, List<String> tooltip) {
		final TextComponentTranslation shift = new TextComponentTranslation(Reference.MODID + ".holdshift");
		final TextComponentTranslation whenworn = new TextComponentTranslation(Reference.MODID + ".onequip");

		if (GuiScreen.isShiftKeyDown()) {
			if (attributes.armorEnabled() ||
					attributes.armorToughnessEnabled() ||
					attributes.attackSpeedEnabled() ||
					attributes.attackDamageEnabled() ||
					attributes.healthEnabled() ||
					attributes.knockbackEnabled() ||
					attributes.luckEnabled() ||
					attributes.movementSpeedEnabled() ||
					attributes.reachEnabled() ||
					attributes.swimSpeedEnabled() ||
					attributes.jumpEnabled() ||
					attributes.stepHeightEnabled()) {
				tooltip.add(TextFormatting.RESET + addTextColorFromLangKey(whenworn.getFormattedText()));
				addAttributeTooltips(attributes, tooltip);
			}
		} else {
			if (attributes.armorEnabled() ||
					attributes.armorToughnessEnabled() ||
					attributes.attackSpeedEnabled() ||
					attributes.attackDamageEnabled() ||
					attributes.healthEnabled() ||
					attributes.knockbackEnabled() ||
					attributes.luckEnabled() ||
					attributes.movementSpeedEnabled() ||
					attributes.reachEnabled() ||
					attributes.swimSpeedEnabled() ||
					attributes.jumpEnabled() ||
					attributes.stepHeightEnabled()) {
				tooltip.add(TextFormatting.RESET + "" + dGray + addTextColorFromLangKey(shift.getFormattedText()));
			}
		}
	}

	public static String TrinketOptionsKeyTranslate(ItemStack stack, String string) {
		final TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		final boolean isCapNull = iCap == null;
		if (stack.getItem() instanceof TrinketWitherRing) {
			final ConfigWitherRing config = TrinketsConfig.SERVER.Items.WITHER_RING;
			final String Key = "wither";
			final String Key2 = "leech";
			string = TranslationKeyReplace(stack.getTranslationKey(), config.wither, Key, string);
			string = VariableReplace(config.wither, "witherchance", MathHelper.clamp((1F / config.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.wither), string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.leech, Key2, string);
			string = VariableReplace(config.leech, "leechamount", (config.leech_amount * 0.5) + "", string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.leech), string);
		}
		if (stack.getItem() instanceof TrinketDamageShield) {
			final ConfigDamageShield config = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
			final String Key = "explosionresist";
			final String Key2 = "damageignored";
			final String Key3 = "headshots";
			final String Key4 = "soheffect";

			string = TranslationKeyReplace(stack.getTranslationKey(), config.explosion_resist, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.explosion_resist), string);
			string = VariableReplace(config.explosion_resist, "explosionresistamount", ((100F - (config.explosion_amount * 100F)) + "%"), string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.damage_ignore, Key2, string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.damage_ignore), string);
			int hitcount = 0;
			//			final TrinketProperties prop = Capabilities.getTrinketProperties(stack);
			if (TrinketHelper.getTagCompoundSafe(stack).hasKey("hitcount")) {
				hitcount = TrinketHelper.getTagCompoundSafe(stack).getInteger("hitcount");
			}
			//			if (prop != null) {
			//				hitcount = prop.Count();
			//			}
			//			System.out.println(hitcount + " Hmm");
			string = VariableReplace(config.damage_ignore, "damageignoredhitcount", hitcount + "/" + config.hits, string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.compat.firstaid.chance_ignore, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(config.compat.firstaid.chance_ignore), string);
			string = VariableReplace(config.compat.firstaid.chance_ignore, "headshotchance", MathHelper.clamp((1F / config.compat.firstaid.chance_headshots) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			final Potion peffect = Potion.getPotionFromResourceLocation(config.potionEffect);
			if (peffect != null) {
				string = VariableReplace(config.enabled, Key4, I18n.format(peffect.getName()), string);
			} else {
				string = VariableReplace(false, Key4, "", string);
			}
		}
		if (stack.getItem() instanceof TrinketRaceBase) {
			final TrinketRaceBase rr = (TrinketRaceBase) stack.getItem();
			string = VariableReplace(rr.ItemEnabled(), "rsize", rr.getRacialTraits().getRaceSize() + "%", string);
			if (rr.getRacialTraits().equals(EntityRaces.fairy)) {
				final FairyConfig config = TrinketsConfig.SERVER.races.fairy;
				final String Key = "creativeflight";
				string = TranslationKeyReplace(stack.getTranslationKey(), config.creative_flight, Key, string);
				string = VariableReplace(true, Key, booleanCheckTranslation(config.creative_flight), string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.dwarf)) {
				final DwarfConfig config = TrinketsConfig.SERVER.races.dwarf;
				final String Key = "fortune";
				final String Key2 = "skilledminer";
				final String Key3 = "staticminer";
				string = TranslationKeyReplace(stack.getTranslationKey(), config.fortune, Key, string);
				string = VariableReplace(true, Key, booleanCheckTranslation(config.fortune), string);
				string = TranslationKeyReplace(stack.getTranslationKey(), config.skilled_miner, Key2, string);
				string = VariableReplace(true, Key2, booleanCheckTranslation(config.skilled_miner), string);
				string = TranslationKeyReplace(stack.getTranslationKey(), config.static_mining, Key3, string);
				string = VariableReplace(true, Key3, booleanCheckTranslation(config.static_mining), string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.elf)) {
				final ElfConfig config = TrinketsConfig.SERVER.races.elf;
				final String Key = "chargeshot";
				string = TranslationKeyReplace(stack.getTranslationKey(), config.charge_shot, Key, string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.dragon)) {
				final String breathKey = ModKeyBindings.RACE_ABILITY.getDisplayName();
				string = string
						.replace("*breathkb:", breathKey + "");
				final String Key4 = "nightvision";

				string = VariableReplace(false, Key4, toggleCheckTranslation(false), string);

			}
			if (rr.getRacialTraits().equals(EntityRaces.titan)) {
				final TitanConfig config = TrinketsConfig.SERVER.races.titan;
				final String sinks = ModKeyBindings.RACE_ABILITY.getDisplayName();
				string = TranslationKeyReplace(stack.getTranslationKey(), config.sink, "heavy", string);
			}
		}
		if (stack.getItem() instanceof TrinketDragonsEye) {
			final ConfigDragonsEye config = TrinketsConfig.SERVER.Items.DRAGON_EYE;

			final String Key = "treasurefinder";
			final String Key3 = "heatimmune";
			final String Key4 = "nightvision";
			final String Key5 = "looking";
			string = TranslationKeyReplace(stack.getTranslationKey(), config.oreFinder, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.oreFinder), string);
			final boolean looking = !isCapNull && (iCap.Target() >= 0);
			if (config.oreFinder) {
				final int index = looking ? iCap.Target() : -1;
				string = translateDragonEyeTarget(string, index);
			}

			final boolean heatImmune = config.compat.tan.immuneToHeat && TrinketsConfig.compat.toughasnails;
			string = TranslationKeyReplace(stack.getTranslationKey(), heatImmune, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(heatImmune), string);

			//			string = VariableReplace(config.enabled, Key4, toggleCheckTranslation(!isCapNull && iCap.mainAbility()), string);

			string = VariableReplace(config.oreFinder, Key5, toggleCheckTranslation(looking), string);

			final String DragonsEyeKeybindNV = ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName();
			final String DragonsEyeKeybindOF = ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
			string = string
					.replace("*denvkb:", DragonsEyeKeybindNV + "")
					.replace("*deofkb:", DragonsEyeKeybindOF + "");
		}
		if (stack.getItem() instanceof TrinketInertiaNull) {
			final String inertiaNullFD = "" + ((100F - (TrinketsConfig.SERVER.Items.INERTIA_NULL.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$inertianullfd:", inertiaNullFD + "");
		}
		if (stack.getItem() instanceof TrinketGreaterInertia) {
			final String greaterInertiaFD = "" + ((100F - (TrinketsConfig.SERVER.Items.GREATER_INERTIA.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$greaterinertiafd:", greaterInertiaFD + "");
		}
		if (stack.getItem() instanceof TrinketSeaStone) {
			final ConfigSeaStone config = TrinketsConfig.SERVER.Items.SEA_STONE;
			final String key = "bubbles";
			final String key2 = "betterswimming";
			final String key3 = "tanthirst";
			string = TranslationKeyReplace(stack.getTranslationKey(), config.underwater_breathing, key, string);
			string = VariableReplace(config.underwater_breathing, key, TrinketsConfig.SERVER.Items.SEA_STONE.always_full ? 10 + "" : 1 + "", string);
			string = TranslationKeyReplace(stack.getTranslationKey(), config.Swim_Tweaks, key2, string);
			string = VariableReplace(config.Swim_Tweaks, key2, booleanCheckTranslation(config.Swim_Tweaks), string);
			string = TranslationKeyReplace(stack.getTranslationKey(), config.compat.tan.prevent_thirst, key3, string);
			string = VariableReplace(TrinketsConfig.compat.toughasnails, key3, booleanCheckTranslation(config.compat.tan.prevent_thirst), string);

		}
		if (stack.getItem() instanceof TrinketPolarized) {
			final ConfigPolarizedStone config = TrinketsConfig.SERVER.Items.POLARIZED_STONE;
			final String key = "collect";
			final String key2 = "collectxp";
			final String key3 = "repel";

			string = TranslationKeyReplace(stack.getTranslationKey(), config.enabled, key, string);
			final boolean collectMode = ((stack.getItemDamage() == 1) || (stack.getItemDamage() == 3));
			string = TranslationKeyReplace(stack.getTranslationKey(), config.collectXP, key2, string);
			string = VariableReplace(config.collectXP, key2, booleanCheckTranslation(config.collectXP), string);
			string = VariableReplace(config.repell, "collecttoggle", toggleCheckTranslation(collectMode), string);

			final boolean repelMode = ((stack.getItemDamage() == 2) || (stack.getItemDamage() == 3));
			string = TranslationKeyReplace(stack.getTranslationKey(), config.repell, key3, string);
			string = VariableReplace(config.repell, key2, booleanCheckTranslation(config.repell), string);
			string = VariableReplace(config.repell, "repeltoggle", toggleCheckTranslation(repelMode), string);

			final String PolarizedStoneKeybind = ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName();
			string = string
					.replace("*magnetkb:", PolarizedStoneKeybind + "");
		}
		if (stack.getItem() instanceof TrinketPoisonStone) {
			final ConfigPoisonStone config = TrinketsConfig.SERVER.Items.POISON_STONE;
			final boolean PoisonChance = config.poison;
			final String key = "poison";
			string = TranslationKeyReplace(stack.getTranslationKey(), PoisonChance, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(PoisonChance), string);
			string = VariableReplace(PoisonChance, "poisonchance", MathHelper.clamp((1F / config.poison_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			final boolean bonusDamage = config.bonus_damage;
			final String key2 = "bonusdamage";
			string = TranslationKeyReplace(stack.getTranslationKey(), bonusDamage, key2, string);
			string = VariableReplace(config.enabled, key2, toggleCheckTranslation(bonusDamage), string);
			final float pa = config.bonus_damage_amount <= 0 ? config.bonus_damage_amount : config.bonus_damage_amount - 1;
			string = VariableReplace(PoisonChance, "damagemultiplier", translateAttributeValue(1, pa), string);
		}
		if (stack.getItem() instanceof TrinketEnderTiara) {
			final ConfigEnderCrown config = TrinketsConfig.SERVER.Items.ENDER_CROWN;
			final String key = "damageignored";
			final String key1 = "endermenchance";
			final String key2 = "endermenfollow";
			final String key3 = "waterhurts";

			string = TranslationKeyReplace(stack.getTranslationKey(), config.dmgChance, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(config.dmgChance), string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.spawnChance, key1, string);
			string = VariableReplace(config.enabled, key1, toggleCheckTranslation(config.spawnChance), string);

			string = VariableReplace(config.dmgChance || config.spawnChance, "chance", MathHelper.clamp((1F / config.chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.Follow, key2, string);
			string = VariableReplace(config.enabled, key2, toggleCheckTranslation(config.Follow), string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.water_hurts, key3, string);
			string = VariableReplace(config.enabled, key3, booleanCheckTranslation(config.water_hurts), string);

		}
		if (stack.getItem() instanceof TrinketArcingOrb) {
			final ConfigArcingOrb config = TrinketsConfig.SERVER.Items.ARCING_ORB;

			final String key = "dodge";
			final String key1 = "dodgecost";
			final String key2 = "dodgestun";
			final String key3 = "boltattack";
			final String key4 = "boltcost";
			final String key5 = "boltdamage";

			string = TranslationKeyReplace(stack.getTranslationKey(), config.dodgeAbility, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(config.dodgeAbility), string);

			string = VariableReplace(config.enabled, key1, config.dodgeCost + "", string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.dodgeStuns, key2, string);
			string = VariableReplace(config.enabled, key2, booleanCheckTranslation(config.dodgeStuns), string);

			string = TranslationKeyReplace(stack.getTranslationKey(), config.attackAbility, key3, string);
			string = VariableReplace(config.enabled, key3, toggleCheckTranslation(config.attackAbility), string);

			string = VariableReplace(config.enabled, key4, config.attackCost + "", string);

			string = VariableReplace(config.enabled, key5, config.attackDmg + "", string);

			final String ArcingOrbKeybindBoltAttack = ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName();
			string = string
					.replace("*arckb:", ArcingOrbKeybindBoltAttack + "");
		}
		if (stack.getItem() instanceof TrinketFaelisClaws) {
			final ConfigFaelisClaw config = TrinketsConfig.SERVER.Items.FAELIS_CLAW;
			string = VariableReplace(true, "clawbleed", config.duration + "", string);
		}
		//		if (stack.getItem() instanceof Mana_Candy) {
		//			string = VariableReplace(true, "MPRestore", "25-50" + "", string);
		//		}
		if (stack.getItem() instanceof Mana_Crystal) {
			string = VariableReplace(true, "MPMax", "10" + "", string);
		}
		if (stack.getItem() instanceof Mana_Reagent) {
			string = VariableReplace(true, "MPMax", "10" + "", string);
		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			if (ModPotionTypes.TrinketPotionTypes.containsValue(pot)) {
				string = string
						.replace("$humanticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.human.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$fairyticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.fairy.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$dwarfticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.dwarf.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$titanticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.titan.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$goblinticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.goblin.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$elfticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.elf.Duration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$faelisticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.faelis.Duration), 0, Integer.MAX_VALUE) / 20F) + "");
			}
		}
		final String AuxKeybind = ModKeyBindings.AUX_KEY.getDisplayName();

		return string

				.replace("*auxkb:", AuxKeybind + "");
	}

	public static String TranslationKeyReplace(String itemKey, boolean enabled, String key, String translation) {
		if (enabled) {
			final String translatedKey = new TextComponentTranslation(itemKey + "." + key).getFormattedText();
			return translation.replace("@" + key.toLowerCase() + ":", translatedKey);
		} else {
			return translation.replace("@" + key.toLowerCase() + ":", "");
		}
	}

	public static String VariableReplace(boolean enabled, String key, String Variable, String translation) {
		if (enabled) {
			return translation.replace("$" + key.toLowerCase() + ":", Variable);
		} else {
			return translation.replace("$" + key.toLowerCase() + ":", "");
		}
	}

	public static String booleanCheckTranslation(boolean bool) {
		final TextComponentTranslation enabled = new TextComponentTranslation(Reference.MODID + ".tooltip.enabled");
		final TextComponentTranslation disabled = new TextComponentTranslation(Reference.MODID + ".tooltip.disabled");
		if (bool == true) {
			return enabled.getFormattedText();
		} else {
			return disabled.getFormattedText();
		}
	}

	public static String toggleCheckTranslation(boolean bool) {
		final TextComponentTranslation on = new TextComponentTranslation(Reference.MODID + ".tooltip.on");
		final TextComponentTranslation off = new TextComponentTranslation(Reference.MODID + ".tooltip.off");
		if (bool == true) {
			return on.getFormattedText();
		} else {
			return off.getFormattedText();
		}
	}

	public static String addTextColorFromLangKey(String string) {
		return string
				.replace("#black:", "" + black)
				.replace("#white:", "" + white)
				.replace("#red:", "" + red)
				.replace("#darkred:", "" + dRed)
				.replace("#blue:", "" + blue)
				.replace("#darkblue:", "" + dBlue)
				.replace("#green:", "" + green)
				.replace("#darkgreen:", "" + dGreen)
				.replace("#lightpurple:", "" + lPurple)
				.replace("#darkpurple:", "" + dPurple)
				.replace("#yellow:", "" + yellow)
				.replace("#aqua:", "" + aqua)
				.replace("#darkaqua:", "" + dAqua)
				.replace("#gray:", "" + gray)
				.replace("#darkgray:", "" + dGray)
				.replace("#gold:", "" + gold)
				.replace("#strikethrough:", "" + ST)
				.replace("#underline:", "" + UL)
				.replace("#italic:", "" + italic)
				.replace("#bold:", "" + bold)
				.replace("#reset:", "" + reset);
	}

	public static String formatLangKeys(ItemStack stack, TextComponentTranslation string) {
		final String addOptions = TrinketOptionsKeyTranslate(stack, string.getFormattedText());
		final String addColor = addTextColorFromLangKey(addOptions);
		return addColor;
	}

	// tooltip.add("#black: " + TextFormatting.BLACK + "Color");
	// tooltip.add("#white: " + TextFormatting.WHITE + "Color");
	// tooltip.add("#red: " + TextFormatting.RED + "Color");
	// tooltip.add("#darkred: " + TextFormatting.DARK_RED + "Color");
	// tooltip.add("#blue: " + TextFormatting.BLUE + "Color");
	// tooltip.add("#darkblue: " + TextFormatting.DARK_BLUE + "Color");
	// tooltip.add("#green: " + TextFormatting.GREEN + "Color");
	// tooltip.add("#darkgreen: " + TextFormatting.DARK_GREEN + "Color");
	// tooltip.add("#lightpurple: " + TextFormatting.LIGHT_PURPLE + "Color");
	// tooltip.add("#darkpurple: " + TextFormatting.DARK_PURPLE + "Color");
	// tooltip.add("#yellow: " + TextFormatting.YELLOW + "Color");
	// tooltip.add("#aqua: " + TextFormatting.AQUA + "Color");
	// tooltip.add("#darkaqua: " + TextFormatting.DARK_AQUA + "Color");
	// tooltip.add("#gray: " + TextFormatting.GRAY + "Color");
	// tooltip.add("#darkgray: " + TextFormatting.DARK_GRAY + "Color");
	// tooltip.add("#gold: " + TextFormatting.GOLD + "Color");
	// tooltip.add("#strikethrough: " + TextFormatting.STRIKETHROUGH + "Color");
	// tooltip.add("#underline: " + TextFormatting.UNDERLINE + "Color");
	// tooltip.add("#italic: " + TextFormatting.ITALIC + "Color");
	// tooltip.add("#bold: " + TextFormatting.BOLD + "Color");
	// tooltip.add(TextFormatting.AQUA + "#reset: " + TextFormatting.RESET +
	// "Color");

	public static String translateDragonEyeTarget(String string, int targetID) {
		final ConfigDragonsEye config = TrinketsConfig.SERVER.Items.DRAGON_EYE;

		final String Key1 = "$target:";
		final String Key5 = "looking";
		if (config.oreFinder) {
			final String target = CallHelper.getStringFromArray(TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks, targetID).replace("[", ";").replace("]", "");
			final String[] itemConfig = target.split(";");
			final String itemString = CallHelper.getStringFromArray(itemConfig, 0);
			final String metaString = itemString.contains(":") ? CallHelper.getStringFromArray(itemConfig, 1) : "";
			String translatedTarget = OreTrackingHelper.translateOreName(itemString, metaString);
			final NonNullList<ItemStack> targetStack = OreDictionary.getOres(itemString, false);
			if (!targetStack.isEmpty()) {
				translatedTarget = targetStack.get(0).getDisplayName();
			}
			final String getName = itemString.contains(":") ? OreTrackingHelper.translateOreName(itemString, metaString) : translatedTarget.replace("ore", "").replace("Ore", "");
			final String dragonsEyeTarget = targetID >= 0 ? getName.replace("Oak Chest", "Chest").replace("Chest", "Treasure Chests").trim() : "None";
			string = TranslationHelper.VariableReplace(config.oreFinder, Key5, TranslationHelper.toggleCheckTranslation(targetID >= 0), string);
			string = TranslationHelper.addTextColorFromLangKey(string);
			return string.replace(Key1, dragonsEyeTarget + "");
		} else {
			return string.replace(Key1, "");
		}
	}

	public static String translateAttributeValue(int OP, double Amount) {
		// float percentage = 100/MathHelper.clamp((1F/0)*100, 0, 1000);
		// float translatedFloat = MathHelper.clamp(percentage/100, Float.MIN_VALUE,
		// Float.MAX_VALUE);
		double TranslatedAmount = Amount;
		String string = "";
		if (OP > 0) {
			TranslatedAmount = Math.round(Amount * (100));
			if (TranslatedAmount > 0) {
				string = "+" + TranslatedAmount + "%";
				//				if (OP == 1) {
				//					string = "+" + string;
				//				}
			} else {
				string = "" + TranslatedAmount + "%";
			}
		} else {
			//			TranslatedAmount = Math.round(Amount * (100));
			if (TranslatedAmount > 0) {
				string = "+" + TranslatedAmount;
			} else {
				string = "" + TranslatedAmount;
			}
		}
		final String color = TranslatedAmount > 0 ? "" + green : "" + red;
		return color + string;
	}

	public static void addAttributeTooltips(AttributeConfigWrapper attributes, List<String> tooltip) {
		if (attributes.attackSpeedEnabled()) {
			//Base Value = 4.0
			final int OP = attributes.getAttackSpeedOperation();
			final double Amount = attributes.getAttackSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String attackspeed = I18n.format(Reference.MODID + ".tooltip.attackspeed");
			tooltip.add(string + " " + blue + attackspeed);
		}
		if (attributes.attackDamageEnabled()) {
			//Base value = 1.0
			final int OP = attributes.getAttackDamageOperation();
			final double Amount = attributes.getAttackDamage();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String attackdamage = I18n.format(Reference.MODID + ".tooltip.attackdamage");
			tooltip.add(string + " " + blue + attackdamage);
		}
		if (attributes.healthEnabled()) {
			//Base Value = 20
			final int OP = attributes.getHealthOperation();
			final double Amount = attributes.getHealth();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String health = I18n.format(Reference.MODID + ".tooltip.health");
			tooltip.add(string + " " + blue + health);
		}
		if (attributes.armorEnabled()) {
			//Base value = 0
			final int OP = attributes.getArmorOperation();
			final double Amount = attributes.getArmor();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String armor = I18n.format(Reference.MODID + ".tooltip.armor");
			tooltip.add(string + " " + blue + armor);
		}
		if (attributes.armorToughnessEnabled()) {
			//Base value = 0
			final int OP = attributes.getArmorToughnessOperation();
			final double Amount = attributes.getArmorToughness();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String toughness = I18n.format(Reference.MODID + ".tooltip.toughness");
			tooltip.add(string + " " + blue + toughness);
		}
		if (attributes.knockbackEnabled()) {
			//Base value = 0
			final int OP = attributes.getKnockbackOperation();
			final double Amount = attributes.getKnockback();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String knockbackresist = I18n.format(Reference.MODID + ".tooltip.knockbackresist");
			tooltip.add(string + " " + blue + knockbackresist);
		}
		if (attributes.movementSpeedEnabled()) {
			//Base value = 0.10000000149011612
			final int OP = attributes.getMovementSpeedOperation();
			final double Amount = attributes.getMovementSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String movementspeed = I18n.format(Reference.MODID + ".tooltip.movementspeed");
			tooltip.add(string + " " + blue + movementspeed);
		}
		if (attributes.luckEnabled()) {
			//Base Value = 0.0
			final int OP = attributes.getLuckOperation();
			final double Amount = attributes.getLuck();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String luck = I18n.format(Reference.MODID + ".tooltip.luck");
			tooltip.add(string + " " + blue + luck);
		}
		if (attributes.swimSpeedEnabled()) {
			//Base value = 1.0
			final int OP = attributes.getSwimSpeedOperation();
			final double Amount = attributes.getSwimSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String swimspeed = I18n.format(Reference.MODID + ".tooltip.swimspeed");
			tooltip.add(string + " " + blue + swimspeed);
		}
		if (attributes.jumpEnabled()) {
			//Base value = 0.42
			final int OP = attributes.getJumpOperation();
			final double Amount = attributes.getJump();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String jumpheight = I18n.format(Reference.MODID + ".tooltip.jumpheight");
			tooltip.add(string + " " + blue + jumpheight);
		}
		if (attributes.stepHeightEnabled()) {
			//Base value = 0.6
			final int OP = attributes.getStepHeightOperation();
			final double Amount = attributes.getStepHeight();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String stepheight = I18n.format(Reference.MODID + ".tooltip.stepheight");
			tooltip.add(string + " " + blue + stepheight);
		}
	}

	public static double parseDoubleSafely(String d) {
		final String number = d.replaceAll("[^.\\d]", "");
		if (!number.isEmpty()) {
			try {
				return Double.parseDouble(number);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
