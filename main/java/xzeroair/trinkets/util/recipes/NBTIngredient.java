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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NBTIngredient extends Ingredient {
    private final ItemStack[] matchingStacks;
    private IntList matchingStacksPacked;

    protected NBTIngredient(final ItemStack... stacks) {
        super(0);

        this.matchingStacks = stacks;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return this.matchingStacks;
    }

    @Override
    public boolean apply(@Nullable final ItemStack stackToCheck) {
        if (stackToCheck == null) {
            return false;
        }
        for (final ItemStack stack : this.matchingStacks) {
            final int metadata = stack.getMetadata();
            if ((metadata == OreDictionary.WILDCARD_VALUE) || (metadata == stackToCheck.getMetadata())) {
                if (stack.getItem() == stackToCheck.getItem()) {
                    if (this.checkTrinket(stack, stackToCheck)) {
                        return true;
//                    } else if (checkElement(stack2, stackToCheck)) {
//                        return true;
                    } else {
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
                    }
                    if ((ItemStack.areItemStackTagsEqual(stackToCheck, stack))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkElement(ItemStack stack, ItemStack stackToCheck) {
//        boolean bool1 = stack.getItem() instanceof IElementProvider;
//        boolean bool2 = stackToCheck.getItem() instanceof IElementProvider;
//        if (bool1 && bool2) {
//            ItemElementalAttributes prop = Capabilities.getElementProperties(stack, new ItemElementalAttributes(stack), (prop1, emptyProp) -> prop1);
//            ItemElementalAttributes prop2 = Capabilities.getElementProperties(stackToCheck);
//            if (prop2 != null) {
//                if (stack.getTagCompound() == null) {
//                    stack.setTagCompound(prop.saveToNBT(prop.getTag()));
//                } else {
//                    prop.loadFromNBT(stack.getTagCompound());
//                    prop.saveToNBT(prop.getTag());
//                }
//                return prop.compareElementAttributes(prop2);
//            }
//        }
        return false;
    }

    public boolean checkTrinket(ItemStack stack, ItemStack stackToCheck) {
        boolean bool1 = stack.getItem() instanceof ITrinketInterface;
        boolean bool2 = stackToCheck.getItem() instanceof ITrinketInterface;
        if (bool1 && bool2) {
            boolean sameID = stack.getItem().getRegistryName().compareTo(stackToCheck.getItem().getRegistryName()) == 0;
            if (sameID) {
                ItemStack stackCopy = stack.copy();
                TrinketProperties prop = Capabilities.getTrinketProperties(stackCopy, new TrinketProperties(stackCopy), (prop1, emptyProp) -> prop1);
                TrinketProperties prop2 = Capabilities.getTrinketProperties(stackToCheck);
                if (prop2 != null) {
                    Element s = prop.getElementalAttributes().getPrimaryElement();
                    Element s2 = prop2.getElementalAttributes().getPrimaryElement();
                    boolean sameElement = s.equals(s2);
                    if (sameElement) {
                        int exp = prop.StoredExp();
                        int exp2 = prop2.StoredExp();
                        return exp == exp2;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        if (this.matchingStacksPacked == null) {
            this.matchingStacksPacked = new IntArrayList(this.matchingStacks.length);

            for (final ItemStack itemstack : this.matchingStacks) {
                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }

            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.matchingStacksPacked;
    }

    @Override
    protected void invalidate() {
        this.matchingStacksPacked = null;
    }

    public static Ingredient fromStacks(@Nonnull final ItemStack... stacks) {
        for (final ItemStack itemstack : stacks) {
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ITrinketInterface) {
                    TrinketProperties prop = Capabilities.getTrinketProperties(itemstack, new TrinketProperties(itemstack), (prop1, prop2) -> prop1);
                    prop.loadFromNBT(prop.getTag());
                    return new NBTIngredient(prop.getItemStack());
                } else {
                    return new NBTIngredient(stacks);
                }
            }
        }
        return EMPTY;
    }

    public static class Factory implements IIngredientFactory {

        @Override
        public Ingredient parse(final JsonContext context, final JsonObject json) {
            final ItemStack stack = CraftingHelper.getItemStack(json, context);
            ItemStack newStack = Capabilities.getTrinketProperties(stack, stack, (prop, norm) -> {
                prop.loadFromNBT(prop.getTag());
                return prop.getItemStack();
            });
            return NBTIngredient.fromStacks(newStack);
        }
    }
}
