package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;

public class AbilityIceImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility {

	Potion ice_resist;

	public AbilityIceImmunity() {
		super(Abilities.iceImmunity);
		ice_resist = ModPotionTypes.TrinketPotions.get(ModPotionTypes.iceResist);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		// Immune to slowness?
		final AbilityHolder holder = this.getAbilityHolder();
		if (holder.getInfo().getHandlerType() == ItemHandlerType.POTION) {
			return;
		}
		if (ice_resist != null) {
			if (!entity.isPotionActive(ice_resist)) {
				entity.addPotionEffect(new PotionEffect(ice_resist, 60, 0, false, false));
			}
			if (entity.world.isRemote) {
				if (entity.isPotionActive(ice_resist)) {
					entity.getActivePotionEffect(ice_resist).setPotionDurationMax(true);
				}
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
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

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		if (ice_resist != null) {
			entity.removePotionEffect(ice_resist);
		}
	}
}
