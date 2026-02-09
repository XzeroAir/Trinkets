package xzeroair.trinkets.races.dwarf;

import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.util.TrinketsConfig;

import javax.annotation.Nonnull;

public class RaceDwarf extends EntityRacePropertiesHandler {

    public static final DwarfConfig serverConfig = TrinketsConfig.SERVER.races.dwarf;

    public RaceDwarf(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.dwarf);
    }

    @Override
    public void startTransformation() {
        //		if (serverConfig.fortune) {
        //			this.addAbility(new AbilityPsudoFortune());
        //		}
//        if (serverConfig.skilled_miner) {
        this.addAbility(new AbilitySkilledMiner());
//        }
    }
}
