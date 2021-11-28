package xzeroair.trinkets.init;

import net.minecraft.util.ResourceLocation;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.traits.abilities.Ability;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.Reference;

public class Abilities {

	public static final IAbilityInterface nightVision;
	public static final IAbilityInterface creativeFlight;
	public static final IAbilityInterface fireBreathing;
	public static final IAbilityInterface fireImmunity;
	public static final IAbilityInterface blockDetection;
	public static final IAbilityInterface blockClimbing;
	public static final IAbilityInterface witherImmunity;
	public static final IAbilityInterface weightless;
	public static final IAbilityInterface wellRested;
	public static final IAbilityInterface poisonAffinity;
	public static final IAbilityInterface waterAffinity;
	public static final IAbilityInterface fallResistance;
	public static final IAbilityInterface nullKinetic;
	public static final IAbilityInterface safeGuard;
	public static final IAbilityInterface enderQueen;
	public static final IAbilityInterface viciousStrike;
	public static final IAbilityInterface magnetic;
	public static final IAbilityInterface repel;
	public static final IAbilityInterface skilledMiner;
	public static final IAbilityInterface psudoFortune;
	public static final IAbilityInterface chargedShot;
	public static final IAbilityInterface lightningBolt;
	public static final IAbilityInterface dodging;
	public static final IAbilityInterface largeHands;
	public static final IAbilityInterface heavy;

	// External Mods
	public static final IAbilityInterface survivalHeatImmunity;
	public static final IAbilityInterface survivalColdImmunity;
	public static final IAbilityInterface survivalThirstImmunity;
	public static final IAbilityInterface survivalParasitesImmunity;
	public static final IAbilityInterface firstAidReflex;

	private static IAbilityInterface getFromRegistry(String id) {
		final IAbilityInterface race = Ability.Registry.getObject(new ResourceLocation(Reference.MODID, id));
		if (race == null) {
			//			throw new IllegalStateException("Invalid Race requested: " + name);
			Trinkets.log.error("Invalid Ability Entry: " + id);
			return null;
		} else {
			return race;
		}
	}

	//@formatter:off
	static {
		nightVision 				= 	getFromRegistry("night_vision");
		creativeFlight				=	getFromRegistry("creative_flight");

		//Fire
		fireBreathing				=	getFromRegistry("fire_breathing");
		fireImmunity				=	getFromRegistry("fire_immunity");

		//Heat
		survivalHeatImmunity		=	getFromRegistry("heat_immunity");
		//Cold
		survivalColdImmunity		=	getFromRegistry("cold_immunity");
		//Thirst
		survivalThirstImmunity		=	getFromRegistry("thirst_immunity");
		//Parasites
		survivalParasitesImmunity	=	getFromRegistry("parasites_immunity");

		blockDetection				=	getFromRegistry("block_detection");
		blockClimbing				=	getFromRegistry("block_climbing");
		witherImmunity				= 	getFromRegistry("wither_immunity");
		weightless					= 	getFromRegistry("weightless");
		wellRested					= 	getFromRegistry("well_rested");
		poisonAffinity				=	getFromRegistry("poison_affinity");
		waterAffinity				=	getFromRegistry("water_affinity");
		fallResistance				=	getFromRegistry("fall_resistance");
		nullKinetic					=	getFromRegistry("nullify_kinetic");
		safeGuard					=	getFromRegistry("safe_guard");
		firstAidReflex				=	getFromRegistry("firstaid_reflex");
		enderQueen					=	getFromRegistry("ender_queen");
		viciousStrike				=	getFromRegistry("vicious_strike");
		magnetic					=	getFromRegistry("magnetic");
		repel						=	getFromRegistry("repel");
		skilledMiner				=	getFromRegistry("skilled_miner");
		psudoFortune				= 	getFromRegistry("psudo_fortune");
		chargedShot					=	getFromRegistry("charged_shot");
		lightningBolt				=	getFromRegistry("lightning_bolt");
		dodging						=	getFromRegistry("dodging");
		largeHands					=	getFromRegistry("large_hands");
		heavy						=	getFromRegistry("heavy");
	}
	//@formatter:on

}
