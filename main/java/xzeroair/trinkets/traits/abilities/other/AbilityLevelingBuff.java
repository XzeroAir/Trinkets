package xzeroair.trinkets.traits.abilities.other;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IPickupExpAbility;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.util.TrinketsRegistryNames;

public class AbilityLevelingBuff extends Ability implements ITickableAbility, IPickupExpAbility {

    public AbilityLevelingBuff() {
        super(TrinketsRegistryNames.ModAbilities.LEVELING);
    }

    @Override
    public void onPickup(EntityPlayer player, EntityXPOrb orb) {
        //		int xp = orb.getXpValue();
        //		if (xp > 1) {
        //			final ItemStack stack = this.getAbilityHolder().getInfo().getStackFromHandler(player);
        //			if (!stack.isEmpty()) {
        //				Capabilities.getTrinketProperties(stack, prop -> {
        //					int v = xp / 2;
        //					prop.setStoredExp(v);
        //					orb.xpValue -= v;
        //					/// Testing
        //					// Testing
        //				});
        //				//			orb.xpValue -= xp;
        //				//			System.out.println(xp + "");
        //			}
        //		}
    }

    @Override
    public void tickAbility(EntityLivingBase entity) {
    }
}