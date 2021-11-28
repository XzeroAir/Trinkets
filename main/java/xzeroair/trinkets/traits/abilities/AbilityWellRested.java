package xzeroair.trinkets.traits.abilities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.trinkets.TrinketTeddyBear;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.IPotionAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ISleepAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.PotionHelper;
import xzeroair.trinkets.util.helpers.PotionHelper.PotionHolder;

public class AbilityWellRested extends AbilityBase implements IPotionAbility, ISleepAbility {

	@Override
	public void onWakeUp(EntityLivingBase entity, boolean wakeImmediately, boolean updatedWorld, boolean setSpawn) {
		if (entity.world.isRemote) {
			return;
		}
		if (TrinketsConfig.SERVER.Items.TEDDY_BEAR.sleep_bonus) {
			if (((entity.getHeldItemMainhand().getItem() instanceof TrinketTeddyBear) && entity.getHeldItemOffhand().isEmpty()) ||
					((entity.getHeldItemOffhand().getItem() instanceof TrinketTeddyBear) && entity.getHeldItemMainhand().isEmpty()) ||
					(entity.getHeldItemMainhand().isEmpty() && entity.getHeldItemOffhand().isEmpty() && TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketTeddyBear))) {
				final String[] config = TrinketsConfig.SERVER.Items.TEDDY_BEAR.buffs;
				int amount = TrinketsConfig.SERVER.Items.TEDDY_BEAR.randomBuff;
				if (amount > config.length) {
					amount = config.length;
				}
				if (amount > 0) {
					String potID = "";
					for (int i = 0; i < amount; i++) {
						final int potRand = Reference.random.nextInt(config.length);
						potID += config[potRand] + ",";
					}
					if (!potID.isEmpty()) {
						final String[] pots = potID.split(",");
						for (final String p : pots) {
							if (!p.isEmpty()) {
								final PotionHolder potion = PotionHelper.getPotionHolder(p);
								if (!entity.isPotionActive(potion.getPotion())) {
									entity.addPotionEffect(potion.getPotionEffect());
								} else {
									entity.getActivePotionEffect(potion.getPotion()).combine(potion.getPotionEffect());
								}
							}
						}
					}
				} else {
					for (final String potID : config) {
						final PotionHolder potion = PotionHelper.getPotionHolder(potID);
						if (potion.getPotion() != null) {
							entity.addPotionEffect(potion.getPotionEffect());
						}
					}
				}
			}
		}
	}

	@Override
	public boolean potionApplied(EntityLivingBase entity, PotionEffect effect, boolean cancel) {
		//		if (TrinketsConfig.SERVER.Items.TEDDY_BEAR.compat.lycanites.preventFear) {
		final String e = effect.getPotion().getRegistryName().toString();
		for (final String immunity : TrinketsConfig.SERVER.Items.TEDDY_BEAR.immunities) {
			final Potion fear = Potion.getPotionFromResourceLocation(immunity);
			if ((fear != null) && e.contentEquals(fear.getRegistryName().toString())) {
				return true;
			}
		}
		//			final String e = effect.getPotion().getRegistryName().toString();
		//			final Potion fear = LycanitesCompat.getPotionEffectByName("fear");
		//			if ((fear != null) && e.contentEquals(fear.getRegistryName().toString())) {
		//				return true;
		//			}
		//			final Potion insomnia = LycanitesCompat.getPotionEffectByName("insomnia");
		//			if ((insomnia != null) && e.contentEquals(insomnia.getRegistryName().toString())) {
		//				return true;
		//			}
		//		}
		return cancel;
	}

}
