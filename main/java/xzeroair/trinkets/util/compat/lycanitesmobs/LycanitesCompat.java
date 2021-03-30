package xzeroair.trinkets.util.compat.lycanitesmobs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.util.TrinketsConfig;

public class LycanitesCompat {

	private static final String paralysis = "paralysis";
	private static final String penetration = "penetration";
	private static final String weight = "weight";
	private static final String fear = "fear";
	private static final String decay = "decay";
	private static final String insomnia = "insomnia";
	private static final String instability = "instability";
	private static final String lifeleak = "lifeleak";
	private static final String bleed = "bleed";
	private static final String plague = "plague";
	private static final String aphagia = "aphagia";
	private static final String smited = "smited";
	private static final String smouldering = "smouldering";
	private static final String leech = "leech";
	private static final String swiftswimming = "swiftswimming";
	private static final String fallresist = "fallresist";
	private static final String rejuvenation = "rejuvenation";
	private static final String immunization = "immunization";
	private static final String cleansed = "cleansed";
	private static final String repulsion = "repulsion";

	private static boolean enabled = TrinketsConfig.compat.lycanites;

	public static void removeParalysis(EntityLivingBase entity) {
		removeEffect(entity, paralysis);
	}

	public static void removePenetration(EntityLivingBase entity) {
		removeEffect(entity, penetration);
	}

	public static void removeWeight(EntityLivingBase entity) {
		removeEffect(entity, weight);
	}

	public static void removeFear(EntityLivingBase entity) {
		removeEffect(entity, fear);
	}

	public static void removeDecay(EntityLivingBase entity) {
		removeEffect(entity, decay);
	}

	public static void removeInsomnia(EntityLivingBase entity) {
		removeEffect(entity, insomnia);
	}

	public static void removeInstability(EntityLivingBase entity) {
		removeEffect(entity, instability);
	}

	public static void removeLifeleak(EntityLivingBase entity) {
		removeEffect(entity, lifeleak);
	}

	public static void removeBleed(EntityLivingBase entity) {
		removeEffect(entity, bleed);
	}

	public static void removePlague(EntityLivingBase entity) {
		removeEffect(entity, plague);
	}

	public static void removeAphagia(EntityLivingBase entity) {
		removeEffect(entity, aphagia);
	}

	public static void removeSmited(EntityLivingBase entity) {
		removeEffect(entity, smited);
	}

	public static void removeLeech(EntityLivingBase entity) {
		removeEffect(entity, leech);
	}

	public static void removeSwiftSwimming(EntityLivingBase entity) {
		removeEffect(entity, swiftswimming);
	}

	public static void removeFallResist(EntityLivingBase entity) {
		removeEffect(entity, fallresist);
	}

	public static void removeRejuvenation(EntityLivingBase entity) {
		removeEffect(entity, rejuvenation);
	}

	public static void removeImmunization(EntityLivingBase entity) {
		removeEffect(entity, immunization);
	}

	public static void removeCleansed(EntityLivingBase entity) {
		removeEffect(entity, cleansed);
	}

	public static void removeRepulsion(EntityLivingBase entity) {
		removeEffect(entity, repulsion);
	}

	public static void applyEffect(EntityLivingBase entity, String name, int duration, int amplifier) {
		if (Loader.isModLoaded("lycanitesmobs") && enabled) {
			try {
				Potion effect = Potion.getPotionFromResourceLocation("lycanitesmobs:" + name);
				if ((effect != null) && !entity.isPotionActive(effect)) {
					entity.addPotionEffect(new PotionEffect(effect, duration, amplifier, false, false));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeEffect(EntityLivingBase entity, String name) {
		if (Loader.isModLoaded("lycanitesmobs") && enabled) {
			try {
				Potion effect = Potion.getPotionFromResourceLocation("lycanitesmobs:" + name);
				if ((effect != null) && entity.isPotionActive(effect)) {
					entity.removePotionEffect(effect);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void convertManaToSpirit(EntityLivingBase entity) {
		//		if (Loader.isModLoaded("lycanitesmobs") && enabled) {
		//			if ((entity == null) || !(entity instanceof EntityPlayer)) {
		//				return;
		//			}
		//			try {
		//				int spirit = ExtendedPlayer.getForPlayer((EntityPlayer) entity).summonFocus;
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}
	}

}
