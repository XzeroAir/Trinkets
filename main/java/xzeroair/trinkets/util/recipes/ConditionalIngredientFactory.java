<<<<<<< Updated upstream
package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConditionalIngredientFactory implements IIngredientFactory {
	@Nonnull
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		if (CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), context)) {
			return CraftingHelper.getIngredient(json.get("ingredient"), context);
		}
		return IngredientNever.INSTANCE;
	}
=======
package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class ConditionalIngredientFactory implements IIngredientFactory {
	@Nonnull
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		if (CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), context)) {
			return CraftingHelper.getIngredient(json.get("ingredient"), context);
		}
		return IngredientNever.INSTANCE;
	}
>>>>>>> Stashed changes
}