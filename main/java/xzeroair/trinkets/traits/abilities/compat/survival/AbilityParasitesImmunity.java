package xzeroair.trinkets.traits.abilities.compat.survival;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.compat.SurvivalCompat;

public class AbilityParasitesImmunity extends AbilityBase implements ITickableAbility, IPotionAbility {

	@Override
	public void tickAbility(EntityLivingBase entity) {
		SurvivalCompat.clearParasites(entity);
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		final Potion parasites = SurvivalCompat.getSDParasitesPotionEffect();
		if ((parasites != null) && effect.getPotion().equals(parasites)) {
			return true;
		}
		return cancel;
	}

}