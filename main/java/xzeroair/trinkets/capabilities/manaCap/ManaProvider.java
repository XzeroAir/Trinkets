package xzeroair.trinkets.capabilities.manaCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.Capabilities;

public class ManaProvider implements ICapabilitySerializable<NBTTagCompound> {

	private final ManaStats mana = new ManaStats();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == Capabilities.PLAYER_MANA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == Capabilities.PLAYER_MANA) {
			return (T) mana;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		mana.savedNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		mana.loadNBTData(nbt);
	}


}
