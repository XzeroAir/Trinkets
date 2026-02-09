package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.api.ITrinketInterface;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.traits.elements.Element;

import javax.annotation.Nullable;

public class NBTIngredient extends Ingredient {
    private final ItemStack[] matchingStacks;
    private IntList matchingStacksPacked;

    protected NBTIngredient(final ItemStack... stacks) {
        super(0);

        matchingStacks = stacks;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return matchingStacks;
    }

    @Override
    public boolean apply(@Nullable final ItemStack stackToCheck) {
        if (stackToCheck == null) {
            return false;
        }
        for (final ItemStack stack : matchingStacks) {
            final int metadata = stack.getMetadata();
            if ((metadata == OreDictionary.WILDCARD_VALUE) || (metadata == stackToCheck.getMetadata())) {
                if (stack.getItem() == stackToCheck.getItem()) {
                    boolean bool1 = stack.getItem() instanceof ITrinketInterface;
                    boolean bool2 = stackToCheck.getItem() instanceof ITrinketInterface;
                    if (bool1 && bool2) {
                        boolean sameID = stack.getItem().getRegistryName().compareTo(stackToCheck.getItem().getRegistryName()) == 0;
                        if (sameID) {
                            ItemStack stack2 = new ItemStack(stack.getItem());
                            TrinketProperties prop = Capabilities.getTrinketProperties(stack2, new TrinketProperties(stack2), (prop1, emptyProp) -> {
                                return prop1;
                            });
                            TrinketProperties prop2 = Capabilities.getTrinketProperties(stackToCheck);
                            if (prop2 != null) {
                                if (stack.getTagCompound() == null) {
                                    stack2.setTagCompound(prop.saveToNBT(prop.getTag()));
                                } else {
                                    prop.loadFromNBT(stack.getTagCompound());
                                    prop.saveToNBT(prop.getTag());
                                }
                                Element s = prop.getElementAttributes().getPrimaryElement();
                                Element s2 = prop2.getElementAttributes().getPrimaryElement();
                                boolean sameElement = s.equals(s2);
                                if (sameElement) {
                                    Integer exp = prop.StoredExp();
                                    Integer exp2 = prop2.StoredExp();
                                    return sameID && sameElement && exp == exp2;
                                }
                            }
                        }
                    }
                    if (Trinkets.MOD_COMPAT.IceAndFire) {
                        boolean isSkull = (stack.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull")) && (stackToCheck.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull"));
                        if (isSkull) {
                            int skull1 = stack.hasTagCompound() ? stack.getTagCompound().getInteger("Stage") : -1;
                            int skull2 = stackToCheck.hasTagCompound() ? stackToCheck.getTagCompound().getInteger("Stage") : -1;
                            boolean allowCraft = ((skull1 != -1) && (skull2 != -1)) && (skull1 == skull2);
                            if (isSkull && allowCraft) {
                                return true;
                            }
                        }
                    }
                    if ((ItemStack.areItemStackTagsEqual(stackToCheck, stack))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        if (matchingStacksPacked == null) {
            matchingStacksPacked = new IntArrayList(matchingStacks.length);

            for (final ItemStack itemstack : matchingStacks) {
                matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }

            matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return matchingStacksPacked;
    }

    @Override
    protected void invalidate() {
        matchingStacksPacked = null;
    }

    public static Ingredient fromStacks(final ItemStack... stacks) {
        if (stacks.length > 0) {
            for (final ItemStack itemstack : stacks) {
                if (!itemstack.isEmpty()) {
                    return new NBTIngredient(stacks);
                }
            }
        }

        return EMPTY;
    }

    @SuppressWarnings("unused")
    public static class Factory implements IIngredientFactory {

        @Override
        public Ingredient parse(final JsonContext context, final JsonObject json) {
            final ItemStack stack = CraftingHelper.getItemStack(json, context);
            if (stack.getItem() instanceof ITrinketInterface) {
                ItemStack copy = stack.copy();
                TrinketProperties prop = Capabilities.getTrinketProperties(copy, new TrinketProperties(copy), (prop1, prop2) -> {
                    return prop1;
                });
                if (copy.getTagCompound() == null) {
//                    copy.setTagCompound(prop.saveToNBT(new NBTTagCompound()));
                } else {
                    prop.loadFromNBT(copy.getTagCompound());
//                    prop.saveToNBT(copy.getTagCompound());
                }
                return NBTIngredient.fromStacks(copy);
            }
            return NBTIngredient.fromStacks(stack);
        }
    }
}
