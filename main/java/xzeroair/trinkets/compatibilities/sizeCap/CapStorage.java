package xzeroair.trinkets.compatibilities.sizeCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapStorage implements IStorage<ICap> {

	public static final CapStorage storage = new CapStorage();

	@Override
	public NBTBase writeNBT(Capability<ICap> capability, ICap instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("size", instance.getSize());
		tag.setBoolean("transformed", instance.getTrans());
		tag.setInteger("target", instance.getTarget());
		return tag;
	}

	@Override
	public void readNBT(Capability<ICap> capability, ICap instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
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
