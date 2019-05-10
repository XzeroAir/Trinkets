package xzeroair.trinkets.capabilities.sizeCap;

import net.minecraft.nbt.NBTTagCompound;

public interface ISizeCap {

	boolean getTrans();

	void setTrans(boolean transformed);

	int getSize();

	void setSize(int size);

	int getTarget();

	void setTarget(int target);

	float getWidth();

	void setWidth(float width);

	float getHeight();

	void setHeight(float height);

	float getDefaultWidth();

	void setDefaultWidth(float width);

	float getDefaultHeight();

	void setDefaultHeight(float height);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}
