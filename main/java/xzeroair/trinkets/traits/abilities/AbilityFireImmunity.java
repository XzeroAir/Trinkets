package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;

public class AbilityFireImmunity extends AbilityBase implements ITickableAbility, IPotionAbility, IAttackAbility {

	@Override
	public void tickAbility(EntityLivingBase entity) {
		final PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE, 150, 0, false, false);
		entity.addPotionEffect(fireResist);
		if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
			if (entity.world.isRemote) {
				entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).setPotionDurationMax(true);
			}
		}
		if (entity.isBurning()) {
			entity.extinguish();
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		final String e = effect.getPotion().getRegistryName().toString();
		final Potion smouldering = LycanitesCompat.getPotionEffectByName("smouldering");
		if ((smouldering != null) && e.contentEquals(smouldering.getRegistryName().toString())) {
			return true;
		}
		return cancel;
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource damage, float dmg, boolean cancel) {
		if (damage.isFireDamage()) {
			return true;
		}
		return cancel;
	}

}
