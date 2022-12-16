package xzeroair.trinkets.items.base;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.races.fairy.config.FairyConfig;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.shared.TransformationRingConfig;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyBindEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.KeyEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.LangEntry;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class TrinketRaceBase extends AccessoryBase implements IRaceProvider {

	public TransformationRingConfig serverConfig;

	protected EntityRace race;

	public TrinketRaceBase(String name, EntityRace race, TransformationRingConfig config) {
		super(name);
		this.race = race;
		this.setUUID("892cfd1f-25c5-44a0-9154-f3b630538c82");
		serverConfig = config;
		this.setAttributeConfig(race.getRaceAttributes().getAttributes());
	}

	@Override
	public void initAbilities(ItemStack stack, EntityLivingBase entity, List<IAbilityInterface> abilities) {

	}

	@Override
	protected void initAttributes(String[] attributeConfig, EntityLivingBase entity) {

	}

	@Override
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		final EntityRace r = this.getRace();
		if (r != null) {
			final KeyEntry size = new OptionEntry("rsize", r.getRaceHeight() + "%");
			if (r.equals(EntityRaces.fairy)) {
				final FairyConfig config = TrinketsConfig.SERVER.races.fairy;
				final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "creativeflight", config.creative_flight);
				return helper.formatAddVariables(translation, size, key1);
			}
			if (r.equals(EntityRaces.dwarf)) {
				final DwarfConfig config = TrinketsConfig.SERVER.races.dwarf;
				final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "fortune", config.fortune);
				final KeyEntry key2 = new LangEntry(this.getTranslationKey(stack), "skilledminer", config.skilled_miner);
				final KeyEntry key3 = new LangEntry(this.getTranslationKey(stack), "staticminer", config.static_mining);
				return helper.formatAddVariables(translation, size, key1, key2, key3);
			}
			if (r.equals(EntityRaces.elf)) {
				final ElfConfig config = TrinketsConfig.SERVER.races.elf;
				final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "chargeshot", config.charge_shot);
				return helper.formatAddVariables(translation, size, key1);
			}
			if (r.equals(EntityRaces.dragon)) {
				final KeyEntry key1 = new KeyBindEntry("denvkb", ModKeyBindings.DRAGONS_EYE_ABILITY.getDisplayName());
				final KeyEntry key2 = new KeyBindEntry("breathkb", ModKeyBindings.RACE_ABILITY.getDisplayName());
				return helper.formatAddVariables(translation, size, key1, key2);
			}
			if (r.equals(EntityRaces.titan)) {
				final TitanConfig config = TrinketsConfig.SERVER.races.titan;
				final KeyEntry key1 = new LangEntry(this.getTranslationKey(stack), "heavy", config.sink);
				return helper.formatAddVariables(translation, size, key1);
			}
			return helper.formatAddVariables(translation, size);
		} else {
			return helper.formatAddVariables(translation);
		}
	}

	@Override
	public EntityRace getRace() {
		return race;
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
