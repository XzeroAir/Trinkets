<<<<<<< Updated upstream
package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface ITrinketCapability<T> {

	void onUpdate();

	NBTTagCompound saveToNBT(NBTTagCompound tag);

	void loadFromNBT(NBTTagCompound tag);

	void copyFrom(T capability, boolean wasDeath, boolean keepInv);
}
=======
package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;

public interface ITrinketCapability<T> {

	void onUpdate();

	NBTTagCompound saveToNBT(NBTTagCompound tag);

	void loadFromNBT(NBTTagCompound tag);

	void copyFrom(T capability, boolean wasDeath, boolean keepInv);
}
>>>>>>> Stashed changes
