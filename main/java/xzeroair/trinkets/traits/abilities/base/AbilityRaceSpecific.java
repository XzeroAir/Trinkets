package xzeroair.trinkets.traits.abilities.base;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;

public class AbilityRaceSpecific extends Ability implements IKeyBindInterface {


    public AbilityRaceSpecific(String modID, String name) {
        super(modID, name);
    }

    public AbilityRaceSpecific(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getKey() {
        return ModKeyBindings.RACE_ABILITY.getDisplayName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getAuxKey() {
        return ModKeyBindings.AUX_KEY.getDisplayName();
    }


}