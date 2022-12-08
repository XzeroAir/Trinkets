package xzeroair.trinkets.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityStorage<T extends CapabilityBase> implements IStorage<T> {

	@Override
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
		if (instance == null) {
			return null;
		}
		return instance.saveToNBT(instance.getTag());
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
		if ((instance == null) || (nbt == null)) {
			return;
		}
		instance.loadFromNBT((NBTTagCompound) nbt);
	}

}
