package xzeroair.trinkets.capabilities.TrinketCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketProvider implements ICapabilitySerializable<NBTTagCompound>{

	private IAccessoryInterface capabilityItem = null;

	public TrinketProvider() {
		capabilityItem = new DefaultTrinketCapability();
	}

	public TrinketProvider(IAccessoryInterface capability) {
		capabilityItem = capability;
	}

	@CapabilityInject(IAccessoryInterface.class)
	public static final Capability<IAccessoryInterface> itemCapability = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == itemCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if ((itemCapability != null) && (capability == itemCapability)) {
			return (T) capabilityItem;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return this.getCapability(TrinketProvider.itemCapability, null).saveNBT();
		//		return this.capabilityItem.saveNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.getCapability(TrinketProvider.itemCapability, null).loadNBT(nbt);
		//		this.capabilityItem.loadNBT(nbt);
	}
}