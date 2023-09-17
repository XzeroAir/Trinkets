package xzeroair.trinkets.util.recipes;

import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
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
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

public class RecipeDragonEye extends ShapedOreRecipe {
	public RecipeDragonEye(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
		super(group, result, primer);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		if (Trinkets.IceAndFire) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				final ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty() && stack.hasTagCompound()) {
					if ((stack.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull"))) {
						if ((stack.getTagCompound().getInteger("Stage") < TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage)) {
							return false;
						}
					}
				}
			}
		}
		//		System.out.println(super.matches(inv, world));
		return super.matches(inv, world);
	}

	@Override
	public ItemStack getRecipeOutput() {
		final ItemStack output = super.getRecipeOutput().copy();
		for (Ingredient i : this.getIngredients()) {
			for (ItemStack stack : i.getMatchingStacks()) {
				if (!stack.isEmpty() && (stack.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull"))) {
					int meta = stack.getMetadata();
					if (meta == 1) {
						setTrinketProperties(output, 1, Elements.ICE);
					} else if (meta == 2) {
						setTrinketProperties(output, 2, Elements.LIGHTNING);
					} else {
						setTrinketProperties(output, 0, Elements.FIRE);
					}
					break;
				}
			}
		}
		return output;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final ItemStack output = super.getCraftingResult(inv); // Get the default output
		if (Trinkets.IceAndFire) {
			try {
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack stack = inv.getStackInSlot(i);
					if (!stack.isEmpty()) {
						if ((stack.getItem().getRegistryName().toString().equalsIgnoreCase("iceandfire:dragon_skull"))) {
							if (!stack.getTagCompound().isEmpty() && (stack.getTagCompound().getInteger("Stage") >= TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage)) {
								NBTTagCompound tagCompound = output.getTagCompound();
								if (tagCompound == null) {
									tagCompound = new NBTTagCompound();
									output.setTagCompound(tagCompound);
								}
								if (stack.getItemDamage() == 1) {
									if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
										setTrinketProperties(output, 1, Elements.ICE);
									}
								} else if (stack.getItemDamage() == 2) {
									if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.LIGHTNING_VARIANT) {
										setTrinketProperties(output, 2, Elements.LIGHTNING);
									}
								} else {
									setTrinketProperties(output, 0, Elements.FIRE);
								}
							}
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

	private void setTrinketProperties(ItemStack stack, int variant, Element element) {
		Capabilities.getTrinketProperties(
				stack, prop -> {
					prop.setVariant(variant);
					prop.getElementAttributes().setPrimaryElement(element);
				}
		);
	}
}
