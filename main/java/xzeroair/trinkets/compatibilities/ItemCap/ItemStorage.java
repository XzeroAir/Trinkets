package xzeroair.trinkets.compatibilities.ItemCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import xzeroair.trinkets.compatibilities.sizeCap.ICap;

public class ItemStorage implements IStorage<ItemCap> {

	public static final ItemStorage storage = new ItemStorage();

	@Override
	public NBTBase writeNBT(Capability<ItemCap> capability, ItemCap instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("type", instance.oreType());
		tag.setBoolean("NightVision", instance.nightVision());
		return tag;
	}

	@Override
	public void readNBT(Capability<ItemCap> capability, ItemCap instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("type")) {
				instance.setOreType(tag.getInteger("type"));
			}
			if (tag.hasKey("NightVision")) {
				instance.nightVisionOn(tag.getBoolean("NightVision"));
			}
		}
	}

}
