package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

import java.util.Random;

public abstract class CapabilityBase<T, E> implements ITrinketCapability<T> {

    protected Random random = Reference.random;
    protected NBTTagCompound tag;
    protected TickHandler tickHandler;

    protected E object;

    public CapabilityBase(E object) {
        tickHandler = new TickHandler();
        tag = new NBTTagCompound();
        this.object = object;
    }

    public NBTTagCompound getTag() {
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        return tag;
    }

    public E getObject() {
        return object;
    }

    public TickHandler getTickHandler() {
        if (tickHandler == null) {
            tickHandler = new TickHandler();
        }
        return tickHandler;
    }

    @Override
    public void onUpdate() {
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
