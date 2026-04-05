package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class TrinketShapedRecipe extends ShapedOreRecipe {
    public TrinketShapedRecipe(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
        super(group, result, primer);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return super.matches(inv, world);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return super.getRecipeOutput().copy();
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        // Get the default output
        return super.getCraftingResult(inv); // Return the modified output
    }

    @Override
    public String getGroup() {
        return this.group == null ? "" : this.group.toString();
    }

    public static class Factory implements IRecipeFactory {

        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json) {
            final String group = JsonUtils.getString(json, "group", "");
            final CraftingHelper.ShapedPrimer primer = RecipeHelperUtil.parseShaped(context, json);
            final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            TrinketProperties prop = Capabilities.getTrinketProperties(result, new TrinketProperties(result), (prop1, prop2) -> prop1);
            prop.loadFromNBT(prop.getTag());
            return new TrinketShapedRecipe(group.isEmpty() ? null : new ResourceLocation(group), prop.getItemStack(), primer);
        }
    }

    private boolean testItem(ItemStack stack, Predicate<ItemStack> predicate) {
        return (stack != null) && !stack.isEmpty() && (predicate != null) && predicate.test(stack);
    }

    private boolean matchItem(ItemStack stack, String regName) {
        return (regName != null) && !regName.isEmpty() && this.testItem(stack, s -> {
            Item item = s.getItem();
            return item.getRegistryName().toString().equalsIgnoreCase(regName);
        });
    }

}
