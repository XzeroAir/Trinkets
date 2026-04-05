package xzeroair.trinkets.util.handlers;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TickHandler {

    private final Map<String, Counter> Counters;

    public TickHandler() {
        this.Counters = new TreeMap<>();
    }

    public Map<String, Counter> getCounters() {
        return this.Counters;
    }

    public void addCounter(String key, int length, boolean countdown, boolean shouldTick, boolean saveToNBT) {
        if ((this.Counters != null) && !this.Counters.containsKey(key)) {
            this.Counters.put(key, new Counter(key, length, countdown, shouldTick, saveToNBT));
        }
    }

    public void removeCounter(String key) {
        if (!this.Counters.isEmpty()) {
            this.Counters.remove(key);
        }
    }

    public void clearCounters() {
        if (!this.Counters.isEmpty()) {
            this.Counters.clear();
        }
    }

    @Nullable
    public Counter getCounter(String key, int length, boolean isCountdown, boolean shouldTick, boolean create, boolean saveNBT) {
        return this.getCounter(key, length, isCountdown, shouldTick, true, create, saveNBT);
    }

    @Nullable
    public Counter getCounter(String key, int length, boolean isCountdown, boolean shouldTick, boolean autoReset, boolean create, boolean saveNBT) {
        if (!this.Counters.isEmpty() && this.Counters.containsKey(key)) {
            return this.Counters.get(key);//.setLength(length).setCountdown(isCountdown);
        } else if (create) {
            final Counter value = new Counter(key, length, isCountdown, shouldTick, autoReset, saveNBT);
            this.Counters.put(key, value);
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

    @Nullable
    public Counter getCounter(String key) {
        if (!this.Counters.isEmpty() && this.Counters.containsKey(key)) {
            return this.Counters.get(key);
        }
        return null;
    }

    public void saveCountersToNBT(NBTTagCompound compound) {
        NBTTagCompound counters = new NBTTagCompound();
        for (Entry<String, Counter> counter : this.Counters.entrySet()) {
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
        if (!counters.isEmpty()) {
            compound.setTag("Counters", counters);
        }
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
                    if (this.Counters.containsKey(name)) {
                        this.Counters.replace(name, nbtCounter);
                    } else {
                        this.Counters.putIfAbsent(name, nbtCounter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
