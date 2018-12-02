package xzeroair.trinkets.compatibilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public class DefaultItemCapability implements ItemCap {

	int type = -1;
	boolean on = false;

	public DefaultItemCapability(){

	}

	public DefaultItemCapability(int type, boolean on) {
		this.type = type;
		this.on = on;
	}

	@Override
	public boolean nightVision() {
		return on;
	}

	@Override
	public void nightVisionOn(boolean on) {
		if(this.on != on) {
			this.on = on;
		}
	}
	@Override
	public int oreType() {
		return type;
	}

	@Override
	public void setOreType(int type) {
		if (this.type != type) {
			this.type = type;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) ItemStorage.storage.writeNBT(ItemProvider.itemCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		ItemStorage.storage.readNBT(ItemProvider.itemCapability, this, null, compound);
	}
}
