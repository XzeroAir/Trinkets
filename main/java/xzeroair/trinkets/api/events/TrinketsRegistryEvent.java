package xzeroair.trinkets.api.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.IElementInterface;
import xzeroair.trinkets.util.registry.TrinketRegistry;

public class TrinketsRegistryEvent<V> extends Event {

	private TrinketRegistry<ResourceLocation, V> Registry;
	private Map<ResourceLocation, V> newEntries;

	public TrinketsRegistryEvent(TrinketRegistry registry) {
		Registry = registry;
		newEntries = new HashMap<>();
	}

	public Map<ResourceLocation, V> getEntries() {
		return newEntries;
	}

	public void register(ResourceLocation key, V value) {
		if (!newEntries.containsKey(key)) {
			newEntries.put(key, value);
		} else {
			Trinkets.log.warn("Warning Duplicate Entry: " + key.toString());
		}
	}

	public static class RegisterRaceEvent extends TrinketsRegistryEvent<EntityRace> {
		public RegisterRaceEvent(TrinketRegistry registry) {
			super(registry);
		}
	}

	public static class RegisterAbilitiesEvent extends TrinketsRegistryEvent<IAbilityInterface> {
		public RegisterAbilitiesEvent(TrinketRegistry registry) {
			super(registry);
		}
	}

	public static class RegisterElementsEvent extends TrinketsRegistryEvent<IElementInterface> {
		public RegisterElementsEvent(TrinketRegistry registry) {
			super(registry);
		}
	}
}
