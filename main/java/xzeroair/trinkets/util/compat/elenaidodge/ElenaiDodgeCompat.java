package xzeroair.trinkets.util.compat.elenaidodge;

import com.elenai.elenaidodge.api.DodgeEvent.ServerDodgeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.events.EventBaseHandler;
import xzeroair.trinkets.traits.abilities.AbilityDodge;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class ElenaiDodgeCompat extends EventBaseHandler {

    public static boolean isModEnabled() {
        return Trinkets.MOD_COMPAT.ElenaiDodge1 && TrinketsConfig.compat.ELENAI_DODGE;
    }

    @SubscribeEvent
    public void DodgeEvent(ServerDodgeEvent event) {
        if (!isModEnabled()) {
            return;
        }
        if (event.getCooldown() > 0) {
            return;
        }
        final EntityPlayer player = event.getPlayer();
        Capabilities.getEntityProperties(player, prop -> {
            try {
                final IAbilityInterface ability = prop.getAbilityHandler().getAbility(Reference.MODID + ":" + TrinketsRegistryNames.ModAbilities.DODGING);
                if (ability instanceof AbilityDodge) {
                    Capabilities.getMagicStats(player, magic -> {
                        final float cost = TrinketsConfig.SERVER.ITEMS.ARCING_ORB.ABILITIES.DODGE.COST;
                        if (magic.spendMana(cost)) {
                            final AbilityDodge dodge = (AbilityDodge) ability;
                            dodge.applyDodgeEffects(player);
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    //	@SubscribeEvent // This is a Client Side only Event
    //	public void RequestDodge(RequestDodgeEvent event) {
    //	}
}
