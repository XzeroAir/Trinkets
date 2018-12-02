package xzeroair.trinkets.compatibilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public interface ItemCap {

	boolean nightVision();

	void nightVisionOn(boolean transformed);

	int oreType();

	void setOreType(int type);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}