<<<<<<< Updated upstream
package xzeroair.trinkets.traits.abilities.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;

public class AbilityPotionImmune extends Ability implements IPotionAbility {

	public AbilityPotionImmune() {
	}

	private String[] potions;

	public AbilityPotionImmune(String... strings) {
		potions = strings;
	}

	public AbilityPotionImmune setPotions(String... potions) {
		this.potions = potions;
		return this;
	}

	@Override
	public void onAbilityAdded(EntityLivingBase entity) {
		for (final String potion : potions) {
			final Potion pot = Potion.getPotionFromResourceLocation(potion);
			if (pot != null) {
				entity.removePotionEffect(pot);
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		final String e = effect.getPotion().getRegistryName().toString();
		for (final String potion : potions) {
			if (e.contentEquals(potion)) {
				return true;
			}
		}
		return cancel;
	}

}
=======
package xzeroair.trinkets.traits.abilities.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;

public class AbilityPotionImmune extends Ability implements IPotionAbility {

	public AbilityPotionImmune() {
	}

	private String[] potions;

	public AbilityPotionImmune(String... strings) {
		potions = strings;
	}

	public AbilityPotionImmune setPotions(String... potions) {
		this.potions = potions;
		return this;
	}

	@Override
	public void onAbilityAdded(EntityLivingBase entity) {
		for (final String potion : potions) {
			final Potion pot = Potion.getPotionFromResourceLocation(potion);
			if (pot != null) {
				entity.removePotionEffect(pot);
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		final String e = effect.getPotion().getRegistryName().toString();
		for (final String potion : potions) {
			if (e.contentEquals(potion)) {
				return true;
			}
		}
		return cancel;
	}

}
>>>>>>> Stashed changes
