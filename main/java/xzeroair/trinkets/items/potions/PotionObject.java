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

	private Potion potion;
	private PotionType base;
	private PotionType normal;
	private PotionType extended;
	private Ingredient ingredient;
	private int duration;
	private String name;
	private int color;
	private boolean isBase;

	public PotionObject(Potion potion, String name, int color, int duration, Ingredient ingredient) {
		this.potion = potion;
		this.name = name;
		this.color = color;
		this.duration = duration;
		this.ingredient = ingredient;
		normal = this.createPotionType(name, potion, duration);
		extended = this.createPotionTypeExtended(name, potion, duration);
		isBase = true;
	}

	public PotionObject(Potion potion, PotionType base, String name, int color, int duration, Ingredient ingredient) {
		this(potion, name, color, duration, ingredient);
		this.base = base;
		isBase = false;
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

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public int getDuration() {
		return duration;
	}

	public PotionObject registerPotion() {
		ForgeRegistries.POTIONS.register(potion);
		ModPotionTypes.TrinketPotions.put(name, potion);
		ForgeRegistries.POTION_TYPES.register(normal);
		ModPotionTypes.TrinketPotionTypes.put(name, normal);
		if (isBase) {
			this.addBaseMix(normal, ingredient);
		} else {
			PotionHelper.addMix(base, ingredient, normal);
		}
		if (duration > 0) {
			ForgeRegistries.POTION_TYPES.register(extended);
			ModPotionTypes.TrinketPotionTypes.put("extended_" + name, extended);
			PotionHelper.addMix(normal, Items.REDSTONE, extended);
		}
		return this;
	}

	public PotionObject registerPotionWithoutRecipe() {
		ForgeRegistries.POTIONS.register(potion);
		ModPotionTypes.TrinketPotions.put(name, potion);
		ForgeRegistries.POTION_TYPES.register(normal);
		ModPotionTypes.TrinketPotionTypes.put(name, normal);
		return this;
	}

	private PotionType createPotionType(String name, Potion pot, int duration) {
		final PotionType type = new PotionType(Reference.MODID + "." + name, new PotionEffect[] { new PotionEffect(pot, duration) }).setRegistryName(name);
		return type;
	}

	private PotionType createPotionTypeExtended(String name, Potion pot, int duration) {
		final PotionType type = new PotionType(Reference.MODID + "." + name, new PotionEffect[] { new PotionEffect(pot, duration * 3) }).setRegistryName("extended_" + name);
		return type;
	}

	private void addBaseMix(PotionType potion, Ingredient ingredient) {
		PotionHelper.addMix(PotionTypes.MUNDANE, ingredient, potion);
		PotionHelper.addMix(PotionTypes.AWKWARD, ingredient, potion);
		PotionHelper.addMix(PotionTypes.THICK, ingredient, potion);
	}
}
