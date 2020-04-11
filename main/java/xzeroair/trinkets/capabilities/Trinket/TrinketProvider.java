package xzeroair.trinkets.capabilities.Trinket;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.Capabilities;

public class TrinketProvider implements ICapabilitySerializable<NBTTagCompound> {

	private TrinketProperties trinket = new TrinketProperties();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == Capabilities.ITEM_TRINKET;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == Capabilities.ITEM_TRINKET) {
			return (T) this.trinket;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		this.trinket.savedNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.trinket.loadNBTData(nbt);
	}
}