<<<<<<< Updated upstream
package xzeroair.trinkets.capabilities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

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

	public TickHandler getTickHandler() {
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
=======
package xzeroair.trinkets.capabilities;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.handlers.TickHandler;

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

	public TickHandler getTickHandler() {
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
>>>>>>> Stashed changes
