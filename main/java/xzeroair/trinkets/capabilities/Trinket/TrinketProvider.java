package xzeroair.trinkets.capabilities.Trinket;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.Capabilities;

public class TrinketProvider implements ICapabilitySerializable<NBTTagCompound> {

	private TrinketProperties trinket;

	public TrinketProvider(ItemStack stack) {
		trinket = new TrinketProperties(stack);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == Capabilities.ITEM_TRINKET;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == Capabilities.ITEM_TRINKET) {
			return (T) trinket;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		trinket.savedNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		trinket.loadNBTData(nbt);
	}
}