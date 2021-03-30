package xzeroair.trinkets.capabilities.TileEntityCap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.Capabilities;

public class ManaEssenceProvider implements ICapabilitySerializable<NBTTagCompound> {

	private final ManaEssenceProperties essence;

	public ManaEssenceProvider(TileEntity te) {
		essence = new ManaEssenceProperties(te, null);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == Capabilities.TILE_ENTITY_MANA_ESSENCE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == Capabilities.TILE_ENTITY_MANA_ESSENCE) {
			return (T) essence;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		essence.saveToNBT(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		essence.loadFromNBT(nbt);
	}

}
