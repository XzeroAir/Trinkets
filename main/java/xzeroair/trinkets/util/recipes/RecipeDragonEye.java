package xzeroair.trinkets.util.recipes;

import java.util.function.Predicate;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

public class RecipeDragonEye extends ShapedOreRecipe {
	public RecipeDragonEye(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
		super(group, result, primer);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		//		if (Trinkets.IceAndFire && (this.getRecipeOutput() != null) && (this.getRecipeOutput().getItem() instanceof TrinketDragonsEye)) {
		//			for (int i = 0; i < inv.getSizeInventory(); i++) {
		//				final ItemStack stack = inv.getStackInSlot(i);
		//				if (this.matchItem(stack, "iceandfire:dragon_skull") && stack.hasTagCompound()) {
		//					if ((stack.getTagCompound().getInteger("Stage") < TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage)) {
		//						return false;
		//					}
		//				}
		//			}
		//		}
		return super.matches(inv, world);
	}

	@Override
	public ItemStack getRecipeOutput() {
		final ItemStack output = super.getRecipeOutput().copy();
		boolean checkSkull = false;
		if (Trinkets.MOD_COMPAT.IceAndFire && (output.getItem() instanceof AccessoryBase)) {
			for (Ingredient i : this.getIngredients()) {
				if (checkSkull) {
					break;
				}
				for (ItemStack stack : i.getMatchingStacks()) {
					if (checkSkull) {
						break;
					}
					if (this.matchItem(stack, "iceandfire:dragon_skull")) {
						int meta = stack.getMetadata();
						if (meta == 1) {
							this.setTrinketProperties(output, 2, Elements.ICE);
						} else if (meta == 2) {
							this.setTrinketProperties(output, 3, Elements.LIGHTNING);
						} else {
							this.setTrinketProperties(output, 1, Elements.FIRE);
						}
						checkSkull = true;
					}
				}
			}
		}
		return output;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final ItemStack output = super.getCraftingResult(inv); // Get the default output
		try {
			if (output.hasTagCompound()) {
				Capabilities.getTrinketProperties(output, prop -> {
					prop.loadFromNBT(output.getTagCompound());
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Trinkets.MOD_COMPAT.IceAndFire && (output.getItem() instanceof AccessoryBase)) {
			try {
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack stack = inv.getStackInSlot(i);
					if (this.matchDragonSkullStage(stack)) {
						if (stack.getItemDamage() == 1) {
							if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
								this.setTrinketProperties(output, 2, Elements.ICE);
							}
						} else if (stack.getItemDamage() == 2) {
							if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
								this.setTrinketProperties(output, 3, Elements.LIGHTNING);
							}
						} else {
							this.setTrinketProperties(output, 1, Elements.FIRE);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output; // Return the modified output

	}

	@Override
	public String getGroup() {
		return group == null ? "" : group.toString();
	}

	public static class Factory implements IRecipeFactory {

		@Override
		public IRecipe parse(final JsonContext context, final JsonObject json) {
			final String group = JsonUtils.getString(json, "group", "");
			final CraftingHelper.ShapedPrimer primer = RecipeHelperUtil.parseShaped(context, json);
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

			return new RecipeDragonEye(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
		}
	}

	private boolean testItem(ItemStack stack, Predicate<ItemStack> predicate) {
		return (stack == null) || stack.isEmpty() || (predicate == null) ? false : predicate.test(stack);
	}

	private boolean matchItem(ItemStack stack, String regName) {
		return (regName == null) || regName.isEmpty() ? false : this.testItem(stack, s -> {
			Item item = s.getItem();
			if (item.getRegistryName().toString().equalsIgnoreCase(regName)) {
				return true;
			}
			return false;
		});
	}

	private boolean matchDragonSkullStage(ItemStack stack) {
		if (this.matchItem(stack, "iceandfire:dragon_skull")) {
			if (stack.hasTagCompound() && !stack.getTagCompound().isEmpty() && (stack.getTagCompound().getInteger("Stage") >= TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage)) {
				return true;
			}
		}
		return false;
	}

	//	private void applyActionToMatch(ItemStack stack, String regName, Consumer<ItemStack> consumer) {
	//		if ((consumer != null) && this.matchItem(stack, regName)) {
	//			consumer.accept(stack);
	//		}
	//	}

	private void setTrinketProperties(ItemStack stack, int variant, Element element) {
		Capabilities.getTrinketProperties(
				stack, prop -> {
					if (variant >= 0) {
						prop.setVariant(variant);
					}
					prop.getElementAttributes().setPrimaryElement(element);
				}
		);
	}
}
