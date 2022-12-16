package xzeroair.trinkets.util.helpers;

import net.minecraft.entity.Entity;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;

public class MagicHelper {

	public static void refillMana(Entity entity) {
		Capabilities.getMagicStats(entity, MagicStats::refillMana);
	}

	public static void refillMana(Entity entity, float amount) {
		Capabilities.getMagicStats(entity, magic -> magic.addMana(amount));
	}

	public static void refillManaByPrecentage(Entity entity, float percent) {
		Capabilities.getMagicStats(entity, magic -> magic.addMana(magic.getMaxMana() * percent));
	}

	public static boolean spendMana(Entity entity, float cost) {
		return Capabilities.getMagicStats(entity, true, (magic, rtn) -> magic.spendMana(cost));
	}

}
