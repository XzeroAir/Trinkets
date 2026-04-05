package xzeroair.trinkets.traits.abilities.interfaces;

public interface IToggleAbility extends IAbilityInterface {

    boolean isAbilityToggled();

    int getToggleMode();

    IToggleAbility toggleAbility(boolean enabled);

    IToggleAbility toggleAbility(int value);

}
