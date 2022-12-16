package xzeroair.trinkets.traits.abilities;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class Ability implements IAbilityInterface {

	//	public static final TrinketRegistry<ResourceLocation, IAbilityInterface> Registry = Trinkets.abilityRegistry;

	private ResourceLocation regName;
	private String uuid;
	private String translationKey;

	private boolean removeAbility;
	protected Random random = Reference.random;
	protected TickHandler tickHandler;
	protected boolean enabled;
	protected int value;

	public Ability() {
		tickHandler = new TickHandler();
		this.setTranslationKey(this.getClass().getSimpleName());
		this.setRegistryName(this.getClass().getCanonicalName());
	}

	public Ability(String name) {
		tickHandler = new TickHandler();
		this.setTranslationKey(name);
		this.setRegistryName(name);
	}

	public Ability(String modID, String name) {
		tickHandler = new TickHandler();
		this.setTranslationKey(name);
		this.setRegistryName(modID, name);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	@Override
	public ResourceLocation getRegistryName() {
		return regName;
	}

	protected Ability setRegistryName(String name) {
		String[] astring = new String[] { Reference.MODID, name };
		int i = name.indexOf(58);

		if (i >= 0) {
			astring[1] = name.substring(i + 1, name.length());

			if (i > 1) {
				astring[0] = name.substring(0, i);
			}
		}

		String modID = (astring[0] == null) || (astring.length == 0) ? Reference.MODID : astring[0].toLowerCase(Locale.ROOT);
		String abilityname = astring[1].toLowerCase(Locale.ROOT);

		return this.setRegistryName(modID, abilityname);
	}

	protected Ability setRegistryName(ResourceLocation name) {
		if (regName != null) {
			throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + this.getRegistryName());
		}
		regName = name;
		return this;
	}

	protected Ability setRegistryName(String modID, String name) {
		return this.setRegistryName(new ResourceLocation(modID, name));
	}

	@Override
	public String getUUID() {
		return uuid;
	}

	protected Ability setUUID(String uuid) {
		this.uuid = uuid;
		return this;
	}

	protected Ability setTranslationKey(String translationKey) {
		this.translationKey = translationKey;
		return this;
	}

	@Override
	public String getTranslationKey() {
		return "ability." + translationKey;
	}

	@Override
	public String getDisplayName() {
		return new TextComponentTranslation(this.getTranslationKey() + ".name").getFormattedText();
	}

	@Override
	public void getDescription(List<String> tooltips) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		for (int i = 1; i < 10; i++) {
			final int index = i;
			final String string = helper.getLangTranslation(this.getTranslationKey() + ".tooltip" + i);
			//			final String string = helper.getLangTranslation(this.getTranslationKey() + ".tooltip" + i,
			//					lang -> this.customItemInformation(stack, world, flagIn, index, lang));
			if (!helper.isStringEmpty(string)) {
				tooltips.add(
						string
				);
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	public boolean isCreativePlayer(Entity entity) {
		final boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		return flag;
	}

	public boolean isSpectator(Entity entity) {
		final boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isSpectator());
		return flag;
	}

	@Override
	public boolean shouldRemove() {
		return removeAbility;
	}

	@Override
	public Ability scheduleRemoval() {
		removeAbility = true;
		return this;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //

	//	private static int IndexID = 0;
	//
	//	private static int nextID() {
	//		return IndexID++;
	//	}
	//
	//	public static void init() {
	//		registerAbility(nextID(), new Ability("night_vision").setUUID("8893b02a-019b-4779-8415-62dc7ca091a0"));
	//		registerAbility(nextID(), new Ability("creative_flight").setUUID("7d75fc7c-5525-4e7f-8ca0-c74c300be0fe"));
	//		registerAbility(nextID(), new Ability("fire_immunity").setUUID("06a3beda-9655-4ef0-8b09-53b160ec2ed9"));
	//		registerAbility(nextID(), new Ability("fire_breathing").setUUID("c4121690-6473-48d3-b1c5-eb968ef2d708")); // New
	//		registerAbility(nextID(), new Ability("block_detection").setUUID("d8234607-20ff-48da-8f10-a10baf250704"));
	//		registerAbility(nextID(), new Ability("block_climbing").setUUID("5f67c4af-6a65-4e0b-8312-ac467ff49987"));
	//		registerAbility(nextID(), new Ability("wither_immunity").setUUID("4f9a208c-c741-4f97-ab67-a160dbee8042"));
	//		registerAbility(nextID(), new Ability("weightless").setUUID("6854d761-6a55-457a-863e-bf441c3a3091"));
	//		registerAbility(nextID(), new Ability("well_rested").setUUID("bcc0c8c6-e583-46f8-9b9c-bbed465a7cec")); // New
	//		registerAbility(nextID(), new Ability("poison_affinity").setUUID("0f6f712f-8064-4727-bac1-22cb6c3142f8"));
	//		registerAbility(nextID(), new Ability("water_affinity").setUUID("046d0580-c619-462d-871c-f997c2e7bcb3"));
	//
	//		registerAbility(nextID(), new Ability("fall_resistance").setUUID("325386ca-5d8d-4c04-881b-072cf8e96719"));
	//		registerAbility(nextID(), new Ability("nullify_kinetic").setUUID("25db938a-5995-46cc-bac5-48e6d9702070"));
	//
	//		registerAbility(nextID(), new Ability("safe_guard").setUUID("293cc22d-6e6a-4411-9f79-7841a2c8414a")); // SoH ability, Maybe rename
	//
	//		registerAbility(nextID(), new Ability("ender_queen").setUUID("e500958e-74de-4627-9564-5b91868a5169"));
	//		registerAbility(nextID(), new Ability("vicious_strike").setUUID("fa670f49-6bc2-4afa-b24b-bffb0deefd4e")); // New
	//		registerAbility(nextID(), new Ability("magnetic").setUUID("65737c3d-af93-45f7-8320-743a8d98cfaf"));
	//		registerAbility(nextID(), new Ability("repel").setUUID("3c998bf3-b4de-4f6c-ae96-98f55717a169"));
	//
	//		registerAbility(nextID(), new Ability("skilled_miner").setUUID("8695126d-bde6-4bd5-9966-9a9ddc02a83b"));
	//		registerAbility(nextID(), new Ability("psudo_fortune").setUUID("1932b836-f813-4503-9794-5efcb2379a7e"));
	//		registerAbility(nextID(), new Ability("charged_shot").setUUID("353d81e0-d83d-4a56-9031-9c6c5b1da1dd")); // New
	//		registerAbility(nextID(), new Ability("lightning_bolt").setUUID("f8a43ed9-a2af-4f32-a699-f2cebdc6b1da")); // New
	//		registerAbility(nextID(), new Ability("dodging").setUUID("3a2a91e0-a068-4e73-b115-45deb9c2e0ad")); // New
	//
	//		registerAbility(nextID(), new Ability("large_hands").setUUID("ea1b4af6-840f-4cc5-8adf-c31ae003fc02")); // New
	//		registerAbility(nextID(), new Ability("heavy").setUUID("526830f2-6ec9-46e1-8674-a1c385b3612b")); // New
	//
	//		// Other Mods
	//		registerAbility(nextID(), new Ability("firstaid_reflex").setUUID("2529a6c3-5e77-4889-81d8-b09efcb5e590")); // New
	//
	//		registerAbility(nextID(), new Ability("heat_immunity").setUUID("fab64df5-6fc8-45a2-a1bd-3d543509ea49")); // New
	//		registerAbility(nextID(), new Ability("cold_immunity").setUUID("9723e39d-0569-48d3-8686-6119ed80414d")); // New
	//		registerAbility(nextID(), new Ability("thirst_immunity").setUUID("a1c5c95b-0083-4bd4-99e7-22b7f9fd2333")); // New
	//		registerAbility(nextID(), new Ability("parasites_immunity").setUUID("90ae2490-10bd-45ba-a281-941a9b1c5219")); // New
	//		//				registerAbility(nextID(), new Ability("magnetic").setUUID(""));
	//
	//		final RegisterAbilitiesEvent registryEvent = new RegisterAbilitiesEvent(Registry);
	//		MinecraftForge.EVENT_BUS.post(registryEvent);
	//		for (final Entry<ResourceLocation, IAbilityInterface> entry : registryEvent.getEntries().entrySet()) {
	//			Trinkets.log.info("Registering " + entry.getKey());
	//			registerAbility(nextID(), entry.getKey(), entry.getValue());
	//		}
	//	}

	//	public static int getIDFromAbility(IAbilityInterface ability) {
	//		return ability == null ? 0 : Registry.getIDForObject(ability);
	//	}
	//	//
	//	//	public static ResourceLocation getRegistryNameFromAbility(IAbilityInterface ability) {
	//	//		return ability == null ? new ResourceLocation(Reference.MODID, "human") : Registry.getNameForObject(ability);
	//	//	}
	//
	//	public static IAbilityInterface getById(int id) {
	//		return Registry.getObjectById(id);
	//	}
	//
	//	public static IAbilityInterface getByUUID(UUID uuid) {
	//		return Registry.getObjectByUUID(uuid);
	//	}

	/**
	 * Tries to get Object by it's name or a String representation of a numerical
	 * ID. If both fail, null is returned.
	 */
	//	@Nullable
	//	public static IAbilityInterface getByNameOrId(String id) {
	//		final IAbilityInterface race = Registry.getObject(new ResourceLocation(id.toLowerCase()));
	//
	//		if (race == null) {
	//			try {
	//				return getById(Integer.parseInt(id));
	//			} catch (final NumberFormatException var3) {
	//			}
	//		}
	//
	//		return race;
	//	}
	//
	//	private static void registerAbility(int id, IAbilityInterface ability) {
	//		registerAbility(id, ability.getRegistryName(), ability);
	//	}
	//
	//	private static void registerAbility(int id, ResourceLocation textualID, IAbilityInterface ability) {
	//		Registry.register(id, textualID, UUID.fromString(ability.getUUID()), ability);
	//	}

}
