package xzeroair.trinkets.util.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEICompat implements IModPlugin {

	@SuppressWarnings("deprecation")
	@Override
	public void register(IModRegistry registry) {
		//		for (final Item item : ModItems.crafting.ITEMS) {
		//			final boolean addDescription = ((IDescriptionInterface) item).hasDiscription(item.getDefaultInstance());
		//			TextComponentTranslation FormattedKey = new TextComponentTranslation(item.getTranslationKey() + ".info");
		//			final String key = TranslationHelper.formatLangKeys(item.getDefaultInstance(), FormattedKey);
		//			if (addDescription == true) {
		//				registry.addDescription(item.getDefaultInstance(), key);
		//			}
		//		}
		//		for (final Item item : ModItems.foods.ITEMS) {
		//			final boolean addDescription = ((IDescriptionInterface) item).hasDiscription(item.getDefaultInstance());
		//			TextComponentTranslation FormattedKey = new TextComponentTranslation(item.getTranslationKey() + ".info");
		//			final String key = TranslationHelper.formatLangKeys(item.getDefaultInstance(), FormattedKey);
		//			if (addDescription == true) {
		//				registry.addDescription(item.getDefaultInstance(), key);
		//			}
		//		}
		//		for (final Item item : ModItems.baubles.ITEMS) {
		//			final boolean addDescription = ((IDescriptionInterface) item).hasDiscription(item.getDefaultInstance());
		//			TextComponentTranslation FormattedKey = new TextComponentTranslation(item.getTranslationKey() + ".info");
		//			final String key = TranslationHelper.formatLangKeys(item.getDefaultInstance(), FormattedKey);
		//			if (addDescription == true) {
		//				registry.addDescription(item.getDefaultInstance(), key);
		//			}
		//		}
		//		for (final Item item : ModItems.trinkets.ITEMS) {
		//			final boolean addDescription = ((IDescriptionInterface) item).hasDiscription(item.getDefaultInstance());
		//			TextComponentTranslation FormattedKey = new TextComponentTranslation(item.getTranslationKey() + ".info");
		//			final String key = TranslationHelper.formatLangKeys(item.getDefaultInstance(), FormattedKey);
		//			if (addDescription == true) {
		//				registry.addDescription(item.getDefaultInstance(), key);
		//			}
		//		}
	}
}
