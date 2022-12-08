package xzeroair.trinkets.util.interfaces;

import net.minecraft.item.ItemStack;

public interface IDescriptionInterface {

	boolean hasDiscription(ItemStack stack);

	//	@SideOnly(Side.CLIENT)
	//	default String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
	//		final TranslationHelper helper = TranslationHelper.INSTANCE;
	//		return helper.formatAddVariables(translation);
	//	}

}
