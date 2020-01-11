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
		tag.setString("food", instance.getFood());
		tag.setFloat("height", instance.getHeight());
		tag.setFloat("width", instance.getWidth());
		tag.setFloat("default_height", instance.getDefaultHeight());
		tag.setFloat("default_width", instance.getDefaultWidth());
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
			if(tag.hasKey("food")) {
				instance.setFood(tag.getString("food"));
			}
			if(tag.hasKey("height")) {
				instance.setHeight(tag.getFloat("height"));
			}
			if(tag.hasKey("width")) {
				instance.setWidth(tag.getFloat("width"));
			}
			if(tag.hasKey("default_height")) {
				instance.setDefaultHeight(tag.getFloat("default_height"));
			}
			if(tag.hasKey("default_width")) {
				instance.setDefaultWidth(tag.getFloat("default_width"));
			}
		}
	}

}
