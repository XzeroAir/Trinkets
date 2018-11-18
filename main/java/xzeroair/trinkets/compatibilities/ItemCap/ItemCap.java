package xzeroair.trinkets.compatibilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.helpers.TrinketHelper.targetOreType;

public interface ItemCap {

	boolean nightVision();

	void nightVisionOn(boolean transformed);

	int oreType();

	void setOreType(int type);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}