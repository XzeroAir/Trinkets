package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;

public class AbilityFireImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility {

	protected int amplifier = 0;

	public AbilityFireImmunity() {
		super(Abilities.fireImmunity);
	}

	public int getAmplifier() {
		return amplifier;
	}

	public AbilityFireImmunity setAmplifier(int amp) {
		if (amplifier != amp) {
			amplifier = amp;
		}
		return this;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (entity.isBurning()) {
			entity.extinguish();
		}
		int amp = this.getAmplifier();
		if (Trinkets.FireResistanceTiers) {
			AbilityHolder holder = this.getAbilityHolder();
			if (holder != null) {
				if (holder.getSourceID().equalsIgnoreCase("xat:" + ModItems.DragonsEye)) {
					amp = TrinketsConfig.SERVER.Items.DRAGON_EYE.compat.FRTiers.amplifier;
				} else if (holder.getSourceID().equalsIgnoreCase("xat:dragon")) {
					amp = TrinketsConfig.SERVER.races.dragon.compat.FRTiers.amplifier;
				} else {

				}
			}
		}
		final PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600, amp, false, false);
		entity.addPotionEffect(fireResist);
		if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
			if (entity.world.isRemote) {
				entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).setPotionDurationMax(true);
			}
		}
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		if (entity.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
			entity.removePotionEffect(MobEffects.FIRE_RESISTANCE);
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

	@Override
	public float damaged(EntityLivingBase attacked, DamageSource source, float dmg) {
		if (Trinkets.FireResistanceTiers) {
			return dmg;
		}
		if (source.isFireDamage()) {
			return 0;
		}
		return dmg;
	}

}
