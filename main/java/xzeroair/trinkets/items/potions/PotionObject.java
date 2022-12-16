package xzeroair.trinkets.items.potions;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.Reference;

public class PotionObject {

	protected Potion potion;
	protected PotionType base;
	protected PotionType normal;
	protected PotionType extended;
	protected Ingredient ingredient;
	protected int duration;
	protected String modid;
	protected String name;
	protected int color;
	protected boolean isBase;
	protected boolean addMundane;
	protected boolean addAwkward;
	protected boolean addThick;

	/*
	 * Create Base Potions
	 */
	public PotionObject(Potion potion, String modid, String name, int color, int duration, Ingredient ingredient) {
		this(potion, modid, name, color, duration, duration * 3, ingredient);
	}

	/*
	 * Create Base Potions
	 */
	public PotionObject(Potion potion, String modid, String name, int color, int duration, int extendedDuration, Ingredient ingredient) {
		this.potion = potion;
		this.modid = modid;
		this.name = name;
		this.color = color;
		this.duration = duration;
		this.ingredient = ingredient;
		normal = this.createPotionType(modid, name, potion, duration).setRegistryName(name);
		extended = this.createPotionType(modid, name, potion, extendedDuration).setRegistryName("extended_" + name);
		isBase = true;
		addMundane = true;
		addAwkward = true;
		addThick = true;
	}

	/*
	 * Create Sub Potions With Recipes
	 */
	public PotionObject(Potion potionProduct, PotionType craftingBase, String modid, String potionName, int color, int duration, Ingredient craftingIngredient) {
		this(potionProduct, craftingBase, modid, potionName, color, duration, duration * 3, craftingIngredient);
	}

	/*
	 * Create Sub Potions With Recipes
	 */
	public PotionObject(Potion potionProduct, PotionType craftingBase, String modid, String potionName, int color, int duration, int extendedDuration, Ingredient craftingIngredient) {
		this(potionProduct, modid, potionName, color, duration, extendedDuration, craftingIngredient);
		base = craftingBase;
		isBase = false;
	}

	public PotionObject makeMundane(boolean make) {
		addMundane = make;
		return this;
	}

	public PotionObject makeAwkward(boolean make) {
		addAwkward = make;
		return this;
	}

	public PotionObject makeThick(boolean make) {
		addThick = make;
		return this;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Potion getPotion() {
		return potion;
	}

	public PotionType getBasePotionType() {
		return base;
	}

	public PotionType getPotionType() {
		return normal;
	}

	public PotionType getPotionTypeExtended() {
		return extended;
	}

	public String getModid() {
		return modid;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public int getDuration() {
		return duration;
	}

	public PotionObject registryType() {
		final boolean isInternal = this.getModid().contentEquals(Reference.MODID);
		return this.registerType(isInternal);
	}

	public PotionObject registerTypeWithoutRecipe() {
		final boolean isInternal = this.getModid().contentEquals(Reference.MODID);
		return this.registerTypeWithoutRecipe(isInternal);
	}

	public PotionObject registerPotion() {
		final boolean isInternal = this.getModid().contentEquals(Reference.MODID);
		return this.registerPotion(isInternal);
	}

	public PotionObject registerWithPotion() {
		final boolean isInternal = this.getModid().contentEquals(Reference.MODID);
		return this.registerPotion(isInternal).registerType(isInternal);
	}

	public PotionObject registerWithPotionWithoutRecipe() {
		final boolean isInternal = this.getModid().contentEquals(Reference.MODID);
		return this.registerPotion(isInternal).registerTypeWithoutRecipe(isInternal);
	}

	public PotionObject registerType(boolean addToInternalRegistry) {
		ForgeRegistries.POTION_TYPES.register(normal);
		if (addToInternalRegistry) {
			ModPotionTypes.TrinketPotionTypes.put(name, normal);
		}
		if (isBase) {
			this.addBaseMix(normal, ingredient);
		} else {
			PotionHelper.addMix(base, ingredient, normal);
		}
		if (duration > 0) {
			ForgeRegistries.POTION_TYPES.register(extended);
			if (addToInternalRegistry) {
				ModPotionTypes.TrinketPotionTypes.put("extended_" + name, extended);
			}
			PotionHelper.addMix(normal, Items.REDSTONE, extended);
		}
		return this;
	}

	public PotionObject registerTypeWithoutRecipe(boolean addToInternalRegistry) {
		ForgeRegistries.POTION_TYPES.register(normal);
		if (addToInternalRegistry) {
			ModPotionTypes.TrinketPotionTypes.put(name, normal);
		}
		if (duration > 0) {
			ForgeRegistries.POTION_TYPES.register(extended);
			if (addToInternalRegistry) {
				ModPotionTypes.TrinketPotionTypes.put("extended_" + name, extended);
			}
		}
		return this;
	}

	public PotionObject registerPotion(boolean addToInertnalRegistry) {
		ForgeRegistries.POTIONS.register(potion);
		if (addToInertnalRegistry) {
			ModPotionTypes.TrinketPotions.put(name, potion);
		}
		return this;
	}

	public PotionType createPotionType(String modID, String name, Potion pot, int duration) {
		return this.createPotionType(modID, name, pot, duration, 0);
	}

	/*
	 * Disabling Particles makes the Potion Color 0
	 */
	public PotionType createPotionType(String modID, String name, Potion pot, int duration, int amplifier) {
		return this.createPotionType(modID, name, pot, duration, amplifier, true);
	}

	public PotionType createPotionType(String modID, String name, Potion pot, int duration, int amplifier, boolean showParticles) {
		return this.createPotionType(modID, name, pot, duration, amplifier, false, showParticles);
	}

	public PotionType createPotionType(String modID, String name, Potion pot, int duration, int amplifier, boolean isAmbient, boolean showParticles) {
		//		final TrinketsPotionEffect effect = new TrinketsPotionEffect(pot, duration, amplifier, isAmbient, showParticles);
		final TrinketsPotionEffect effect = new TrinketsPotionEffect(pot, duration, amplifier, isAmbient, showParticles);
		return this.createPotionType(modID, name, effect);
	}

	public PotionType createPotionType(String modID, String name, PotionEffect... effects) {
		final PotionType type = new PotionType(
				modID + "." + name, effects
		);
		return type;
	}

	public void addBaseMix(PotionType potion, Ingredient ingredient) {
		if (addMundane) {
			PotionHelper.addMix(PotionTypes.MUNDANE, ingredient, potion);
		}
		if (addAwkward) {
			PotionHelper.addMix(PotionTypes.AWKWARD, ingredient, potion);
		}
		if (addThick) {
			PotionHelper.addMix(PotionTypes.THICK, ingredient, potion);
		}
	}
}
