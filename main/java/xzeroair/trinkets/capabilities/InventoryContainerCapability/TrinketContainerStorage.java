package xzeroair.trinkets.capabilities.InventoryContainerCapability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TrinketContainerStorage implements IStorage<IContainerHandler> {

	public final static TrinketContainerStorage storage = new TrinketContainerStorage();

	@Override
	public NBTBase writeNBT(Capability<IContainerHandler> capability, IContainerHandler instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IContainerHandler> capability, IContainerHandler instance, EnumFacing side, NBTBase nbt) {
	}

}