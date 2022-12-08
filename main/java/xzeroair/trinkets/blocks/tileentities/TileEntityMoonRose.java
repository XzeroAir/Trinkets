package xzeroair.trinkets.blocks.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.TileEntityCap.TileEntityProperties;

public class TileEntityMoonRose extends TileEntity implements ITickable {

	public TileEntityMoonRose() {
	}

	@Override
	public void update() {
		if (world != null) {
			Capabilities.getTEProperties(this, TileEntityProperties::onUpdate);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return Capabilities.getTEProperties(this, super.getUpdateTag(), TileEntityProperties::saveToNBT);
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		Capabilities.getTEProperties(this, prop -> prop.loadFromNBT(tag));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return Capabilities.getTEProperties(this, super.writeToNBT(compound), TileEntityProperties::saveToNBT);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		Capabilities.getTEProperties(this, prop -> prop.loadFromNBT(compound));
	}
}
