package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class IngredientNever extends Ingredient {
	public static IngredientNever INSTANCE = new IngredientNever();

	private IngredientNever() {
		super(0);
	}

	@Override
	public boolean apply(@Nullable final ItemStack p_apply_1_) {
		return false;
	}

	public static class Factory implements IIngredientFactory {

		@Nonnull
		@Override
		public Ingredient parse(final JsonContext context, final JsonObject json) {
			return IngredientNever.INSTANCE;
		}
	}
}