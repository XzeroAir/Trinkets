package xzeroair.trinkets.util.compat.iceandfire.crafting.conditions;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.recipes.IngredientNever;
import xzeroair.trinkets.util.recipes.NBTIngredient;

public class DragonSkullIngredientFactory implements IIngredientFactory {
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		final String itemName = context.appendModId(JsonUtils.getString(json, "item", "iceandfire:dragon_skull"));
		final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
		// handle Fire Dragon Skull
		ItemStack stack = new ItemStack(item);
		stack.setItemDamage(0);
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		tagCompound.setInteger("Stage", TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage);

		if (TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.ICE_VARIANT) {
			// handle Ice Dragon Skull
			ItemStack stack2 = new ItemStack(item);
			stack2.setItemDamage(1);
			NBTTagCompound tagCompound2 = stack2.getTagCompound();
			if (tagCompound2 == null) {
				tagCompound2 = new NBTTagCompound();
				stack2.setTagCompound(tagCompound2);
			}
			tagCompound2.setInteger("Stage", TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage);
			return NBTIngredient.fromStacks(stack, stack2);
		}
		return Trinkets.IceAndFire ? NBTIngredient.fromStacks(stack) : IngredientNever.EMPTY;
	}
}
