package xzeroair.trinkets.traits.abilities.compat.survival;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.compat.SurvivalCompat;

public class AbilityHeatImmunity extends AbilityBase implements ITickableAbility, IPotionAbility {

	@Override
	public void tickAbility(EntityLivingBase entity) {
		SurvivalCompat.immuneToHeat(entity);
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		final List<Potion> hyperthermia = SurvivalCompat.getHyperthermiaEffects();
		if (hyperthermia.contains(effect.getPotion())) {
			return true;
		}
		return cancel;
	}

}