package xzeroair.trinkets.util.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import javax.annotation.Nullable;
import net.minecraft.util.IntIdentityHashBiMap;

public class TrinketRegistry<K, V> extends TrinketRegistrySimple<K, V> {
	/** The backing store that maps Integers to objects. */
	protected final IntIdentityHashBiMap<V> underlyingIntegerMap = new IntIdentityHashBiMap<>(256);
	/** A BiMap of objects (key) to their names (value). */
	protected final Map<V, K> inverseObjectRegistry;

	protected final Map<UUID, V> uuidObjectRegistry = new HashMap<>();

	public TrinketRegistry() {
		this.inverseObjectRegistry = ((BiMap) registryObjects).inverse();
	}

	public void register(int id, K key, UUID uuid, V value) {
		this.underlyingIntegerMap.put(value, id);
		uuidObjectRegistry.put(uuid, value);
		this.putObject(key, value);
	}

	/**
	 * Creates the Map we will use to map keys to their registered values.
	 */
	@Override
	protected Map<K, V> createUnderlyingMap() {
		return HashBiMap.<K, V>create();
	}

	@Override
	@Nullable
	public V getObject(@Nullable K name) {
		return super.getObject(name);
	}

	/**
	 * Gets the name we use to identify the given object.
	 */
	@Nullable
	public K getNameForObject(V value) {
		return this.inverseObjectRegistry.get(value);
	}

	/**
	 * Does this registry contain an entry for the given key?
	 */
	@Override
	public boolean containsKey(K key) {
		return super.containsKey(key);
	}

	/**
	 * Gets the integer ID we use to identify the given object.
	 */
	public int getIDForObject(@Nullable V value) {
		return this.underlyingIntegerMap.getId(value);
	}

	/**
	 * Gets the object identified by the given ID.
	 */
	@Nullable
	public V getObjectById(int id) {
		return this.underlyingIntegerMap.get(id);
	}

	@Nullable
	public V getObjectByUUID(UUID uuid) {
		return this.uuidObjectRegistry.get(uuid);
	}

	@Override
	public Iterator<V> iterator() {
		return this.underlyingIntegerMap.iterator();
	}
}
