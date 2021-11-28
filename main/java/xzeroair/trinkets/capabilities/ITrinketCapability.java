package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface ITrinketCapability<T> {

	void onUpdate();

	void saveToNBT(NBTTagCompound tag);

	void loadFromNBT(NBTTagCompound tag);

	void copyFrom(T capability, boolean wasDeath, boolean keepInv);
}
