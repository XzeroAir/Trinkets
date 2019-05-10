package xzeroair.trinkets.util.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.Item;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.util.interfaces.IDescriptionInterface;

@JEIPlugin
public class JEICompat implements IModPlugin {

	@Override
	public void register(IModRegistry registry)
	{
		for(final Item item:ModItems.ITEMS) {
			final boolean addDescription = ((IDescriptionInterface)item).hasDiscription(item.getDefaultInstance());
			final String key = item.getTranslationKey() + ".info";
			if(addDescription == true) {
				registry.addDescription(item.getDefaultInstance(), key);
			}
		}
	}
}
