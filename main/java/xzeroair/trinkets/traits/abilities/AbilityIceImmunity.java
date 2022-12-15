package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;

public class AbilityIceImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility {

	public AbilityIceImmunity() {
		super(Abilities.iceImmunity);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		// Immune to slowness?
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		//		final String e = effect.getPotion().getRegistryName().toString();
		//		final Potion smouldering = LycanitesCompat.getPotionEffectByName("smouldering");
		//		if ((smouldering != null) && e.contentEquals(smouldering.getRegistryName().toString())) {
		//			return true;
		//		}
		return cancel;
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		if (source.damageType.contentEquals("ooze") ||
				source.damageType.contentEquals("dragon_ice") ||
				source.damageType.contentEquals("cold_fire")) {
			return true;
		}
		//icefireball
		return cancel;
	}
}
