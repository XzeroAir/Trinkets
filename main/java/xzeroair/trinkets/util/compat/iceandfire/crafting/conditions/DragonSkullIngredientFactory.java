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
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.recipes.NBTIngredient;

public class DragonSkullIngredientFactory implements IIngredientFactory {
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		final String itemName = context.appendModId(JsonUtils.getString(json, "item", "iceandfire:dragon_skull"));
		final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
		//
		ItemStack stack = new ItemStack(item);
		stack.setItemDamage(0);
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null) {
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		tagCompound.setInteger("Stage", TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.iaf.stage);
		return NBTIngredient.fromStacks(stack);
	}
}
