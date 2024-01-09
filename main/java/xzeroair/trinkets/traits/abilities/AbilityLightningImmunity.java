package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import xzeroair.trinkets.api.TrinketHelper.SlotInformation.ItemHandlerType;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.traits.AbilityHandler.AbilityHolder;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ILightningStrikeAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.iceandfire.IceAndFireCompat;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;

public class AbilityLightningImmunity extends Ability implements ITickableAbility, IPotionAbility, IAttackAbility, ILightningStrikeAbility {

	public static ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;

	Potion lightning_resist;

	public AbilityLightningImmunity() {
		super(Abilities.lightningImmunity);
		lightning_resist = ModPotionTypes.TrinketPotions.get(ModPotionTypes.lightningResist);
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		// Immune to slowness?
		final AbilityHolder holder = this.getAbilityHolder();
		if (holder.getInfo().getHandlerType() == ItemHandlerType.POTION) {
			return;
		}
		if (lightning_resist != null) {
			if (!entity.isPotionActive(lightning_resist)) {
				entity.addPotionEffect(new PotionEffect(lightning_resist, 60, 0, false, false));
			}
			if (entity.world.isRemote) {
				if (entity.isPotionActive(lightning_resist)) {
					entity.getActivePotionEffect(lightning_resist).setPotionDurationMax(true);
				}
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		if (!serverConfig.compat.iaf.PARALYSIS_IMMUNITY) {
			return cancel;
		}
		final String e = effect.getPotion().getRegistryName().toString();
		final Potion lycanitesParalysis = LycanitesCompat.getPotionEffectByName("paralysis");
		if ((lycanitesParalysis != null) && e.contentEquals(lycanitesParalysis.getRegistryName().toString())) {
			return true;
		}
		final Potion iceAndFireParalysis = IceAndFireCompat.getPotionEffectByName("paralysis");
		if ((iceAndFireParalysis != null) && e.contentEquals(iceAndFireParalysis.getRegistryName().toString())) {
			return true;
		}
		return cancel;
	}

	@Override
	public boolean attacked(EntityLivingBase attacked, DamageSource source, float dmg, boolean cancel) {
		if (source.damageType.contentEquals(DamageSource.LIGHTNING_BOLT.damageType)
				|| source.damageType.contentEquals("dragon_lightning")
				|| source.damageType.contentEquals("locks.shock")) {
			return true;
		}
		return cancel;
	}

	@Override
	public void onAbilityRemoved(EntityLivingBase entity) {
		if (lightning_resist != null) {
			entity.removePotionEffect(lightning_resist);
		}
	}

	@Override
	public boolean onStruckByLightning(EntityLivingBase entity, boolean cancel) {
		entity.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 1));
		return true;
	}
}
