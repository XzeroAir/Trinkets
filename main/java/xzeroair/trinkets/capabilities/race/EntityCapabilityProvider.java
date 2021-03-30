package xzeroair.trinkets.capabilities.race;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import xzeroair.trinkets.capabilities.Capabilities;

public class EntityCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

	private final EntityProperties properties;

	public EntityCapabilityProvider(EntityLivingBase e) {
		properties = new EntityProperties(e);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == Capabilities.ENTITY_RACE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == Capabilities.ENTITY_RACE) {
			return (T) properties;
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		properties.savedNBTData(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		properties.loadNBTData(nbt);
	}

}
