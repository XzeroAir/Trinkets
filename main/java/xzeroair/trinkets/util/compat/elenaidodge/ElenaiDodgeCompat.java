<<<<<<< Updated upstream
package xzeroair.trinkets.util.compat.elenaidodge;

import com.elenai.elenaidodge.api.DodgeEvent.ServerDodgeEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;

public class ElenaiDodgeCompat {

	@SubscribeEvent
	public void DodgeEvent(ServerDodgeEvent event) {
		final EntityPlayer player = event.getPlayer();
		Capabilities.getEntityProperties(player, prop -> {
			try {
				final IAbilityInterface ability = prop.getAbilityHandler().getAbility(Abilities.dodging.toString());
				if (ability instanceof AbilityDodge) {
					final MagicStats magic = Capabilities.getMagicStats(player);
					final float cost = TrinketsConfig.SERVER.Items.ARCING_ORB.dodgeCost;
					if ((magic != null) && magic.spendMana(cost)) {
						final AbilityDodge dodge = (AbilityDodge) ability;
						dodge.dodge(player);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
=======
package xzeroair.trinkets.util.compat.elenaidodge;

import com.elenai.elenaidodge.api.DodgeEvent.ServerDodgeEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;

public class ElenaiDodgeCompat {

	@SubscribeEvent
	public void DodgeEvent(ServerDodgeEvent event) {
		final EntityPlayer player = event.getPlayer();
		Capabilities.getEntityProperties(player, prop -> {
			try {
				final IAbilityInterface ability = prop.getAbilityHandler().getAbility(Abilities.dodging.toString());
				if (ability instanceof AbilityDodge) {
					final MagicStats magic = Capabilities.getMagicStats(player);
					final float cost = TrinketsConfig.SERVER.Items.ARCING_ORB.dodgeCost;
					if ((magic != null) && magic.spendMana(cost)) {
						final AbilityDodge dodge = (AbilityDodge) ability;
						dodge.dodge(player);
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}
>>>>>>> Stashed changes
