package xzeroair.trinkets.util.handlers;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

public class TickHandler {

	Map<String, Counter> Counters;

	public TickHandler() {
		Counters = new TreeMap<>();
	}

	public Map<String, Counter> getCounters() {
		return Counters;
	}

	public void addCounter(String key, int length, boolean countdown, boolean shouldTick, boolean saveToNBT) {
		if ((Counters != null) && !Counters.containsKey(key)) {
			Counters.put(key, new Counter(key, length, countdown, shouldTick, saveToNBT));
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

	public Counter getCounter(String key, int length, boolean isCountdown, boolean shouldTick, boolean create, boolean saveNBT) {
		return this.getCounter(key, length, isCountdown, shouldTick, true, create, saveNBT);
	}

	public Counter getCounter(String key, int length, boolean isCountdown, boolean shouldTick, boolean autoReset, boolean create, boolean saveNBT) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			return Counters.get(key);//.setLength(length).setCountdown(isCountdown);
		} else if (create) {
			final Counter value = new Counter(key, length, isCountdown, shouldTick, autoReset, saveNBT);
			Counters.put(key, value);
			return value;
		} else {
			return null;
		}
	}
	//	public Counter getCounter(String key, int length, boolean isCountdown, boolean shouldTick) {
	//		return this.getCounter(key, length, isCountdown, shouldTick, false);
	//	}
	//	public Counter getCounter(String key, int length, boolean isCountdown) {
	//		return this.getCounter(key, length, isCountdown, true, false);
	//	}
	//	public Counter getCounter(String key, int length) {
	//		return this.getCounter(key, length, false, true, false);
	//	}
	//
	//	public Counter getCounter(String key, boolean create) {
	//		return this.getCounter(key, 20, false, create);
	//	}

	public Counter getCounter(String key) {
		if (!Counters.isEmpty() && Counters.containsKey(key)) {
			return Counters.get(key);
		}
		return null;
	}

	public void saveCountersToNBT(NBTTagCompound compound) {
		NBTTagCompound counters = new NBTTagCompound();
		for (Entry<String, Counter> counter : Counters.entrySet()) {
			final String name = counter.getValue().getName();
			final int tick = counter.getValue().getTick();
			final int length = counter.getValue().getLength();
			final boolean countdown = counter.getValue().getCountdown();
			final boolean shouldTick = counter.getValue().shouldTick();
			final boolean saveToNBT = counter.getValue().saveToNBT();
			if (!saveToNBT) {
				continue;
			}
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("Tick", tick);
			nbt.setInteger("Length", length);
			nbt.setBoolean("Countdown", countdown);
			nbt.setBoolean("ShouldTick", shouldTick);
			counters.setTag(name, nbt);
		}
		compound.setTag("Counters", counters);
	}

	public void loadCountersFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("Counters")) {
			NBTTagCompound counters = compound.getCompoundTag("Counters");
			counters.getKeySet().forEach(name -> {
				try {
					NBTTagCompound counter = counters.getCompoundTag(name);
					int tick = counter.getInteger("Tick");
					int length = counter.getInteger("Length");
					boolean countdown = counter.getBoolean("Countdown");
					boolean shouldTick = counter.getBoolean("ShouldTick");
					Counter nbtCounter = new Counter(name, length, countdown, shouldTick).setTick(tick);
					if (Counters.containsKey(name)) {
						Counters.replace(name, nbtCounter);
					} else {
						Counters.putIfAbsent(name, nbtCounter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

}
