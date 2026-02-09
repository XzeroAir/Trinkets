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
import xzeroair.trinkets.api.ITrinketInterface;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class RecipeDragonEye extends ShapedOreRecipe {
    public RecipeDragonEye(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
        super(group, result, primer);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return super.matches(inv, world);
    }

    @Override
    public ItemStack getRecipeOutput() {
        final ItemStack output = super.getRecipeOutput().copy();
        return output;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        final ItemStack output = super.getCraftingResult(inv); // Get the default output
        if (!output.isEmpty() && output.getItem() instanceof ITrinketInterface) {
            TrinketProperties prop = Capabilities.getTrinketProperties(output, new TrinketProperties(output), (prop1, prop2) -> {
                return prop1;
            });
            if (output.getTagCompound() == null) {
                output.setTagCompound(prop.saveToNBT(prop.getTag()));
            } else {
                prop.loadFromNBT(output.getTagCompound());
                prop.saveToNBT(prop.getTag());
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
        Capabilities.getTrinketProperties(stack, prop -> {
            if (variant >= 0) {
                prop.setVariant(variant);
            }
            prop.getElementAttributes().setPrimaryElement(element);
        });
    }
}
