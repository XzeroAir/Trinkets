package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;

public class PotionHelper {

	public static class PotionHolder {
		Potion potion;
		PotionEffect effect;
		int duration;
		int amplifier;

		public PotionHolder(String configPot) {
			this.getPotionEffect(configPot);
		}

		public Potion getPotion() {
			return potion;
		}

		public PotionEffect getPotionEffect() {
			return effect;
		}

		public int getDuration() {
			return duration;
		}

		public int getAmplifier() {
			return amplifier;
		}

		private void getPotionEffect(String potID) {
			if (!potID.isEmpty()) {
				String pot = "";
				String[] configString = potID.split(":");
				String modID = configString[0];
				pot = modID;
				if (configString.length > 1) {
					String effectID = configString[1];
					pot = modID + ":" + effectID;
				} else {
					pot = "minecraft:" + pot;
				}
				Potion potion = Potion.getPotionFromResourceLocation(pot);
				if (potion != null) {
					int duration = 300;
					int amplifier = 0;
					if (configString.length > 2) {
						try {
							String durationString = configString[2];
							duration = Integer.parseInt(durationString);
						} catch (Exception e) {

						}
						if (configString.length > 3) {
							try {
								String amplifierString = configString[3];
								amplifier = Integer.parseInt(amplifierString);
							} catch (Exception e) {

							}
						}
					}
					this.potion = potion;
					this.duration = duration;
					this.amplifier = amplifier;
					effect = new PotionEffect(potion, duration, amplifier, false, false);
				}
			}
		}
	}

	public static PotionHolder getPotionHolder(String potConfig) {
		return new PotionHolder(potConfig);
	}

	public static boolean isModPotionActive(EntityLivingBase entity, String modID, String potionEffect) {
		Potion potion = getModPotion(modID, potionEffect);
		if ((potion != null) && entity.isPotionActive(potion)) {
			return true;
		}
		return false;
	}

	public static Potion getModPotion(String modID, String potionEffect) {
		if (Loader.isModLoaded(modID)) {
			try {
				return Potion.getPotionFromResourceLocation(potionEffect);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	//	private Potion getPotion(String potID) {
	//		if (!potID.isEmpty()) {
	//			String pot = "";
	//			String[] configString = potID.split(":");
	//			String modID = configString[0];
	//			pot = modID;
	//			if (configString.length > 1) {
	//				String effectID = configString[1];
	//				pot = modID + ":" + effectID;
	//			} else {
	//				pot = "minecraft:" + pot;
	//			}
	//			Potion potion = Potion.getPotionFromResourceLocation(pot);
	//			return potion;
	//		}
	//		return null;
	//	}
}
