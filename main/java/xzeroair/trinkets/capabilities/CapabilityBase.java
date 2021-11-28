package xzeroair.trinkets.capabilities;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

public abstract class CapabilityBase<T, E> implements ITrinketCapability<T> {

	protected Random random = Reference.random;
	protected Map<String, TickHandler> Counters;
	protected NBTTagCompound tag;

	protected E object;

	public CapabilityBase(E object) {
		this.object = object;
		Counters = new TreeMap<>();
	}

	public NBTTagCompound getTag() {
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		return tag;
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void saveToNBT(NBTTagCompound tag) {

	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {

	}

	@Override
	public void copyFrom(T capability, boolean wasDeath, boolean keepInv) {

	}

	public void addCounter(String key, int length) {
		if ((Counters != null) && !Counters.containsKey(key)) {
			Counters.put(key, new TickHandler(key, length));
		}
	}

	public void removeCounter(String key) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			Counters.remove(key);
		}
	}

	public void clearCounters() {
		if (!Counters.isEmpty()) {
			Counters.clear();
		}
	}

	public TickHandler getCounter(String key, int length, boolean isCountdown, boolean create) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			return Counters.get(key);//.setLength(length).setCountdown(isCountdown);
		} else if (create) {
			final TickHandler value = new TickHandler(key, length, isCountdown);
			Counters.put(key, value);
			return value;
		} else {
			return null;
		}
	}

	public TickHandler getCounter(String key, int length, boolean isCountdown) {
		return this.getCounter(key, length, isCountdown, true);
	}

	public TickHandler getCounter(String key, boolean create) {
		return this.getCounter(key, 20, false, create);
	}

	public TickHandler getCounter(String key) {
		return this.getCounter(key, false);
	}

}
