package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

import java.util.Random;

public abstract class CapabilityBase<T, E> implements ITrinketCapability<T> {

    protected Random random = Reference.random;
    private NBTTagCompound tag;
    private TickHandler tickHandler;

    private final E object;

    public CapabilityBase(E object) {
        this.tickHandler = new TickHandler();
        this.tag = new NBTTagCompound();
        this.object = object;
    }

    public NBTTagCompound getTag() {
        if (this.tag == null) {
            this.tag = new NBTTagCompound();
        }
        return this.tag;
    }

    public E getObject() {
        return this.object;
    }

    public TickHandler getTickHandler() {
        if (this.tickHandler == null) {
            this.tickHandler = new TickHandler();
        }
        return this.tickHandler;
    }

    @Override
    public NBTTagCompound saveToNBT(NBTTagCompound tag) {
        return tag;
    }

    @Override
    public void loadFromNBT(NBTTagCompound tag) {
    }

    @Override
    public void copyFrom(T capability, boolean wasDeath, boolean keepInv) {
    }

}
