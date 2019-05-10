package xzeroair.trinkets.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public interface IItemCap {

	boolean effect();

	void setEffect(boolean transformed);

	int oreType();

	void setOreType(int type);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}