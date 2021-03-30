package xzeroair.trinkets.util.compat;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Loader;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.simpledifficulty.SDCompat;
import xzeroair.trinkets.util.compat.toughasnails.TANCompat;

public class SurvivalCompat {

	public static void addThirst(EntityLivingBase player, int amount, int saturation) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				SDCompat.addThirst((EntityPlayer) player, amount, saturation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				TANCompat.addThirst((EntityPlayer) player, amount, saturation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void ClearTempurature(EntityLivingBase player) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				SDCompat.ClearTempurature((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				TANCompat.ClearTempurature((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void immuneToHeat(EntityLivingBase player) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				SDCompat.immuneToHeat((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				TANCompat.immuneToHeat((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void immuneToCold(EntityLivingBase player) {
		if (!(player instanceof EntityPlayer)) {
			return;
		}
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				SDCompat.immuneToCold((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				TANCompat.immuneToCold((EntityPlayer) player);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void clearThirst(EntityLivingBase entity) {
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				SDCompat.clearThirst(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				TANCompat.clearThirst(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static List<Potion> thirsts = new ArrayList();
	static List<Potion> cold = new ArrayList();
	static List<Potion> hot = new ArrayList();

	public static List<Potion> getThirstEffects() {
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				Potion SDThirst = SDCompat.getSDThirst();
				if (!thirsts.contains(SDThirst)) {
					thirsts.add(SDCompat.getSDThirst());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				Potion TANThirst = TANCompat.getTANThirst();
				if (!thirsts.contains(TANThirst)) {
					thirsts.add(TANCompat.getTANThirst());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return thirsts;
	}

	public static List<Potion> getHypothermiaEffects() {
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				Potion SDHypo = SDCompat.getSDHypothermia();
				if (!cold.contains(SDHypo)) {
					cold.add(SDCompat.getSDHypothermia());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				Potion TANHypo = TANCompat.getTANHypothermia();
				if (!cold.contains(TANHypo)) {
					cold.add(TANCompat.getTANHypothermia());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cold;
	}

	public static List<Potion> getHyperthermiaEffects() {
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				Potion SDHyper = SDCompat.getSDHyperthermia();
				if (!hot.contains(SDHyper)) {
					hot.add(SDCompat.getSDHyperthermia());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TrinketsConfig.compat.toughasnails && Loader.isModLoaded("toughasnails")) {
			try {
				Potion TANHyper = TANCompat.getTANHyperthermia();
				if (!hot.contains(TANHyper)) {
					hot.add(TANCompat.getTANHyperthermia());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hot;
	}

	public static Potion getSDParasitesPotionEffect() {
		if (TrinketsConfig.compat.simpledifficulty && Loader.isModLoaded("simpledifficulty")) {
			try {
				return SDCompat.getSDParasites();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
