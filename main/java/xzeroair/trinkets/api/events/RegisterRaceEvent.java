package xzeroair.trinkets.api.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.util.RaceRegistry;

public class RegisterRaceEvent extends Event {

	RaceRegistry<ResourceLocation, EntityRace> Registry;
	Map<ResourceLocation, EntityRace> newRaces;

	public RegisterRaceEvent(RaceRegistry registry) {
		Registry = registry;
		newRaces = new HashMap<>();
	}

	public Map<ResourceLocation, EntityRace> getNewRaceEntries() {
		return newRaces;
	}

	public void register(ResourceLocation key, EntityRace value) {
		if (!newRaces.containsKey(key)) {
			newRaces.put(key, value);
		} else {
			Trinkets.log.warn("Warning Duplicate Entry: " + key.toString());
		}
	}
}
