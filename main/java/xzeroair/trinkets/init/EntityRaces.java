package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.Reference;

public class EntityRaces {

	public static final EntityRace none;
	public static final EntityRace human;
	public static final EntityRace fairy;
	public static final EntityRace dwarf;
	public static final EntityRace titan;
	public static final EntityRace goblin;
	public static final EntityRace elf;
	public static final EntityRace faelis;
	//	public static final EntityRace slime;
	public static final EntityRace dragon;
	//	public static final EntityRace orc;
	//	public static final EntityRace succubus;
	//	public static final EntityRace incubus;
	//	public static final EntityRace nymph;
	//	public static final EntityRace siren;
	//	public static final EntityRaceMixed mixed;

	private static EntityRace getRegisteredRace(String name) {
		EntityRace race = EntityRace.Registry.getObject(new ResourceLocation(Reference.MODID, name));

		if (race == null) {
			throw new IllegalStateException("Invalid Race requested: " + name);
		} else {
			return race;
		}
	}

	//@formatter:off
	static {
		none 		= 	getRegisteredRace("none");
		human 		= 	getRegisteredRace("human");
		fairy 		= 	getRegisteredRace("fairy");
		dwarf 		= 	getRegisteredRace("dwarf");
		titan 		= 	getRegisteredRace("titan");
		goblin 		=	getRegisteredRace("goblin");
		elf 		= 	getRegisteredRace("elf");
		faelis 		= 	getRegisteredRace("faelis");
//		slime 		= 	getRegisteredRace("slime");
		dragon 		= 	getRegisteredRace("dragon");
//		orc 		= 	getRegisteredRace("orc");
//		succubus 	= 	getRegisteredRace("succubus");
//		incubus 	= 	getRegisteredRace("incubus");
//		nymph 		= 	getRegisteredRace("nymph");
//		siren 		= 	getRegisteredRace("siren");
//		mixed 		= 	(EntityRaceMixed) getRegisteredRace("mixed");
	}
	//@formatter:on

}
