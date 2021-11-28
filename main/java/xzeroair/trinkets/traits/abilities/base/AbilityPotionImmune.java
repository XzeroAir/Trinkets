package xzeroair.trinkets.traits.abilities.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;

public class AbilityPotionImmune extends AbilityBase implements IPotionAbility {

	private String[] potions;

	public AbilityPotionImmune(String... strings) {
		potions = strings;
	}

	public AbilityPotionImmune setPotions(String... potions) {
		this.potions = potions;
		return this;
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		String e = effect.getPotion().getRegistryName().toString();
		for (String potion : potions) {
			if (e.contentEquals(potion)) {
				return true;
			}
		}
		return cancel;
	}

}
