package xzeroair.trinkets.items.base;

import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.interfaces.IsModelLoaded;

public class ItemBase extends Item implements IsModelLoaded {
	public ItemBase(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setCreativeTab(Trinkets.trinketstab);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return super.getItemStackDisplayName(stack);
	}

	@SideOnly(Side.CLIENT)
	protected String customItemInformation(ItemStack stack, World world, ITooltipFlag flagIn, int index, String translation) {
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		return helper.formatAddVariables(translation);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltips, ITooltipFlag flagIn) {
		super.addInformation(stack, world, tooltips, flagIn);
		final TranslationHelper helper = TranslationHelper.INSTANCE;
		for (int i = 1; i < 10; i++) {
			final int index = i;
			final String string = helper.getLangTranslation(stack.getTranslationKey() + ".tooltip" + i, lang -> this.customItemInformation(stack, world, flagIn, index, lang));
			if (!helper.isStringEmpty(string)) {
				tooltips.add(
						string
				);
			}
		}
	}

	@Override
	public void registerModels() {
		Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
