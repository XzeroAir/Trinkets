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
					Capabilities.getTrinketProperties(
							output, prop -> prop.getElementAttributes().setPrimaryElement(meta == 1 ? Elements.ICE : Elements.FIRE)
							//							output, prop -> prop.getElementAttributes().setPrimaryElement(meta == 1 ? Elements.ICE : Elements.NEUTRAL)
					);
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
								if (stack.getItemDamage() == 0) {
									//									Capabilities.getTrinketProperties(
									//											output, prop -> {
									//												prop.getElementAttributes().setPrimaryElement(Elements.FIRE);
									//												prop.setVariant(Elements.FIRE.getID());
									//											}
									//									);
								} else {
									if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
										Capabilities.getTrinketProperties(
												output, prop -> {
													prop.setVariant(Elements.ICE.getID());
													prop.getElementAttributes().setPrimaryElement(Elements.ICE);
												}
										);
									}
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
}
