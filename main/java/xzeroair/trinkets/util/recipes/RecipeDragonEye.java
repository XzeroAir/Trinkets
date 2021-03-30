package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xzeroair.trinkets.util.TrinketsConfig;

public class RecipeDragonEye extends ShapedOreRecipe {
	public RecipeDragonEye(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
		super(group, result, primer);
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> recipe = super.getIngredients();
		return recipe;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		boolean foundSkull = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if ((stack.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull")) && !foundSkull) {
					if (stack.getItemDamage() == 0) {
						if (!stack.getTagCompound().isEmpty() && (stack.getTagCompound().getInteger("Stage") >= TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage)) {
							foundSkull = true;
							break;
						}
					}
				}
			}
		}

		return super.matches(inv, world) && foundSkull;
	}

	@Override
	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		return super.checkMatch(inv, startX, startY, mirror);
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final ItemStack output = super.getCraftingResult(inv); // Get the default output
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
}
