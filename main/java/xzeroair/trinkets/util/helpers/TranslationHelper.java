package xzeroair.trinkets.util.helpers;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketDwarfRing;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;
import xzeroair.trinkets.items.trinkets.TrinketFairyRing;
import xzeroair.trinkets.items.trinkets.TrinketGlowRing;
import xzeroair.trinkets.items.trinkets.TrinketGreaterInertia;
import xzeroair.trinkets.items.trinkets.TrinketInertiaNull;
import xzeroair.trinkets.items.trinkets.TrinketPoison;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.items.trinkets.TrinketSea;
import xzeroair.trinkets.items.trinkets.TrinketWitherRing;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.DamageShield;
import xzeroair.trinkets.util.config.trinkets.DragonsEye;
import xzeroair.trinkets.util.config.trinkets.DwarfRing;
import xzeroair.trinkets.util.config.trinkets.EnderCrown;
import xzeroair.trinkets.util.config.trinkets.FairyRing;
import xzeroair.trinkets.util.config.trinkets.GlowRing;
import xzeroair.trinkets.util.config.trinkets.PoisonStone;
import xzeroair.trinkets.util.config.trinkets.PolarizedStone;
import xzeroair.trinkets.util.config.trinkets.SeaStone;
import xzeroair.trinkets.util.config.trinkets.WitherRing;
import xzeroair.trinkets.util.interfaces.IAttributeConfigHelper;

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

	public static void addTooltips(ItemStack stack, List<String> tooltip) {
		for (int i = 1; i < 10; i++) {
			TextComponentTranslation info = new TextComponentTranslation(stack.getTranslationKey() + ".tooltip" + i);
			if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
				String string = formatLangKeys(stack, info);
				if (!(string.replace("�r", "").isEmpty())) {
					tooltip.add(
							string
					);
				}
			}
		}
	}

	public static void addPotionTooltips(ItemStack stack, World world, List<String> tooltip) {
		if (world == null) {
			return;
		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			for (int i = 1; i < 10; i++) {
				TextComponentTranslation info = new TextComponentTranslation(Reference.MODID + "." + stack.getTranslationKey() + "." + pot.getRegistryName().getPath() + ".tooltip" + i);
				if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
					String string = formatLangKeys(stack, info);
					if (!(string.replace("�r", "").replace("�6", "").replace("�", "").isEmpty())) {
						tooltip.add(
								string
						);
					}
				}
			}
		}
	}

	public static void addOtherTooltips(ItemStack stack, World world, IAttributeConfigHelper attributes, List<String> tooltip) {
		if (world == null) {
			return;
		}
		TextComponentTranslation ctrl = new TextComponentTranslation(Reference.MODID + ".holdctrl");
		TextComponentTranslation shift = new TextComponentTranslation(Reference.MODID + ".holdshift");
		TextComponentTranslation whenworn = new TextComponentTranslation(Reference.MODID + ".onequip");
		TextComponentTranslation noAttributes = new TextComponentTranslation(Reference.MODID + ".noattributes");

		TextComponentTranslation CompatTan = new TextComponentTranslation(stack.getTranslationKey() + ".compat.tan");
		TextComponentTranslation CompatFA = new TextComponentTranslation(stack.getTranslationKey() + ".compat.firstaid");
		TextComponentTranslation CompatEV = new TextComponentTranslation(stack.getTranslationKey() + ".compat.enhancedvisuals");

		addTooltips(stack, tooltip);
		// §r

		if (GuiScreen.isCtrlKeyDown()) {
			if (Loader.isModLoaded("toughasnails") && TrinketsConfig.compat.toughasnails) {
				if (!(CompatTan.getUnformattedComponentText().contentEquals(CompatTan.getKey()) || CompatTan.getUnformattedText().isEmpty())) {
					String string = formatLangKeys(stack, CompatTan);
					if (!(string.replace("�r", "").isEmpty())) {
						tooltip.add(
								string + gold + " (Tough as Nails)"
						);
					}
				}
			}
			if (Loader.isModLoaded("firstaid")) {
				if (!(CompatFA.getUnformattedComponentText().contentEquals(CompatFA.getKey()) || CompatFA.getUnformattedText().isEmpty())) {
					String string = formatLangKeys(stack, CompatFA);
					if (!(string.replace("�r", "").isEmpty())) {
						tooltip.add(
								string + gold + " (First Aid)"
						);
					}
				}
			}
			if (Loader.isModLoaded("enhancedvisuals") && TrinketsConfig.compat.enhancedvisuals) {
				if (!(CompatEV.getUnformattedComponentText().contentEquals(CompatEV.getKey()) || CompatEV.getUnformattedText().isEmpty())) {
					String string = formatLangKeys(stack, CompatEV);
					if (!(string.replace("�r", "").isEmpty())) {
						tooltip.add(
								string + gold + " (Enhanced Visuals)"
						);
					}
				}
			}
		} else {
			tooltip.add(TextFormatting.RESET + "" + dGray + addTextColorFromLangKey(ctrl.getFormattedText()));
		}
		if (GuiScreen.isShiftKeyDown()) {
			if (attributes.ArmorAttributeEnabled() ||
					attributes.ArmorToughnessAttributeEnabled() ||
					attributes.AttackSpeedAttributeEnabled() ||
					attributes.DamageAttributeEnabled() ||
					attributes.HealthAttributeEnabled() ||
					attributes.KnockbackAttributeEnabled() ||
					attributes.LuckAttributeEnabled() ||
					attributes.MovementSpeedAttributeEnabled() ||
					attributes.ReachAttributeEnabled() ||
					attributes.SwimSpeedAttributeEnabled()) {
				tooltip.add(TextFormatting.RESET + addTextColorFromLangKey(whenworn.getFormattedText()));
				addAttributeTooltips(attributes, tooltip);
			} else {
				tooltip.add(TextFormatting.RESET + addTextColorFromLangKey(noAttributes.getFormattedText()));
			}
		} else {
			tooltip.add(TextFormatting.RESET + "" + dGray + addTextColorFromLangKey(shift.getFormattedText()));
		}
	}

	public static String TrinketOptionsKeyTranslate(ItemStack stack, String string) {
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		boolean isCapNull = iCap == null;
		if (stack.getItem() instanceof TrinketGlowRing) {
			GlowRing config = TrinketsConfig.SERVER.GLOW_RING;
			String Key = "blindness";
			string = TranslationKeyReplace(stack, config.prevent_blindness, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.prevent_blindness), string);
		}
		if (stack.getItem() instanceof TrinketWitherRing) {
			WitherRing config = TrinketsConfig.SERVER.WITHER_RING;
			String Key = "wither";
			String Key2 = "leech";
			string = TranslationKeyReplace(stack, config.wither, Key, string);
			string = VariableReplace(config.wither, "witherchance", MathHelper.clamp((1F / config.wither_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.wither), string);

			string = TranslationKeyReplace(stack, config.leech, Key2, string);
			string = VariableReplace(config.leech, "leechamount", (config.leech_amount * 0.5) + "", string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.leech), string);
		}
		if (stack.getItem() instanceof TrinketDamageShield) {
			DamageShield config = TrinketsConfig.SERVER.DAMAGE_SHIELD;
			String Key = "explosionresist";
			String Key2 = "damageignored";
			String Key3 = "headshots";

			string = TranslationKeyReplace(stack, config.explosion_resist, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.explosion_resist), string);
			string = VariableReplace(config.explosion_resist, "explosionresistamount", ((100F - (config.explosion_amount * 100F)) + "%"), string);

			string = TranslationKeyReplace(stack, config.damage_ignore, Key2, string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.damage_ignore), string);
			string = VariableReplace(config.damage_ignore, "damageignoredhitcount", CapabilityProperties.getDamageShield_HitCount() + "/" + config.hits, string);

			string = TranslationKeyReplace(stack, config.compat.firstaid.chance_ignore, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(config.compat.firstaid.chance_ignore), string);
			string = VariableReplace(config.compat.firstaid.chance_ignore, "headshotchance", MathHelper.clamp((1F / config.compat.firstaid.chance_headshots) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

		}
		if (stack.getItem() instanceof TrinketFairyRing) {
			FairyRing config = TrinketsConfig.SERVER.FAIRY_RING;
			String Key = "creativeflight";
			string = TranslationKeyReplace(stack, config.creative_flight, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.creative_flight), string);
		}
		if (stack.getItem() instanceof TrinketDwarfRing) {
			DwarfRing config = TrinketsConfig.SERVER.DWARF_RING;
			String Key = "fortune";
			String Key2 = "skilledminer";
			String Key3 = "staticminer";
			string = TranslationKeyReplace(stack, config.fortune, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.fortune), string);
			string = TranslationKeyReplace(stack, config.skilled_miner, Key2, string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.skilled_miner), string);
			string = TranslationKeyReplace(stack, config.static_mining, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(config.static_mining), string);

		}
		if (stack.getItem() instanceof TrinketDragonsEye) {
			DragonsEye config = TrinketsConfig.SERVER.DRAGON_EYE;

			String Key = "treasurefinder";
			String Key1 = "$target:";
			String Key2 = "blindness";
			String Key3 = "heatimmune";
			String Key4 = "nightvision";
			String Key5 = "looking";
			string = TranslationKeyReplace(stack, config.oreFinder, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.oreFinder), string);
			boolean looking = !isCapNull && (iCap.Target() >= 0);
			if (config.oreFinder) {
				String Type = !looking ? "None" : TrinketsConfig.SERVER.DRAGON_EYE.BLOCKS.Blocks[iCap.Target()];
				String translatedTarget = OreTrackingHelper.translateOreName(Type);
				NonNullList<ItemStack> target = OreDictionary.getOres(Type, false);
				if (!target.isEmpty()) {
					translatedTarget = target.get(0).getDisplayName();
				}
				String getName = Type.contains(":") || Type.contains("[") || Type.contains("]") ? OreTrackingHelper.translateOreName(Type) : translatedTarget.replace(" ore", "").replace(" Ore", "");
				String dragonsEyeTarget = !looking ? "None" : (getName);
				string = string.replace(Key1, dragonsEyeTarget + "");
			} else {
				string = string.replace(Key1, "");
			}

			string = TranslationKeyReplace(stack, config.prevent_blindness, Key2, string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.prevent_blindness), string);
			boolean heatImmune = config.compat.tan.immuneToHeat && TrinketsConfig.compat.toughasnails;
			string = TranslationKeyReplace(stack, heatImmune, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(heatImmune), string);

			string = VariableReplace(config.enabled, Key4, toggleCheckTranslation(!isCapNull && iCap.mainAbility()), string);

			string = VariableReplace(config.oreFinder, Key5, toggleCheckTranslation(looking), string);

			String DragonsEyeKeybindNV = ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName();
			String DragonsEyeKeybindOF = ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
			string = string
					.replace("*denvkb:", DragonsEyeKeybindNV + "")
					.replace("*deofkb:", DragonsEyeKeybindOF + "");
		}
		if (stack.getItem() instanceof TrinketInertiaNull) {
			String inertiaNullFD = "" + ((100F - (TrinketsConfig.SERVER.INERTIA_NULL.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$inertianullfd:", inertiaNullFD + "");
		}
		if (stack.getItem() instanceof TrinketGreaterInertia) {
			String greaterInertiaFD = "" + ((100F - (TrinketsConfig.SERVER.GREATER_INERTIA.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$greaterinertiafd:", greaterInertiaFD + "");
		}
		if (stack.getItem() instanceof TrinketSea) {
			SeaStone config = TrinketsConfig.SERVER.SEA_STONE;
			String key = "bubbles";
			String key2 = "betterswimming";
			String key3 = "tanthirst";
			string = TranslationKeyReplace(stack, config.underwater_breathing, key, string);
			string = VariableReplace(config.underwater_breathing, key, TrinketsConfig.SERVER.SEA_STONE.always_full ? 10 + "" : 1 + "", string);
			string = TranslationKeyReplace(stack, config.Swim_Tweaks, key2, string);
			string = VariableReplace(config.Swim_Tweaks, key2, booleanCheckTranslation(config.Swim_Tweaks), string);
			string = TranslationKeyReplace(stack, config.compat.tan.prevent_thirst, key3, string);
			string = VariableReplace(TrinketsConfig.compat.toughasnails, key3, booleanCheckTranslation(config.compat.tan.prevent_thirst), string);

		}
		if (stack.getItem() instanceof TrinketPolarized) {
			PolarizedStone config = TrinketsConfig.SERVER.POLARIZED_STONE;
			String key = "collect";
			String key2 = "collectxp";
			String key3 = "repel";

			string = TranslationKeyReplace(stack, config.enabled, key, string);
			boolean collectMode = ((stack.getItemDamage() == 1) || (stack.getItemDamage() == 3));
			string = TranslationKeyReplace(stack, config.collectXP, key2, string);
			string = VariableReplace(config.collectXP, key2, booleanCheckTranslation(config.collectXP), string);
			string = VariableReplace(config.repell, "collecttoggle", toggleCheckTranslation(collectMode), string);

			boolean repelMode = ((stack.getItemDamage() == 2) || (stack.getItemDamage() == 3));
			string = TranslationKeyReplace(stack, config.repell, key3, string);
			string = VariableReplace(config.repell, key2, booleanCheckTranslation(config.repell), string);
			string = VariableReplace(config.repell, "repeltoggle", toggleCheckTranslation(repelMode), string);

			String PolarizedStoneKeybind = ModKeyBindings.POLARIZED_STONE_ABILITY.getDisplayName();
			string = string
					.replace("*magnetkb:", PolarizedStoneKeybind + "");
		}
		if (stack.getItem() instanceof TrinketPoison) {
			PoisonStone config = TrinketsConfig.SERVER.POISON_STONE;
			boolean PoisonChance = config.poison;
			String key = "poison";
			string = TranslationKeyReplace(stack, PoisonChance, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(PoisonChance), string);
			string = VariableReplace(PoisonChance, "poisonchance", MathHelper.clamp((1F / config.poison_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			boolean bonusDamage = config.bonus_damage;
			String key2 = "bonusdamage";
			string = TranslationKeyReplace(stack, bonusDamage, key2, string);
			string = VariableReplace(config.enabled, key2, toggleCheckTranslation(bonusDamage), string);
			string = VariableReplace(PoisonChance, "damagemultiplier", translateAttributeValue(1, config.bonus_damage_amount), string);
		}
		if (stack.getItem() instanceof TrinketEnderTiara) {
			EnderCrown config = TrinketsConfig.SERVER.ENDER_CROWN;
			String key = "damageignored";
			String key1 = "endermenchance";
			String key2 = "endermenfollow";
			String key3 = "waterhurts";

			string = TranslationKeyReplace(stack, config.dmgChance, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(config.dmgChance), string);

			string = TranslationKeyReplace(stack, config.spawnChance, key1, string);
			string = VariableReplace(config.enabled, key1, toggleCheckTranslation(config.spawnChance), string);

			string = VariableReplace(config.dmgChance || config.spawnChance, "chance", MathHelper.clamp((1F / config.chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			string = TranslationKeyReplace(stack, config.Follow, key2, string);
			string = VariableReplace(config.enabled, key2, toggleCheckTranslation(config.Follow), string);

			string = TranslationKeyReplace(stack, config.water_hurts, key3, string);
			string = VariableReplace(config.enabled, key3, booleanCheckTranslation(config.water_hurts), string);

		}
		if (stack.getItem().equals(Items.POTIONITEM)) {
			final PotionType pot = PotionUtils.getPotionFromItem(stack);
			if (pot.equals(ModPotionTypes.Enhanced) ||
					pot.equals(ModPotionTypes.Restorative) ||
					pot.equals(ModPotionTypes.DwarfType) ||
					pot.equals(ModPotionTypes.FairyType) ||
					pot.equals(ModPotionTypes.TitanType)) {
				string = string
						.replace("$unused:", "")
						.replace("$fairyticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.FairyDuration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$dwarfticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.DwarfDuration), 0, Integer.MAX_VALUE) / 20F) + "")
						.replace("$titanticks:", (MathHelper.clamp((TrinketsConfig.SERVER.Potion.TitanDuration), 0, Integer.MAX_VALUE) / 20F) + "");
			}
		}
		String AuxKeybind = ModKeyBindings.AUX_KEY.getDisplayName();

		return string

				.replace("*auxkb:", AuxKeybind + "");
	}

	private static String TranslationKeyReplace(ItemStack stack, boolean enabled, String key, String translation) {
		if (enabled) {
			String translatedKey = new TextComponentTranslation(stack.getTranslationKey() + "." + key).getFormattedText();
			translation = translation.replace("@" + key + ":", translatedKey);
		} else {
			translation = translation.replace("@" + key + ":", "");
		}
		return translation;
	}

	private static String VariableReplace(boolean enabled, String key, String Variable, String translation) {
		if (enabled) {
			translation = translation.replace("$" + key + ":", Variable);
			return translation;
		} else {
			translation = translation.replace("$" + key + ":", "");
		}
		return translation;
	}

	public static String booleanCheckTranslation(boolean bool) {
		TextComponentTranslation enabled = new TextComponentTranslation(Reference.MODID + ".tooltip.enabled");
		TextComponentTranslation disabled = new TextComponentTranslation(Reference.MODID + ".tooltip.disabled");
		if (bool == true) {
			return enabled.getFormattedText();// addTextColorFromLangKey(enabled.getFormattedText());
		} else {
			return disabled.getFormattedText();// addTextColorFromLangKey(disabled.getFormattedText());
		}
	}

	public static String toggleCheckTranslation(boolean bool) {
		TextComponentTranslation on = new TextComponentTranslation(Reference.MODID + ".tooltip.on");
		TextComponentTranslation off = new TextComponentTranslation(Reference.MODID + ".tooltip.off");
		if (bool == true) {
			return on.getFormattedText();// addTextColorFromLangKey(on.getFormattedText());
		} else {
			return off.getFormattedText();// addTextColorFromLangKey(off.getFormattedText());
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
		return reset + addColor;
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

	public static String translateAttributeValue(int OP, double Amount) {
		// float percentage = 100/MathHelper.clamp((1F/0)*100, 0, 1000);
		// float translatedFloat = MathHelper.clamp(percentage/100, Float.MIN_VALUE,
		// Float.MAX_VALUE);
		double TranslatedAmount = Amount;
		String string = "";
		if (OP > 0) {
			TranslatedAmount = Amount * (100);
			if (TranslatedAmount > 0) {
				string = "+" + TranslatedAmount + "%";
			} else {
				string = "" + TranslatedAmount + "%";
			}
		} else {
			TranslatedAmount = Amount * (100);
			if (TranslatedAmount > 0) {
				string = "+" + TranslatedAmount;
			} else {
				string = "" + TranslatedAmount;
			}
		}
		return string;
	}

	public static void addAttributeTooltips(IAttributeConfigHelper attributes, List<String> tooltip) {
		if (attributes.AttackSpeedAttributeEnabled()) {
			final int OP = attributes.AttackSpeedAttributeOperation();
			final double Amount = attributes.AttackSpeedAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String attackspeed = I18n.format(Reference.MODID + ".tooltip.attackspeed");
			tooltip.add(color + string + "  " + blue + attackspeed);
		}
		if (attributes.DamageAttributeEnabled()) {
			final int OP = attributes.DamageAttributeOperation();
			final double Amount = attributes.DamageAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String attackdamage = I18n.format(Reference.MODID + ".tooltip.attackdamage");
			tooltip.add(color + string + "  " + blue + attackdamage);
		}
		if (attributes.HealthAttributeEnabled()) {
			final int OP = attributes.HealthAttributeOperation();
			final double Amount = attributes.HealthAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String health = I18n.format(Reference.MODID + ".tooltip.health");
			tooltip.add(color + string + "  " + blue + health);
		}
		if (attributes.ArmorAttributeEnabled()) {
			final int OP = attributes.ArmorAttributeOperation();
			final double Amount = attributes.ArmorAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String armor = I18n.format(Reference.MODID + ".tooltip.armor");
			tooltip.add(color + string + "  " + blue + armor);
		}
		if (attributes.ArmorToughnessAttributeEnabled()) {
			final int OP = attributes.ArmorToughnessAttributeOperation();
			final double Amount = attributes.ArmorToughnessAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String toughness = I18n.format(Reference.MODID + ".tooltip.toughness");
			tooltip.add(color + string + "  " + blue + toughness);
		}
		if (attributes.KnockbackAttributeEnabled()) {
			final int OP = attributes.KnockbackAttributeOperation();
			final double Amount = attributes.KnockbackAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String knockbackresist = I18n.format(Reference.MODID + ".tooltip.knockbackresist");
			tooltip.add(color + string + "  " + blue + knockbackresist);
		}
		if (attributes.MovementSpeedAttributeEnabled()) {
			final int OP = attributes.MovementSpeedAttributeOperation();
			final double Amount = attributes.MovementSpeedAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String movementspeed = I18n.format(Reference.MODID + ".tooltip.movementspeed");
			tooltip.add(color + string + "  " + blue + movementspeed);
		}
		if (attributes.SwimSpeedAttributeEnabled()) {
			final int OP = attributes.SwimSpeedAttributeOperation();
			final double Amount = attributes.SwimSpeedAttributeAmount();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String swimspeed = I18n.format(Reference.MODID + ".tooltip.swimspeed");
			tooltip.add(color + string + "  " + blue + swimspeed);
		}

		// final String stepheight = TextFormatting.BLUE + I18n.format(Reference.MODID +
		// ".tooltip.stepheight");
		// final String jumpheight = TextFormatting.BLUE + I18n.format(Reference.MODID +
		// ".tooltip.jumpheight");
		// tooltip.add(jumpheight);
		// tooltip.add(stepheight);
	}

}
