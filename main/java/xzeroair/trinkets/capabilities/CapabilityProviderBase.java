package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityProviderBase<Handler extends CapabilityBase> implements ICapabilitySerializable<NBTTagCompound> {

	private final Capability<?> capability;
	private final Handler handler;

	public CapabilityProviderBase(Capability<?> capability, Handler handler) {
		this.capability = capability;
		this.handler = handler;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == this.capability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == this.capability) {
			return (T) handler;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tag = handler.getTag();
		handler.saveToNBT(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		handler.loadFromNBT(nbt);
	}

}
