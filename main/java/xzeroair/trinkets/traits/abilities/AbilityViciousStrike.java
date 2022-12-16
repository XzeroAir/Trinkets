package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.statushandler.StatusHandler;
import xzeroair.trinkets.capabilities.statushandler.TrinketStatusEffect;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.traits.abilities.interfaces.IAttackAbility;
import xzeroair.trinkets.traits.statuseffects.StatusEffectsEnum;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.lycanitesmobs.LycanitesCompat;
import xzeroair.trinkets.util.config.trinkets.ConfigFaelisClaw;

public class AbilityViciousStrike extends Ability implements IAttackAbility {

	public static final ConfigFaelisClaw serverConfig = TrinketsConfig.SERVER.Items.FAELIS_CLAW;

	public AbilityViciousStrike() {
		super(Abilities.viciousStrike);
	}

	@Override
	public float hurtEntity(EntityLivingBase target, DamageSource source, float dmg) {
		if (this.isIndirectDamage(source))
			return dmg;
		boolean faelis = false;
		int chance = serverConfig.chance > 0 ? random.nextInt(serverConfig.chance) : 0;
		if (source.getTrueSource() instanceof EntityLivingBase) {
			boolean isFaelis = Capabilities.getEntityProperties(source.getTrueSource(), faelis, (prop, rtn) -> prop.getCurrentRace().equals(EntityRaces.faelis));
			if (isFaelis) {
				chance /= 2;
				faelis = true;
			}
		}
		if (chance == 0) {
			final int duration = (int) (faelis ? serverConfig.bleedDuration : serverConfig.bleedDuration * 0.33);
			final int amplifier = 1;//faelis ? 2 : 1;
			if (Loader.isModLoaded("lycanitesmobs") && TrinketsConfig.compat.lycanites && serverConfig.compat.lycanites.useLycaniteBleed) {
				LycanitesCompat.applyEffect(target, "bleed", duration, 0);
			} else if (Loader.isModLoaded("defiledlands") && TrinketsConfig.compat.defiledlands && serverConfig.compat.defiledlands.useDefiledLandsBleed) {
				try {
					final Potion effect = Potion.getPotionFromResourceLocation("defiledlands:" + "bleeding");
					if ((effect != null)) {
						final boolean hasPot = target.isPotionActive(effect);
						PotionEffect bleed;
						if (!hasPot) {
							bleed = new PotionEffect(effect, duration, 0, false, false);
							target.addPotionEffect(bleed);
						} else {
							final PotionEffect bleeding = target.getActivePotionEffect(effect);
							if (bleeding.getAmplifier() < 3) {
								bleed = new PotionEffect(effect, duration, bleeding.getAmplifier() + 1, false, false);
								target.addPotionEffect(bleed);
							}
						}
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} else {
				final StatusHandler status = Capabilities.getStatusHandler(target);
				if (status != null) {
					if (!status.getActiveEffects().containsKey(StatusEffectsEnum.bleed.getName())) {
						final TrinketStatusEffect effect = new TrinketStatusEffect(StatusEffectsEnum.bleed, duration, amplifier, source.getTrueSource());
						status.apply(effect);
					}
				}
			}
		}
		return dmg;
	}
}
