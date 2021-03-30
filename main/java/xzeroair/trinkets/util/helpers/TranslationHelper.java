package xzeroair.trinkets.util.helpers;

import java.util.List;

import javax.annotation.Nullable;

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
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.attributes.AttributeConfigWrapper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.items.base.TrinketRaceBase;
import xzeroair.trinkets.items.foods.Mana_Candy;
import xzeroair.trinkets.items.foods.Mana_Crystal;
import xzeroair.trinkets.items.trinkets.TrinketArcingOrb;
import xzeroair.trinkets.items.trinkets.TrinketDamageShield;
import xzeroair.trinkets.items.trinkets.TrinketDragonsEye;
import xzeroair.trinkets.items.trinkets.TrinketEnderTiara;
import xzeroair.trinkets.items.trinkets.TrinketFaelisClaws;
import xzeroair.trinkets.items.trinkets.TrinketGlowRing;
import xzeroair.trinkets.items.trinkets.TrinketGreaterInertia;
import xzeroair.trinkets.items.trinkets.TrinketInertiaNull;
import xzeroair.trinkets.items.trinkets.TrinketPoison;
import xzeroair.trinkets.items.trinkets.TrinketPolarized;
import xzeroair.trinkets.items.trinkets.TrinketSea;
import xzeroair.trinkets.items.trinkets.TrinketWitherRing;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigArcingOrb;
import xzeroair.trinkets.util.config.trinkets.ConfigDamageShield;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.config.trinkets.ConfigEnderCrown;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;
import xzeroair.trinkets.util.config.trinkets.ConfigGlowRing;
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

	private static boolean isStringEmpty(String string) {
		return string
				.replace("�r", "")
				.replace("�6", "")
				.replace("�", "")
				.replace("§r", "")
				.replaceAll(" ", "")
				.isEmpty();
	}

	public static void addTooltips(ItemStack stack, @Nullable World world, List<String> tooltip) {
		for (int i = 1; i < 10; i++) {
			TextComponentTranslation info = new TextComponentTranslation(stack.getTranslationKey() + ".tooltip" + i);
			if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
				String string = formatLangKeys(stack, info);
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
				TextComponentTranslation info = new TextComponentTranslation(Reference.MODID + "." + stack.getTranslationKey() + "." + pot.getRegistryName().getPath() + ".tooltip" + i);
				if (!(info.getUnformattedComponentText().contentEquals(info.getKey()) || info.getUnformattedText().isEmpty())) {
					String string = formatLangKeys(stack, info);
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
		TextComponentTranslation ctrl = new TextComponentTranslation(Reference.MODID + ".holdctrl");
		TextComponentTranslation CompatTan = new TextComponentTranslation(stack.getTranslationKey() + ".compat.tan");
		TextComponentTranslation CompatFA = new TextComponentTranslation(stack.getTranslationKey() + ".compat.firstaid");
		TextComponentTranslation CompatEV = new TextComponentTranslation(stack.getTranslationKey() + ".compat.enhancedvisuals");

		boolean tanEmpty = (CompatTan.getUnformattedComponentText().contentEquals(CompatTan.getKey()) || CompatTan.getUnformattedText().isEmpty());
		boolean evEmpty = (CompatEV.getUnformattedComponentText().contentEquals(CompatEV.getKey()) || CompatEV.getUnformattedText().isEmpty());
		boolean faEmpty = (CompatFA.getUnformattedComponentText().contentEquals(CompatFA.getKey()) || CompatFA.getUnformattedText().isEmpty());
		String TAN = formatLangKeys(stack, CompatTan);
		String EV = formatLangKeys(stack, CompatEV);
		String FA = formatLangKeys(stack, CompatFA);
		if (GuiScreen.isCtrlKeyDown()) {
			if (Loader.isModLoaded("toughasnails") && TrinketsConfig.compat.toughasnails) {
				if (!tanEmpty) {
					String string = formatLangKeys(stack, CompatTan);
					if (!isStringEmpty(string)) {
						tooltip.add(
								string + gold + " (Tough as Nails)"
						);
					}
				}
			}
			if (Loader.isModLoaded("firstaid")) {
				if (!faEmpty) {
					String string = formatLangKeys(stack, CompatFA);
					if (!isStringEmpty(string)) {
						tooltip.add(
								string + gold + " (First Aid)"
						);
					}
				}
			}
			if (Loader.isModLoaded("enhancedvisuals") && TrinketsConfig.compat.enhancedvisuals) {
				if (!evEmpty) {
					String string = formatLangKeys(stack, CompatEV);
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

	public static void addOtherTooltips(ItemStack stack, @Nullable World world, AttributeConfigWrapper attributes, List<String> tooltip) {
		if (world == null) {
			return;
		}
		TextComponentTranslation shift = new TextComponentTranslation(Reference.MODID + ".holdshift");
		TextComponentTranslation whenworn = new TextComponentTranslation(Reference.MODID + ".onequip");

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
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		boolean isCapNull = iCap == null;
		if (stack.getItem() instanceof TrinketGlowRing) {
			ConfigGlowRing config = TrinketsConfig.SERVER.Items.GLOW_RING;
			String Key = "blindness";
			string = TranslationKeyReplace(stack, config.prevent_blindness, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.prevent_blindness), string);
		}
		if (stack.getItem() instanceof TrinketWitherRing) {
			ConfigWitherRing config = TrinketsConfig.SERVER.Items.WITHER_RING;
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
			ConfigDamageShield config = TrinketsConfig.SERVER.Items.DAMAGE_SHIELD;
			String Key = "explosionresist";
			String Key2 = "damageignored";
			String Key3 = "headshots";

			string = TranslationKeyReplace(stack, config.explosion_resist, Key, string);
			string = VariableReplace(config.enabled, Key, booleanCheckTranslation(config.explosion_resist), string);
			string = VariableReplace(config.explosion_resist, "explosionresistamount", ((100F - (config.explosion_amount * 100F)) + "%"), string);

			string = TranslationKeyReplace(stack, config.damage_ignore, Key2, string);
			string = VariableReplace(config.enabled, Key2, booleanCheckTranslation(config.damage_ignore), string);
			int hitcount = 0;
			if (TrinketHelper.getTagCompoundSafe(stack).hasKey("count")) {
				hitcount = TrinketHelper.getTagCompoundSafe(stack).getInteger("count");
			}
			string = VariableReplace(config.damage_ignore, "damageignoredhitcount", hitcount + "/" + config.hits, string);

			string = TranslationKeyReplace(stack, config.compat.firstaid.chance_ignore, Key3, string);
			string = VariableReplace(config.enabled, Key3, booleanCheckTranslation(config.compat.firstaid.chance_ignore), string);
			string = VariableReplace(config.compat.firstaid.chance_ignore, "headshotchance", MathHelper.clamp((1F / config.compat.firstaid.chance_headshots) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

		}
		if (stack.getItem() instanceof TrinketRaceBase) {
			TrinketRaceBase rr = (TrinketRaceBase) stack.getItem();
			string = VariableReplace(rr.ItemEnabled(), "rsize", rr.getRacialTraits().getRaceSize() + "%", string);
			if (rr.getRacialTraits().equals(EntityRaces.fairy)) {
				FairyConfig config = TrinketsConfig.SERVER.races.fairy;
				String Key = "creativeflight";
				string = TranslationKeyReplace(stack, config.creative_flight, Key, string);
				string = VariableReplace(true, Key, booleanCheckTranslation(config.creative_flight), string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.dwarf)) {
				DwarfConfig config = TrinketsConfig.SERVER.races.dwarf;
				String Key = "fortune";
				String Key2 = "skilledminer";
				String Key3 = "staticminer";
				string = TranslationKeyReplace(stack, config.fortune, Key, string);
				string = VariableReplace(true, Key, booleanCheckTranslation(config.fortune), string);
				string = TranslationKeyReplace(stack, config.skilled_miner, Key2, string);
				string = VariableReplace(true, Key2, booleanCheckTranslation(config.skilled_miner), string);
				string = TranslationKeyReplace(stack, config.static_mining, Key3, string);
				string = VariableReplace(true, Key3, booleanCheckTranslation(config.static_mining), string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.elf)) {
				ElfConfig config = TrinketsConfig.SERVER.races.elf;
				String Key = "chargeshot";
				string = TranslationKeyReplace(stack, config.charge_shot, Key, string);
			}
			if (rr.getRacialTraits().equals(EntityRaces.dragon)) {
				String breathKey = ModKeyBindings.RACE_ABILITY.getDisplayName();
				string = string
						.replace("*breathkb:", breathKey + "");
			}
		}
		if (stack.getItem() instanceof TrinketDragonsEye) {
			ConfigDragonsEye config = TrinketsConfig.SERVER.Items.DRAGON_EYE;

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
				String Type = !looking ? "None" : TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks[iCap.Target()];
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
			String inertiaNullFD = "" + ((100F - (TrinketsConfig.SERVER.Items.INERTIA_NULL.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$inertianullfd:", inertiaNullFD + "");
		}
		if (stack.getItem() instanceof TrinketGreaterInertia) {
			String greaterInertiaFD = "" + ((100F - (TrinketsConfig.SERVER.Items.GREATER_INERTIA.falldamage_amount * 100F)) + "%");
			string = string
					.replace("$greaterinertiafd:", greaterInertiaFD + "");
		}
		if (stack.getItem() instanceof TrinketSea) {
			ConfigSeaStone config = TrinketsConfig.SERVER.Items.SEA_STONE;
			String key = "bubbles";
			String key2 = "betterswimming";
			String key3 = "tanthirst";
			string = TranslationKeyReplace(stack, config.underwater_breathing, key, string);
			string = VariableReplace(config.underwater_breathing, key, TrinketsConfig.SERVER.Items.SEA_STONE.always_full ? 10 + "" : 1 + "", string);
			string = TranslationKeyReplace(stack, config.Swim_Tweaks, key2, string);
			string = VariableReplace(config.Swim_Tweaks, key2, booleanCheckTranslation(config.Swim_Tweaks), string);
			string = TranslationKeyReplace(stack, config.compat.tan.prevent_thirst, key3, string);
			string = VariableReplace(TrinketsConfig.compat.toughasnails, key3, booleanCheckTranslation(config.compat.tan.prevent_thirst), string);

		}
		if (stack.getItem() instanceof TrinketPolarized) {
			ConfigPolarizedStone config = TrinketsConfig.SERVER.Items.POLARIZED_STONE;
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
			ConfigPoisonStone config = TrinketsConfig.SERVER.Items.POISON_STONE;
			boolean PoisonChance = config.poison;
			String key = "poison";
			string = TranslationKeyReplace(stack, PoisonChance, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(PoisonChance), string);
			string = VariableReplace(PoisonChance, "poisonchance", MathHelper.clamp((1F / config.poison_chance) * 100, Integer.MIN_VALUE, Integer.MAX_VALUE) + "%", string);

			boolean bonusDamage = config.bonus_damage;
			String key2 = "bonusdamage";
			string = TranslationKeyReplace(stack, bonusDamage, key2, string);
			string = VariableReplace(config.enabled, key2, toggleCheckTranslation(bonusDamage), string);
			float pa = config.bonus_damage_amount <= 0 ? config.bonus_damage_amount : config.bonus_damage_amount - 1;
			string = VariableReplace(PoisonChance, "damagemultiplier", translateAttributeValue(1, pa), string);
		}
		if (stack.getItem() instanceof TrinketEnderTiara) {
			ConfigEnderCrown config = TrinketsConfig.SERVER.Items.ENDER_CROWN;
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
		if (stack.getItem() instanceof TrinketArcingOrb) {
			ConfigArcingOrb config = TrinketsConfig.SERVER.Items.ARCING_ORB;

			String key = "dodge";
			String key1 = "dodgecost";
			String key2 = "dodgestun";
			String key3 = "boltattack";
			String key4 = "boltcost";
			String key5 = "boltdamage";

			string = TranslationKeyReplace(stack, config.dodgeAbility, key, string);
			string = VariableReplace(config.enabled, key, toggleCheckTranslation(config.dodgeAbility), string);

			string = VariableReplace(config.enabled, key1, config.dodgeCost + "", string);

			string = TranslationKeyReplace(stack, config.dodgeStuns, key2, string);
			string = VariableReplace(config.enabled, key2, booleanCheckTranslation(config.dodgeStuns), string);

			string = TranslationKeyReplace(stack, config.attackAbility, key3, string);
			string = VariableReplace(config.enabled, key3, toggleCheckTranslation(config.attackAbility), string);

			string = VariableReplace(config.enabled, key4, config.attackCost + "", string);

			string = VariableReplace(config.enabled, key5, config.attackDmg + "", string);

			String ArcingOrbKeybindBoltAttack = ModKeyBindings.ARCING_ORB_ABILITY.getDisplayName();
			string = string
					.replace("*arckb:", ArcingOrbKeybindBoltAttack + "");
		}
		if (stack.getItem() instanceof TrinketFaelisClaws) {
			ConfigFaelisClaw config = TrinketsConfig.SERVER.Items.FAELIS_CLAW;
			string = VariableReplace(true, "clawbleed", config.duration + "", string);
		}
		if (stack.getItem() instanceof Mana_Candy) {
			string = VariableReplace(true, "MPRestore", "25-50" + "", string);
		}
		if (stack.getItem() instanceof Mana_Crystal) {
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
		String AuxKeybind = ModKeyBindings.AUX_KEY.getDisplayName();

		return string

				.replace("*auxkb:", AuxKeybind + "");
	}

	private static String TranslationKeyReplace(ItemStack stack, boolean enabled, String key, String translation) {
		if (enabled) {
			String translatedKey = new TextComponentTranslation(stack.getTranslationKey() + "." + key).getFormattedText();
			return translation.replace("@" + key.toLowerCase() + ":", translatedKey);
		} else {
			return translation.replace("@" + key.toLowerCase() + ":", "");
		}
	}

	private static String VariableReplace(boolean enabled, String key, String Variable, String translation) {
		//TODO the .replace("§r", "") is added somewhere in this
		if (enabled) {
			return translation.replace("$" + key.toLowerCase() + ":", Variable);
		} else {
			return translation.replace("$" + key.toLowerCase() + ":", "");
		}
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
		return string;
	}

	public static void addAttributeTooltips(AttributeConfigWrapper attributes, List<String> tooltip) {
		if (attributes.attackSpeedEnabled()) {
			//Base Value = 4.0
			final int OP = attributes.getAttackSpeedOperation();
			final double Amount = attributes.getAttackSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String attackspeed = I18n.format(Reference.MODID + ".tooltip.attackspeed");
			tooltip.add(color + string + " " + blue + attackspeed);
		}
		if (attributes.attackDamageEnabled()) {
			//Base value = 1.0
			final int OP = attributes.getAttackDamageOperation();
			final double Amount = attributes.getAttackDamage();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String attackdamage = I18n.format(Reference.MODID + ".tooltip.attackdamage");
			tooltip.add(color + string + " " + blue + attackdamage);
		}
		if (attributes.healthEnabled()) {
			//Base Value = 20
			final int OP = attributes.getHealthOperation();
			final double Amount = attributes.getHealth();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String health = I18n.format(Reference.MODID + ".tooltip.health");
			tooltip.add(color + string + " " + blue + health);
		}
		if (attributes.armorEnabled()) {
			//Base value = 0
			final int OP = attributes.getArmorOperation();
			final double Amount = attributes.getArmor();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String armor = I18n.format(Reference.MODID + ".tooltip.armor");
			tooltip.add(color + string + " " + blue + armor);
		}
		if (attributes.armorToughnessEnabled()) {
			//Base value = 0
			final int OP = attributes.getArmorToughnessOperation();
			final double Amount = attributes.getArmorToughness();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String toughness = I18n.format(Reference.MODID + ".tooltip.toughness");
			tooltip.add(color + string + " " + blue + toughness);
		}
		if (attributes.knockbackEnabled()) {
			//Base value = 0
			final int OP = attributes.getKnockbackOperation();
			final double Amount = attributes.getKnockback();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String knockbackresist = I18n.format(Reference.MODID + ".tooltip.knockbackresist");
			tooltip.add(color + string + " " + blue + knockbackresist);
		}
		if (attributes.movementSpeedEnabled()) {
			//Base value = 0.10000000149011612
			final int OP = attributes.getMovementSpeedOperation();
			final double Amount = attributes.getMovementSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String movementspeed = I18n.format(Reference.MODID + ".tooltip.movementspeed");
			tooltip.add(color + string + " " + blue + movementspeed);
		}
		if (attributes.swimSpeedEnabled()) {
			//Base value = 1.0
			final int OP = attributes.getSwimSpeedOperation();
			final double Amount = attributes.getSwimSpeed();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String swimspeed = I18n.format(Reference.MODID + ".tooltip.swimspeed");
			tooltip.add(color + string + " " + blue + swimspeed);
		}
		if (attributes.jumpEnabled()) {
			//Base value = 0.42
			final int OP = attributes.getJumpOperation();
			final double Amount = attributes.getJump();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String jumpheight = I18n.format(Reference.MODID + ".tooltip.jumpheight");
			tooltip.add(color + string + " " + blue + jumpheight);
		}
		if (attributes.stepHeightEnabled()) {
			//Base value = 0.6
			final int OP = attributes.getStepHeightOperation();
			final double Amount = attributes.getStepHeight();
			final String string = TranslationHelper.translateAttributeValue(OP, Amount);
			final String color = Double.parseDouble(string.replace("+", "").replace("%", "")) > 0 ? "" + green : "" + red;
			final String stepheight = I18n.format(Reference.MODID + ".tooltip.stepheight");
			tooltip.add(color + string + " " + blue + stepheight);
		}
	}

}
