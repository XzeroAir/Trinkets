package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IJumpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;

public class AbilityWeightless extends AbilityBase implements ITickableAbility, IPotionAbility, IJumpAbility {

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (!entity.onGround) {
			entity.motionY = 0;
			if ((!(entity.isSneaking())) && entity.isSwingInProgress) {
				entity.motionY += 0.1;
			}
			if (entity.isSneaking() && entity.isSwingInProgress) {
				entity.motionY -= 0.1;
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		String e = effect.getPotion().getRegistryName().toString();
		Potion weight = LycanitesCompat.getPotionEffectByName("weight");
		if ((weight != null) && e.contentEquals(weight.getRegistryName().toString())) {
			return true;
		}
		return cancel;
	}

	@Override
	public boolean fall(EntityLivingBase entity, float distance, float damage, boolean cancel) {
		return true;
	}

}
