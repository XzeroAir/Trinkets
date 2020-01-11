package xzeroair.trinkets.capabilities.TrinketCap;

import net.minecraft.nbt.NBTTagCompound;
import xzeroair.trinkets.util.interfaces.IAccessoryInterface;

public class DefaultTrinketCapability implements IAccessoryInterface {

	int type = -1;
	int slot = -1;
	int hits = 0;
	int exp = 0;
	boolean abilityOn = false;
	boolean abilityOnAlt = false;

	public DefaultTrinketCapability(){

	}

	public DefaultTrinketCapability(int type, int slot, int hits, int exp, boolean ability, boolean altability) {
		this.type = type;
		this.slot = slot;
		this.hits = hits;
		this.exp = exp;
		abilityOn = ability;
		abilityOnAlt = altability;
	}

	@Override
	public boolean ability() {
		return abilityOn;
	}

	@Override
	public void setAbility(boolean ability) {
		if(abilityOn != ability) {
			abilityOn = ability;
		}
	}

	@Override
	public boolean altAbility() {
		return abilityOnAlt;
	}

	@Override
	public void setAltAbility(boolean altability) {
		if(abilityOnAlt != altability) {
			abilityOnAlt = altability;
		}
	}
	@Override
	public int oreTarget() {
		return type;
	}

	@Override
	public void setOreTarget(int type) {
		if (this.type != type) {
			this.type = type;
		}
	}

	@Override
	public int hitCount() {
		return hits;
	}

	@Override
	public void setHitCount(int hits) {
		if (this.hits != hits) {
			this.hits = hits;
		}
	}

	@Override
	public int storedExp() {
		return exp;
	}

	@Override
	public void setStoredExp(int exp) {
		if (this.exp != exp) {
			this.exp = exp;
		}
	}

	@Override
	public int wornSlot() {
		return slot;
	}

	@Override
	public void setWornSlot(int slot) {
		if (this.slot != slot) {
			this.slot = slot;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) TrinketStorage.storage.writeNBT(TrinketProvider.itemCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		TrinketStorage.storage.readNBT(TrinketProvider.itemCapability, this, null, compound);
	}

}
