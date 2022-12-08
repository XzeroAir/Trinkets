package xzeroair.trinkets.traits.abilities.interfaces;

public interface IToggleAbility extends IAbilityInterface {

	boolean abilityEnabled();

	IToggleAbility toggleAbility(boolean enabled);

	IToggleAbility toggleAbility(int value);

}
