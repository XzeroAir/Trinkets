package xzeroair.trinkets.util.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IDescriptionInterface {

//    boolean hasDiscription(ItemStack stack);

    String getDisplayName();

    @SideOnly(Side.CLIENT)
    default void getDescription(List<String> tooltips, int rendMod, int rendID) {

    }

    //	@SideOnly(Side.CLIENT)
    //	default String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
    //		final TranslationHelper helper = TranslationHelper.INSTANCE;
    //		return helper.formatAddVariables(translation);
    //	}

}
