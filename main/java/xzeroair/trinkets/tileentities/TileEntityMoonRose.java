package xzeroair.trinkets.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.TileEntityCap.ManaEssenceProperties;

public class TileEntityMoonRose extends TileEntity implements ITickable {

	public TileEntityMoonRose() {
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return super.getCapability(capability, facing);
	}

	public ManaEssenceProperties getProperties() {
		return Capabilities.getTileEntityManaProperties(this);
	}

	@Override
	public void update() {
		if (world != null) {
			if (this.getProperties() != null) {
				this.getProperties().onUpdate();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (this.getProperties() != null) {
			this.getProperties().saveToNBT(compound);
		}
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (this.getProperties() != null) {
			this.getProperties().loadFromNBT(compound);
		}
	}
}
