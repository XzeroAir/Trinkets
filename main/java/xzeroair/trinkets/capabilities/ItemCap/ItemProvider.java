package xzeroair.trinkets.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class ItemProvider implements ICapabilitySerializable<NBTTagCompound>{

	private IItemCap capabilityItem = null;

	public ItemProvider() {
		capabilityItem = new DefaultItemCapability();
	}

	public ItemProvider(IItemCap capability) {
		capabilityItem = capability;
	}

	@CapabilityInject(IItemCap.class)
	public static final Capability<IItemCap> itemCapability = null;

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
		return this.getCapability(ItemProvider.itemCapability, null).saveNBT();
		//		return this.capabilityItem.saveNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.getCapability(ItemProvider.itemCapability, null).loadNBT(nbt);
		//		this.capabilityItem.loadNBT(nbt);
	}
}