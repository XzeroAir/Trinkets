package xzeroair.trinkets.compatibilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.compatibilities.sizeCap.DeCap;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class ItemProvider implements ICapabilitySerializable<NBTTagCompound>{

	private ItemCap capabilityItem = null;

	public ItemProvider() {
		this.capabilityItem = new DefaultItemCapability();
	}

	public ItemProvider(ItemCap capability) {
		this.capabilityItem = capability;
	}

	@CapabilityInject(ItemCap.class)
	public static final Capability<ItemCap> itemCapability = null;

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
		return capabilityItem.saveNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		capabilityItem.loadNBT(nbt);
	}
}