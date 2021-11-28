package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;

public class MagicHelper {

	public static void refillMana(Entity entity) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			magic.refillMana();
		}
	}

	public static void refillMana(Entity entity, float amount) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			magic.addMana(amount);
		}
	}

	public static void refillManaByPrecentage(Entity entity, float percent) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			magic.addMana(magic.getMaxMana() * percent);
		}
	}

	public static boolean spendMana(Entity entity, float cost) {
		final MagicStats magic = Capabilities.getMagicStats(entity);
		if (magic != null) {
			if (magic.spendMana(cost)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
