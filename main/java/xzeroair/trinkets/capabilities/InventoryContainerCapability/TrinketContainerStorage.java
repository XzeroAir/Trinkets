package xzeroair.trinkets.capabilities.InventoryContainerCapability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class TrinketContainerStorage implements IStorage<ITrinketContainerHandler> {

	public final static TrinketContainerStorage storage = new TrinketContainerStorage();

	@Override
	public NBTBase writeNBT(Capability<ITrinketContainerHandler> capability, ITrinketContainerHandler instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<ITrinketContainerHandler> capability, ITrinketContainerHandler instance, EnumFacing side, NBTBase nbt) {
	}

}