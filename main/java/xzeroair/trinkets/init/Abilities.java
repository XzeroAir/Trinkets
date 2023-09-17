package xzeroair.trinkets.init;

import javax.annotation.Nullable;
import xzeroair.trinkets.traits.abilities.AbilityNightVision;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;

public class Abilities {

	//@formatter:off
	public static final String nightVision 					="night_vision";
	public static final String creativeFlight				="creative_flight";
	public static final String fireBreathing				="fire_breathing";
	public static final String fireImmunity					="fire_immunity";
	public static final String iceImmunity					="ice_immunity";
	public static final String lightningImmunity					="lightning_immunity";
	public static final String blockDetection				="block_detection";
	public static final String blockClimbing				="block_climbing";
	public static final String witherImmunity				="wither_immunity";
	public static final String weightless					="weightless";
	public static final String wellRested					="well_rested";
	public static final String poisonAffinity				="poison_affinity";
	public static final String waterAffinity				="water_affinity";
	public static final String fallResistance				="fall_resistance";
	public static final String nullKinetic					="nullify_kinetic";
	public static final String reduceKinetic				="reduce_kinetic";
	public static final String safeGuard					="safe_guard";
	public static final String enderQueen					="ender_queen";
	public static final String viciousStrike				="vicious_strike";
	public static final String magnetic						="magnetic";
	public static final String repel						="repel";
	public static final String skilledMiner					="skilled_miner";
	public static final String psudoFortune					="psudo_fortune";
	public static final String chargedShot					="charged_shot";
	public static final String lightningBolt				="lightning_bolt";
	public static final String dodging						="dodging";
	public static final String frostWalker					="frost_walker";

	public static final String largeHands					="large_hands";
	public static final String heavy						="heavy";
	public static final String mountEnhancement				="mount_enhancement";

	// External Mods
	public static final String survivalHeatImmunity			="heat_immunity";
	public static final String survivalColdImmunity			="cold_immunity";
	public static final String survivalThirstImmunity		="thirst_immunity";
	public static final String survivalParasitesImmunity	="parasites_immunity";
	public static final String firstAidReflex				="firstaid_reflex";

//	private static final String name(String id) {
//		return Reference.MODID + ":" + id;
//	}

//	static {
//		nightVision 				= 	name("night_vision");
//		creativeFlight				=	name("creative_flight");

		//Fire
//		fireBreathing				=	name("fire_breathing");
//		fireImmunity				=	name("fire_immunity");

		//Heat
//		survivalHeatImmunity		=	name("heat_immunity");
//		Cold
//		survivalColdImmunity		=	name("cold_immunity");
		//Thirst
//		survivalThirstImmunity		=	name("thirst_immunity");
		//Parasites
//		survivalParasitesImmunity	=	name("parasites_immunity");

//		blockDetection				=	name("block_detection");
//		blockClimbing				=	name("block_climbing");
//		witherImmunity				= 	name("wither_immunity");
//		weightless					= 	name("weightless");
//		wellRested					= 	name("well_rested");
//		poisonAffinity				=	name("poison_affinity");
//		waterAffinity				=	name("water_affinity");
//		fallResistance				=	name("fall_resistance");
//		nullKinetic					=	name("nullify_kinetic");
//		safeGuard					=	name("safe_guard");
//		firstAidReflex				=	name("firstaid_reflex");
//		enderQueen					=	name("ender_queen");
//		viciousStrike				=	name("vicious_strike");
//		magnetic					=	name("magnetic");
//		repel						=	name("repel");
//		skilledMiner				=	name("skilled_miner");
//		psudoFortune				= 	name("psudo_fortune");
//		chargedShot					=	name("charged_shot");
//		lightningBolt				=	name("lightning_bolt");
//		dodging						=	name("dodging");
//		largeHands					=	name("large_hands");
//		heavy						=	name("heavy");
//	}
	//@formatter:on

	@Nullable
	public static final IAbilityInterface getAbility(String ability) {
		switch (ability) {
		case Reference.MODID + ":" + nightVision:
			return new AbilityNightVision();
		//		case creativeFlight:
		//			return new AbilityFlying();
		default:
			return null;
		}
	}

}
