package xzeroair.trinkets.capabilities.TrinketCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class TrinketStorage implements IStorage<IAccessoryInterface> {

	public static final TrinketStorage storage = new TrinketStorage();

	@Override
	public NBTBase writeNBT(Capability<IAccessoryInterface> capability, IAccessoryInterface instance, EnumFacing side) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("type", instance.oreTarget());
		tag.setInteger("slot", instance.wornSlot());
		tag.setBoolean("NightVision", instance.ability());
		tag.setBoolean("altAbility", instance.altAbility());
		tag.setInteger("hits", instance.hitCount());
		tag.setInteger("exp", instance.storedExp());
		return tag;
	}

	@Override
	public void readNBT(Capability<IAccessoryInterface> capability, IAccessoryInterface instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			final NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("type")) {
				instance.setOreTarget(tag.getInteger("type"));
			}
			if(tag.hasKey("slot")) {
				instance.setWornSlot(tag.getInteger("slot"));
			}
			if (tag.hasKey("NightVision")) {
				instance.setAbility(tag.getBoolean("NightVision"));
			}
			if (tag.hasKey("altAbility")) {
				instance.setAltAbility(tag.getBoolean("altAbility"));
			}
			if (tag.hasKey("hits")) {
				instance.setHitCount(tag.getInteger("hits"));
			}
			if (tag.hasKey("exp")) {
				instance.setStoredExp(tag.getInteger("exp"));
			}
		}
	}

}
