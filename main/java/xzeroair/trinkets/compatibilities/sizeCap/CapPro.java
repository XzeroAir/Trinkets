package xzeroair.trinkets.compatibilities.sizeCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapPro implements ICapabilitySerializable<NBTTagCompound>{

	private ICap capabilitySize = null;

	public CapPro() {
		this.capabilitySize = new DeCap();
	}

	public CapPro(ICap capability) {
		this.capabilitySize = capability;
	}

	@CapabilityInject(ICap.class)
	public static final Capability<ICap> sizeCapability = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == sizeCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if ((sizeCapability != null) && (capability == sizeCapability)) {
			return (T) capabilitySize;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return capabilitySize.saveNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		capabilitySize.loadNBT(nbt);
	}
}