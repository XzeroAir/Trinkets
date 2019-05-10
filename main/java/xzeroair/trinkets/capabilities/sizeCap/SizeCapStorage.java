package xzeroair.trinkets.capabilities.sizeCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SizeCapStorage implements IStorage<ISizeCap> {

	public static final SizeCapStorage storage = new SizeCapStorage();

	@Override
	public NBTBase writeNBT(Capability<ISizeCap> capability, ISizeCap instance, EnumFacing side) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", instance.getSize());
		tag.setBoolean("transformed", instance.getTrans());
		tag.setInteger("target", instance.getTarget());
		return tag;
	}

	@Override
	public void readNBT(Capability<ISizeCap> capability, ISizeCap instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			final NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("size")) {
				instance.setSize(tag.getInteger("size"));
			}
			if (tag.hasKey("transformed")) {
				instance.setTrans(tag.getBoolean("transformed"));
			}
			if(tag.hasKey("target")) {
				instance.setTarget(tag.getInteger("target"));
			}
		}
	}

}
