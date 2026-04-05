package xzeroair.trinkets.api.events;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.registry.TrinketRegistry;

import java.util.HashMap;
import java.util.Map;

public class TrinketsRegistryEvent<V> extends Event {

    private final TrinketRegistry<ResourceLocation, V> Registry;
    private final Map<ResourceLocation, V> newEntries;

    public TrinketsRegistryEvent(TrinketRegistry registry) {
        this.Registry = registry;
        this.newEntries = new HashMap<>();
    }

    public Map<ResourceLocation, V> getEntries() {
        return this.newEntries;
    }

    public void register(ResourceLocation key, V value) {
        if (!this.newEntries.containsKey(key)) {
            this.newEntries.put(key, value);
        } else {
            Trinkets.LOGGER.warn("Warning Duplicate Entry: " + key.toString());
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

    public static class RegisterElementsEvent extends TrinketsRegistryEvent<Element> {
        public RegisterElementsEvent(TrinketRegistry registry) {
            super(registry);
        }
    }
}
