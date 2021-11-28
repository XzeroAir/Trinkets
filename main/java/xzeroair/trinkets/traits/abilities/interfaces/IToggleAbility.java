package xzeroair.trinkets.traits.abilities.interfaces;

import xzeroair.trinkets.traits.abilities.IAbilityHandler;

public interface IToggleAbility extends IAbilityHandler {

	boolean abilityEnabled();

	IToggleAbility toggleAbility(boolean enabled);

	IToggleAbility toggleAbility(int value);

}
