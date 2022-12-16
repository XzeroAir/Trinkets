<<<<<<< Updated upstream
package xzeroair.trinkets.races.dwarf;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.util.TrinketsConfig;

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
		this.addAbility(new AbilitySkilledMiner());
	}
}
=======
package xzeroair.trinkets.races.dwarf;

import javax.annotation.Nonnull;
import net.minecraft.entity.EntityLivingBase;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dwarf.config.DwarfConfig;
import xzeroair.trinkets.traits.abilities.AbilitySkilledMiner;
import xzeroair.trinkets.util.TrinketsConfig;

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
		this.addAbility(new AbilitySkilledMiner());
	}
}
>>>>>>> Stashed changes
