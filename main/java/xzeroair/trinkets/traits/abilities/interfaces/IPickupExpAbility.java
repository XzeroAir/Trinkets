package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;

public interface IPickupExpAbility extends IAbilityInterface {

	void onPickup(EntityPlayer player, EntityXPOrb orb);

}
