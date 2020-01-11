package xzeroair.trinkets.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public interface IItemCap {

	boolean ability();

	void setAbility(boolean transformed);

	boolean altAbility();

	void setAltAbility(boolean transformed);

	int oreTarget();

	void setOreTarget(int type);

	int hitCount();

	void setHitCount(int hits);

	int storedExp();

	void setStoredExp(int exp);

	int wornSlot();

	void setWornSlot(int slot);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}